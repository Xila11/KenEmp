package com.ken.emp.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.ken.emp.res.EmployeeResource;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

/**
 * REST configuration class which register all rest end-points (Jax-RS)
 * 
 * @author cmenerip
 *
 */
@Component
@ApplicationPath(RestServiceConfig.appBasePath)
public class RestServiceConfig extends ResourceConfig {

	/**
	 * base path of the application
	 */
	public static final String appBasePath = "/kenEmp/api/v1";

	/**
	 * constructor
	 */
	public RestServiceConfig() {
		register(EmployeeResource.class);
		configureSwagger();
	}

	private void configureSwagger() {
		// swagger.json is Available at /kenEmp/api/v1/swagger.json
		// add swagger-core's providers to the set up process.
		register(ApiListingResource.class);
	    register(SwaggerSerializers.class);

	    //set various properties for Swagger's initialization.
	    BeanConfig config = new BeanConfig();
	    config.setConfigId("Employee REST Service");
	    config.setTitle("Employee REST Service");
	    config.setVersion("v1");
	    config.setContact("Dilan Weerasinghe");
	    config.setSchemes(new String[] { "http"});
	    config.setBasePath(appBasePath);
	    config.setResourcePackage("com.ken.emp.res");
	    config.setPrettyPrint(true);
	    config.setScan(true);
	}
}
