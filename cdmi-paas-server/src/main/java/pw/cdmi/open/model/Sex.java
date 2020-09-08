package pw.cdmi.open.model;

/****************************************************
 * 基础数据，枚举类型，表示性别。
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 27, 2014
 ***************************************************/
public enum Sex {
	MALE(1,"男"), // 男
	FEMALE(2,"女"); // 女
//	OTHER(0,"未知");// 未知

	private String text;
	private int value;

	private Sex(int value,String text) {
		this.text = text;
		this.value = value;
	}

	public static Sex fromText(String text) {
		for (Sex sex : Sex.values()) {
			if (sex.text.equals(text)) {
				return sex;
			}
		}
		return null;
	}

	public static Sex fromValue(int value) {
		for (Sex sex : Sex.values()) {
			if (sex.value == value) {
				return sex;
			}
		}
		return null;
	}

	public String getText() {
		return this.text;
	}

	public int getValue() {
		return this.value;
	}
}
