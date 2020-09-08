package pw.cdmi.paas.account.model;

import lombok.Data;

@Data
public class NewUser {
	private RegisterChannel channel;			//注册通道
	private Info info;							//待注册的用户信息
	@Data
	public class Info {
		private String account;					//注册身份名称
		private String password;				//注册用户密码，可能为空
		private String mobile;					//电话
		private String email;					//邮箱
		private String head_image;				//头像url
		
	}
}
