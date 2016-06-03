package cn.edu.cylg.cis.hicloud.core.validates;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 校验
 * @author Jeanzhou
 * @version
 *		1.0 2015年3月22日下午8:14:57
 */
public class ValidateUtils {

	private ValidateUtils() {
		
	}
	
	/**
	 * 校验
	 * @param t
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InstantiationException 
	 */
	public static <T> List<String> validate(T t){
		return validate(t, null);
	}
	
	/**
	 * 校验
	 * @param t
	 * @param excludeSet - 剔除校验的属性名
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InstantiationException 
	 */
	public static <T> List<String> validate(T t, Set<String> excludeSet) {
		List<String> errorList = new ArrayList<String>();
		try {
			validate(errorList, t, excludeSet);
		}catch(Exception e) {
		}
		return errorList;
	}
	
	private static <T> void validate(List<String> errorList, T t, Set<String> excludeSet) throws IllegalArgumentException, IllegalAccessException {
		Class clazz = t.getClass();
		
		if(!clazz.getSuperclass().getSimpleName().equals("Object")) {
			validate(errorList, clazz.getSuperclass(), excludeSet);
		}
		
		Field[] fieldArr = clazz.getDeclaredFields();
		for(Field field : fieldArr) {
			if(excludeSet != null && excludeSet.contains(field.getName())) {
				continue;
			}
			Annotation[] annArr = field.getAnnotations();
			if(annArr != null) {
				if(!isValidation(annArr)) {
					continue;
				}
				field.setAccessible(true);
				Object fieldValue = field.get(t);
				for(Annotation annotation : annArr) {
					String typeName = annotation.annotationType().getSimpleName();
					try {
						switch(ValidateType.valueOf(typeName)) {
							case RequiredField : 
								if(!validateRequiredField(fieldValue)) {
									errorList.add(((RequiredField)annotation).message());
								}
								break;
						}
					}catch(Exception e) {
						
					}
				}
			}
		}
	}

	/**
	 * 是否可为校验
	 * @param annArr
	 * @return
	 */
	private static boolean isValidation(Annotation[] annArr) {
		boolean flag = false;
		for(Annotation annotation : annArr) {
			if(annotation instanceof Validation) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 校验是否为空
	 * @param object
	 * @return
	 */
	public static boolean validateRequiredField(Object object) {
		return object instanceof String ? (object != null && !"".equals(object)) : object != null;
	}
}
