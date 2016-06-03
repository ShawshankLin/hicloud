package cn.edu.cylg.cis.hicloud.core.validates;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注此属性可校验
 * @author Jeanzhou
 * @version
 *		1.0 2015年3月22日下午3:52:07
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validation {
	
}
