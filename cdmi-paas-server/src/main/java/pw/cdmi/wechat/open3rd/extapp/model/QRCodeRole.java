package pw.cdmi.wechat.open3rd.extapp.model;

import lombok.Data;

@Data
public class QRCodeRole {
	private String prefix;
	private String permitSubRule;
	private String path;
	private String openVersion;
	private String[] debugUrl;
	private int is_edit;
}
