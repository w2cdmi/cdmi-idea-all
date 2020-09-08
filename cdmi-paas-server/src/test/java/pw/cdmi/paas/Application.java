package pw.cdmi.paas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import pw.cdmi.core.db.MongoRepositoryBean;

/**
 * @author No.11
 *
 */

@SpringBootApplication
@ComponentScan(basePackages = { "pw.cdmi.open", "pw.cdmi.wechat", "pw.cdmi.wishlist", "pw.cdmi.paas", "pw.cdmi.msm" })
@EnableJpaRepositories(basePackages = { "pw.cdmi.open", "pw.cdmi.wechat", "pw.cdmi.wishlist", "pw.cdmi.paas",
		"pw.cdmi.msm" }, excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = MongoRepositoryBean.class))
@EntityScan(basePackages = { "pw.cdmi.open", "pw.cdmi.wechat", "pw.cdmi.wishlist", "pw.cdmi.paas", "pw.cdmi.msm" })
@EnableTransactionManagement
@EnableCaching
public class Application {

	public static void main(String[] args) throws Exception {
		// Log4jUtils.init(); // # 日志初始化
		// BeanUtils.init(); //# Spring bean初始化
		SpringApplication.run(Application.class, args);
	}
}
