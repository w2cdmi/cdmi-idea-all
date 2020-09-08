package pw.cdmi.open.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import pw.cdmi.open.service.PermissionService;
import pw.cdmi.open.service.SingleSiteApplicationService;
import pw.cdmi.paas.app.model.entities.SiteRole;

/**
 * **********************************************************
 * 根据角色的访问白名单，判断当前用户是否可以访问资源
 * 
 * @author wuwei
 * @version iSoc Service Platform, 2015年8月17日
 ***********************************************************
 */
public class IPRoleAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private SingleSiteApplicationService appService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		String appId = "";
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

			// 是否权限验证可以通过
			boolean hasAuthority = false;

			String requestsIp = request.getRemoteAddr();
			// 对权限进行对比
			for (GrantedAuthority grantedAuthority : authorities) {
				// 如果对比的权限相同 ，那么就可以说明有权限
				SiteRole site_role = permissionService.getSiteRoleByRoleEnum(appId,
					grantedAuthority.getAuthority());
				String ips = site_role.getWhiteIpList();
				if (!StringUtils.isEmpty(ips)) {// 存在Ip限制清单
					String[] ls_ips = ips.split(",");
					for (String ip : ls_ips) {
						if (ip.equals(requestsIp)) {
							hasAuthority = true;
							break;
						}
					}
					if (hasAuthority) { //来访Ip，已在授权里面中找到。
						break;
					}
				}
			}
			if (!hasAuthority) {
				throw new AccessDeniedException("ip被拦截,您的ip是: " + requestsIp);
			}
		}
		filterChain.doFilter(request, response);
	}

}
