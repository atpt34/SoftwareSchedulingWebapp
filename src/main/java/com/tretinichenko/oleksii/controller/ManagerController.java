package com.tretinichenko.oleksii.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tretinichenko.oleksii.dao.EmployeeDAO;
import com.tretinichenko.oleksii.dao.EmployeeRequestDAO;
import com.tretinichenko.oleksii.dao.ProjectDAO;
import com.tretinichenko.oleksii.dao.ProjectServiceDAO;
import com.tretinichenko.oleksii.dao.SprintDAO;
import com.tretinichenko.oleksii.dao.TaskAssignmentDAO;
import com.tretinichenko.oleksii.dao.TaskDAO;
import com.tretinichenko.oleksii.dao.TaskDependencyDAO;
import com.tretinichenko.oleksii.entity.EmployeeRequest;
import com.tretinichenko.oleksii.entity.Project;
import com.tretinichenko.oleksii.entity.Sprint;
import com.tretinichenko.oleksii.entity.Task;
import com.tretinichenko.oleksii.entity.TaskAssignment;
import com.tretinichenko.oleksii.entity.TaskDependency;
import com.tretinichenko.oleksii.model.EmployeeOvertimeActual;
import com.tretinichenko.oleksii.model.EmployeeOvertimeEstimated;
import com.tretinichenko.oleksii.model.HumanHoursBySprint;

@Controller
public class ManagerController {

	@Autowired
	private SprintDAO sprintDAO;
	
	@Autowired
	private TaskDAO taskDAO;
	
	@Autowired
	private TaskDependencyDAO taskDependencyDAO;
	
	@Autowired
	private TaskAssignmentDAO taskAssignmentDAO;
//	
//	@Autowired
//	private EmployeeDAO employeeDAO;
	
	@Autowired
	private EmployeeRequestDAO employeeRequestDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private ProjectServiceDAO projectServiceDAO;
	
	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	
	@RequestMapping(value = {"/manager", "/manager/", "/manager/home"}, method = RequestMethod.GET)
	public String viewManagerPage(){
		// manager page dashboard
		return "/manager/manager";
	}
	
	
	
	@RequestMapping(value = "/manager/listProjects", method = RequestMethod.GET)
	public String listProjects(Model model){
		List<Project> listProjects = projectDAO.listAllProjects();
		model.addAttribute("listProjects", listProjects);
		return "/manager/listProjects";
	}
	// manager's edit project 
	@RequestMapping(value = "/manager/editProject", method = RequestMethod.GET)
	public String editProject(Model model,
			@RequestParam ("projectId") int id){
		Project project = projectDAO.findProjectById(id);
		model.addAttribute("project", project);
		return "/manager/editProject";
	}
	
	@RequestMapping(value = "/manager/updateProject", method = RequestMethod.POST)
	public String updateProject(Model model, 
			@RequestParam("id") int id,
			@RequestParam("name") String name,
			@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate,
			@RequestParam("company") String company,
			@RequestParam("customer") String customer,
			@RequestParam("projectManagerId") int projectManagerId) {
		Project project = null;
		try {
			project = new Project(id, name, new SimpleDateFormat(DATE_PATTERN).parse(startDate), 
					new SimpleDateFormat(DATE_PATTERN).parse(endDate), projectManagerId, company, customer);
		} catch (ParseException e) {
			// do not know what to do
			e.printStackTrace();
		}
		projectDAO.updateProject(project);
		return "redirect:/manager/listProjects";
	}
	
	
	
	
	@RequestMapping(value = "/manager/listSprints", method = RequestMethod.GET)
	public String listSprints(Model model){
		List<Sprint> listSprints = sprintDAO.listAllSprints();
		model.addAttribute("listSprints", listSprints);
		return "/manager/listSprints";
	}
	
