package pw.cdmi.wechat.open3rd.model;

import lombok.Data;

@Data
public class AuthorizerTokenResponse {
	private String authorizer_access_token;
	private int expires_in;
	private String authorizer_refresh_token;
}
