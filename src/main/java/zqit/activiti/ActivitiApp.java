package zqit.activiti;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ActivitiApp 
{
	public static void main(String[] args) {
		 
        SpringApplication.run(ActivitiApp.class, args);
    }
	
	/**
	 * 这可以说是Activiti自动配置类中的一个错误。它依赖于它们TaskExecutor在应用程序上下文中唯一的一个bean，或者如果有多个bean，则它们中的一个是主要的。
	 * 您应该能够通过声明自己的TaskExecutorbean并将其标记为@Primary：解决该问题。
	 * @return
	 */
	@Primary
	@Bean
	public TaskExecutor primaryTaskExecutor() {
	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	    return executor;
	}
}
