package zqit.activiti.pulgins.mvc;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * mvc 配置
 * 
 * @author mc
 *
 */
@ServletComponentScan // 扫描Filter等servlet容器
@ComponentScan
@Configurable
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	// 这种方式会在默认的基础上增加/static/**映射到classpath:/static/，不会影响默认的方式，可以同时使用。
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}

	//添加跨域允许
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedMethods("*")
		.allowedOrigins("*")
		.allowedHeaders("*");
		super.addCorsMappings(registry);
	}

}
