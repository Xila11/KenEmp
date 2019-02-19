package com.ken.emp;

import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ken.emp.model.Employee;
import com.ken.emp.model.Status;
import com.ken.emp.service.EmployeeService;

/**
 * Main spring boot application loading class.
 * 
 * loading initial data file on start up.
 * 
 * @author cmenerip
 *
 */
@SpringBootApplication
public class KenEmpApplication {

	private static final Logger logger = LoggerFactory.getLogger(KenEmpApplication.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	private static String fileName = null;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private ResourceLoader resourceLoader;

	/**
	 * Application main method
	 * 
	 * @param args
	 *            java main method argument and --filename= can be used to pass
	 *            initial loading for production.
	 */
	public static void main(String[] args) {
		for (String input : args) {
			if (input.startsWith("--fileName")) {
				String name = input.split("=")[1];
				fileName = (name != null && name.trim().length() > 0 ? name : null);
			}
		}

		SpringApplication.run(KenEmpApplication.class, args);
	}

	/**
	 * Init method when spring boot starts.
	 */
	@PostConstruct
	private void init() {
		// loading default employees
		initialLoading();
		logger.info("Application loading finished...");
	}

	/**
	 * security encryption and decryption bean.
	 * 
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * This method implements data loading using external file as initial load
	 * for application.
	 * 
	 * File should be CSV comma separated format with header as in sample of
	 * static/emps.csv
	 * 
	 * skip header, read line by line and map them into a {@link Employee}
	 * object and persist directly into DB.
	 */
	private void initialLoading() {

		Resource banner = null;
		// If file is provided by outside, it will process it otherwise it will
		// process default file that used for testing.
		if (fileName != null) {
			logger.info("Employees loading file -> " + fileName);
			banner = resourceLoader.getResource("file:" + fileName);
		} else {// Testing purpose
			logger.info("Employees loading file -> classpath:static/emps.csv");
			banner = resourceLoader.getResource("classpath:static/emps.csv");
		}

		// reading csv file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(banner.getFile().toPath())) {
			// skip the header, process each record and save them one by one to
			// the DB.
			stream.skip(1).forEach(line -> {
				String[] val = line.split(",");
				try {
					employeeService.save(new Employee(val[0].trim(), val[1].trim(), val[2].trim(),
							dateFormat.parse(val[3].trim()), dateFormat.parse(val[4].trim()), Status.ACTIVE));
				} catch (ParseException e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);

				}

			});

		} catch (IOException ioe) {
			ioe.printStackTrace();
			logger.error(ioe.getMessage(), ioe);
		}

	}

}
