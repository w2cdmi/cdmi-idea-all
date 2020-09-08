package pw.cdmi.open.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能权限，用来标示需要进行功能权限检验的一个注解
 * @author wuwei
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MothedAuth {
	String group() default "";		//权限标示所处的分组编号或者标示
	String auth();	  				//权限标示（系统中在同一个模块中唯一）
	String name();	  				//权限名称
}
