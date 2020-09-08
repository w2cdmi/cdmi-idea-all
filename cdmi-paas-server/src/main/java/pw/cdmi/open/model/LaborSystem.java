package pw.cdmi.open.model;

/************************************************************
 * 枚举类，企业的用工编制.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月2日
 ************************************************************/
public enum LaborSystem {
	Administrative(0,"行政编制"), 			// 行政编制
	PublicInstitutions(1,"事业编制"), 		// 事业编制
	GroundWorker(2,"工勤编制"), 			// 工勤编制
	Employed(3,"社聘人员"), 				// 社聘人员
	LaborDispatch(4,"劳务派遣人员"); 		// 劳务派遣人员

	private String text;

	private int value;

	private LaborSystem(int value, String text) {
		this.text = text;
		this.value = value;
	}

	public static LaborSystem fromText(String text) {
		for (LaborSystem labor : LaborSystem.values()) {
			if (labor.text.equals(text)) {
				return labor;
			}
		}
		return null;
	}

	public static LaborSystem fromValue(int value) {
		for (LaborSystem labor : LaborSystem.values()) {
			if (labor.value == value) {
				return labor;
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
