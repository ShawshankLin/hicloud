package cn.edu.cylg.cis.hicloud.core.validates;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 必填
 * @author Jeanzhou
 * @version
 *		1.0 2015年3月22日下午7:54:12
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredField {
	public String message();
}
