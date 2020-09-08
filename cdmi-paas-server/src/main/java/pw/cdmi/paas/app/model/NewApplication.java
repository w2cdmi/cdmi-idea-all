package pw.cdmi.paas.app.model;

import lombok.Data;

@Data
public class NewApplication {
	private String name;
	private SiteAttribution attribution;
	private AuthApplicationType type;
	private String logo_url;
    private String description;					// 应用的简要描述
}
