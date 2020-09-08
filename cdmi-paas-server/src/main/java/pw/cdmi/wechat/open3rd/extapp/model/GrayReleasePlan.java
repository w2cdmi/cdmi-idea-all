package pw.cdmi.wechat.open3rd.extapp.model;

import lombok.Data;

@Data
public class GrayReleasePlan {
	private int status;				//0:初始状态 1:执行中 2:暂停中 3:执行完毕 4:被删除
	private long create_timestamp;	//创建时间
	private int gray_percentage;	//
}
