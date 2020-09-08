package pw.cdmi.open.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import pw.cdmi.open.service.SingleSiteApplicationService;
import pw.cdmi.paas.app.model.entities.SiteMenu;
import pw.cdmi.paas.app.model.entities.SiteMenuAcl;
import pw.cdmi.paas.app.model.entities.SiteRole;
import pw.cdmi.paas.app.repositories.SiteMenuAclRepository;
import pw.cdmi.paas.app.repositories.SiteMenuRepository;
import pw.cdmi.paas.app.repositories.SiteRoleRepository;
import pw.cdmi.security.util.AntUrlPathMatcher;
import pw.cdmi.security.util.UrlMatcher;

/**
 * 如果当前用户存在访问角色，则加载资源与权限的对应关系 如果验证成功，转到该类执行获取所有的资源及其对应角色的关联关系
 * 前置方法是FilterSecurityInterceptor的doFilter方法
 * FilterSecurityInterceptor检查用户是否登录，并获得有操作角色 这里应当是用来判断用户的该角色是否有权利访问该请求
 * 
 * 配置文件描述为：资源数据定义，即定义某一资源可以被哪些角色访问 似乎不应该每次都去定义
 * 
 * @author wuwei
 *
 */
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {
	private static Logger logger = LoggerFactory.getLogger(FilterInvocationSecurityMetadataSourceImpl.class);

	@Autowired
	private SiteRoleRepository roleRepository;

	@Autowired
	private SiteMenuRepository menuRepository;

	@Autowired
	private SiteMenuAclRepository aclRepository;
	
	@Autowired
	private SingleSiteApplicationService siteService;

	private UrlMatcher urlMatcher = new AntUrlPathMatcher();

	// private List<String> urls;

	private String loginRole; // 对应的登录域

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		String appId = "";
		logger.info("授权：根据用户拥有的角色，获得用户可以访问的资源");
		// object 是一个URL，被用户请求的url。
		String requrl = ((FilterInvocation) object).getRequestUrl();
		// 获取所有角色。
		String sql = "select r from SiteRole r  where r.siteId = '" + appId + "'";
		Iterable<SiteRole> roleList = null;
		if (StringUtils.isBlank(loginRole)) {
			sql = "select r from SiteRole r where r.siteId = '" + appId + "' and r.loginRole＝'" + loginRole + "'";
			roleList = roleRepository.findByAppId(appId);
		}else {
			roleList = roleRepository.findByAppIdAndRoleEnum(appId,loginRole);
		}
		 

		// 以下四种情况会不受Spring Security的权限控制
		// 第1种情况，没有角色表示账号可以访问所有的资源
		if (!roleList.iterator().hasNext()) {
			return null;
		}
		// 获取所有资源
		Iterable<SiteMenu> menulist = menuRepository.findByAppId(appId);
		// 第2种情况，系统没有设置Menu，表示所有的Menu都可以访问
		if (!menulist.iterator().hasNext()) {
			return null;
		} else {
			// 第3种情况，如果当前请求不对应Menu表中数据，则表明，该请求不受到MenuAcl的限制。
			boolean isLimited = false;
			for (SiteMenu menu : menulist) {
				String url = menu.getResourceURL();
				if (urlMatcher.pathMatchesUrl(url, requrl)) {
					isLimited = true;
					break;
				}
			}
			if (!isLimited) {
				return null;
			}
		}
		// 获取权限和资源的对应关系
		Iterable<SiteMenuAcl> acllist = aclRepository.findByAppId(appId);
		// 第4种情况，如果系统在有Menu情况下没有ACL，则只有安全管理员才能访问
		if (!acllist.iterator().hasNext()) {
			return null;
		}

		// 用与装入url对应的权限
		Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
		// 装入url对应的权限
		for (SiteRole role : roleList) {
			ConfigAttribute ca = new SecurityConfig(role.getRoleEnum());
			for (SiteMenu menu : menulist) {
				for (SiteMenuAcl acl : acllist) {
					if (acl.getMenuId().equals(menu.getId()) && acl.getRoleId().equals(role.getId())) {
						String url = menu.getResourceURL();
						if (StringUtils.isNotEmpty(url)) {
							if (resourceMap.containsKey(url)) {
								Collection<ConfigAttribute> value = resourceMap.get(url);
								value.add(ca);
								resourceMap.put(url, value);
							} else {
								Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
								atts.add(ca);
								resourceMap.put(url, atts);
							}
						}

					}
				}
			}

			// 根据url查询权限,并将权限放入Collection<ConfigAttribute>
			Iterator<String> ite = resourceMap.keySet().iterator();
			while (ite.hasNext()) {
				String resURL = ite.next();
				if (urlMatcher.pathMatchesUrl(resURL, requrl)) {
					return resourceMap.get(resURL);
				}
			}
		}

		// 返回为空，则不会继续执行访问决策器MyAccessDecisionManager了。
		return null;
	}

	public String getLoginRole() {
		return loginRole;
	}

	public void setLoginRole(String loginRole) {
		this.loginRole = loginRole;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}


}