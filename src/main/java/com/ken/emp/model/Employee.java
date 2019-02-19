package com.ken.emp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
/**
 * This class represents employees properties as an entity with 
 * DB and JSON compatibility
 * 
 * @author cmenerip
 *
 */
@Entity(name = "EMPLOYEE")
public class Employee {

	// ID - Unique identifier for an employee
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_gen")
	@SequenceGenerator(name = "employee_gen", sequenceName = "employee_seq", allocationSize = 25)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;

	// FirstName - Employees first name
	private String firstName;

	// MiddleInitial - Employees middle initial
	private String middleInitial;

	// LastName - Employee last name
	private String lastName;

	// DateOfBirth - Employee birthday and year
	@ApiModelProperty(required = true,example = "10/10/2016")
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date dateOfBirth;

	// DateOfEmployment - Employee start date
	@ApiModelProperty(required = true,example = "10/10/2016")
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date dateOfEmployment;

	// Status - ACTIVE or INACTIVE
	@Column(name="status", nullable = false)
	private Status status;

	/*
	 * empty constructor
	 */
	public Employee() {

	}

	/**
	 * constructor
	 * 
	 * @param firstName
	 * @param middleInitial
	 * @param lastName
	 * @param dateOfBirth
	 * @param dateOfEmployment
	 * @param status
	 */
	public Employee(String firstName, String middleInitial, String lastName, Date dateOfBirth, Date dateOfEmployment,
			Status status) {
		super();
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.dateOfEmployment = dateOfEmployment;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getDateOfEmployment() {
		return dateOfEmployment;
	}

	public void setDateOfEmployment(Date dateOfEmployment) {
		this.dateOfEmployment = dateOfEmployment;
	}

	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}