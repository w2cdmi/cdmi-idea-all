package pw.cdmi.open.model;

import lombok.Getter;

/************************************************************
 * 用于存放员工在职性质的枚举
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-29
 ************************************************************/
@Getter
public enum EmployeeProperty {

	TRAINEE(0,"实习生"),
	PROBATION(1,"试用期"),
	OFFICIAL(2,"正式员工"),
	COOPERATION(3,"合作员工");

	private int value;

	private String text;

	private EmployeeProperty(int value, String text) {
		this.value = value;
		this.text = text;
	}

}
