package pw.cdmi.open.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限，用来标示需要进行数据权限检验的一个注解
 * 不同的应用需要继承该注解，自行补充其数据权限内容
 * @author wuwei
 *
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataAuth {
	
	String model() default "";		//权限标示所处的分组编号或者标示
	String auth();	  				//权限标示（系统中在同一个模块中唯一）
	String title();	  				//权限说明
	String query();	  				//获得可用的数据权限值
}
