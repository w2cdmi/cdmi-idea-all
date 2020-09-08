package pw.cdmi.paas.app.model;

/****************************************************
 * 基础数据，枚举类型，表示应用的接入状态。
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 27, 2014
 ***************************************************/
public enum SiteAccessStatus {
	Normal,							//接入应用的状态，正常状态
	Test,							//应用的状态为测试，该状态应用在一段时间内不计费
	Pause,							//应用暂停访问，在该状态下，应用不能访问数据服务，不计费
	Arrearage,						//欠费的应用，欠费用户超过一定时间后将不能进行访问
	ToBeDeleted;					//待删除应用，超过一定时间后该应用信息将会被删除
}
