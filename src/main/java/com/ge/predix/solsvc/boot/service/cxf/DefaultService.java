package com.ge.predix.solsvc.boot.service.cxf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * An example of how to create a Rest service using standard javax.ws.rs
 * annotations and registered with CXF as a spring bean... see
 * src/main/resources/META-INF/spring/predix-microservice-cf-jsr-cxf-context.xml
 * 
 * @author predix
 *
 */
@Component
@Path("/")
@Api(value = "/")
public class DefaultService {

	/**
	 * 
	 */
	public DefaultService() {
		super();
	}

	/**
	 * -
	 * 
	 * @return string
	 */
	@SuppressWarnings("nls")
	@GET
	@Path("/health")
	@ApiOperation(value = "/health")
	public Response greetings() {
		return handleResult("{\"status\":\"up\", \"date\": \" " + new Date() + "\"}", MediaType.TEXT_PLAIN_TYPE);
	}

	/**
	 * Serve up static HTML files
	 * 
	 * @param path - path to HTML file
	 * @return -
	 * @throws IOException
	 *             -
	 */
	@SuppressWarnings({ "nls", "resource" })
	@GET
	@Path("{path:.*.html}")
	@Produces({ MediaType.TEXT_HTML })
	public Response javadoc(@PathParam("path") String path) throws IOException {
		InputStream in = null;
		in = this.getClass().getClassLoader().getResourceAsStream(path);
		// String string = IOUtils.toString(in);
		ResponseBuilder responseBuilder = Response.status(Status.OK);
		responseBuilder.type(MediaType.TEXT_HTML_TYPE);
		responseBuilder.entity(in);
		responseBuilder.header("X-Frame-Options", "SAMEORIGIN");
		return responseBuilder.build();
	}

	/**
	 * Serve up static CSS files
	 * @param path - path to CSS file
	 * @return -
	 * @throws IOException
	 *             -
	 */
	@SuppressWarnings({ "nls", "resource" })
	@GET
	@Path("{path:.*.css}")
	public Response css(@PathParam("path") String path) throws IOException {
		InputStream in = null;
		in = this.getClass().getClassLoader().getResourceAsStream(path);
		// String string = IOUtils.toString(in);
		ResponseBuilder responseBuilder = Response.status(Status.OK);
		responseBuilder.type("text/css");
		responseBuilder.entity(in);
		responseBuilder.header("X-Frame-Options", "SAMEORIGIN");
		return responseBuilder.build();
	}

	/**
	 * Serve up static JavaScript files
	 * @param path - path to JavaScript file
	 * @return -
	 * @throws IOException
	 *             -
	 */
	@SuppressWarnings({ "nls", "resource" })
	@GET
	@Path("{path:.*.js}")
	public Response js(@PathParam("path") String path) throws IOException {
		InputStream in = null;
		in = this.getClass().getClassLoader().getResourceAsStream(path);
		// String string = IOUtils.toString(in);
		ResponseBuilder responseBuilder = Response.status(Status.OK);
		responseBuilder.entity(in);
		responseBuilder.header("X-Frame-Options", "SAMEORIGIN");
		return responseBuilder.build();
	}

	/**
	 * @param entity
	 *            to be wrapped into JSON response
	 * @param mediaType
	 *            -
	 * @return JSON response with entity wrapped
	 */
	protected Response handleResult(Object entity, MediaType mediaType) {
		ResponseBuilder responseBuilder = Response.status(Status.OK);
		responseBuilder.type(mediaType);
		responseBuilder.entity(entity);
		return responseBuilder.build();
	}
}
