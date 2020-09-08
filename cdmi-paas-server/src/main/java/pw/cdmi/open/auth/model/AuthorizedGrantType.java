package pw.cdmi.open.auth.model;

/**
 * OAuth认证微服务所支持的三种认证方式
 * @author No.11
 *
 */
public enum AuthorizedGrantType {
	UserCenter,		//第三方网站通过本系统作为其用户登陆方式
	AppAccess,		//第三方应用通过存储的Access Key/Secret Access Key方式调用微服务接口，Secret Access Key不会在网络上扭转
	BrowerAccess;	//第三方网站通过前端JS调用微服务接口，实现方式：用户通过appid获得code，根据code获取token。
}
