package com.senith.smartcampus.resource;

import com.senith.smartcampus.exception.ApiError;
import com.senith.smartcampus.exception.RoomNotEmptyException;
import com.senith.smartcampus.model.Room;
import com.senith.smartcampus.model.Sensor;
import com.senith.smartcampus.store.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/rooms")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SensorRoomResource {

    @GET
    public List<Room> getAllRooms() {
        return DataStore.ROOMS;
    }

    @POST
    public Response createRoom(Room room) {
        if(room == null || room.getId() == null || room.getId().trim().isEmpty()) {
            ApiError error = new ApiError(400, "Room ID is required.");

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        for (Room existingRoom:DataStore.ROOMS) {
            if (existingRoom.getId().equalsIgnoreCase(room.getId())) {
                ApiError error = new ApiError(409, "Room with ID " + room.getId() + " already exists.");

                return Response.status(Response.Status.CONFLICT)
                        .entity(error)
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        }

        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        DataStore.ROOMS.add(room);

        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    @GET
    @Path("/{roomId}")
    public Room getRoomById(@PathParam("roomId") String roomId) {
        for (Room room : DataStore.ROOMS) {
            if (room.getId().equalsIgnoreCase(roomId)) {
                return room;
            }
        }

        throw new NotFoundException("Room with ID " + roomId + " not found.");
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {

        Room roomToDelete = null;

        for (Room room : DataStore.ROOMS) {
            if (room.getId().equalsIgnoreCase(roomId)) {
                roomToDelete = room;
                break;
            }
        }

        if (roomToDelete == null) {
            throw new NotFoundException("Room with ID " + roomId + " not found.");
        }

        for (Sensor sensor : DataStore.SENSORS) {
            if (sensor.getRoomId() != null && sensor.getRoomId().equalsIgnoreCase(roomId)) {
                throw new RoomNotEmptyException("Cannot delete room " + roomId + " because it still has active sensors assigned.");
            }
        }

        DataStore.ROOMS.remove(roomToDelete);

        return Response.ok("Room " + roomId + " deleted successfully.").build();
    }


}


