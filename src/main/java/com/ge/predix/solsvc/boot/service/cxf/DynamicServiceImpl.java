package com.ge.predix.solsvc.boot.service.cxf;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ge.predix.solsvc.spi.IServiceManagerService;

/**
 * This Rest service registers itself. See init() method how the service is created.
 * Any method annotated with PostContruct is like a init method.
 * 
 * @author 212307911
 */
@Component
public class DynamicServiceImpl implements DynamicService {

    @Autowired
	private IServiceManagerService serviceManagerService;
    
	/**
	 * 
	 */
	public DynamicServiceImpl() {
		super();
	}

    /**
     * 
     */
    @PostConstruct
    public void init()
    {
        this.serviceManagerService.createRestWebService(this, null);
    }
    /**
	 * 
	 * @return - Response
	 */
    @Override
    public Response selfRegisteredService() {
		return handleResult("Greetings from Self Registering Cloud Service " + new Date(),MediaType.TEXT_PLAIN_TYPE); //$NON-NLS-1$
	}

	/**
	 * @param entity to be wrapped into JSON response
	 * @param mediaType -
	 * @return JSON response with entity wrapped
	 */
	protected Response handleResult(Object entity ,MediaType mediaType) {
		ResponseBuilder responseBuilder = Response.status(Status.OK);
		responseBuilder.type(mediaType);
		responseBuilder.entity(entity);
		return responseBuilder.build();
	}
}

