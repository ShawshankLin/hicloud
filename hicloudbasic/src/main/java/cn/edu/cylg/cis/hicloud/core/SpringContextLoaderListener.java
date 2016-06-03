package cn.edu.cylg.cis.hicloud.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import cn.edu.cylg.cis.hicloud.core.init.StartInit;

//@WebListener("Spring Context Loader Listener")
public class SpringContextLoaderListener extends ContextLoaderListener {
	private Logger logger = Logger.getLogger(getClass().getName());

	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		servletContext.setInitParameter("contextConfigLocation", "/spring/basic-*-context.xml");
		super.contextInitialized(event);
		
		SpringContext.setApplicationContext(getCurrentWebApplicationContext());
		
		//Add Context basePath
		servletContext.setAttribute("basePath", servletContext.getContextPath());
		
		//处理启动初始化
		ApplicationContext context = getCurrentWebApplicationContext();
		Map<String, StartInit> initMap = context.getBeansOfType(StartInit.class);
		if (initMap.size() > 0) {
			List<StartInit> initList = new ArrayList<StartInit>(initMap.values());
			Collections.sort(initList, new Comparator<StartInit>() {
				@Override
				public int compare(StartInit o1, StartInit o2) {
					return o1.getSortNo() - o2.getSortNo();
				}
			});
			for (StartInit startup : initList) {
				logger.log(Level.INFO, "正在启动初始化:"+ startup.getClass().getName());
				try{
					startup.init();
					logger.log(Level.INFO, startup.getClass().getName() + ":初始化结束");
				}catch (Exception e) {
					logger.log(Level.INFO, "初始化失败:"+ startup.getClass().getName());
					e.printStackTrace();
				}
			}
		}
	}
	
}
