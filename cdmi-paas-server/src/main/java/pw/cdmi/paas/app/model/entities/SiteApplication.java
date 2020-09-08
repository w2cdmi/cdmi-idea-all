package pw.cdmi.paas.app.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import pw.cdmi.paas.app.model.SiteStatus;

/**
 * **********************************************************
 * 内容站点对象模型，一般与频道，栏目沟通内容管理的几大要素。
 * 
 * @author 伍伟
 * @version cdm Service Platform, 2016年6月23日
 ***********************************************************
 */

@Data
@Entity
@Table(name = "p_authapp_site")
@Document(collection = "p_authapp_site")
public class SiteApplication {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(unique = true)
    private String id;                      	// 子站点的ID，固定字符串长度
    
    private String appId;						// 子站点对应的应用编号

    private String siteName;                    // 子站点的名称表示，比如：智能运维系统

    private String endpoint;                    // 子站点的访问路径

    private String labels;                      // 为子站点贴标签

    private String description;                 // 子站点的简要描述

    private Date createTime;                    // 子站点创建的时间

    private boolean isPublished;                // 表示该子站点是否已发布，待发布，正常

    private String appUserId;                   // 站点的拥有人，对应的应用用户Id。
    
    private String userId;                   	// 站点的拥有人，对应的平台用户Id。

}