package pw.cdmi.open.model;

import lombok.Getter;

/**
 * **********************************************************
 * 基础数据类型，枚举类型，标识学历
 * 
 * @author liujh
 * @version iSoc Service Platform, 2015-7-8
 ***********************************************************
 */
@Getter
public enum Education {

	Junior("初中",0),            // 初中
	High("高中",1),              // 高中
	Polytechnic("中技",2),       // 中技
	Technical("中专",3),         // 中专
	College("大专",4),           // 大专
	Undergraduate("本科",5),     // 本科
	Mba("MBA",6),				 // MBA
	Graduate("硕士",7),          // 研究生
	Dr("博士",8),                // 博士
	Primary("其他",9);           // 其他

	private String text;
	
	private int value;
	
	private Education(String text,int value) {
		this.text = text;
		this.value = value;
	}
}
