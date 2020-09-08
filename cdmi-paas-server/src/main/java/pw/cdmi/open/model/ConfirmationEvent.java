package pw.cdmi.open.model;

public enum ConfirmationEvent {
	MobileSignUp(1, 10),//手机注册, 10分钟内有效
	EmailSignUp(2, 7 * 24 * 60),//邮件注册, 一周内有效，其实应该是永久有效，点击激活连接，只是改变状态的状态。
	ModifiedMobile(3, 10),//修改手机号码,10分钟内有效
	ModifiedEmail(4, 7 *24 *60),//修改邮件地址
	ResetPasswordByMobile(5, 10),//通过手机重置密码
	ResetPasswordByEmail(6, 7 * 24 * 60); //通过邮件重置密码
	
	private int value;
	private int deadline; 

	private ConfirmationEvent(int value, int deadline) {
		this.value = value;
		this.deadline = deadline;
	}

	public static ConfirmationEvent fromValue(int value) {
		for (ConfirmationEvent event : ConfirmationEvent.values()) {
			if (event.value == value) {
				return event;
			}
		}
		return null;
	}

	public int getValue() {
		return this.value;
	}
	
	public int getDeadline(){
		return this.deadline;
	}
}
