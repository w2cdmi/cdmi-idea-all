package pw.cdmi.open.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.AWSServiceException;
import pw.cdmi.core.http.exception.ClientError;
import pw.cdmi.core.http.exception.ClientReason;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.core.http.exception.SystemReason;
import pw.cdmi.open.model.PeopleIDCard;
import pw.cdmi.open.model.WorkStatus;
import pw.cdmi.open.model.entities.Area;
import pw.cdmi.open.model.entities.Commissioner;
import pw.cdmi.open.model.entities.Company;
import pw.cdmi.open.model.entities.Department;
import pw.cdmi.open.model.entities.DepartmentGroup;
import pw.cdmi.open.model.entities.Employee;
import pw.cdmi.open.model.entities.EmployeeAndCommissioner;
import pw.cdmi.open.model.entities.EmployeeAndDeptGroup;
import pw.cdmi.open.model.entities.EmployeeAndOffice;
import pw.cdmi.open.model.entities.Office;
import pw.cdmi.open.model.entities.People;
import pw.cdmi.open.model.entities.Position;
import pw.cdmi.open.model.entities.PositionalTitle;
import pw.cdmi.open.model.queryObject.EmployeeQuery;
import pw.cdmi.open.repositories.AreaRepository;
import pw.cdmi.open.repositories.CommissionerRepository;
import pw.cdmi.open.repositories.CompanyRepository;
import pw.cdmi.open.repositories.DepartmentGroupRepository;
import pw.cdmi.open.repositories.DepartmentRepository;
import pw.cdmi.open.repositories.EmployeeAndCommissionerRepository;
import pw.cdmi.open.repositories.EmployeeAndDeptGroupRepository;
import pw.cdmi.open.repositories.EmployeeAndOfficeRepository;
import pw.cdmi.open.repositories.EmployeeRepository;
import pw.cdmi.open.repositories.OfficeRepository;
import pw.cdmi.open.repositories.PositionRepository;
import pw.cdmi.open.repositories.PositionalTitleRepository;
import pw.cdmi.open.service.BusinessOrganizationService;
import pw.cdmi.open.service.EmployeeService;
import pw.cdmi.open.service.PeopleService;
import pw.cdmi.paas.account.model.entities.UserAccount;
import pw.cdmi.paas.account.service.UserService;
import pw.cdmi.utils.UUIDUtils;

/**
 * ********************************************************** 企业组织的实现类
 * 
 * @author liujh
 * @version cas Service Platform, 2015-5-7
 ***********************************************************
 */
@Service("BusinessOrganizationService")
public class BusinessOrganizatinServiceImpl implements BusinessOrganizationService {

	private static final Logger log = LoggerFactory.getLogger(BusinessOrganizatinServiceImpl.class);

	@Autowired
	private CompanyRepository daoImpl;

	@Autowired
	private OfficeRepository officeRepository;

	@Autowired
	private DepartmentRepository deptRepository;

	@Autowired
	private DepartmentGroupRepository deptGroupRepository;

	@Autowired
	private CommissionerRepository commissionerRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private PositionalTitleRepository positionalTitleRepository;

	@Autowired
	private EmployeeAndCommissionerRepository employeeAndCommissionerRepository;

	@Autowired
	private EmployeeAndDeptGroupRepository employeeAndDeptGroupRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private EmployeeAndOfficeRepository employeeAndOfficeRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PeopleService peopleService;
	
	@Autowired
	private EmployeeService employeeService;

	@Transactional
	@Override
	public void createCompany(Company company) {
		String appId = "";
		// 判断企业的几个关键唯一的信息是否在系统中存在，存在，则不会被保存
		Company comp = this.getCompanyByKeyFields(company.getName(), company.getCodeCertificate(),
				company.getLicenseNumber(), company.getTaxCode());
		if (comp != null) {
			throw new AWSClientException(ClientError.DataAlreadyExists, ClientReason.DataAlreadyExists);
		}

		if (StringUtils.isEmpty(company.getName())) {
			throw new AWSClientException(GlobalHttpClientError.IncompleteBody, ClientReason.IncompleteBody);
		}
		company.setOpenId(UUIDUtils.getUUIDTo64());
		company.setAppId(appId);
		company.setCreateTime(new Date());
		company.setId(null);
		daoImpl.save(company);
	}

	@Transactional
	@Override
	public void updateCompany(Company company) {
		Company comp = daoImpl.findById(company.getId()).get();
		if (comp == null) {
			throw new AWSClientException(ClientError.NoFoundData, ClientReason.NoFoundData);
		}
		// TODO 企业的几个关键唯一信息是不允许修改的。
		daoImpl.save(company);
	}

