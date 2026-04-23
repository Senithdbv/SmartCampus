package com.senith.smartcampus.store;

import com.senith.smartcampus.model.Room;
import com.senith.smartcampus.model.Sensor;
import com.senith.smartcampus.model.SensorReading;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class DataStore {

    public static final List<Room> ROOMS = new ArrayList<>();
    public static final List<Sensor> SENSORS = new ArrayList<>();
    public static final Map<String, List<SensorReading>> READINGS = new HashMap<>();

    static {
        Room room1 = new Room("LIB-301", "Library Quiet Study", 40);
        room1.getSensorIds().add("TEMP-001");
        room1.getSensorIds().add("CO2-001");

        Room room2 = new Room("LAB-201", "Computer Lab", 60);
        room2.getSensorIds().add("TEMP-002");

        Room room3 = new Room("HALL-101", "Main Hall", 150);

        ROOMS.add(room1);
        ROOMS.add(room2);
        ROOMS.add(room3);

        SENSORS.add(new Sensor("TEMP-001", "Temperature", "ACTIVE", 24.5, "LIB-301"));
        SENSORS.add(new Sensor("CO2-001", "CO2", "ACTIVE", 410.0, "LIB-301"));
        SENSORS.add(new Sensor("TEMP-002", "Temperature", "MAINTENANCE", 0.0, "LAB-201"));

        READINGS.put("TEMP-001", new ArrayList<>());
        READINGS.put("CO2-001", new ArrayList<>());
        READINGS.put("TEMP-002", new ArrayList<>());
    }

    private DataStore() {
    }
}