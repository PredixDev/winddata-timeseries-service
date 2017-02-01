package com.ge.predix.solsvc.winddata.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author predix -
 */
@Consumes(
{
        "application/json", "application/xml"
})
@Produces(
{
        "application/json", "application/xml"
})
@Path("/windservices")
@Api(value = "/windservices")
public interface WindDataAPI
{
    /**
     * @return -
     */
    @GET
    @Path("/ping")
    @ApiOperation(value = "/ping")
    public Response greetings();


    /**
     * @param id
     *            -
     * @param authorization
     *            -
     * @param starttime
     *            -
     * @param tagLimit -
     * @param tagorder -
     * @return -
     */
    @GET
    @Path("/yearly_data/sensor_id/{id}")
    @ApiOperation(value = "/yearly_data/sensor_id/{id}")
    public Response getYearlyWindDataPoints(@PathParam("id") String id,
            @HeaderParam(value = "Authorization") String authorization,
            @DefaultValue("1y-ago") @QueryParam("starttime") String starttime,
            @DefaultValue("25") @QueryParam("taglimit") String tagLimit,
            @DefaultValue("desc") @QueryParam("order") String tagorder);

    /**
     * @param id
     *            -
     * @param authorization
     *            -
     * @return -
     */
    @GET
    @Path("/latest_data/sensor_id/{id}")
    @ApiOperation(value = "/latest_data/sensor_id/{id}")
    public Response getLatestWindDataPoints(@PathParam("id") String id,
            @HeaderParam(value = "authorization") String authorization);

    /**
     * 
     * @param authorization -
     * @return List of tags
     */
    @GET
    @Path("/tags")
    @ApiOperation(value ="/tags")
    public Response getWindDataTags(@HeaderParam(value = "authorization") String authorization);

}
