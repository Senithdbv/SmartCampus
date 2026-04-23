package com.senith.smartcampus.resource;

import com.senith.smartcampus.exception.ApiError;
import com.senith.smartcampus.exception.LinkedResourceNotFoundException;
import com.senith.smartcampus.model.Room;
import com.senith.smartcampus.model.Sensor;
import com.senith.smartcampus.store.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/sensors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        if (type == null || type.trim().isEmpty()) {
            return DataStore.SENSORS;
        }

        List<Sensor> filteredSensors = new ArrayList<>();

        for (Sensor sensor : DataStore.SENSORS) {
            if (sensor.getType() != null && sensor.getType().equalsIgnoreCase(type)) {
                filteredSensors.add(sensor);
            }
        }

        return filteredSensors;
    }
        @POST
        public Response createSensor(Sensor sensor) {

            if (sensor == null
                    || sensor.getId() == null || sensor.getId().trim().isEmpty()
                    || sensor.getType() == null || sensor.getType().trim().isEmpty()
                    || sensor.getStatus() == null || sensor.getStatus().trim().isEmpty()
                    || sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {

                ApiError error = new ApiError(400, "Sensor id, type, status, and roomId are required.");

                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(error)
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            for (Sensor existingSensor : DataStore.SENSORS) {
                if (existingSensor.getId().equalsIgnoreCase(sensor.getId())) {
                    ApiError error = new ApiError(409, "Sensor with ID " + sensor.getId() + " already exists.");

                    return Response.status(Response.Status.CONFLICT)
                            .entity(error)
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                }
            }

            Room linkedRoom = null;

            for (Room room : DataStore.ROOMS) {
                if (room.getId().equalsIgnoreCase(sensor.getRoomId())) {
                    linkedRoom = room;
                    break;
                }
            }

            if (linkedRoom == null) {
                throw new LinkedResourceNotFoundException(
                        "Room with ID " + sensor.getRoomId() + " does not exist."
                );
            }

            DataStore.SENSORS.add(sensor);
            linkedRoom.getSensorIds().add(sensor.getId());

            return Response.status(Response.Status.CREATED)
                    .entity(sensor)
                    .build();
        }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {

        for (Sensor sensor : DataStore.SENSORS) {
            if (sensor.getId().equalsIgnoreCase(sensorId)) {
                return new SensorReadingResource(sensorId);
            }
        }

        throw new NotFoundException("Sensor with ID " + sensorId + " not found.");
    }
    }




