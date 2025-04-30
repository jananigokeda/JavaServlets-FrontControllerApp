/**
 * Author :JananiKrishnaveni Gokeda
 * Due date March 30 ,2025
 * Professor :Sarah Khan 
 */
package com.algonquincollege.Janani_Gokeda.Assign2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author Janani
 */
@Path("rest")
public class JakartaEE8Resource {
    
    @GET
    public Response ping(){
        return Response
                .ok("ping")
                .build();
    }
}
