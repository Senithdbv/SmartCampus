package com.senith.smartcampus.resource;

import com.senith.smartcampus.exception.ApiError;
import com.senith.smartcampus.exception.SensorUnavailableException;
import com.senith.smartcampus.model.Sensor;
import com.senith.smartcampus.model.SensorReading;
import com.senith.smartcampus.store.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getReadings() {
        return DataStore.READINGS.getOrDefault(sensorId, new ArrayList<>());
    }

    @POST
    public Response addReading(SensorReading reading) {

        if (reading == null) {
            ApiError error = new ApiError(400, "Reading data is required.");

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        Sensor targetSensor = null;

        for (Sensor sensor : DataStore.SENSORS) {
            if (sensor.getId().equalsIgnoreCase(sensorId)) {
                targetSensor = sensor;
                break;
            }
        }

        if (targetSensor == null) {
            throw new NotFoundException("Sensor with ID " + sensorId + " not found.");
        }

        if ("MAINTENANCE".equalsIgnoreCase(targetSensor.getStatus())) {
            throw new SensorUnavailableException(
                    "Sensor with ID " + sensorId + " is currently under maintenance and cannot accept new readings."
            );
        }

        if (reading.getId() == null || reading.getId().trim().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }

        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        List<SensorReading> readingList = DataStore.READINGS.get(sensorId);

        if (readingList == null) {
            readingList = new ArrayList<>();
            DataStore.READINGS.put(sensorId, readingList);
        }

        readingList.add(reading);
        targetSensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}


