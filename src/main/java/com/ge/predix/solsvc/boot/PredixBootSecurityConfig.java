package com.ge.predix.solsvc.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.SpringBootWebSecurityConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

/*
 * This class adds the required Headers to the HTTP response being sent to the client
 * @Author - Swapna Vad
 */

/**
 * 
 * @author 212546387 -
 */
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(prefix = "security.basic", name = "enabled", havingValue = "false")
public class PredixBootSecurityConfig extends WebSecurityConfigurerAdapter{
	/**
	 * read the security headers from the properties file
	 */
	@Autowired
	private SecurityProperties security;
	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		//Do Nothing
	}
	
	/* 
	 * Adds the following headers to the http response. Reads the value from properties file.
	 * security.basic.enabled
	 * security.headers.cache 
	 * security.headers.content-type 
	 * security.headers.frame
	 * security.headers.xss
	 * security.filter-order
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		if (this.security.isRequireSsl()) {
			http.requiresChannel().anyRequest().requiresSecure();
		}
		if (!this.security.isEnableCsrf()) {
			http.csrf().disable();
		}
		SpringBootWebSecurityConfiguration.configureHeaders(http.headers(),
				this.security.getHeaders());
		
		http.headers().addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy","script-src 'self'"));	 //$NON-NLS-1$ //$NON-NLS-2$
		http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));

	}
	
	/* 
	 *
	 * 
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		//do Nothing
	}

}
