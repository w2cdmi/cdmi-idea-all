package pw.cdmi.open.model;

/************************************************************
 * 枚举类，表示员工的在职状态.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月2日
 ************************************************************/
public enum WorkStatus {
	OK(0,"正常"), 				// 正常
	Bridge(1,"返聘"), 			// 返聘
	Temporary(2,"挂职"),			// 挂职
	Loaned(3,"借调其他单位"),		// 借调其他单位
	Unpaid(4,"停薪留职"),			// 停薪留职
	Quited(5,"已离职"),			// 已离职
	IllRetired(6,"已因病退休"),		// 已因病退休
	EarlyRetired(7,"已提前退休"),	// 已提前退休
	Retired(8,"已正常退休");		// 已正常退休
	//Deaded(9,"已死亡");			// 已死亡

	private String text;

	private int value;

	private WorkStatus(int value, String text) {
		this.text = text;
		this.value = value;
	}

	public static WorkStatus fromText(String text) {
		for (WorkStatus status : WorkStatus.values()) {
			if (status.text.equals(text)) {
				return status;
			}
		}
		return null;
	}

	public static WorkStatus fromValue(int value) {
		for (WorkStatus status : WorkStatus.values()) {
			if (status.value == value) {
				return status;
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
