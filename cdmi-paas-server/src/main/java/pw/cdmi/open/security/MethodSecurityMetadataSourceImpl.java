package pw.cdmi.open.security;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.method.AbstractMethodSecurityMetadataSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.method.HandlerMethod;

import pw.cdmi.open.annotation.DataAuth;
import pw.cdmi.open.annotation.DefaultPermission;
import pw.cdmi.open.annotation.MothedAuth;
import pw.cdmi.open.service.SingleSiteApplicationService;
import pw.cdmi.paas.app.model.entities.SiteDataPrivilegeAcl;
import pw.cdmi.paas.app.repositories.SiteDataPrivilegeAclRepository;

public class MethodSecurityMetadataSourceImpl extends AbstractMethodSecurityMetadataSource {

	public static final String SHARP = "#";

	@Autowired
	private SiteDataPrivilegeAclRepository daoImpl;

	private Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();

	private HandlerMethod handlerMethod;
	private Object[] arguments;

	@Autowired
	private SingleSiteApplicationService appService;

	public MethodSecurityMetadataSourceImpl() {
		super();
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();
		for (Map.Entry<String, Collection<ConfigAttribute>> entry : resourceMap.entrySet()) {
			allAttributes.addAll(entry.getValue());
		}
		return allAttributes;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass) {
		logger.info("授权：根据用户拥有的角色，获得用户可以访问的方法");
		String appId = null;
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();
		
		MothedAuth accessAuth = method.getAnnotation(MothedAuth.class);

//		// 获得当前登陆用户对应的对象
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		Collection<? extends GrantedAuthority> authorities = null;
//		if (authentication != null) {
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			// 获得当前登陆用户所拥有的所有权限
//			authorities = userDetails.getAuthorities();
//		}
//
//		if (authorities == null || authorities.size() == 0) {// 当前用户无什么权限，返回false;
//			return null;
//		}

		// 需验证登录用户的检查应当放在这里
		DataAuth dataAuth = null;
		MethodParameter[] parameters = this.handlerMethod.getMethodParameters();
		int i = 0;
		for (MethodParameter mp : parameters) {
			dataAuth =mp.getParameterAnnotation(DataAuth.class);
			if (dataAuth != null) {
//				String authValue = dataAuth.auth();
				Object auth = this.arguments[i];
				//FIXME 目前只支持基本类型的查询
				Iterable<SiteDataPrivilegeAcl> roles = daoImpl.findByAuthEnumAndAppId(auth, appId);
				Iterator<SiteDataPrivilegeAcl> iter = roles.iterator();
				while(iter.hasNext()){
					ConfigAttribute ca = new SecurityConfig(iter.next().getRoleEnum());
					allAttributes.add(ca);
				}
				break;
			}
			i++;
		}

		// 验证的最终结果
		boolean authResult = false;

		// 没有声明需要权限就不验证权限
		if (accessAuth == null && dataAuth == null) {
			return null;
		}
		if (accessAuth != null) {
			// 在这里实现自己的权限验证逻辑

			// 1.先看用户是否有该方法的允许访问角色，有就返回true，没有，再从数据库里找
			DefaultPermission permission = this.handlerMethod.getMethodAnnotation(DefaultPermission.class);

//			if (!validityDefaultPermission(permission, authorities)) {// 和默认角色不匹配，则查看数据库中资源对应的角色是否匹配
//				// 获得允许访问该功能的角色列表
//				String sql = "select roleEnum from SitePrivilegeAcl where authEnum = '" + auth + "' and siteId ='"
//						+ appService.getCurrentAppId() + "'";
//				List<String> roles = jpaImpl.find(sql);
//
//				authResult = validityFromDatabasePermission(roles, authorities);
//				if (!authResult) {// 从数据库中也没有找到用户有对应的角色来访问该资源
//					return null;
//				}
//			}
		}

		return resourceMap.get("a");
	}

	public HandlerMethod getHandlerMethod() {
		return handlerMethod;
	}

	public void setHandlerMethod(HandlerMethod handlerMethod) {
		this.handlerMethod = handlerMethod;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}
	
	/**
	 * 从方法上默认允许的角色与当前用户所拥有的权限角色做对比
	 * @param permission
	 * @param authorities
	 * @return
	 */
	private boolean validityDefaultPermission(DefaultPermission permission,
		Collection<? extends GrantedAuthority> authorities) {
		if (permission != null) {
			for (GrantedAuthority authoritie : authorities) {
				String user_role = authoritie.getAuthority();
				for (String role : permission.value()) {
					if (role.equals(user_role)) {// 匹配成功，有权访问
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 从数据库中获得当前可访问的角色列表后与当前用户所拥有的权限角色做对比
	 * @param roles
	 * @param authorities
	 * @return
	 */
	private boolean validityFromDatabasePermission(List<String> roles,
		Collection<? extends GrantedAuthority> authorities) {
		if (roles != null && roles.size() > 0) {
			for (GrantedAuthority authoritie : authorities) {
				String user_role = authoritie.getAuthority();
				for (String role : roles) {
					if (role.equals(user_role)) {// 匹配成功，有权访问
						return true;
					}
				}
			}
		}
		return false;
	}
}
