package pw.cdmi.paas.account.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import pw.cdmi.collection.PageView;
import pw.cdmi.open.model.entities.Tenant;
import pw.cdmi.open.model.queryObject.UserAccountQuery;
import pw.cdmi.paas.account.commons.NumberGenerate;
import pw.cdmi.paas.account.model.ProtectAQ;
import pw.cdmi.paas.account.model.entities.UserAccount;
import pw.cdmi.paas.account.repositories.UserRepository;
import pw.cdmi.paas.account.service.UserService;
import pw.cdmi.paas.app.model.entities.SiteUser;

@Component
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Override

	public String createUserAccount(UserAccount account) {
		String password = account.getPassword();
		if(!StringUtils.isBlank(password)) {
			// 对用户的密码进行加密处理
			BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
			String hashPassword = bcryptEncoder.encode(password);
			account.setPassword(hashPassword);
		}
		
		account.setRegisterTime(new Date());
		account.setOpenId(NumberGenerate.toOpenId());
		
		account = userRepository.save(account);
		//TODO 不同的应用开发商获得的用户OpenId不同。
		return account.getId();
	}

	@Override
	public boolean existUserAccount(UserAccount userAccount) {
		UserAccount user = userRepository.findOne(Example.of(userAccount)).get();
		if(user==null){
			return false;			
		}
		return true;
	}

	@Override
	public void updateUserAccount(UserAccount userAccount) {
		// TODO 实体更新
		
		
	}

	@Override
	public void updatePassword(String newpassword, String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPassword(String newpassword, String accountId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUserAccountByPeopleId(String peopleId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserAccount getUserAccountById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAccount findUserAccountByUserNameAndPassword(String userName, String hash_password) {
		// TODO 
		UserAccount userAccount = new UserAccount();
		userAccount.setUserName(userName);
		//加密
		BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
		String hashPassword = bcryptEncoder.encode(hash_password);
		userAccount.setPassword(hashPassword);
		
		return userRepository.findOne(Example.of(userAccount)).get();
	}
	@Override
	public UserAccount findUserAccountByMobileAndPassword(String mobile, String password){
		// TODO 
		UserAccount userAccount = new UserAccount();
		userAccount.setMobile(mobile);
		//加密
		BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
		String hashPassword = bcryptEncoder.encode(password);
		userAccount.setPassword(hashPassword);
		
		return userRepository.findOne(Example.of(userAccount)).get();
		
	}
	@Override
	public UserAccount FindUserAccountByEmailAndPassword(String email, String password){
		// TODO 
		UserAccount userAccount = new UserAccount();
		userAccount.setEmail(email);
		//加密
		BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
		String hashPassword = bcryptEncoder.encode(password);
		userAccount.setPassword(hashPassword);
				
		return userRepository.findOne(Example.of(userAccount)).get();
		
	}
	@Override
	public UserAccount getInitAccount(String roleName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAccount getUserAccountByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAccount getUserAccountByMobile(String mobile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAccount getUserAccountByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAccount getUserAccountByKeyFields(String username, String email, String mobile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAccount getUserAccountByPeopleId(String peopleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAccount getUserAccountByUserNameAndPassword(String userName, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<UserAccount> findAllUserAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<UserAccount> findAllUserAccountBySite(String siteId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageView findUserAccountByConditionAndPage(int pageNo, int pageSize, UserAccount queeryObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createProtectAQ(ProtectAQ protectAQ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createProtectAQs(long userAccountId, List<Map<String, String>> protectAQList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProtectAQ(ProtectAQ protectAQ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProtectAQById(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ProtectAQ> findProtectAQListByUserAccountId(long userAccountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProtectAQ findSingleProtectAQByUserAccountIdAndRandom(long userAccountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProtectAQ findProtectAQById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createTenant(String appId, Tenant tenant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTenant(String appId, Tenant tenant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteTenant(String appId, long tenantId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Tenant getTenantById(long tenantId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tenant getTenantByAccountId(long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageView findUserAccountList(UserAccountQuery userAccountQuery,
			Integer page, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAccount getUserAccountByIdAndStatus(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    public SiteUser getSiteUserByAccountId(String userId, String appId) {
    	return null;
    }
    
	@Override
	public SiteUser getSiteUserByAccountAndUserDomain(String accountId, String domain, String appId) {
		return null;
	}

	@Override
	public SiteUser updateSiteUser(SiteUser user) {
		return null;
	}

	@Override
	public UserAccount getUserAccountByWxUserId(String wxOpenid) {
		// TODO Auto-generated method stub
		return userRepository.findOneByWxOpenId(wxOpenid);
	}
}
