package pw.cdmi.open.model;

/************************************************************
 * 枚举类，雇员的岗位性质枚举.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月2日
 ************************************************************/
public enum PositionNature {
	PieceWork(0,"计件工资"), 			// 按计件定工资
	Performance(1,"绩效工资"); 		// 按绩效定工资

	private String text;
	private int value;

	private PositionNature(int value, String text) {
		this.text = text;
		this.value = value;
	}

	public static PositionNature fromText(String text) {
		for (PositionNature position : PositionNature.values()) {
			if (position.text.equals(text)) {
				return position;
			}
		}
		return null;
	}

	public static PositionNature fromValue(int value) {
		for (PositionNature position : PositionNature.values()) {
			if (position.value == value) {
				return position;
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
