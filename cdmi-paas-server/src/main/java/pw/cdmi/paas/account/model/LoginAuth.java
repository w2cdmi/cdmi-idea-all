package pw.cdmi.paas.account.model;

import lombok.Data;

@Data
public class LoginAuth {
	private AuthMethod type;				//用户登陆方式
	private Info info;						//登陆认证的用户信息
	
	
	public class Info {
		private String account;					//用户登陆的账号信息
		private String password;				//用户登陆的账号密码，非必须
		private String verificationCode;		//用户登陆时候的验证码信息
		
		public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getVerificationCode() {
			return verificationCode;
		}
		public void setVerificationCode(String verificationCode) {
			this.verificationCode = verificationCode;
		}
	}
}