	@Transactional
	@Override
	public boolean deleteCompany(String id) {
		boolean b = false;
		try {
			Company comp = getCompany(id);
			if (comp != null) {
				/*
				 * Department queryObject = new Department(); queryObject.setCompanyId(id);
				 * List<Department> deptList = findDepartmentList(queryObject); for (Department
				 * dept : deptList) { jpaImpl.delete(dept); }
				 */
				daoImpl.delete(comp);
				b = true;
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return b;
	}

	@Override
	public Company getCompany(String id) {
		Company company = null;
		try {
			company = daoImpl.findById(id).get();
		} catch (NoResultException e) {
			log.info("没有找到相关的公司信息！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return company;
	}

	@Override
	public Company getCompanyByName(String companyName) {
		return daoImpl.findByNameLike(companyName);
	}

	@Override
	public Company getCompanyByOpenId(String openId) {
		return daoImpl.findOneByOpenId(openId);
	}

	@Override
	public Company getCompanyByRegistrationName(String registrationName) {
		return daoImpl.findByBusinessRegistrationNameLike(registrationName);
	}

	@Override
	public Company getCompanyByTaxCode(String taxCode) {
		return daoImpl.findByTaxCode(taxCode);
	}

	@Override
	public Company getCompanyByKeyFields(String name, String licenseNumber, String codeCertificate, String taxCode) {
		StringBuilder hql = new StringBuilder("from Company where ");
		if (!StringUtils.isEmpty(name)) {
			hql.append("name = '" + name + "'");
		}
		if (!StringUtils.isEmpty(licenseNumber)) {
			if ((!StringUtils.isEmpty(name))) {
				hql.append(" or licenseNumber ='" + licenseNumber + "'");
			} else {
				hql.append(" licenseNumber ='" + licenseNumber + "'");
			}
		}
		if (!StringUtils.isEmpty(codeCertificate)) {
			if (!StringUtils.isEmpty(name) || !StringUtils.isEmpty(licenseNumber)) {
				hql.append(" or codeCertificate ='" + codeCertificate + "'");
			} else {
				hql.append(" codeCertificate ='" + codeCertificate + "'");
			}
		}
		if (!StringUtils.isEmpty(taxCode)) {
			if (!StringUtils.isEmpty(name) || !StringUtils.isEmpty(licenseNumber)
					|| !StringUtils.isEmpty(codeCertificate)) {
				hql.append(" or taxCode ='" + taxCode + "'");
			} else {
				hql.append(" taxCode ='" + taxCode + "'");
			}
		}
		return null;
	}

	@Override
	public String getCurrentCompanyId() {
		String appId = "";
		Company company = daoImpl.findOneByAppId(appId);
		if (company == null) {
			throw new AWSClientException(ClientError.NoFoundData, ClientReason.NoFoundData);
		}
		return company.getId();
	}

	@Override
	public Company getCurrentCompany() {
		String appId = "";
		Company company = daoImpl.findOneByAppId(appId);
		if (company == null) {
			throw new AWSClientException(ClientError.NoFoundData, ClientReason.NoFoundData);
		}
		return company;
	}

	@Override
	public List<String> getCurrentAndSubCompanyIds() {

		return null;
	}

	@Override
	public Company getSuperCompany() {
		String current = null;
		Company company = null;
		try {
			company = daoImpl.findById(current).get();
			company = daoImpl.findById(company.getParentId()).get();
		} catch (NoResultException e) {
			log.info("没有找到相关的公司信息！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return company;
	}

	@Override
	public Iterable<Company> findAllCompany() {
		Iterable<Company> list = new ArrayList<Company>();
		try {
			list = daoImpl.findAll();
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	@Override
	public Iterable<Company> findParentCompany() {
		Iterable<Company> list = null;
		try {
			list = daoImpl.findByParentIdNotNull();
		} catch (NoResultException e) {
			log.info("没有找到相关的公司列表！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	@Override
	public Page<Company> findCompanyList(Company company, int pageNo, int pageSize) {
		try {
			ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("businessRegistrationName")
					.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnorePaths("name")
					.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

			Example<Company> example = Example.of(company, matcher);
			Pageable pageable = new PageRequest(pageNo, pageSize);
			return daoImpl.findAll(example, pageable);
		} catch (NoResultException e) {
			log.info("没有找到相关的公司列表！");
			return null;
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);

		}
	}

	@Override
	public Page<Company> findAllCompany(int pageNo, int pageSize) {
		try {
			Pageable pageable = new PageRequest(pageNo, pageSize);
			return daoImpl.findAll(pageable);
		} catch (NoResultException e) {
			log.info("没有找到相关的公司列表！");
			return null;
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public Iterable<Company> findSubCompanyList(Company company) {
		company.setParentId(this.getCurrentCompanyId());
		Iterable<Company> list = null;
		try {
			ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("businessRegistrationName")
					.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnorePaths("name")
					.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

			Example<Company> example = Example.of(company, matcher);
			list = daoImpl.findAll(example);

		} catch (NoResultException e) {
			log.info("没有找到相关的公司列表！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	@Override
	public Iterable<Company> findSubCompanyListByEmployeeId(String employeeId) {
		try {
			return daoImpl.findBySupervisorId(employeeId);
		} catch (NoResultException e) {
			log.info("没有找到相关的公司列表！");
			return null;
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public Page<Company> findSubCompanyListPage(Company company, int pageNo, int pageSize) {
		// FIXME
		company.setParentId(this.getCurrentCompanyId());
		try {
			ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("businessRegistrationName")
					.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnorePaths("name")
					.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

			Example<Company> example = Example.of(company, matcher);
			Pageable pageable = new PageRequest(pageNo, pageSize);
			return daoImpl.findAll(example, pageable);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void createOffice(Office office) {
		// 办事处的名称不能重复
		String companyId = office.getCompanyId();
		if (companyId == null) {
			companyId = this.getCurrentCompanyId();
			office.setCompanyId(companyId);
		}
		Office o = this.getOfficeByName(companyId, office.getName());
		if (o != null) {
			throw new AWSClientException(ClientError.DataAlreadyExists, ClientReason.DataAlreadyExists);
		}
		office.setCompanyId(companyId);
		office.setId(null);
		try {
			officeRepository.save(office);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void updateOffice(Office office) {
		// 办事处的名称不能重复
		String companyId = office.getCompanyId();
		if (companyId == null) {
			companyId = this.getCurrentCompanyId();
		}
		Office o = this.getOfficeByName(companyId, office.getName());
		if (o != null && o.getId() != office.getId() && o.getId().equals(office.getId())) {
			throw new AWSClientException(ClientError.DataAlreadyExists, ClientReason.DataAlreadyExists);
		}
		try {
			officeRepository.save(office);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public boolean deleteOffice(String id) {
		boolean b = false;
		try {
			Office office = getOffice(id);
			if (office != null) {
				deleteEmployeeAndOfficeByOfficeId(id);
				officeRepository.delete(office);
				b = true;
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return b;
	}

	@Override
	public Office getOffice(String id) {
		Office office = null;
		try {
			office = officeRepository.findById(id).get();
		} catch (NoResultException e) {
			log.info("没有找到相关的公司信息！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return office;
	}

	@Override
	public Office getOfficeByName(String companyId, String officeName) {
		return officeRepository.findOneByCompanyIdAndName(companyId, officeName);
	}

	@Override
	public Iterable<Office> findAllOffice() {
		try {
			return officeRepository.findAllByCompanyId(this.getCurrentCompanyId());
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Deprecated
	@Override
	public Page<Office> findAllOffice(int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Office> findOfficeByKeyword(String keyword) {
		try {
			if (keyword == null || keyword == "") {
				return findAllOffice();
			}
			return officeRepository.findAllByCompanyIdAndNameLike(this.getCurrentCompanyId(), keyword);
		} catch (NoResultException e) {
			log.info("没有找到相关的公司列表！");
			return null;
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public Iterable<Office> findOfficeByEmployeeId(String employeeId) {
		try {
			return officeRepository.findAllBySupervisorId(employeeId);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void createDepartment(Department department) {
		if (department.getCompanyId() == null) {
			department.setCompanyId(this.getCurrentCompanyId());
		}
		department.setId(null);
		if (department.getParentId() != null) {
			department.setParentId(null);
		}
		// 需判断同一个ParentId下，部门的名称是否存在重复
		Department d = this.getDepartmentByName(this.getCurrentCompanyId(), department.getParentId(),
				department.getName());
		if (d != null) {
			throw new AWSClientException(ClientError.DataAlreadyExists, ClientReason.DataAlreadyExists);
		}
		try {
			deptRepository.save(department);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void updateDepartment(Department department) {
		department.setCompanyId(this.getCurrentCompanyId());
		if (department.getParentId() != null) {
			department.setParentId(null);
		}
		// 需判断同一个ParentId下，部门的名称是否存在重复
		Department d = this.getDepartmentByName(this.getCurrentCompanyId(), department.getParentId(),
				department.getName());
		if (d != null && !d.getId().equals(department.getId())) {
			throw new AWSClientException(ClientError.DataAlreadyExists, ClientReason.DataAlreadyExists);
		}
		try {
			deptRepository.save(department);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public boolean deleteDepartment(String id) {
		boolean b = false;
		Department queryObject = new Department();
		try {
			Department dept = getDepartment(id);
			if (dept != null) {
				queryObject.setParentId(id);
				Iterable<Department> deptList = findDepartmentList(queryObject);
				Iterable<DepartmentGroup> deptGroupList = findDepartmentGroupByDeptId(id);
				for (Department department : deptList) {
					// jpaImpl.delete(department);
					deleteDepartment(department.getId());
				}
				for (DepartmentGroup deptGroup : deptGroupList) {
					deptGroupRepository.delete(deptGroup);
				}
				deptRepository.delete(dept);
				b = true;
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return b;
	}

	@Override
	public Department getDepartment(String id) {
		Department department = null;
		try {
			department = deptRepository.findById(id).get();
		} catch (NoResultException e) {
			log.info("没有找到相关的部门信息！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return department;
	}

	@Override
	public Department getDepartmentByName(String companyId, String parentId, String deptName) {
		return deptRepository.findOneByCompanyIdAndNameAndParentId(companyId, deptName, parentId);
	}

	@Override
	@ResponseBody
	public Iterable<Department> findFirstDepartment() {
		try {
			return deptRepository.findAllByCompanyIdAndParentIdIsNull(this.getCurrentCompanyId());
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public Iterable<Department> findAllDepartment() {
		List<Department> list = new ArrayList<Department>();
		try {
			return deptRepository.findAllByCompanyId(this.getCurrentCompanyId());
		} catch (NoResultException e) {
			log.info("没有找到相关的部门列表！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	@Override
	public Iterable<Department> findDepartmentList(Department queryObject) {

		Iterable<Department> list = null;
		try {
			if (queryObject == null) {

				list = deptRepository.findAll();
			} else {
				Example<Department> example = Example.of(queryObject);

				queryObject.setCompanyId(this.getCurrentCompanyId());
				list = deptRepository.findAll(example);
			}
		} catch (NoResultException e) {
			log.info("没有找到相关的部门列表！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	@Override
	public Page<Department> findDepartmentListPage(Department queryObject, int pageNo, int pageSize) {
		queryObject.setCompanyId(this.getCurrentCompanyId());
		try {
			Example<Department> example = Example.of(queryObject);
			Pageable pageable = new PageRequest(pageNo, pageSize);
			return deptRepository.findAll(example, pageable);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void createDepartmentGroup(DepartmentGroup departmentGroup) {
		// 部门群组的名称在全公司范围内不能重复
		String companyId = departmentGroup.getCompanyId();
		if (companyId == null) {
			companyId = this.getCurrentCompanyId();
			departmentGroup.setCompanyId(companyId);
		}
		DepartmentGroup o = this.getDepartmentGroupByName(companyId, departmentGroup.getName());
		if (o != null) {
			throw new AWSClientException(ClientError.DataAlreadyExists, ClientReason.DataAlreadyExists);
		}
		try {
			departmentGroup.setId(null);
			deptGroupRepository.save(departmentGroup);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void updateDepartmentGroup(DepartmentGroup departmentGroup) {
		try {
			deptGroupRepository.save(departmentGroup);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public boolean deleteDepartmentGroup(String id) {
		boolean b = false;
		DepartmentGroup depg = getDepartmentGroup(id);
		try {
			if (depg != null) {
				deleteEmployeeAndDeptGroupByDeptGroup(id);
				deptGroupRepository.delete(depg);
				b = true;
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return b;
	}

	@Override
	public DepartmentGroup getDepartmentGroup(String id) {
		DepartmentGroup deptGroup = null;
		try {
			deptGroup = deptGroupRepository.findById(id).get();
		} catch (NoResultException e) {
			log.info("没有找到相关的群组信息！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return deptGroup;
	}

	@Override
	public DepartmentGroup getDepartmentGroupByName(String deptId, String deptGroupName) {
		return deptGroupRepository.findOneByDeptIdAndName(deptId, deptGroupName);
	}

	@Override
	public Iterable<DepartmentGroup> findDeptGroupByEmployeeId(String employeeId) {
		try {
			if (employeeId == null) {
				return findAllDepartmentGroup();
			}
			Iterable<EmployeeAndDeptGroup> emp_deptgroup_List = employeeAndDeptGroupRepository.findByEmployeeId(employeeId);
			Collection<DepartmentGroup> list = new ArrayList<DepartmentGroup>();
			for(EmployeeAndDeptGroup emp_deptgroup : emp_deptgroup_List) {
				list.add(deptGroupRepository.findById(emp_deptgroup.getDeptGroupId()).get());
			}
			return list;
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public Iterable<DepartmentGroup> findDepartmentGroupByDeptId(String departmentId) {
		List<DepartmentGroup> list = null;
		try {
			return deptGroupRepository.findAllByDeptId(departmentId);
		} catch (NoResultException e) {
			log.info("没有找到相关的群组列表！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	@Override
	public Iterable<DepartmentGroup> findAllDepartmentGroup() {
		try {
			return deptGroupRepository.findAll();
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public Iterable<DepartmentGroup> findDepartmentGroupByKeyword(String keyword) {
		try {
			if (keyword == null) {
				return findAllDepartmentGroup();
			} else {

				return deptGroupRepository.findByNameLike(keyword);
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void createCommissioner(Commissioner commissioner) {
		commissioner.setCompanyId(this.getCurrentCompanyId());
		commissioner.setId(null);
		try {
			commissionerRepository.save(commissioner);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void updateCommissioner(Commissioner commissioner) {
		commissioner.setCompanyId(this.getCurrentCompanyId());
		try {
			commissionerRepository.save(commissioner);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public boolean deleteCommissioner(String id) {
		boolean b = false;
		try {
			Commissioner comm = getCommissioner(id);
			if (comm != null) {
				deleteEmployeeAndCommissionerByCommissioner(id);
				commissionerRepository.delete(comm);
				b = true;
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return b;
	}

	@Override
	public Commissioner getCommissioner(String id) {
		Commissioner commissioner = null;
		try {
			commissioner = commissionerRepository.findById(id).get();
		} catch (NoResultException e) {
			log.info("没有找到相关的专员信息！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return commissioner;
	}

	@Override
	public Commissioner getCommissionerByName(String companyId, String commissionerName) {
		return commissionerRepository.findOneByCompanyIdAndName(companyId, commissionerName);
	}

	@Override
	public Iterable<Commissioner> findCommissionerByName(String commissionerName) {
		try {
			return commissionerRepository.findByCompanyIdAndName(this.getCurrentCompanyId(), commissionerName);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public Iterable<Commissioner> findAllCommissioner() {
		try {
			return commissionerRepository.findByCompanyId(this.getCurrentCompanyId());
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void createArea(Area area) {
		area.setId(null);
		area.setCompanyId(this.getCurrentCompanyId());
		try {
			areaRepository.save(area);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void updateArea(Area area) {
		Area newArea = null;
		try {
			newArea = getArea(area.getId());
			newArea.setName(area.getName());
			newArea.setSupervisorId(area.getSupervisorId());
			newArea.setDescription(area.getDescription());
			areaRepository.save(newArea);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public boolean deleteArea(String id) {
		Company company = new Company();
		boolean b = false;
		try {
			Area area = getArea(id);
			company.setAreaId(id);
			Iterable<Company> companyList = findSubCompanyList(company);
			Iterable<Office> officeList = officeRepository.findByAreaId(id);
			for (Office office : officeList) {
				if (null != office.getId()) {
					try {
						List<Employee> emps = new ArrayList<Employee>();
						String officeId = office.getId();
						emps = employeeService.findEmployeeByOfficeId(officeId);
						if (emps.size() > 0) {
							for (Employee emp : emps) {
								emp.setCompanyId(null);
								emp.setOfficeId(null);
								employeeRepository.save(emp);
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage());
						e.printStackTrace();
					}
				}
				officeRepository.delete(office);
			}
			for (Company comp : companyList) {
				if (null != comp.getId()) {
					try {
						List<Employee> emps = new ArrayList<Employee>();
						EmployeeQuery employeeQuery = new EmployeeQuery();
						employeeQuery.setCompanyId(comp.getId());
						emps = employeeService.findEmployeeByQuery(employeeQuery);
						if (emps.size() > 0) {
							for (Employee emp : emps) {
								emp.setCompanyId(null);
								employeeRepository.save(emp);
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage());
						e.printStackTrace();
					}
				}
				daoImpl.delete(comp);
			}
			areaRepository.delete(area);
			b = true;
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return b;
	}

	@Override
	public Area getArea(String id) {
		Area area = null;
		try {
			area = areaRepository.findById(id).get();
		} catch (NoResultException e) {
			log.info("没有找到相关的片区信息！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return area;
	}

	@Override
	public Iterable<Area> findAreaList(Area area) {
		area.setCompanyId(this.getCurrentCompanyId());
		try {
			Example<Area> example = Example.of(area);
			return areaRepository.findAll(example);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public Iterable<Area> findAllArea() {
		try {
			return areaRepository.findByCompanyId(this.getCurrentCompanyId());
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void createPosition(Position position) {
		position.setCompanyId(this.getCurrentCompanyId());
		position.setId(null);
		try {
			positionRepository.save(position);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void updatePosition(Position position) {
		position.setCompanyId(this.getCurrentCompanyId());
		try {
			positionRepository.save(position);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public boolean deletePosition(String id) {
		boolean b = false;
		try {
			Position pos = getPosition(id);
			if (pos != null) {
				PositionalTitle posTitle = new PositionalTitle();
				posTitle.setPositionId(id);
				Iterable<PositionalTitle> posTitleList = findPositionalTitle(posTitle);
				for (PositionalTitle postitle : posTitleList) {
					positionalTitleRepository.delete(postitle);
				}
				positionRepository.delete(pos);
				b = true;
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return b;
	}

	@Override
	public Position getPosition(String id) {
		Position position = null;
		try {
			position = positionRepository.findById(id).get();
		} catch (NoResultException e) {
			log.info("没有找到相关的岗位信息！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return position;
	}

	@Override
	public Position getPositionByName(String companyId, String positionName) {
		return positionRepository.findOneByCompanyIdAndName("companyId", "positionName");
	}

	@Override
	public Iterable<Position> findAllPosition() {
		try {
			return positionRepository.findByCompanyId(this.getCurrentCompanyId());
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public Iterable<Position> findPositionByName(String positionName) {
		try {
			return positionRepository.findByCompanyIdAndNameLike(this.getCurrentCompanyId(), positionName);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void createPositionalTitle(PositionalTitle positionalTitle) {
		try {
			positionalTitleRepository.save(positionalTitle);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void updatePositionTitle(PositionalTitle positionalTitle) {
		PositionalTitle title = null;
		try {
			title = getPositionalTitle(positionalTitle.getId());
		} catch (Exception e) {
			log.error("执行updatePositionalTitle方法出错!");
			throw new AWSServiceException(SystemReason.SQLError);
		}
		title.setName(positionalTitle.getName());
		title.setPositionId(positionalTitle.getPositionId());
		title.setDescription(positionalTitle.getDescription());
		try {
			positionalTitleRepository.save(title);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public boolean deletePositionalTitle(String id) {
		boolean b = false;
		try {
			PositionalTitle pot = getPositionalTitle(id);
			if (pot != null) {
				positionalTitleRepository.delete(pot);
				b = true;
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return b;
	}

	@Override
	public PositionalTitle getPositionalTitle(String id) {
		PositionalTitle positionalTitle = null;
		try {
			positionalTitle = positionalTitleRepository.findById(id).get();
		} catch (NoResultException e) {
			log.info("没有找到相关的职称信息！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return positionalTitle;
	}

	@Override
	public PositionalTitle getPositionalTitleByName(String companyId, String positionalTitleName) {
		return positionalTitleRepository.findOneByCompanyIdAndName(companyId, positionalTitleName);
	}

	@Override
	public Iterable<PositionalTitle> findPositionalTitle(PositionalTitle positionalTitle) {
		try {
			if (positionalTitle == null) {
				return findAllPositionalTitle();
			} else {
				Example<PositionalTitle> example = Example.of(positionalTitle);
				return positionalTitleRepository.findAll(example);
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public Iterable<PositionalTitle> findPositionalTitle(String name, String positionName) {
		try {

			if (StringUtils.isBlank(name) && StringUtils.isBlank(positionName)) {
				return findAllPositionalTitle();
			} else {

				StringBuffer hql = new StringBuffer("select pt from PositionalTitle pt");

				String positionIds = getPositionIds(positionName);
				if (StringUtils.isNotBlank(name) && StringUtils.isBlank(positionIds)) {
					hql.append(" where pt.name like '%" + name.trim() + "%' ");
				} else if (StringUtils.isBlank(name) && StringUtils.isNotBlank(positionIds)) {
					hql.append(" where pt.positionId in " + positionIds + "");
				} else {
					hql.append(" where pt.name like '%" + name.trim() + "%' and pt.positionId in " + positionIds + "");
				}

				PositionalTitle posTitle = new PositionalTitle();
				posTitle.setCompanyId(this.getCurrentCompanyId());
				posTitle.setName(positionName);
				posTitle.setPositionId(positionIds);

				ExampleMatcher matcher = ExampleMatcher.matching()
						.withMatcher("name", GenericPropertyMatchers.startsWith())
						.withMatcher("positionId", GenericPropertyMatchers.contains()).withIgnorePaths("description");
				Example<PositionalTitle> example = Example.of(posTitle, matcher);
				return positionalTitleRepository.findAll(example);
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	private String getPositionIds(String positionName) {
		if (StringUtils.isBlank(positionName)) {
			return null;
		}
		Iterable<Position> positions = findPositionByName(positionName);
		List<String> arr_list = new ArrayList<String>();
		int i = 0;
		if (positions.iterator().hasNext()) {
			arr_list.add(positions.iterator().next().getId());
			i++;
		}
		String[] list = new String[i];
		arr_list.toArray(list);
		return list.toString();
	}

	@Override
	public Iterable<PositionalTitle> findAllPositionalTitle() {
		try {
			return positionalTitleRepository.findAllByCompanyId(this.getCurrentCompanyId());
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void createEmployee(String companyId, Employee employee) {
		Employee empl = this.getEmployeeByCompanyIdAndCode(companyId, employee.getCode());
		if (empl != null) {
			throw new AWSClientException(ClientError.DataAlreadyExists, ClientReason.DataAlreadyExists);
		}
		employee.setCompanyId(companyId);
		employeeRepository.save(employee);
	}

	@Override
	@Transactional
	public void createEmployee(String companyId, Employee employee, PeopleIDCard people) {
		try {
			String newid = peopleService.createPeople(people);
			employee.setPeopleId(newid);
			employee.setId(null);
			employeeRepository.save(employee);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void updateEmployee(Employee employee) {
		employee.setCompanyId(this.getCurrentCompanyId());
		try {
			employeeRepository.save(employee);
			if (employee.getDeptManagerId() != null) {
				Department dept = this.getDepartment(employee.getDeptManagerId());
				if (dept != null) {
					dept.setSupervisorId(employee.getId());
					this.updateDepartment(dept);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	@Override
	@Transactional
	public void updateEmployee(Employee employee, People people) {
		employee.setCompanyId(this.getCurrentCompanyId());
		try {
			employeeRepository.save(employee);
			peopleService.updatePeople(people);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void deleteEmployeeById(String id) {
		try {
			Employee employee = employeeRepository.findById(id).get();
			if (employee != null) {
				employeeRepository.delete(employee);
				if (employee.getId() != null) {
					this.deleteEmployeeAndDeptGroupByEmployee(id);
					this.deleteEmployeeAndOfficeByEmployeeId(id);
					this.deleteEmployeeAndCommissionerByCommissioner(id);
					userService.deleteUserAccountByPeopleId(employee.getPeopleId());
//					userService.deleteSiteUserByEmployeeId(employee.getId());
					// FIXME 删除企业雇员时候，需要检查该雇员是否具有IT权限，有则删除IT权限
					// permissionService.deleteSiteUserPermissionByUserId(employee
					// .getId());
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	@Override
	public Map<String, Object> getEmployeeById(String id) {
		Map<String, Object> map = null;
		try {
			Employee employee = employeeRepository.findById(id).get();
			People people = peopleService.getPeopleById(employee.getPeopleId());
			UserAccount userAccount = userService.getUserAccountByPeopleId(employee.getPeopleId());
			Department dept = this.getDepartment(employee.getDeptId());
			Company company = this.getCompany(employee.getCompanyId());
			Position position = this.getPosition(employee.getPositionId());
			PositionalTitle positionalTitle = this.getPositionalTitle(employee.getPositionalTitleId());
			map = new HashMap<String, Object>();
			map.put("employee", employee);
			map.put("people", people);
			map.put("userAccount", userAccount);
			map.put("department", dept);
			map.put("company", company);
			map.put("position", position);
			map.put("positionalTitle", positionalTitle);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return map;
	}

	@Override
	public Employee getSingleEmployeeById(String id) {
		Employee employee = null;
		try {
			if (id != null) {
				employee = employeeRepository.findById(id).get();
			}
		} catch (NoResultException e) {
			log.info("没有找到相关的员工信息！");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return employee;
	}

	@Override
	public Employee getEmployeeByCompanyIdAndCode(String companyId, String code) {
		return employeeRepository.findOneByCompanyIdAndCode(companyId, code);
	}

	@Override
	public Employee getSingleEmployeeByPeopleId(String peopleId, String companyId) {
		Employee employee = null;
		
		try {
			return employeeRepository.findOneByPeopleIdAndCompanyId(peopleId, companyId);
		} catch (NoResultException e) {
			log.info("没有找到相关的员工信息！");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return employee;
	}

	@Override
	public Iterable<Employee> getAllEmployee() {
		Iterable<Employee> list = new ArrayList<Employee>();
		try {
			list = employeeRepository.findAll();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	@Override
	public Iterable<Employee> findEmployeeByCondition(String code, String name, String telephone,
			String deptId) {
		try {

			String searchHql = "SELECT e from Employee e WHERE e.status ='OK'";
			String conditionHql = getEmployeeConditionHql(code, name, telephone, deptId);
			ExampleMatcher matcher = ExampleMatcher.matching()
					.withMatcher("code", GenericPropertyMatchers.startsWith())
					.withMatcher("name", GenericPropertyMatchers.contains());

			Employee employee = new Employee();
			employee.setName(name);
			employee.setCode(code);
			employee.setDeptId(deptId);
			employee.setTelephone(telephone);
			employee.setStatus(WorkStatus.OK);
			Example<Employee> example = Example.of(employee, matcher);
			return employeeRepository.findAll(example);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public Page<Employee> findEmployeeByConditionAndPage(int pageNo, int pageSize, String code, String name,
			String telephone, String deptId) {
		try {
			ExampleMatcher matcher = ExampleMatcher.matching()
					.withMatcher("code", GenericPropertyMatchers.startsWith())
					.withMatcher("name", GenericPropertyMatchers.contains());

			Employee employee = new Employee();
			employee.setName(name);
			employee.setCode(code);
			employee.setDeptId(deptId);
			employee.setTelephone(telephone);
			employee.setStatus(WorkStatus.OK);
			Example<Employee> example = Example.of(employee, matcher);
			Pageable pageable = new PageRequest(pageNo, pageSize);
			return employeeRepository.findAll(example, pageable);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public List<Employee> findEmployeeByDeptGroupId(String deptGroupId) {
		List<Employee> employeeList = new ArrayList<Employee>();
		try {
			Iterable<EmployeeAndDeptGroup> list = employeeAndDeptGroupRepository.findByDeptGroupId(deptGroupId);
			for (EmployeeAndDeptGroup emp_group : list) {
				Employee employee = getSingleEmployeeById(emp_group.getId());
				employeeList.add(employee);
			}
		} catch (NoResultException e) {
			log.info("没有员工列表！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return employeeList;
	}

	@Override
	public int getCountEmployeeByDeptGroupId(String deptGroupId) {
		long num = 0;
		try {
			num = employeeAndDeptGroupRepository.countByDeptGroupId(deptGroupId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return (int) num;
	}

	@Override
	public List<Employee> findEmployeeByOfficeId(String officeId) {
		List<Employee> employeeList = new ArrayList<Employee>();
		try {
			Iterable<EmployeeAndOffice> list = employeeAndOfficeRepository.findByOfficeId(officeId);
			for (EmployeeAndOffice emp_office : list) {
				Employee employee = getSingleEmployeeById(emp_office.getEmployeeId());
				employeeList.add(employee);
			}
		} catch (NoResultException e) {
			log.info("没有员工列表！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return employeeList;
	}

	@Override
	public int getCountEmployeeByOfficeId(String officeId) {
		long num = 0;
		try {
			num = employeeAndOfficeRepository.countByOfficeId(officeId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return (int) num;
	}

	@Override
	public Iterable<Employee> findEmployeeNotOnOffice(String officeId, String code, String name) {
		Iterable<Employee> list = null;
		String queryString = "from Employee e where e.id not in (select ed.employeeId from EmployeeAndOffice ed where ed.officeId ="
				+ officeId + ")";
		String conditionHql = getEmployeeConditionHql(code, name, null, null);
		try {
			Iterable<EmployeeAndOffice> emp_officers = employeeAndOfficeRepository.findByOfficeId(officeId);
			Collection<String> emp_ids = new ArrayList<String>();
			for(EmployeeAndOffice emp_officer : emp_officers) {
				emp_ids.add(emp_officer.getEmployeeId());
			}
			ExampleMatcher matcher = ExampleMatcher.matching()
					.withMatcher("code", GenericPropertyMatchers.startsWith())
					.withMatcher("name", GenericPropertyMatchers.contains());

			Employee employee = new Employee();
			employee.setName(name);
			employee.setCode(code);
			Example<Employee> example = Example.of(employee, matcher);
			//FIXME
			list = employeeRepository.findAll(example);
//			
//			list = employeeRepository.find((queryString + conditionHql));
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	@Override
	public List<Employee> findEmployeeByCommissionerId(String commissionerId) {
		List<Employee> employeeList = new ArrayList<Employee>();
		try {
			Iterable<EmployeeAndCommissioner> list = employeeAndCommissionerRepository.findByCommissionerId(commissionerId);
			for (EmployeeAndCommissioner employeeAndCommissioner : list) {
				Employee employee = getSingleEmployeeById(employeeAndCommissioner.getEmployeeId());
				employeeList.add(employee);
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return employeeList;
	}

	@Override
	public int getCountEmployeeByCommissionerId(String commissionerId) {
		long num = 0;
		try {
			num = employeeAndCommissionerRepository.countByCommissionerId(commissionerId);
		} catch (NoResultException e) {
			log.info("没有员工！");
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return (int) num;
	}

	@Override
	public Iterable<Employee> findEmployeeNotIsCommissioner(String commissionerId, String code, String name) {
		Iterable<Employee> list = null;
		String queryString = "from Employee e where e.id not in (select ed.employeeId from EmployeeAndCommissioner ed where ed.commissionerId ="
				+ commissionerId + ")";
		String conditionHql = getEmployeeConditionHql(code, name, null, null);
		try {
			Iterable<EmployeeAndCommissioner> emp_commissioners = employeeAndCommissionerRepository.findByCommissionerId(commissionerId);
			Collection<String> emp_ids = new ArrayList<String>();
			for(EmployeeAndCommissioner emp_commissioner : emp_commissioners) {
				emp_ids.add(emp_commissioner.getEmployeeId());
			}
			ExampleMatcher matcher = ExampleMatcher.matching()
					.withMatcher("code", GenericPropertyMatchers.startsWith())
					.withMatcher("name", GenericPropertyMatchers.contains());

			Employee employee = new Employee();
			employee.setName(name);
			employee.setCode(code);
			Example<Employee> example = Example.of(employee, matcher);
			//FIXME
			list = employeeRepository.findAll(example);
		} catch (NoResultException e) {
			log.info("没有找到相关的员工列表！");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	@Override
	public int getCountEmployeeByQuery(Employee employee) {
		long num = 0;
		try {
			ExampleMatcher matcher = ExampleMatcher.matching()
					.withMatcher("code", GenericPropertyMatchers.startsWith())
					.withMatcher("name", GenericPropertyMatchers.contains())
					.withMatcher("telephone", GenericPropertyMatchers.contains());

			Example<Employee> example = Example.of(employee, matcher);
			num = employeeRepository.count(example);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return (int) num;
	}

	@Override
	public Iterable<Employee> findEmployeeByQuery(Employee employee) {
		try {
			ExampleMatcher matcher = ExampleMatcher.matching()
					.withMatcher("code", GenericPropertyMatchers.startsWith())
					.withMatcher("name", GenericPropertyMatchers.contains())
					.withMatcher("telephone", GenericPropertyMatchers.contains());

			Example<Employee> example = Example.of(employee, matcher);
			return employeeRepository.findAll(example);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	/**
	 * 封装Employee的查询条件语句
	 * 
	 * @param code
	 *            员工工号
	 * @param name
	 *            员工的姓名
	 * @param telephone
	 *            员工联系电话
	 * @param deptId
	 *            员工所在部门id
	 * @return 返回封装好的条件（HQL）语句
	 */
	private String getEmployeeConditionHql(String code, String name, String telephone, String deptId) {
		StringBuilder sb = new StringBuilder();
		if (!StringUtils.isBlank(code)) {
			sb.append(" and e.code like '%" + code + "%' ");
		}
		if (!StringUtils.isBlank(name)) {
			sb.append(" and e.name like '%" + name + "%' ");
		}
		if (!StringUtils.isBlank(telephone)) {
			sb.append(" and e.telephone like '%" + telephone + "%' ");
		}
		if (deptId != null) {
			sb.append(" and e.deptId = '" + deptId + "'");
		}

		return sb.toString();
	}

	@Override
	@Transactional
	public void createEmployeeAndDeptGroup(EmployeeAndDeptGroup employeeAndDeptGroup) {
		try {
			employeeAndDeptGroupRepository.save(employeeAndDeptGroup);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public EmployeeAndDeptGroup getEmployeeAndDeptGroup(String employeeId, String deptGroupId) {
		EmployeeAndDeptGroup empAndGroup = null;
		try {
			empAndGroup = employeeAndDeptGroupRepository.findOneByDeptGroupIdAndEmployeeId(deptGroupId, employeeId);
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return empAndGroup;
	}

	@Override
	@Transactional
	public int deleteEmployeeAndDeptGroup(String employeeId, String deptGroupId) {
		int num = 0;
		try {
			EmployeeAndDeptGroup employeeAndDeptGroup = getEmployeeAndDeptGroup(employeeId, deptGroupId);
			DepartmentGroup group = getDepartmentGroup(deptGroupId);
			if (employeeAndDeptGroup != null) {
				if (employeeId != group.getSupervisorId()) {
					employeeAndDeptGroupRepository.delete(employeeAndDeptGroup);
					num = 1;
				} else {

					deleteEmployeeAndDeptGroupByDeptGroup(deptGroupId);

					deleteDepartmentGroup(deptGroupId);

					num = 2;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return num;
	}

	@Override
	@Transactional
	public void deleteEmployeeAndDeptGroupByEmployee(String employeeId) {
		Iterable<EmployeeAndDeptGroup> groupList = null;
		try {
			groupList = employeeAndDeptGroupRepository.findByEmployeeId(employeeId);
				employeeAndDeptGroupRepository.deleteAll(groupList);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void deleteEmployeeAndDeptGroupByDeptGroup(String deptGroupId) {
		Iterable<EmployeeAndDeptGroup> groupList = null;
		try {
			groupList = employeeAndDeptGroupRepository.findByDeptGroupId(deptGroupId);
			for (EmployeeAndDeptGroup empAndGroup : groupList) {
				employeeAndDeptGroupRepository.delete(empAndGroup);
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void createEmployeeAndOffice(EmployeeAndOffice employeeAndOffice) {
		try {
			employeeAndOfficeRepository.save(employeeAndOffice);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public EmployeeAndOffice getEmployeeAndOffice(String employeeId, String officeId) {
		EmployeeAndOffice empAndGroup = null;
		try {
			empAndGroup = employeeAndOfficeRepository.findOneByOfficeIdAndEmployeeId(officeId, employeeId);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			return null;
		}
		return empAndGroup;
	}

	@Override
	public EmployeeAndOffice getEmployeeAndOffice(String employeeId) {
		EmployeeAndOffice empAndOffice = null;
		try {
			empAndOffice = employeeAndOfficeRepository.findOneByEmployeeId(employeeId);
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return empAndOffice;
	}

	@Override
	@Transactional
	public boolean deleteEmployeeAndOffice(String employeeId, String officeId) {
		boolean b = false;
		try {
			EmployeeAndOffice employeeAndOffice = getEmployeeAndOffice(employeeId, officeId);
			if (employeeAndOffice != null) {
				employeeAndOfficeRepository.delete(employeeAndOffice);
				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return b;
	}

	@Transactional
	@Override
	public void deleteEmployeeAndOfficeByOfficeId(String officeId) {
		Iterable<EmployeeAndOffice> list = null;
		try {
			list = employeeAndOfficeRepository.findByOfficeId(officeId);
			if (list.iterator().hasNext()) {
				employeeAndOfficeRepository.deleteAll(list);
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void deleteEmployeeAndOfficeByEmployeeId(String employeeId) {
		Iterable<EmployeeAndOffice> list = null;
		try {
			list = employeeAndOfficeRepository.findByEmployeeId(employeeId);
			if (list.iterator().hasNext()) {
				employeeAndOfficeRepository.deleteAll(list);
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void updateEmployeeAndOffice(EmployeeAndOffice employeeAndOffice) {
		try {
			employeeAndOfficeRepository.save(employeeAndOffice);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void createEmployeeAndCommissioner(EmployeeAndCommissioner employeeAndCommissioner) {
		try {
			employeeAndCommissionerRepository.save(employeeAndCommissioner);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public EmployeeAndCommissioner getEmployeeAndCommissioner(String employeeId, String commissionerId) {
		EmployeeAndCommissioner empAndGroup = null;
		try {
			empAndGroup = employeeAndCommissionerRepository.findOneByCommissionerIdAndEmployeeId(commissionerId,
					employeeId);
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return empAndGroup;
	}

	@Override
	@Transactional
	public boolean deleteEmployeeAndCommissioner(String employeeId, String commissionerId) {
		boolean b = false;
		try {
			EmployeeAndCommissioner employeeAndCommissioner = getEmployeeAndCommissioner(employeeId, commissionerId);
			if (employeeAndCommissioner != null) {
				employeeAndCommissionerRepository.delete(employeeAndCommissioner);
				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return b;
	}

	@Transactional
	@Override
	public void deleteEmployeeAndCommissionerByCommissioner(String commissionerId) {
		Iterable<EmployeeAndCommissioner> list = null;
		try {
			list = employeeAndCommissionerRepository.findByCommissionerId(commissionerId);
			employeeAndCommissionerRepository.deleteAll(list);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Transactional
	@Override
	public void deleteEmployeeAndCommissioner(String employeeId) {
		Iterable<EmployeeAndCommissioner> list = null;
		try {
			list = employeeAndCommissionerRepository.findByEmployeeId(employeeId);
			if (list.iterator().hasNext()) {
				employeeAndCommissionerRepository.deleteAll(list);
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public void fillBankInfo(String openId, String bankid, String bankname, String accountName, String accountNumber) {
		Company company = companyRepository.findOneByOpenId(openId);
		if (company == null) {
			throw new AWSClientException(GlobalHttpClientError.ResourceNotFound, SystemReason.InvalidRequest);
		}
		try {
			company.setBankAccountName(accountName);
			company.setBankName(bankname);
			company.setBankId(bankid);
			company.setBankAccountNubmer(accountNumber);
			companyRepository.save(company);
		} catch (Exception e) {
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public Employee getEmployeeByAccountId(String accountId) {
		return employeeRepository.findOneByAccountId(accountId);
	}

}
