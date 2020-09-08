package pw.cdmi.wechat.open3rd.model;

import lombok.Data;

@Data
public class ComponentAccessToken {
	private String component_access_token;
	private int expires_in;
}
