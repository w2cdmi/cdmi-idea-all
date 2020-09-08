package pw.cdmi.open.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/************************************************************
 * 片区信息实体
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-6
 ************************************************************/
@NamedQueries({ @NamedQuery(name = "Area.findAll", query = "from Area a"),
        @NamedQuery(name = "Area.findAllByCompany", query = "from Area a where a.companyId=?") })
@Data
@Entity
@Table(name = "ept_area")
public class Area {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String id;					// 片区信息编号

    @Column(length = 50, nullable = false)
    private String name;			// 片区名称

    private String supervisorId;		// 片区主管，关联对应雇员信息编号

    private String description;		// 片区备注

    private String companyId;       // 总公司ID

    public String createSearchQuery() {
        StringBuffer queryString = new StringBuffer("from Area a where 1=1");
        if (supervisorId != null) {
            queryString.append(" and a.supervisorId=" + supervisorId);
        }
        if (!StringUtils.isBlank(name)) {
            queryString.append(" and a.name like '%" + name.trim() + "%'");
        }
        queryString.append(" and a.companyId=" + companyId);
        return queryString.toString();
    }
}
