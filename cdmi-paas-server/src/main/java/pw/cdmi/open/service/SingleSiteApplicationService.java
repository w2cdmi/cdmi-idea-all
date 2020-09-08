package pw.cdmi.open.service;

import pw.cdmi.paas.app.model.entities.SiteApplication;

/****************************************************
 * 接口类，获取当前应用的License和配置信息。
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 29, 2014
 ***************************************************/
public interface SingleSiteApplicationService {

    /**
     * 创建或注册一个应用子站点
     * @param name 子站点的名称
     * @param endpoint 子站点的接入地址
     * @return 返回新创建的子站点的NewId
     */
    public String registerSiteAplication(String name, String endpoint);

    /**
     * 获得指定的子站点信息
     * @param siteId 子站点的编号
     * @return 返回子站点信息
     */
    public SiteApplication getSiteAplication(String siteId);

    /**
     * 根据用户的访问路径获取当前子站点信息
     * @param accessurl 子站点的编号 
     * @return 返回当前子站点信息
     */
    public SiteApplication getSiteAplicationByAccessURL(String accessurl);

    /**
     * 管理员启动一个子站点
     * @param siteId 子站点的编号
     */
    public boolean activateSiteApplication(String siteId);

}
