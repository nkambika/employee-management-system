package com.myproject.controllers;

import com.myproject.models.Employee;
import com.myproject.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {
    @Autowired private EmployeeService employeeService;
    @GetMapping("/dashboard")
    public String viewEmployeeDashboard(Model model){
        return findPaginated(1, "name", "asc", model);
    }
    @GetMapping("/registration-form")
    public String addEmployee(Model model){
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "registration-form";
    }
    @PostMapping("/save-employee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee){
        employeeService.saveEmployee(employee);
        return "redirect:/dashboard";
    }
    @GetMapping("/show-update-form/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "update-form";
    }
    @GetMapping("/delete-employee/{id}")
    public String deleteEmployee(@PathVariable(value = "id") Long id){
        employeeService.deleteEmployee(id);
        return "redirect:/dashboard?success";
    }
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable("pageNo") int pageNo,
                                @RequestParam("sortBy") String sortBy,
                                @RequestParam("sortDirection") String sortDirection,
                                Model model)
    {
        int pageSize = 5;
        Page<Employee> page = employeeService.findPagination(pageNo, pageSize, sortBy, sortDirection);
        List<Employee> employeeList = page.getContent();
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("empList",employeeList);
        return "dashboard";
    }
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
