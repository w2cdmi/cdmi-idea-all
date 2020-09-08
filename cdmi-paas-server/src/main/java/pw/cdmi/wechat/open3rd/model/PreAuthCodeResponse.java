package pw.cdmi.wechat.open3rd.model;

import lombok.Data;

@Data
public class PreAuthCodeResponse {
	private String pre_auth_code;
	private int expires_in; 
}
