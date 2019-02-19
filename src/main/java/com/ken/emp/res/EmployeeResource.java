package com.ken.emp.res;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ken.emp.model.Employee;
import com.ken.emp.model.Status;
import com.ken.emp.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * This class implements employee resource and its end-points such as
 * GET,POST,PUT and DELETE
 * 
 * @author cmenerip
 *
 */
@Component
@Path("/employees")
@Api(value = "employees")
public class EmployeeResource {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeResource.class);

	@Autowired
	private EmployeeService employeeService;

	/**
	 * Get all active employees
	 * 
	 * @return list of active employees as response
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get all active employees", response = Employee.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retreive employees"),
			@ApiResponse(code = 500, message = "Unexpected error") })
	public Response getAllEmployees() {

		return Response.ok().entity(employeeService.getAllActiveEmployees()).build();
	}

	/**
	 * Get an active employee by its id
	 * 
	 * @return employee as response and HTTP/OK 200, HTTP/NOT_FOUNF 404 on
	 *         employee not found
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get an active employee by its id", response = Employee.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retreive the employee"),
			@ApiResponse(code = 404, message = "Resource not found for this id"),
			@ApiResponse(code = 500, message = "Unexpected error") })
	public Response getEmployee(@PathParam("id") long id) {

		Optional<Employee> emp = employeeService.getActiveEmployee(id);
		if (emp.isPresent()) {
			return Response.ok().entity(emp).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Resource not found for id = " + id).build();
		}
	}

	/**
	 * Create a new employee resource
	 * 
	 * @param employee
	 *            employee to be created
	 * @return employee as response with id assigned and HTTP/CREATED 201 on
	 *         success, HTTP/INTERNAL_SERVER_ERROR 500 on exception
	 */
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new employee resource", response = Employee.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Successfully created the employee"),
			@ApiResponse(code = 500, message = "Unexpected error") })
	public Response newEmployee(Employee employee) {
		// FIXME if resource already exist ?
		String error = null;
		try {
			if (employee.getStatus() == null) {
				employee.setStatus(Status.ACTIVE);
			}
			employeeService.save(employee);
			return Response.status(Response.Status.CREATED).entity(employee).build();
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getMessage();
			logger.error(error, e);

		}

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();

	}

	/**
	 * Update employee resource
	 * 
	 * @param employee
	 *            employee to be updated
	 * @return updated employee as response and HTTP/OK 201 on success,
	 *         HTTP/INTERNAL_SERVER_ERROR 500 on exception and HTTP/NOT_FOUNF
	 *         404 on employee not found
	 */
	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update employee resource", response = Employee.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully update the employee"),
			@ApiResponse(code = 404, message = "Resource not found for this id"),
			@ApiResponse(code = 500, message = "Unexpected error") })
	public Response updateEmployee(Employee employee) {
		Optional<Employee> emp = employeeService.getEmployee(employee.getId());
		String error = null;
		if (emp.isPresent()) {
			try {
				employeeService.save(employee);
				return Response.ok().entity(employee).build();
			} catch (Exception e) {
				e.printStackTrace();
				error = e.getMessage();
				logger.error(error, e);

			}
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Resource not found for id = " + employee.getId())
					.build();

		}

	}

	/**
	 * Remove employee resource, and delete it from database
	 * 
	 * @param employee
	 *            employee to be deleted
	 * @return updated employee as response and HTTP/OK 200 on success,
	 *         HTTP/NOT_FOUNF 404 on employee not found
	 */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Remove employee resource, and delete it from database", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully delete the employee"),
			@ApiResponse(code = 404, message = "Resource not found for this id"),
			@ApiResponse(code = 500, message = "Unexpected error") })
	public Response delete(@PathParam("id") long id) {

		if (employeeService.delete(id)) {
			return Response.ok().entity("Resource  (id = " + id + ") deleted successfully.").build();
		}
		return Response.status(Response.Status.NOT_FOUND).entity("Resource not found for id = " + id).build();
	}

	/**
	 * Turn employee from ACTIVE to INACTIVE status
	 * 
	 * @param employee
	 *            employee to be in-activated
	 * @return updated employee as response and HTTP/OK 201 on success,
	 *         HTTP/INTERNAL_SERVER_ERROR 500 on exception and HTTP/NOT_FOUNF
	 *         404 on employee not found
	 */
	@DELETE
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Turn employee from ACTIVE to INACTIVE status", response = Employee.class, authorizations = {
			@Authorization(value = "basicAuth") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully changed the status of the employee"),
			@ApiResponse(code = 404, message = "Resource not found for this id"),
			@ApiResponse(code = 500, message = "Unexpected error") })
	public Response delete(Employee employee) {

		Optional<Employee> emp = employeeService.getEmployee(employee.getId());
		String error = null;
		if (emp.isPresent()) {
			try {
				employee.setStatus(Status.INACTIVE);
				employeeService.save(employee);
				return Response.ok().entity(employee).build();
			} catch (Exception e) {
				e.printStackTrace();
				error = e.getMessage();
				logger.error(error, e);

			}
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Resource not found for id = " + employee.getId())
					.build();

		}
	}

}
