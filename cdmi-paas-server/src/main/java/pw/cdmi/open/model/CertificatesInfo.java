package pw.cdmi.open.model;

/************************************************************
 * 枚举类，表示人员的证件类型
 * 
 * @author Chenyl
 * @version iSoc Service Platform, 2016年1月12日
 ************************************************************/
public enum CertificatesInfo {
	IdCard(0,"身份证号"), 				//  居民身份证
	SocialSecurityCode(1,"社保号"), 			// 个人社保编号
	DriverLicenseNumber(2,"驾驶证号"),			// 个人驾驶证号
	PassportNumber(3,"护照号");		// 个人护照号

	private String text;

	private int value;

	private CertificatesInfo(int value, String text) {
		this.text = text;
		this.value = value;
	}

	public static CertificatesInfo fromText(String text) {
		for (CertificatesInfo status : CertificatesInfo.values()) {
			if (status.text.equals(text)) {
				return status;
			}
		}
		return null;
	}

	public static CertificatesInfo fromValue(int value) {
		for (CertificatesInfo status : CertificatesInfo.values()) {
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
