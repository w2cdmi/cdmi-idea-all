package pw.cdmi.paas.app.model;

import lombok.Data;

@Data
public class ListAuthApplicationResponse {
	private String id;
	private String name;
	private AuthApplicationType type;
	private String logo_url;
	private String updatetime;
	private String create_time;
	
    private String description;					// 应用的简要描述
    private SiteAttribution attribution;
}
