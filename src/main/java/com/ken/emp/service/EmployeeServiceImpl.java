package com.ken.emp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ken.emp.model.Employee;
import com.ken.emp.model.Status;
import com.ken.emp.repository.EmployeeRepository;
/**
 * Implementation of {@link EmployeeService} interface
 * 
 * @author cmenerip
 *
 */
@Component
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public void saveAll(List<Employee> empls) {

		employeeRepository.saveAll(empls);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Employee> getAllActiveEmployees() {
		return (List<Employee>) employeeRepository.findAllEmployeeByStatus(Status.ACTIVE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(Employee employee) {
		employeeRepository.save(employee);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Employee> getActiveEmployee(long id) {
		return employeeRepository.findEmployeeByIdAndStatus(id, Status.ACTIVE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delete(long id) {
		Optional<Employee> emp = employeeRepository.findById(id);
		if (emp.isPresent()) {
			employeeRepository.delete(emp.get());
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Employee> getEmployee(long id) {
		return employeeRepository.findById(id);
	}

}
