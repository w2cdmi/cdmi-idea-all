package pw.cdmi.open.service;

import pw.cdmi.collection.PageView;
import pw.cdmi.open.model.PeopleIDCard;
import pw.cdmi.open.model.entities.People;

public interface PeopleService {
    /**
     * 新增一条公民信息
     * 
     * @param people 公民身份证信息对象实体
     * @return 返回新增公民信息的编号（id）
     */
    public String createPeople(PeopleIDCard idcard);

    /**
     * 判断公民信息是否存在
     * 
     * @param people 公民信息对象实体
     * @return true表示公民信息已存在
     */
    public boolean existPeople(People people);

    /**
     * 修改公民信息
     * 
     * @param people 公民信息对象实体
     */
    public void updatePeople(People people);

    /**
     * 根据id删除公民信息
     * 
     * @param id 公民信息编号
     */
    public void deletePeopleById(String id);

    /**
     * 根据公民信息编号获取公民信息对象实体
     * 
     * @param id 公民信息编号
     * @return 返回公民对象实体
     */
    public People getPeopleById(String id);

    /**
     * 根据公民的身份证信息获得公民信息
     * 
     * @param idCard 公民的身份证
     * @return 返回公民对象实体
     */
    public People getPeopleByIdCode(String idCard);

    /**
     * 根据公民的社保号信息获得公民信息
     * 
     * @param idCard 公民的社保号
     * @return 返回公民对象实体
     */
    public People getPeopleBySocialCode(String socialCode);

    /**
     * 根据公民的驾驶证信息获得公民信息
     * 
     * @param driverNumber 公民的驾驶证号
     * @return 返回公民对象实体
     */
    public People getPeopleByDriverLicenseNumber(String driverNumber);

    /**
     * 根据公民的护照信息获得公民信息
     * 
     * @param passportNumber 公民的护照号
     * @return 返回公民对象实体
     */
    public People getPeopleByPassportNumber(String passportNumber);

    /**
     * 根据公民的几个关键信息获得公民信息,必须有一个参数不为空
     * 
     * @param idCard 公民的身份证
     * @param passportNumber 公民的护照号
     * @param socialSecurityCode 公民的社保号
     * @param driverLicenseNumber 公民的驾驶证
     * 
     * @return 返回公民对象实体
     */
    public People getPeopleByKeyFields(String idCard, String passportNumber, String socialSecurityCode,
        String driverLicenseNumber);

    /**
     * 根据外键SiteUser编号获取公民信息对象实体
     * 
     * @param userId 用户在SiteUser中的编号
     * @return 返回公民对象实体
     */
    public People getPeopleByUserId(long userId);

    /**
     * 根据外键UserAccount编号获取公民信息对象实体
     * 
     * @param accountId 用户在UserAccount中的编号
     * @return 返回公民对象实体
     */
    public People getPeopleByAccountId(long accountId);

    /**
     * 查找所有公民信息列表
     * 
     * @return 返回公民信息对象列表
     */
    public Iterable<People> findAllPeople();

    /**
     * 根据条件查询公民信息列表并进行分页展示
     * 
     * @param pageNo 页码
     * @param pageSize 每页最大显示数
     * @param queryObject 条件参数封装的对象
     * @return 返回PageView对象，封装了当前查询页的记录列表和待查询数据的总记录数
     */
    public PageView findPeopleByConditionAndPage(int pageNo, int pageSize, People queryObject);
}
