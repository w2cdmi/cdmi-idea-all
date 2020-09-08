package pw.cdmi.paas.app.model;

/**
 * **********************************************************
 * <br/>
 * 基础数据，枚举类型，表示应用站点的状态。
 * 
 * @author Liujh
 * @version cdm Service Platform, 2016年6月23日
 ***********************************************************
 */
public enum SiteStatus {
    Normal,             		// 正常
    InApproval,					// 等待审批中
    ToBeConfigured,     		// 应用刚部署，等待完成初始配置
    UnderMaintenance,        	// 处于维护或升级状态，这个时候将会暂停服务
    Debugging,              	// 系统处于调试状态,正常提供服务，但日志会有变化
    Stoped;             		// 已停止服务
}
