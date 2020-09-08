package pw.cdmi.open;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class Configuration {
	
	@Value("${product.owner.tenantId}")
	private String tenantId;
	@Value("${product.system.appId}")
	private String appId;
	@Value("${product.system.appname}")
	private String appname;
	@Value("${system.accessKey}")
	private String accessKey;
	@Value("${system.secretKey}") 
	private String secretKey;
	@Value("${system.deploy.superuser}") //临时放在这里
	private String deploy_user;
	@Value("${system.deploy.password}")
	private String deploy_password;
	/** 读取短信服务的配置信息 **/
	@Value("${mms.server.url}")
	private String mms_url;
	@Value("${mms.server.user}")
	private String mms_user;
	@Value("${mms.server.password}")
	private String mms_password;
	@Value("${mms.user.signature}")
	private String mms_signature;
	@Value("${mms.message.sender}")
	private String mms_sender;
	
	private final Map<String, String> metadata = new HashMap<String, String>();
	
	public String getTenantId(){
		return this.tenantId;
	}
	
	public String getAppId(){
		return this.appId;
	}
	public String getAppName(){
		return this.appname;
	}
	
	public String getAccessKey(){
		return this.accessKey;
	}
	
	public String getSecretKey(){
		return this.secretKey;
	}
	
	public String getDeployUser(){
		return this.deploy_user;
	}
	
	public String getDeployPassword(){
		return this.deploy_password;
	}
	
	protected void setMetadata(String key, String value){
		this.metadata.put(key, value);
	}
	
	public String getMetaValue(String key){
		return this.metadata.get(key);
	}

}