	@RequestMapping(value = "/manager/editSprint", method = RequestMethod.GET)
	public String editSprint(Model model, 
			@RequestParam("sprintId") int id){
		Sprint sprint = sprintDAO.findSprintById(id);
		model.addAttribute("sprint", sprint);
		return "/manager/editSprint";
	}
	@RequestMapping(value = "/manager/deleteSprint", method = RequestMethod.GET)
	public String deleteSprint(Model model,
			@RequestParam("sprintId") int id){
		sprintDAO.deleteSprintById(id);
		return "redirect:/manager/listSprints";
	}
	@RequestMapping(value = "/manager/updateSprint", method = RequestMethod.POST)
	public String updateSprint(Model model, 
			@RequestParam("id") int id,
			@RequestParam("name") String name,
			@RequestParam("dependsOn") int dependsOn,
			@RequestParam("projectId") int projectId) {
		Sprint sprint = new Sprint(id, projectId, name, dependsOn);
		sprintDAO.updateSprint(sprint);
		return "redirect:/manager/listSprints";
	}
	
	@RequestMapping(value = "/manager/createSprint", method = RequestMethod.GET)
	public String createSprint() {
		return "/manager/createSprint";
	}
	
	@RequestMapping(value = "/manager/createSprint", method = RequestMethod.POST)
	public String createSprint(Model model, 
			@RequestParam("id") int id,
			@RequestParam("name") String name,
			@RequestParam("dependsOn") int dependsOn,
			@RequestParam("projectId") int projectId) {
		Sprint sprint = new Sprint(id, projectId, name, dependsOn);
		sprintDAO.saveSprint(sprint);
		return "redirect:/manager/listSprints";
	}
	
	
	
	@RequestMapping(value = "/manager/listTasks", method = RequestMethod.GET)
	public String listTasks(Model model){
		List<Task> listTasks = taskDAO.listAllTasks();
		model.addAttribute("listTasks", listTasks);
		return "/manager/listTasks";
	}
	
	@RequestMapping(value = "/manager/editTask", method = RequestMethod.GET)
	public String editTask(Model model, 
			@RequestParam("taskId") int id){
		Task task = taskDAO.findTaskById(id);
		model.addAttribute("task", task);
		return "/manager/editTask";
	}
	@RequestMapping(value = "/manager/deleteTask", method = RequestMethod.GET)
	public String deleteTask(Model model,
			@RequestParam("taskId") int id){
		taskDAO.deleteTaskById(id);
		return "redirect:/manager/listTasks";
	}
	@RequestMapping(value = "/manager/updateTask", method = RequestMethod.POST)
	public String updateTask(Model model, 
			@RequestParam("id") int id,
			@RequestParam("sprintId") int sprintId,
			@RequestParam("name") String name,
			@RequestParam("hoursEstimate") int hoursEstimate,
			@RequestParam("startTime") String startTime) {
		Task task = null;
		try {
			task = new Task(id, sprintId, name, new SimpleDateFormat(DATE_PATTERN).parse(startTime), hoursEstimate);
		} catch (ParseException e) {
			// TODO what to do with this ?
			throw new RuntimeException(e);
		}
		taskDAO.updateTask(task);
		return "redirect:/manager/listTasks";
	}
	
	@RequestMapping(value = "/manager/createTask", method = RequestMethod.GET)
	public String createTask() {
		return "/manager/createTask";
	}
	
	@RequestMapping(value = "/manager/createTask", method = RequestMethod.POST)
	public String createTask(Model model, 
			@RequestParam("id") int id,
			@RequestParam("sprintId") int sprintId,
			@RequestParam("name") String name,
			@RequestParam("hoursEstimate") int hoursEstimate,
			@RequestParam("startTime") String startTime) {
		Task task = null;
		try {
			task = new Task(id, sprintId, name, new SimpleDateFormat(DATE_PATTERN).parse(startTime), hoursEstimate);
		} catch (ParseException e) {
			// TODO what to do with this ?
			throw new RuntimeException(e);
		}
		taskDAO.saveTask(task);
		return "redirect:/manager/listTasks";
	}
	
	
	
