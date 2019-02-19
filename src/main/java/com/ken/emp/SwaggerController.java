package com.ken.emp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller to handle swagger-ui request.
 * 
 * This is required as springfox does not support spring boot and jersey
 * together. *
 * 
 * @author cmenerip
 *
 */
@Controller
public class SwaggerController {

	/**
	 * Request mapping for /swagger-ui request Extract swagger-ui into resource
	 * space for manual configuration and this method help index page to be
	 * rendered using spring MVC
	 * 
	 * @return
	 */
	@RequestMapping(value = "swagger-ui", method = RequestMethod.GET)
	public ModelAndView getSwaggerUi() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		return modelAndView;
	}
}