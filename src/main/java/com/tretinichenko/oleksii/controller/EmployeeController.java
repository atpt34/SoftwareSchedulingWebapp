package com.tretinichenko.oleksii.controller;

import java.text.ParseException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tretinichenko.oleksii.dao.EmployeeRequestDAO;
import com.tretinichenko.oleksii.dao.TaskAssignmentDAO;
import com.tretinichenko.oleksii.dao.TaskDAO;
import com.tretinichenko.oleksii.entity.Employee;
import com.tretinichenko.oleksii.entity.EmployeeRequest;
import com.tretinichenko.oleksii.entity.TaskAssignment;
import com.tretinichenko.oleksii.util.DateUtil;

@Controller
public class EmployeeController {
	
	@Autowired
	private TaskAssignmentDAO taskAssignmentDAO;
//	@Autowired
//	private TaskDAO taskDAO;  // for task details
	@Autowired
	private EmployeeRequestDAO employeeRequestDAO;
	
	
	@RequestMapping(value = {"/employee/employee", "/employee", "/employee/"},
			method = RequestMethod.GET)
	public String viewEmployeePage(){
		// employee page
		return "/employee/employee";
	}

	@RequestMapping(value = "/employee/listTaskAssignments",
			method = RequestMethod.GET)
	public String listTaskAssignments(Model model){
		List<TaskAssignment> taskAssigns = 
				taskAssignmentDAO.listAllTaskAssignments();  // employeeId
		model.addAttribute("taskAssigns", taskAssigns);
		return "/employee/listTaskAssignments";
	}
	
	// only for EMPLOYEE ROLE
	@RequestMapping(value = "/employee/editTaskAssignment", 
			method = RequestMethod.GET)
	public String editTaskAssignment(Model model, 
			@RequestParam("taskAssignmentId") int id){
		TaskAssignment taskAssign = 
				taskAssignmentDAO.findTaskAssignmentById(id);
		model.addAttribute("taskAssign", taskAssign);
		return "/employee/editTaskAssignment";
	}
	
	@RequestMapping(value = "/employee/updateTaskAssignment", 
			method = RequestMethod.POST)
	public String updateTaskAssignment(Model model, 
			@RequestParam("id") int id,
			@RequestParam("taskId") int taskId,
			@RequestParam("employeeId") int employeeId,
			@RequestParam("acceptedTime") String acceptedTime,
			@RequestParam("finishTime") String finishTime) {
		TaskAssignment taskAssign = null;
		try {
			taskAssign = new TaskAssignment(id, taskId, employeeId, 
					DateUtil.parseStringDate(acceptedTime),
					DateUtil.parseStringDate(finishTime));
		} catch (ParseException e) {
			// TODO what to do with this ?
			throw new RuntimeException(e);
//			e.printStackTrace();
		}
		taskAssignmentDAO.updateTaskAssignment(taskAssign);
		return "redirect:/employee/listTaskAssignments";
	}
	
	
	@RequestMapping(value = "/employee/createRequest", 
			method = RequestMethod.GET)
	public String createRequest() {
		return "/employee/createRequest";
	}
	
	@RequestMapping(value = "/employee/createRequest", 
			method = RequestMethod.POST)
	public String createRequest(Model model, 
			@RequestParam("id") int id,
			@RequestParam("taskId") int taskId,
			@RequestParam("hours") int hours,
			@RequestParam("employeeId") int employeeId) {
		EmployeeRequest request = 
				new EmployeeRequest(id, taskId, employeeId, hours);
		employeeRequestDAO.saveEmployeeRequest(request);
		return "redirect:/employee/listTaskAssignments";
	}
	
	// other methods:
	@RequestMapping(value = "/employee/taskDetails", 
			method = RequestMethod.GET)
	public String viewTaskDetails(){
		// employee can request task details
		return "/employee/taskDetails";
	}
}
