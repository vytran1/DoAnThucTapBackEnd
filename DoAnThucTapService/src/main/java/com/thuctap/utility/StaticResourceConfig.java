package com.thuctap.utility;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
	 	
		@Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        
	        registry.addResourceHandler("/employee-images/**")
	                .addResourceLocations("file:/C:/DoAnThucTapImages/InventoryEmployee/");
	        
	        registry.addResourceHandler("/product-images/**")
            		.addResourceLocations("file:C:/DoAnThucTapImages/Product/");
	        
	    }
		
		 

}
