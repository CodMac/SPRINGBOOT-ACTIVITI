package zqit.activiti.pulgins.activiti;

import java.io.IOException;

import javax.sql.DataSource;

import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ActivitiConfig extends AbstractProcessEngineAutoConfiguration {

	/**
	 * [Activiti]
	 * 
	 * 7大接口： 
	 * RepositoryService：提供一系列管理流程部署和流程定义的API。
	 * RuntimeService：在流程运行时对流程实例进行管理与控制。
	 * TaskService：对流程任务进行管理，例如任务提醒、任务完成和创建任务等。
	 * IdentityService：提供对流程角色数据进行管理的API，这些角色数据包括用户组、用户及它们之间的关系。
	 * ManagementService：提供对流程引擎进行管理和维护的服务。
	 * HistoryService：对流程的历史数据进行操作，包括查询、删除这些历史数据。 
	 * FormService：表单服务。
	 * 
	 * Activiti自带的28张表： 
	 * ACT_RE_*:repository，流程定义和流程静态资源 （图片，规则，等等）
	 * ACT_RU_*:runtime，在流程运行时保存数据，流程结束时清除 
	 * ACT_RU_VARIABLE：更新流程信息，一个工作流只有一个流程
	 * ACT_RU_TASK：更新任务信息，一个流程有多个任务，该表对每个任务只做更新，不保留任务轨迹历史数据 
	 * ACT_HI_*: history，包含历史数据，任务轨迹等 
	 * ACT_GE_*: 通用数据，如存放资源文件等 
	 * ACT_ID_*: ‘ID’表示identity。
	 * 这些表包含身份信息，比如用户，组等等
	 */

	@Autowired
	DataSource dataSource;

	@Bean("platformTransactionManager")
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean("processEngineConfiguration")
	public SpringProcessEngineConfiguration processEngineConfiguration(
			@Qualifier("platformTransactionManager") PlatformTransactionManager transactionManager,
			SpringAsyncExecutor springAsyncExecutor) throws IOException {
		SpringProcessEngineConfiguration processEngineConfiguration = this.baseSpringProcessEngineConfiguration(dataSource, transactionManager, springAsyncExecutor);

		// 流程引擎启动和关闭时数据库执行的策略
		// false：false为默认值，设置为该值后，Activiti在启动时，会对比数据库表中保存的版本，如果没有表或者版本不匹配时，将在启动时抛出异常。
		// true：设置为该值后，Activiti会对数据库中所有的表进行更新，如果表不存在，则Activiti会自动创建。
		// create-drop：Activiti启动时，会执行数据库表的创建操作，在Activiti关闭时，执行数据库表的删除操作。
		// drop-create：Activiti启动时，执行数据库表的删除操作在Activiti关闭时，会执行数据库表的创建操作。
		processEngineConfiguration.setDatabaseSchemaUpdate("drop-create");
		// 保存历史数据级别设置为full最高级别，便于历史数据的追溯
		// none：不保存任何的历史数据，因此，在流程执行过程中，这是最高效的。
		// activity：级别高于none，保存流程实例与流程行为，其他数据不保存。
		// audit：除activity级别会保存的数据外，还会保存全部的流程任务及其属性。audit为history的默认值。
		// full：保存历史数据的最高级别，除了会保存audit级别的数据外，还会保存其他全部流程相关的细节数据，包括一些流程参数等
		processEngineConfiguration.setHistoryLevel(HistoryLevel.FULL);

		return processEngineConfiguration;
	}

}
