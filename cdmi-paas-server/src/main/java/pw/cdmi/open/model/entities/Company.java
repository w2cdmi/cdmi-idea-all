package pw.cdmi.open.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import pw.cdmi.open.model.WorkStatus;

/****************************************************
 * 企业基础数据，记录企业信息。
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 23, 2014
 ***************************************************/
@NamedQueries({ @NamedQuery(name = "Company.getSuper", query = "from Company c where c.parentId=0 or c.parentId=null"),
        @NamedQuery(name = "Company.findAll", query = "from Company c"),
        @NamedQuery(name = "Company.findByParentId", query = "from Company c where c.parentId = :parentId"),
        @NamedQuery(name = "Company.getByOpenId", query = "from Company c where c.openId = ?1") })
@Data
@Entity
@Table(name = "ept_company")
public class Company {

    /********************企业基本信息****************/
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String id;

    @Column(nullable = false, unique = true)
    private String openId;

    @Column(unique = true)
    private String name;						// 公司的完整名称

    private String shortName;					// 公司的简称

    private String businessRegistrationName;    // 工商注册名

    private String englishName;					// 公司的第二个名称，可能是英文名

    private String shortEnglishName;			// 公司的简称英文名

    private String telephone;					// 公司的联系电话

    @Column(length = 255)
    private String email;						// 企业的邮件地址

    private String location;					// 企业的经营地址

    private Integer postcode;						// 企业的邮编

    private String logoUrl; 					// 企业LOGO的URL，对应数据库字段

    private String webSite; 					// 企业的网站

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;					// 信息被收录的时间

    private String appId;						// 企业信息是被哪个应用所创建的
    

    /********************企业法人信息****************/
    private String legalPerson;					// 公司的法人代表

    private String legalPersonCode;				// 公司法人的身份证号码

    private String legalPersonCodeUrl;			// 法人代表身份证扫描件

    /********************企业证件信息****************/
    @Column(unique = true)
    private String licenseNumber;				// 企业的营业执照

    private Integer cityId;							// 营业执照所在的城市ID

    private Integer provinceId;						// 营业执照所在的省份ID

    private Integer countryId;						// 公司所在的国家ID

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date foundDate;						// 公司成立的时间

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date licenseLimitDate;				// 营业期限，永久为9999-12-31

    private Integer registeredCapital;				// 企业的注册资本,单位万元

    private String businessScope;				// 营业范围

    private String registrationInstitutional;	// 营业证上的注册机关

    private String licenseImgurl;				// 公司的营业执照扫描件

    @Column(unique = true)
    private String codeCertificate;				// 公司组织机构代码证

    private String codeCertificateUrl;			// 公司组织机构代码证复印件的URL

    @Column(unique = true)
    private String taxCode;						// 企业税务登记代码

    private String taxCodeUrl;					// 企业税务登记代码扫描件

    /********************企业银行信息****************/
    private String bankId;						// 对应的银行信息ID，目前暂无银行信息表，该字段为空

    private String bankName;					// 对应的开户银行名称

    private String bankAccountName;				// 对应的银行账户名

    @Column(unique = true)
    private String bankAccountNubmer;			// 对应的银行账户号

    /*****************分公司信息*******************/
    private String parentId;						// 父公司的编号

    private String areaId;							// 对应的片区信息编号

    private String supervisorId;					// 分公司主管，对应关联雇员信息编号

    @Column(length = 2000)
    private String description;						// 分公司简介

    public String createSearchQuery() {
        StringBuffer queryString = new StringBuffer("from Company c where 1=1");
        if (parentId != null) {
            queryString.append(" and c.parentId=" + parentId);
        }
        if (areaId != null) {
            queryString.append(" and c.areaId=" + areaId);
        }
        if (!StringUtils.isBlank(businessRegistrationName)) {
            queryString.append(" and c.businessRegistrationName like '%" + businessRegistrationName.trim() + "%'");
        }
        if (!StringUtils.isBlank(name)) {
            queryString.append(" and c.name like '%" + name.trim() + "%'");
        }
        return queryString.toString();
    }

    public String createQuery() {
        StringBuffer queryString = new StringBuffer("select count(*) from Company c where 1=1");
        if (parentId != null) {
            queryString.append(" and c.parentId=" + parentId);
        }
        if (areaId != null) {
            queryString.append(" and c.areaId=" + areaId);
        }
        if (!StringUtils.isBlank(businessRegistrationName)) {
            queryString.append(" and c.businessRegistrationName like '%" + businessRegistrationName.trim() + "%'");
        }
        if (!StringUtils.isBlank(name)) {
            queryString.append(" and c.name like '%" + name.trim() + "%'");
        }
        return queryString.toString();
    }

}
