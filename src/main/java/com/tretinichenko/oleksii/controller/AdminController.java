package com.tretinichenko.oleksii.controller;

import java.text.ParseException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tretinichenko.oleksii.dao.AccountDAO;
import com.tretinichenko.oleksii.dao.EmployeeDAO;
import com.tretinichenko.oleksii.dao.ProjectDAO;
import com.tretinichenko.oleksii.entity.Employee;
import com.tretinichenko.oleksii.entity.Project;
import com.tretinichenko.oleksii.util.DateUtil;
//import com.tretinichenko.oleksii.service.AdminService;

@Controller
public class AdminController {
	

//	@Autowired  // not sure about that
//	private AdminService adminService;
	
//	@Autowired
//	private AccountDAO accountDAO;
	
	@Autowired
	private EmployeeDAO employeeDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	

	
	@RequestMapping(value = {"/admin", "/admin/admin", "/admin/"},
			method = RequestMethod.GET)
	public String viewAdminPage(){
		// admin dashboard
		return "/admin/admin";
	}
	
	
	@RequestMapping(value = "/admin/listEmployees", method = RequestMethod.GET)
	public String listEmployees(Model model){
		List<Employee> listEmployees = employeeDAO.listAllEmployees();
		model.addAttribute("listEmployees", listEmployees);
		return "/admin/listEmployees";
	}
	
	@RequestMapping(value = "/admin/editEmployee", method = RequestMethod.GET)
	public String editEmployee(Model model,
			@RequestParam(value = "employeeId", defaultValue = "0")
	int employeeId){
		Employee employee = null;
		// if there is employee with such id ?
		employee = employeeDAO.findEmployeeById(employeeId);
//		if( employee == null){
//			employee = new Employee();
//		}
		model.addAttribute("employee", employee);
		return "/admin/editEmployee";
	}
	
	@RequestMapping(value = "/admin/deleteEmployee", method = RequestMethod.GET)
	public String deleteEmployee(Model model,
			@RequestParam("employeeId") int employeeId){
		employeeDAO.deleteEmployeeById(employeeId);
		return "redirect:/admin/listEmployees";
	}
	
	@RequestMapping(value = "/admin/createEmployee", method = RequestMethod.GET)
	public String createEmployee() {
		return "/admin/createEmployee";
	}
	
	@RequestMapping(value = "/admin/createEmployee", method = RequestMethod.POST)
	public String createEmployee(Model model, 
			@RequestParam("id") int id,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam(value = "managerId", defaultValue = "0")
				int managerId) {
		Employee employee = new Employee(id, name, email, managerId);
		employeeDAO.saveEmployee(employee);
		return "redirect:/admin/listEmployees";
	}
	
	@RequestMapping(value = "/admin/updateEmployee", method = RequestMethod.POST)
	public String updateEmployee(Model model, 
			@RequestParam("id") int id,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("managerId") int managerId) {
		Employee employee = new Employee(id, name, email, managerId);
		employeeDAO.updateEmployee(employee);
		return "redirect:/admin/listEmployees";
	}
	
	
	
	
	@RequestMapping(value = "/admin/listProjects", method = RequestMethod.GET)
	public String listProjects(Model model){
		List<Project> listProjects = projectDAO.listAllProjects();
		model.addAttribute("listProjects", listProjects);
		return "/admin/listProjects";
	}
	
	@RequestMapping(value = "/admin/editProject", method = RequestMethod.GET)
	public String editProject(Model model, 
			@RequestParam ("projectId") int id){
		Project project = projectDAO.findProjectById(id);
		model.addAttribute("project", project);
		return "/admin/editProject";
	}
	
	@RequestMapping(value = "/admin/updateProject", method = RequestMethod.POST)
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
			project = new Project(id, name,
					DateUtil.parseStringDate(startDate),
					DateUtil.parseStringDate(endDate),
					projectManagerId, company, customer);
		} catch (ParseException e) {
			// do not know what to do
			e.printStackTrace();
		}
		projectDAO.updateProject(project);
		return "redirect:/admin/listProjects";
	}
	
	@RequestMapping(value = "/admin/deleteProject", method = RequestMethod.GET)
	public String deleteProject(Model model,
			@RequestParam("projectId") int projectId){
		projectDAO.deleteProjectById(projectId);
		return "redirect:/admin/listProjects";
	}
	
	@RequestMapping(value = "/admin/createProject", method = RequestMethod.GET)
	public String createProject() {
		return "/admin/createProject";
	}
	
	@RequestMapping(value = "/admin/createProject", method = RequestMethod.POST)
	public String createProject(Model model, 
			@RequestParam("id") int id,
			@RequestParam("name") String name,
			@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate,
			@RequestParam("company") String company,
			@RequestParam("customer") String customer,
			@RequestParam("projectManagerId") int projectManagerId) {
		Project project = null;
		try {
			project = new Project(id, name,
					DateUtil.parseStringDate(startDate),
					DateUtil.parseStringDate(endDate),
					projectManagerId, company, customer);
		} catch (ParseException e) {
			// do not know what to do
			e.printStackTrace();
		}
		projectDAO.saveProject(project);
		return "redirect:/admin/listProjects";
	}
}
