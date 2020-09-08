package pw.cdmi.open.model.queryObject;

import lombok.Data;
import pw.cdmi.open.model.WorkStatus;

@Data
public class EmployeeQuery {

    private String companyId;   		// 对应公司Id

    private String deptId; 			// 对应部门Id

    private String positionId;		 	// 对应岗位Id

    private String positionalTitleId;	// 对应职称Id

    public String createSearchQuery() {
        StringBuffer queryString = new StringBuffer("from Employee o where 1=1");
        if (companyId != null) {
            queryString.append(" and o.companyId=" + companyId);
        }
        if (deptId != null) {
            queryString.append(" and o.deptId =" + deptId);
        }
        if (positionId != null) {
            queryString.append(" and o.positionId=" + positionId);
        }
        if (positionalTitleId != null) {
            queryString.append(" and o.positionalTitleId=" + positionalTitleId);
        }
        queryString.append(" and o.status <> '" + WorkStatus.Quited.toString() + "'");
        return queryString.toString();
    }

    public String createQuery() {
        StringBuffer queryString = new StringBuffer("select count(*) from Employee o where 1=1");
        if (companyId != null) {
            queryString.append(" and o.companyId=" + companyId);
        }
        if (deptId != null) {
            queryString.append(" and o.deptId =" + deptId);
        }
        if (positionId != null) {
            queryString.append(" and o.positionId=" + positionId);
        }
        if (positionalTitleId != null) {
            queryString.append(" and o.positionalTitleId=" + positionalTitleId);
        }
        queryString.append(" and o.status <> '" + WorkStatus.Quited.toString() + "'");
        return queryString.toString();
    }
}