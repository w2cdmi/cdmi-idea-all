package pw.cdmi.open.model;

public enum UserOnlineStatus {
	Offline(0),		//用户当前离线
	Hidden(1),		//用户当前在线，但对外显示隐藏，不可以搜索在线
	Online(2),		//用户当前在线
	Busy(3),		//用户在线但是当前处于忙碌状态
	Out(4), 		//用户在线但是当前处于外出状态
	Meeting(5); 	//用户在线但是当前处于会议状态

	
	private int value;
	
	private UserOnlineStatus(int value){
		this.value = value;
	}
	
	public static UserOnlineStatus fromValue(int value){
		for (UserOnlineStatus stutas : UserOnlineStatus.values()) {
			if (stutas.value == value) {
				return stutas;
			}
		}
		return null;
	}
	
	public static UserOnlineStatus fromName(String name){
		for (UserOnlineStatus stutas : UserOnlineStatus.values()) {
			if (stutas.name() == name) {
				return stutas;
			}
		}
		return null;
	}
	
	public int getValue(){
		return this.value;
	}
}
