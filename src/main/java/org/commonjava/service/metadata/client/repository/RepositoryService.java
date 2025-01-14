package org.commonjava.service.metadata.client.repository;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/api/admin/stores")
@RegisterRestClient(configKey="repo-service-api")
public interface RepositoryService
{
    @GET
    @Path("/{packageType}/{type: (hosted|group|remote)}/{name}")
    Response getStore(@PathParam("packageType") String packageType, @PathParam("type") String type, @PathParam("name") String name);

    @GET
    @Path( "/query/affectedBy" )
    Response getGroupsAffectedBy( final @QueryParam( "keys" ) String keys );

}
