package pw.cdmi.open.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.ClientReason;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.open.model.entities.MobileVerification;
import pw.cdmi.open.repositories.MobileVerificationRepository;
import pw.cdmi.open.service.MobileVerificationService;
import pw.cdmi.open.utils.MMSUtils;
import pw.cdmi.utils.DateUtils;
import pw.cdmi.utils.RandomUtils;

@Service
public class MobileVerificationServiceImpl implements MobileVerificationService {

	@Autowired
	private MobileVerificationRepository daoImpl;

	@Override
	public String makeMobileCode(String mobile, String template) {
		MobileVerification mv = new MobileVerification();

		String code = RandomUtils.randomNumber(6); // 生成一个随机数字，长度为6.
		String content = template;// TODO 需要将模版内容替换成为实际内容，如在短信尾部增加［优炫软件］

		if (MMSUtils.sendMMS(mobile, content)) {// 发送短信, 成功继续执行
			// FIXME
			mv.setSiteId(null);
			mv.setCode(code);
			mv.setCreateTime(new Date());
			mv.setExpireTime(10);// 失效时间10分钟
			mv.setMobile(mobile);
			daoImpl.save(mv);
		}
		return code;
	}

	@Override
	public boolean validity(String mobile, String code) {
		MobileVerification mv = daoImpl.findOneByMobileAndCode(mobile, code);
		if (mv != null) {
			Date time = mv.getCreateTime();
			int expire = mv.getExpireTime();
			if (DateUtils.getMinuteDispersion(new Date(), time) > expire) {
				daoImpl.delete(mv);// 将失效信息删除
				throw new AWSClientException(GlobalHttpClientError.InvalidParameter, ClientReason.InvalidParameter);
			}
			return true;
		}
		return false;
	}

}
