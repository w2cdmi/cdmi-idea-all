package pw.cdmi.wechat.open3rd.model;

import lombok.Data;

@Data
public class DraftResponse {
	
	private String draft_id;

	private String user_version;					// 代码版本号，开发者可自定义
	
	private String user_desc;						// 代码描述，开发者可自定义
	
	private String create_time;						// 开发者上传草稿时间
}
