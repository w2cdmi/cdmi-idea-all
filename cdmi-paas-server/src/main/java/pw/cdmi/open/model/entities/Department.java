package pw.cdmi.open.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

/************************************************************
 * 企业基础数据, 记录企业的部门信息.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月2日
 ************************************************************/
@NamedQueries({
        @NamedQuery(name = "Department.findAll", query = "from Department d"),
        @NamedQuery(name = "Department.findFirstDepartment", query = "from Department d where d.parentId = null and d.companyId=:companyId"),
        @NamedQuery(name = "Department.findAllByCompanyId", query = "from Department d where d.companyId=?") })
@Data
@Entity
@Table(name = "ept_dept")
public class Department {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String id;							// 部门信息编号

    @Column(nullable = false)
    private String name;					// 部门名称

    private String code;					// 部门编码,同一企业内唯一

    private String parentId;				// 上级部门ID，自身的外键

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date builtDate;					// 部门的成立时间

    private Integer level;					// 部门的级别（一级部门，二级部门）

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;				// 部门信息的添加时间

    private String description;				// 企业部门信息备注

    private String supervisorId;		    // 部门主管，关联对应的雇员编号

    private String companyId;				// 对应所属的公司

    public String createSearchQuery() {
        StringBuffer queryString = new StringBuffer("from Department d where 1=1");
        if (companyId != null) {
            queryString.append(" and d.companyId=" + companyId);
        }
        if (parentId != null) {
            queryString.append(" and d.parentId=" + parentId);
        }
        if (!StringUtils.isBlank(name)) {
            queryString.append(" and d.name like '%" + name.trim() + "%'");
        }
        return queryString.toString();
    }

}
