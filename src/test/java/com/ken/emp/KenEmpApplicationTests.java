package com.ken.emp;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.restassured.response.Response;

/**
 * Test case for employee services using rest-assured.
 * 
 * It included below functionality testing.
 * 
 * -> Get employees by an ID 
 * -> Create new employees 
 * -> Update existing employees 
 * -> Delete employees 
 * -> Get all employees
 * 
 * static/emps.csv file has been used as initial load to 
 * test all these test case.
 * 
 * Please check {@link KenEmpApplication} class for more info.
 * 
 * @author cmenerip
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = KenEmpApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class KenEmpApplicationTests {

	private static final String EMPLOYEE_URL_PATH = "/kenEmp/api/v1/employees";
	private static final String PROTOCOL = "http://";
	private static final String HOST = "localhost:";

	// Inject running port for the server
	@Value("${local.server.port}")
	private int port;

	/**
	 * This test-case tests get employee by their id
	 */
	@Test
	public void testGetEmpById() {
		Response response = given().when().get(PROTOCOL + HOST + port + EMPLOYEE_URL_PATH).then()
				.statusCode(HttpStatus.OK.value()).extract().response();
		// get all IDs of active employees
		List<Object> ids = response.jsonPath().getList("id");
		assertTrue(!ids.isEmpty() && ids.size() > 0);
		response = given().when().get(PROTOCOL + HOST + port + EMPLOYEE_URL_PATH + "/" + (int) ids.get(0)).then()
				.statusCode(HttpStatus.OK.value()).extract().response();
		/*
		 * Get particular employee and check his name is empty or not to verify
		 * that service is returning the same employee with detail.
		 */
		assertTrue(!response.jsonPath().getString("firstName").isEmpty()
				&& response.jsonPath().getInt("id") == (int) ids.get(0));
	}

	/**
	 * This test-case tests creation of a new employee data
	 */
	@Test
	public void testCreateEmp() {
		createNewEmployee();
	}

	/**
	 * This test-case tests update of employee data
	 */
	@Test
	public void testUpdateEmp() {
		int id = createNewEmployee();

		// Verify update function by changing originally created employee's
		// first name from
		// Arely to ArelyUp
		Response response = given().when().contentType(MediaType.APPLICATION_JSON)
				.body("{\"id\":\"" + id + "\",\"firstName\":\"ArelyUp\"," + "\"middleInitial\":\"C.C\","
						+ "\"status\":\"ACTIVE\"," + "\"lastName\":\"Castro\"," + "\"dateOfBirth\":\"10/10/1987\","
						+ "\"dateOfEmployment\":\"10/10/2018\"}")
				.put(PROTOCOL + HOST + port + EMPLOYEE_URL_PATH).then().statusCode(HttpStatus.OK.value()).extract()
				.response();

		assertTrue(!response.jsonPath().getString("firstName").isEmpty()
				&& response.jsonPath().getString("firstName").equals("ArelyUp")
				&& response.jsonPath().getInt("id") == id);

	}

	/**
	 * This test-case tests delete of employee data (which is turn employee from
	 * ACTIVE to INACTIVE)
	 */
	@Test
	public void testDeleteEmp() {
		int id = createNewEmployee();

		// Verify unauthorized case
		given().when().contentType(MediaType.APPLICATION_JSON)
				.body("{\"id\":\"" + id + "\",\"firstName\":\"ArelyUp\"," + "\"middleInitial\":\"C.C\","
						+ "\"status\":\"ACTIVE\"," + "\"lastName\":\"Castro\"," + "\"dateOfBirth\":\"10/10/1987\","
						+ "\"dateOfEmployment\":\"10/10/2018\"}")
				.delete(PROTOCOL + HOST + port + EMPLOYEE_URL_PATH).then().statusCode(HttpStatus.UNAUTHORIZED.value());

		// Verify authorized case
		Response response = given().auth().preemptive().basic("dilan", "password").when()
				.contentType(MediaType.APPLICATION_JSON)
				.body("{\"id\":\"" + id + "\",\"firstName\":\"ArelyUp\"," + "\"middleInitial\":\"C.C\","
						+ "\"status\":\"ACTIVE\"," + "\"lastName\":\"Castro\"," + "\"dateOfBirth\":\"10/10/1987\","
						+ "\"dateOfEmployment\":\"10/10/2018\"}")
				.delete(PROTOCOL + HOST + port + EMPLOYEE_URL_PATH).then().statusCode(HttpStatus.OK.value()).extract()
				.response();

		assertTrue(!response.jsonPath().getString("firstName").isEmpty()
				&& response.jsonPath().getString("status").equals("INACTIVE")
				&& response.jsonPath().getInt("id") == id);
	}

	/**
	 * This test-case tests delete of employee data (which is completely remove
	 * employee from DB)
	 */
	@Test
	public void testDeleteEmpById() {

		int id = createNewEmployee();
		given().when().delete(PROTOCOL + HOST + port + EMPLOYEE_URL_PATH + "/" + id).then()
				.statusCode(HttpStatus.OK.value());
	}

	/**
	 * This method test Get all employees functionality.
	 * 
	 * after initial load (with two records in static/emps.csv file)
	 * 
	 * send GET request /employees URL path and verifies that two records are
	 * exist.
	 * 
	 */
	@Test
	public void testGetAllEmps() {
		Response response = given().when().get(PROTOCOL + HOST + port + EMPLOYEE_URL_PATH).then()
				.statusCode(HttpStatus.OK.value()).extract().response();

		List<Object> jsonResponse = response.jsonPath().getList("$");
		assertTrue(!jsonResponse.isEmpty() && jsonResponse.size() > 1);
	}

	/**
	 * Create new employee in DB and return its assigned id
	 * 
	 * @return id return employee id assigned by application
	 */
	private int createNewEmployee() {
		Response response = given().when().contentType(MediaType.APPLICATION_JSON)
				.body("{\"firstName\":\"Arely\"," + "\"middleInitial\":\"C.C\"," + "\"lastName\":\"Castro\","
						+ "\"dateOfBirth\":\"10/10/1987\"," + "\"dateOfEmployment\":\"10/10/2018\"}")
				.post(PROTOCOL + HOST + port + EMPLOYEE_URL_PATH).then().statusCode(HttpStatus.CREATED.value())
				.extract().response();
		int id = response.jsonPath().getInt("id");

		assertTrue(!response.jsonPath().getString("firstName").isEmpty()
				&& response.jsonPath().getString("firstName").equals("Arely") && id > 0
				&& response.jsonPath().getString("status").equals("ACTIVE"));

		return id;
	}
}