	@RequestMapping(value = "/manager/listTaskDependencies",
			method = RequestMethod.GET)
	public String listTaskDependencies(Model model){
		List<TaskDependency> list = 
				taskDependencyDAO.listAllTaskDependecies(); 
		model.addAttribute("listTaskDependencies", list);
		return "/manager/listTaskDependencies";
	}
	
	@RequestMapping(value = "/manager/editTaskDependency", method = RequestMethod.GET)
	public String editTaskDependency(Model model, 
			@RequestParam("taskDependencyId") int id){
		TaskDependency taskDep = taskDependencyDAO.findTaskDependencyById(id);
		model.addAttribute("taskDependency", taskDep);
		return "/manager/editTaskDependency";
	}
	
	@RequestMapping(value = "/manager/deleteTaskDependency", method = RequestMethod.GET)
	public String deleteTaskDependency(Model model,
			@RequestParam("taskDependencyId") int id){
		taskDependencyDAO.deleteTaskDependencyById(id);
		return "redirect:/manager/listTaskDependencies";
	}
	@RequestMapping(value = "/manager/updateTaskDependency", method = RequestMethod.POST)
	public String updateTaskDependency(Model model, 
			@RequestParam("id") int id,
			@RequestParam("taskId") int taskId,
			@RequestParam("dependencyTaskId") int dependencyTaskId) {
		TaskDependency taskDep = new TaskDependency(id, taskId, dependencyTaskId);
		taskDependencyDAO.updateTaskDependency(taskDep);
		return "redirect:/manager/listTaskDependencies";
	}
	
	@RequestMapping(value = "/manager/createTaskDependency", method = RequestMethod.GET)
	public String createTaskDependency() {
		return "/manager/createTaskDependency";
	}
	
	@RequestMapping(value = "/manager/createTaskDependency", method = RequestMethod.POST)
	public String createTaskDependency(Model model, 
			@RequestParam("id") int id,
			@RequestParam("taskId") int taskId,
			@RequestParam("dependencyTaskId") int dependencyTaskId) {
		TaskDependency taskDep = new TaskDependency(id, taskId, dependencyTaskId);
		taskDependencyDAO.saveTaskDependency(taskDep);
		return "redirect:/manager/listTaskDependencies";
	}
	
	
	
	@RequestMapping(value = "/manager/listTaskAssignments", method = RequestMethod.GET)
	public String listTaskAssignments(Model model){
		List<TaskAssignment> taskAssigns = taskAssignmentDAO.listAllTaskAssignments(); 
		model.addAttribute("listTaskAssignments", taskAssigns);
		return "/manager/listTaskAssignments";
	}
	
	// only for manager ROLE
	@RequestMapping(value = "/manager/editTaskAssignment", method = RequestMethod.GET)
	public String editTaskAssignment(Model model, 
			@RequestParam("taskAssignmentId") int id){
		TaskAssignment taskAssign = taskAssignmentDAO.findTaskAssignmentById(id);
		model.addAttribute("taskAssign", taskAssign);
		return "/manager/editTaskAssignment";
	}
	
	@RequestMapping(value = "/manager/updateTaskAssignment", method = RequestMethod.POST)
	public String updateTaskAssignment(Model model, 
			@RequestParam("id") int id,
			@RequestParam("taskId") int taskId,
			@RequestParam("employeeId") int employeeId,
			@RequestParam("acceptedTime") String acceptedTime,
			@RequestParam("finishTime") String finishTime) {
		TaskAssignment taskAssign = null;
		try {
			taskAssign = new TaskAssignment(id, taskId, employeeId, 
					new SimpleDateFormat(DATE_PATTERN).parse(acceptedTime), 
					new SimpleDateFormat(DATE_PATTERN).parse(finishTime));
		} catch (ParseException e) {
			// TODO what to do with this ?
			throw new RuntimeException(e);
//			e.printStackTrace();
		}
		taskAssignmentDAO.updateTaskAssignment(taskAssign);
		return "redirect:/manager/listTaskAssignments";
	}
	
