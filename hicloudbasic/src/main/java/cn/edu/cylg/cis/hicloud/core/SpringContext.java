package cn.edu.cylg.cis.hicloud.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * 获取spring容器，以访问容器中定义的其他bean
 * @author Jeanzhou
 *
 */
public class SpringContext{
	
	//Spring应用上下文环境
	private static ApplicationContext applicationContext;

	/**
	 * 实现ApplicationContextAware接口的回调方法，设置上下文环境
	 */
	public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContext.applicationContext = applicationContext;
	}

	//获取spring应用上下文
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/** 
     * 获取对象 
     * 这里重写了bean方法，起主要作用 
     * @param beanName 
     * @return Object 一个以所给名字注册的bean的实例 
     * @throws BeansException 
     */
	public static <T> T getBean(String beanName) {
		return (T)applicationContext.getBean(beanName);
	}
	
	/** 
     * 获取对象 
     * 这里重写了bean方法，起主要作用 
     * @param beanName 
     * @return Object 一个以所给名字注册的bean的实例 
     * @throws BeansException 
     */
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}
}
