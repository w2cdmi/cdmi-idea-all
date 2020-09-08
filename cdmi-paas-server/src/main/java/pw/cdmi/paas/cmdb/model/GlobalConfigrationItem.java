package pw.cdmi.paas.cmdb.model;

public enum GlobalConfigrationItem {
	SMS_ENABLE("global.system.sms.enable"),										//系统是否开启短信网关功能
	SMS_PROVIDER("global.system.sms.provider"),									//短信服务提供商
	SMS_SERVER_URL("global.system.sms.server.url"),								//短信服务调用地址
	SMS_SERVER_AUTH_PARAMETERS("global.system.sms.server.auth.parameters"),		//短信服务调用鉴权参数
	SMS_MESSAGE_SIGNATORY("global.system.sms.message.signatory"),				//短信信息的签名人信息
	
	MAIL_STMP_ENABLE("global.system.mail.stmp.enable"),							//系统是否开启邮件发送功能
	MAIL_STMP_SERVER("global.system.mail.stmp.server"),							//STMP服务器地址
	MAIL_STMP_PORT("global.system.system.mail.stmp.port"),						//STMP服务器的端口
	MAIL_STMP_AUTH_ENABLE("global.system.mail.stmp.auth.enable"),				//是否启动加密发送邮件true/false
	MAIL_STMP_AUTH_SECURITY("global.system.mail.stmp.auth.security"),			//是否加密发送邮件disable/ssl/tls
	MAIL_STMP_USERNAME("global.system.mail.stmp.username"),						//邮件发送服务器用户账号
	MAIL_STMP_PASSWORD("global.system.mail.stmp.password"),						//邮件发送服务器用户密码(加密后的)
	MAIL_STMP_PASSWORD_KEY("global.system.mail.stmp.password.key");				//密码的加密KEY
	
	private String itemkey;
	
	private GlobalConfigrationItem(String itemkey) {
		this.itemkey = itemkey;
	}
	
}
