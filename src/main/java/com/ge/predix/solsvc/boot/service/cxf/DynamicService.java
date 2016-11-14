package com.ge.predix.solsvc.boot.service.cxf;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * An example of how to create a Rest service using standard javax.ws.rs annotations but registering with CXF
 * 
 * @author predix
 */
@Consumes(
{
        "application/json", "application/xml"
})
@Path("/dynamicservice")
@Api("/dynamicservice")
public interface DynamicService
{

    /**
     * 
     * @return - Returns the Rest response
     */
    @GET
    @Path("/dynamic")
    @ApiOperation(value = "/dynamic")
    public Response selfRegisteredService();

}
