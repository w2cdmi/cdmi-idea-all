package pw.cdmi.open.security;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import pw.cdmi.open.service.SingleSiteApplicationService;
import pw.cdmi.paas.account.model.UserStatus;
import pw.cdmi.paas.account.model.entities.UserAccount;
import pw.cdmi.paas.account.service.UserService;
import pw.cdmi.security.util.BaseRoleEnum;

/**
 * **********************************************
 * 认证管理器，实现用户认证的入口
 * 在登录认证阶段，获得访问用户所拥有的访问权限信息
 * 该方法在一定程度上实现了如下的特性：
 * <user name="downpour" password="downpour" authorities="ROLE_USER, ROLE_ADMIN" />
 * 如果用户账号或状态不符，将会导致登录失败，失败后进入AuthenticationFailureHandler进行处理
 * 
 * @author chenchao
 * @date 2015年5月11日
 * @version
 ***********************************************
 */
public class SecurityUserDetailServiceImpl implements UserDetailsService {

	private static Logger logger = LoggerFactory
			.getLogger(SecurityUserDetailServiceImpl.class);
	
	
	@Autowired
	private UserService userAccountService;
	
	@Autowired
	private SingleSiteApplicationService siteService;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		logger.info("认证：认证入口，保存用户所拥有的角色");
		
		Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
//		UserAccount userAccount=userAccountService.getUserAccountByUsername(username);
		UserAccount userAccount = new UserAccount();
		userAccount.setPassword("$2a$06$UIbhltKxnuaJHqMzeKjpO.Oa7MiH93WRxn13wSM16bC9Z3B2byCX.");
		userAccount.setUserName(username);
		userAccount.setStatus(UserStatus.OK);

		if(userAccount == null){
			throw new UsernameNotFoundException("User " + username + " has no GrantedAuthority");
		}
//		//还需要判断该账号是否被允许访问该应用。
//		SiteUser user = userAccountService.getSiteUserByAccountAndUserDomain(siteService.getCurrentAppId(), null, userAccount.getId());
//		if(user == null){
//			throw new UsernameNotFoundException("User " + username + " has no GrantedAuthority");
//		}
		
//		String sql = "select r.roleEnum from SiteRole r,SiteUserRole up,UserAccount ua  where ua.userName='"
//				+ username + "' and  r.roleEnum=up.roleEnum and ua.id=up.userId and r.siteId = '" + siteService.getCurrentAppId() + "'";	
//		List<String> objList = jpaImpl.find(sql);
//		
//		if(objList.size() == 0){//如果角色表中无数据，则登录账号具有最大权限
//			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(BaseRoleEnum.AUTH_SUPER_ADMIN.name());
//			auths.add(authority);
//		}
//		for (int i = 0; i < objList.size(); i++) {
//			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(objList.get(i));
//			auths.add(authority);
//		}
		
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(BaseRoleEnum.AUTH_SUPER_ADMIN.name());
		auths.add(authority);
		
		//FIXME 配置文件中password-encoder似乎没有生效
		return new org.springframework.security.core.userdetails.User(
				userAccount.getUserName(), userAccount.getPassword(), userAccount.getStatus()==UserStatus.OK ? true:false,
				true, true, userAccount.getStatus()==UserStatus.Lock ? false:true, auths);
	}

}