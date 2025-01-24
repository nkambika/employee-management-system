package com.myproject.services;

import com.myproject.models.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    void saveEmployee(Employee employee);
    Employee getEmployeeById(Long id);
    void deleteEmployee(Long id);
    Page<Employee> findPagination(int pageNo, int pageSize, String sortBy, String sortDirection);
}
