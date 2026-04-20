package com.senith.smartcampus.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.security.PublicKey;

@Path("/test")
public class TestResource {
    @GET
    public String test() {
        return "API is working";
    }
}
