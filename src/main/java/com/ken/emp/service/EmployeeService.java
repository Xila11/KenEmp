package com.ken.emp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ken.emp.model.Employee;

/**
 * Employee service interface for all functionality
 * 
 * @author cmenerip
 *
 */
@Component
public interface EmployeeService {

	/**
	 * save list of employees
	 * 
	 * @param empls
	 *            list of employees to be saved
	 */
	public void saveAll(List<Employee> empls);

	/**
	 * Save an employee
	 * 
	 * @param employee
	 *            employee to be saved
	 */
	public void save(Employee employee);

	/**
	 * Get all active employees
	 * 
	 * @return list of active employees
	 */
	public List<Employee> getAllActiveEmployees();

	/**
	 * Get employee by id
	 * 
	 * @param id
	 *            id of the employee
	 * 
	 * @return optional object with employee
	 */
	public Optional<Employee> getEmployee(long id);

	/**
	 * Delete employee from database
	 * 
	 * @param id id of employee to be deleted
	 * @return true if employee was removed successfully, otherwise false
	 */
	public boolean delete(long id);

	/**
	 * Get active employee
	 * 
	 * @param id id of the employee
	 * @return optional object with employee
	 */
	public Optional<Employee> getActiveEmployee(long id);

}
