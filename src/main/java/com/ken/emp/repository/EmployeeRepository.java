package com.ken.emp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ken.emp.model.Employee;
import com.ken.emp.model.Status;

/**
 * Employee repository to support JPA data access layer
 * 
 * @author cmenerip
 *
 */
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

	/**
	 * Retrieve an employee using given id and status
	 * 
	 * @param id id of an employee
	 * @param status status of an employee
	 * 
	 * @return Optional object with employee
	 */
	@Query("SELECT emp FROM EMPLOYEE emp where emp.id = :id and emp.status = :status")
	Optional<Employee> findEmployeeByIdAndStatus(@Param("id") Long id, @Param("status") Status status);
	
	/**
	 * Retrieve all employees using given status
	 * @param status status of the employees
	 * 
	 * @return list of employees filtered with given status
	 */
	@Query("SELECT emp FROM EMPLOYEE emp where emp.status = :status")
	Iterable<Employee> findAllEmployeeByStatus(@Param("status") Status status);

}
