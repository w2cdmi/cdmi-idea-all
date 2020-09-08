package pw.cdmi.open.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import pw.cdmi.core.http.exception.AWSServiceException;
import pw.cdmi.core.http.exception.SystemReason;
import pw.cdmi.open.model.entities.AuditPrivilege;
import pw.cdmi.open.repositories.AuditPrivilegeRepository;
import pw.cdmi.open.service.AuditManageService;
/**
 * **********************************************************
 * 审计管理的实现类
 * 
 * @author liujh
 * @version iSoc Service Platform, 2015-5-5
 ***********************************************************
 */
public class AuditManageServiceImpl implements AuditManageService {

	@Autowired
	private AuditPrivilegeRepository  repository;
	
    @Override
    @Transactional
    public void createAuditPrivailege(AuditPrivilege auditPrivilege) {
        try {
        	repository.save(auditPrivilege);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AWSServiceException(SystemReason.SQLError);
        }

    }

    @Override
    @Transactional
    public void deleteAuditPrivilegeById(String id) {
        try {
            AuditPrivilege ap = getAuditPrivilegeById(id);
            if(ap != null){
            	repository.delete(ap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new AWSServiceException(SystemReason.SQLError);
        }
    }

    @Override
    public AuditPrivilege getAuditPrivilegeById(String id) {
        AuditPrivilege auditPrivilege = null;
        try {
            auditPrivilege = repository.findById(id).get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AWSServiceException(SystemReason.SQLError);
        }
        return auditPrivilege;
    }

    @Override
    public Iterable<AuditPrivilege> findAllAuditPrivilege() {
        Iterable<AuditPrivilege> list = new ArrayList<AuditPrivilege>();
        try {
            list = repository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AWSServiceException(SystemReason.SQLError);
        }
        return list;
    }

    @Override
    public List<AuditPrivilege> findAuditPrivilegeByPage(int pageNo, int pageSize) {
        try {
        	Sort sort = new Sort(Direction.DESC, "id");
            Pageable pageable = new PageRequest(pageNo, pageSize, sort);
            Page<AuditPrivilege> result = repository.findAll(pageable);
            return result.getContent();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AWSServiceException(SystemReason.SQLError);
        }
    }

    @Override
    public List<AuditPrivilege> searchAuditPrivilegeList(AuditPrivilege auditPrivilege) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<AuditPrivilege> searchAuditPrivilegeListPage(AuditPrivilege auditPrivilege, int pageNo, int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }

}
