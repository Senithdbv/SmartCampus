package com.senith.smartcampus.resource;

import com.senith.smartcampus.model.ApiInfo;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiInfo getApiInfo() {
        Map<String, String> resources = new LinkedHashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");

        return new ApiInfo(
                "Smart Campus API",
                "v1",
                "admin@smartcampus.local",
                resources
        );
    }

}