	@RequestMapping(value = "/manager/createTaskAssignment", method = RequestMethod.GET)
	public String createTaskAssignment() {
		return "/manager/createTaskAssignment";
	}
	
	@RequestMapping(value = "/manager/createTaskAssignment", method = RequestMethod.POST)
	public String createTaskAssignment(Model model, 
			@RequestParam("id") int id,
			@RequestParam("taskId") int taskId,
			@RequestParam("acceptedTime") String acceptedTime,
			@RequestParam("finishTime") String finishTime,
			@RequestParam("employeeId") int employeeId) {
		TaskAssignment taskAssign = null;
		try {
			taskAssign = new TaskAssignment(id, taskId, employeeId, 
					new SimpleDateFormat(DATE_PATTERN).parse(acceptedTime), 
					new SimpleDateFormat(DATE_PATTERN).parse(finishTime));
		} catch (ParseException e) {
			// do not know what to do
			e.printStackTrace();
		}
		taskAssignmentDAO.saveTaskAssignment(taskAssign);
		return "redirect:/manager/listTaskAssignments";
	}
	
	@RequestMapping(value = "/manager/deleteTaskAssignment", method = RequestMethod.GET)
	public String deleteTaskAssignment(Model model,
			@RequestParam("taskAssignmentId") int id){
		taskAssignmentDAO.deleteTaskAssignmentById(id);
		return "redirect:/manager/listTaskAssignments";
	}
	
	
	// requests for verifying & statistics
	
	
	@RequestMapping(value = "/manager/listRequests",
			method = RequestMethod.GET)
	public String listRequests(Model model){
		List<EmployeeRequest> listReq = 
				employeeRequestDAO.listAllEmployeeRequests();
		model.addAttribute("listEmployeeRequests", listReq);
		return "/manager/listRequests";
	}
	
	@RequestMapping(value = "/manager/deleteRequest",
			method = RequestMethod.GET)
	public String deleteRequest(Model model,
			@RequestParam("requestId") int requestId){
		employeeRequestDAO.deleteEmployeeRequestById(requestId);
		return "redirect:/manager/listRequests";
	}
	
	
	@RequestMapping(value = "/manager/projectService",
			method = RequestMethod.GET)
	public String projectService(){
		
		return "/manager/projectService";
	}
	
	

	
	@RequestMapping(value = "/manager/listEmployeeEstimatedOvertimes",
			method = RequestMethod.GET)
	public String listEstimatedOvertimes(Model model, 
			@RequestParam("projectId") int projectId) {
		List<EmployeeOvertimeEstimated> list = projectServiceDAO.employeeOvertimeEstimated(projectId);
		model.addAttribute("listEmployeeEstimatedOvertimes", list);
		return "/manager/listEmployeeEstimatedOvertimes";
	}
	@RequestMapping(value = "/manager/listHumanHoursBySprint",
			method = RequestMethod.GET)
	public String listHumanHoursBySprint(Model model,
			@RequestParam("projectId") int projectId){
		List<HumanHoursBySprint> list = projectServiceDAO.humanHoursBySprint(projectId);
		model.addAttribute("listHumanHoursBySprint", list);
		return "/manager/listHumanHoursBySprint";
	}
	@RequestMapping(value = "/manager/listEmployeeActualOvertimes",
			method = RequestMethod.GET)
	public String listEmployeeActualOvertimes(Model model,
			@RequestParam("projectId") int projectId){
		List<EmployeeOvertimeActual> list = projectServiceDAO.employeeOvertimeActual(projectId);
		model.addAttribute("listEmployeeActualOvertimes", list);
		return "/manager/listEmployeeActualOvertimes";
	}
	
}
