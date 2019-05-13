package zqit.activiti.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	RepositoryService repositoryService;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;
	
	/**
	 * 开启工作流
	 * @param name
	 * @return
	 */
	@GetMapping("/startProcess/{name}")
    public String startProcess(@PathVariable("name") String name) {
		
		System.out.println("开启一个工作流 流程");
		
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("apply",name);
        map.put("approve","boss");
        //流程启动
        ExecutionEntity pi = (ExecutionEntity) runtimeService.startProcessInstanceByKey("myProcess",map);
        String processId = pi.getId();
        
        if(StringUtils.isEmpty(processId)){
        	System.out.println("请假工作流初始化失败 - 请假人("+name+")");
        	return "请假工作流初始化失败 - 请假人("+name+")";
        }else{
        	System.out.println("请假工作流初始化成功 - 请假人("+name+") - 流程ID("+processId+")");
        	return "请假工作流初始化成功 - 请假人("+name+") - 流程ID("+processId+")";
        }
        
    }
	
	/**
	 * 获取当前task
	 * @param name
	 * @return
	 */
	@GetMapping("/getTask/{name}")
	public String getTask(@PathVariable("name") String name){
		System.out.println("查询当前流程任务Task - 查询人("+name+")");
		
		List<Task> taskList = taskService.createTaskQuery().taskAssignee(name).list();
		taskList.forEach(s-> System.out.println("任务ID("+s.getId()+") - 任务名("+s.getName()+")"));
		
		String str = "";
		for(Task t : taskList){
			String id = t.getId();
			String taskName = t.getName();
			str += "taskID("+id+")   -   taskName("+taskName+")\n"; 
		}
		return str;
	}
	
	/**
	 * 提交请假申请
	 * @param taskId
	 * @return
	 */
	@GetMapping("/completeApplyTask/{taskId}")
	public String completeApplyTask(@PathVariable("taskId") String taskId){
		String s = "";
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			taskService.complete(taskId, map);//提交请假申请
			s = "提交申请完成 - taskID("+taskId+")";
		}catch (Exception e) {
			s = "提交申请失败 - taskID("+taskId+")";
		}
		System.out.println(s);
		return s;
	}
	
	/**
	 * 主管审核
	 * @param taskId
	 * @param pass
	 * @return
	 */
	@GetMapping("/completeApproveTask/{taskId}")
	public String completeApplyTask(@PathVariable("taskId") String taskId,@RequestParam("pass") Boolean pass){
		String s = "";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pass", pass);
		try{
			taskService.complete(taskId, map);//提交请假申请
			s = "主管审核完成，审核结果("+pass+")";
		}catch (Exception e) {
			s = "主管审核失败，审核结果("+pass+")";
		}
		System.out.println(s);
		return s;
	}
	
}
