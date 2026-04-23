# Smart Campus API

**Student:** Senith Vidanage  
**UOW No:** W2120074  
**IIT No:** 20240665  
**Module:** 5COSC022W - Client-Server Architectures  
**University:** University of Westminster (IIT)

---

## API Overview

This project is a RESTful web service developed for the Smart Campus coursework. The API manages rooms, sensors, and sensor readings within a university campus environment.

The system is implemented using JAX-RS with Jersey and deployed on Apache Tomcat. It follows RESTful design principles, including resource-based endpoints, stateless communication, JSON request and response bodies, and meaningful HTTP status codes.

All data is stored in-memory using Java collections such as `ArrayList` and `HashMap`, as required by the coursework specification. The API also includes custom exception handling and request/response logging to improve reliability, maintainability, and observability.

---

## Technology Stack

- **Framework:** JAX-RS (Jersey)
- **Server:** Apache Tomcat
- **Language:** Java
- **Build Tool:** Maven
- **Data Storage:** In-memory Java collections (`ArrayList`, `HashMap`)
- **Testing Tool:** Postman

---

## Base URL

```text
http://localhost:8080/SmartCampus/api
```

---

## Available Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api` | Discovery endpoint |
| GET | `/api/rooms` | Get all rooms |
| POST | `/api/rooms` | Create a new room |
| GET | `/api/rooms/{roomId}` | Get a single room |
| PUT | `/api/rooms/{roomId}` | Update a room |
| DELETE | `/api/rooms/{roomId}` | Delete a room |
| GET | `/api/sensors` | Get all sensors |
| GET | `/api/sensors?type={type}` | Filter sensors by type |
| POST | `/api/sensors` | Create a new sensor |
| GET | `/api/sensors/{sensorId}` | Get a single sensor |
| PUT | `/api/sensors/{sensorId}` | Update a sensor |
| DELETE | `/api/sensors/{sensorId}` | Delete a sensor |
| GET | `/api/sensors/{sensorId}/readings` | Get reading history for a sensor |
| POST | `/api/sensors/{sensorId}/readings` | Add a new reading for a sensor |

---

## How to Build and Run

### Prerequisites

- Java JDK 11 or higher
- Apache Maven
- Apache Tomcat
- Postman or curl for testing

### Step 1 - Clone the Repository

```bash
git clone <your-github-repository-url>
cd SmartCampus
```

### Step 2 - Build the Project

```bash
mvn clean package
```

After the build is successful, a `.war` file will be generated inside the `target` folder.

### Step 3 - Deploy to Tomcat

Copy the generated WAR file into the Tomcat `webapps` folder.

```bash
cp target/SmartCampus.war <tomcat-folder>/webapps/
```

### Step 4 - Start Tomcat

Start Apache Tomcat using your IDE or terminal.

```bash
catalina start
```

### Step 5 - Verify the API

Open the following URL in a browser or Postman:

```text
http://localhost:8080/SmartCampus/api
```

The API should return discovery information about the available resources.

---

## Sample curl Commands

### 1 - Discovery Endpoint

```bash
curl -X GET http://localhost:8080/SmartCampus/api
```

### 2 - Get All Rooms

```bash
curl -X GET http://localhost:8080/SmartCampus/api/rooms
```

### 3 - Create a New Room

```bash
curl -X POST http://localhost:8080/SmartCampus/api/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"LAB-101","name":"Computer Lab","capacity":30}'
```

### 4 - Get a Single Room

```bash
curl -X GET http://localhost:8080/SmartCampus/api/rooms/LAB-101
```

### 5 - Update a Room

```bash
curl -X PUT http://localhost:8080/SmartCampus/api/rooms/LAB-101 \
  -H "Content-Type: application/json" \
  -d '{"id":"LAB-101","name":"Updated Computer Lab","capacity":35}'
```

### 6 - Delete a Room

```bash
curl -X DELETE http://localhost:8080/SmartCampus/api/rooms/LAB-101
```

### 7 - Create a Sensor

```bash
curl -X POST http://localhost:8080/SmartCampus/api/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":25.5,"roomId":"LAB-101"}'
```

### 8 - Filter Sensors by Type

```bash
curl -X GET "http://localhost:8080/SmartCampus/api/sensors?type=Temperature"
```

### 9 - Add a Sensor Reading

```bash
curl -X POST http://localhost:8080/SmartCampus/api/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":26.2}'
```

### 10 - Get Sensor Reading History

```bash
curl -X GET http://localhost:8080/SmartCampus/api/sensors/TEMP-001/readings
```

### 11 - Try to Create a Sensor With Invalid Room ID

```bash
curl -X POST http://localhost:8080/SmartCampus/api/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"CO2-999","type":"CO2","status":"ACTIVE","currentValue":400.0,"roomId":"INVALID-ROOM"}'
```

### 12 - Try to Delete a Room With Assigned Sensors

```bash
curl -X DELETE http://localhost:8080/SmartCampus/api/rooms/LAB-101
```

### 13 - Try to Add a Reading to a Sensor in Maintenance

```bash
curl -X POST http://localhost:8080/SmartCampus/api/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":27.0}'
```

---

## Error Handling

The API uses custom exceptions and JAX-RS `ExceptionMapper` classes to return structured JSON error responses instead of raw Java stack traces or default server error pages.

### Example Error Response

```json
{
  "status": 404,
  "message": "Room not found"
}
```

### Implemented Error Scenarios

| Error Scenario | HTTP Status | Description |
|---|---|---|
| Resource not found | 404 Not Found | Returned when a requested room, sensor, or reading does not exist |
| Room has assigned sensors | 409 Conflict | Returned when deleting a room that still contains sensors |
| Invalid linked resource | 422 Unprocessable Entity | Returned when creating a sensor with a room ID that does not exist |
| Sensor unavailable | 403 Forbidden | Returned when adding a reading to a sensor in maintenance |
| Unexpected server error | 500 Internal Server Error | Returned through the global exception mapper |

---

## Logging

The project includes a JAX-RS logging filter that implements both:

- `ContainerRequestFilter`
- `ContainerResponseFilter`

The logging filter records the incoming request method, request URI, and outgoing response status code. This keeps logging centralized instead of repeating `Logger.info()` statements inside every resource method.

---

## Project Design

### Resource Layer

The resource layer contains the JAX-RS endpoint classes. These classes receive HTTP requests and return HTTP responses.

### Service Layer

The service layer handles the main business logic, such as validating room existence before assigning sensors and checking whether a sensor can accept new readings.

### DAO / Data Store Layer

The data store layer manages in-memory collections used to store rooms, sensors, and readings during runtime.

### Model Layer

The model layer contains the POJO classes used by the API:

- `Room`
- `Sensor`
- `SensorReading`

### Exception Layer

The exception layer contains custom exceptions and exception mappers used to convert Java exceptions into clean HTTP JSON responses.

### Filter Layer

The filter layer contains the request and response logging logic.

---

## Key RESTful Design Decisions

### Resource-Based URI Design

The API uses nouns for resources, such as:

```text
/rooms
/sensors
/sensors/{sensorId}/readings
```

This keeps the API aligned with RESTful design rather than using action-based URLs.

### JSON Request and Response Bodies

The API consumes and produces JSON for client-server communication.

### Query Parameters for Filtering

Sensor filtering is implemented using query parameters:

```text
/sensors?type=CO2
```

This is suitable because filtering is optional and does not identify a new resource.

### Sub-Resource Design for Readings

Sensor readings are implemented as a nested resource under sensors:

```text
/sensors/{sensorId}/readings
```

This clearly shows that readings belong to a specific sensor.

### In-Memory Storage

The API uses in-memory Java collections because the coursework requires the use of simple data structures instead of a database.

---

## Testing

The API was tested using Postman and curl. The testing covered:

- Creating rooms
- Retrieving rooms
- Updating rooms
- Deleting rooms
- Creating sensors
- Filtering sensors by type
- Adding sensor readings
- Retrieving reading history
- Validating error responses
- Checking logging output in the server console

---

## Conclusion

This project demonstrates a RESTful Smart Campus API using JAX-RS. It includes room management, sensor management, nested sensor reading history, custom exception handling, JSON responses, and centralized request/response logging.

The implementation follows the coursework requirements and applies key client-server architecture principles such as stateless communication, resource-based design, layered structure, and meaningful HTTP responses.


---

## Report: Answers to Coursework Questions

### Part 1.1 - JAX-RS Resource Lifecycle

**Question:**  
In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures to prevent data loss or race conditions.

**Answer:**  
The default lifecycle of a JAX-RS resource class is per-request. This means the JAX-RS runtime creates a new resource class instance for each incoming HTTP request.

In this project, in-memory collections such as `ArrayList` and `HashMap` are used to store rooms, sensors, and sensor readings. Since resource classes are created per request, shared data should not be stored only as normal instance variables inside resource classes. To keep data available while the server is running, the project uses shared in-memory data structures.

However, because multiple requests may access the same collections at the same time, race conditions can occur. In a larger production system, this can be improved by using thread-safe collections such as `ConcurrentHashMap`, synchronization, or a proper database.

---

### Part 1.2 - Hypermedia and HATEOAS

**Question:**  
Why is the provision of Hypermedia considered a hallmark of advanced RESTful design? How does this approach benefit client developers compared to static documentation?

**Answer:**  
Hypermedia, also known as HATEOAS, allows clients to navigate an API through links included in server responses. Instead of hardcoding every endpoint, the client can discover available actions from the response itself.

In this project, the discovery endpoint provides links to main resources such as rooms and sensors. This helps make the API easier to understand and more flexible for client developers. If endpoint paths change later, clients can still follow the links provided by the server instead of depending only on static documentation.

---

### Part 2.1 - Returning Room IDs vs Full Room Objects

**Question:**  
When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.

**Answer:**  
Returning only room IDs creates a smaller response and reduces network bandwidth. However, the client must send extra requests to retrieve the full details of each room.

Returning full room objects increases the response size, but it gives the client all required information in one request. This improves client-side usability because the client does not need to make many additional API calls.

For this Smart Campus API, returning full room objects is suitable because the data size is manageable and it makes the API easier to use.

---

### Part 2.2 - DELETE Idempotency

**Question:**  
Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

**Answer:**  
Yes, the DELETE operation is idempotent in terms of server state. When a room is deleted for the first time, it is removed from the in-memory data store.

If the same DELETE request is sent again, the room will no longer exist, so the API may return a `404 Not Found` response. Even though the response status may be different from the first request, the final server state remains the same because the room is still deleted.

Therefore, the DELETE operation is considered idempotent because repeating it does not create additional changes to the system.

---

### Part 3.1 - @Consumes Media Type Mismatch

**Question:**  
We explicitly use the `@Consumes(MediaType.APPLICATION_JSON)` annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as `text/plain` or `application/xml`. How does JAX-RS handle this mismatch?

**Answer:**  
The `@Consumes(MediaType.APPLICATION_JSON)` annotation tells JAX-RS that the method only accepts JSON request bodies.

If a client sends data using another media type, such as `text/plain` or `application/xml`, JAX-RS will reject the request before the resource method is executed. The server will return `415 Unsupported Media Type`.

This ensures that the API receives data in the expected format and prevents unsupported request bodies from reaching the business logic.

---

### Part 3.2 - QueryParam vs PathParam for Filtering

**Question:**  
You implemented filtering using `@QueryParam`. Contrast this with an alternative design where the type is part of the URL path, such as `/api/v1/sensors/type/CO2`. Why is the query parameter approach generally considered superior for filtering and searching collections?

**Answer:**  
Using `@QueryParam` is better for filtering collections because filtering is optional. For example:

```text
/sensors?type=CO2
```

This means the client is still requesting the sensors collection, but only wants sensors that match a certain type.

Path parameters are better for identifying a specific resource, such as:

```text
/sensors/TEMP-001
```

Using query parameters also makes the API easier to extend because more filters can be added later, such as:

```text
/sensors?type=CO2&status=ACTIVE
```

---

### Part 4.1 - Sub-Resource Locator Pattern

**Question:**  
Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path in one massive controller class?

**Answer:**  
The Sub-Resource Locator pattern helps separate nested resource logic into smaller classes.

In this project, sensor readings belong to a specific sensor, so the endpoint is designed as:

```text
/sensors/{sensorId}/readings
```

Instead of placing all sensor and reading logic inside one large resource class, reading-related logic can be handled separately. This improves readability, maintainability, and testing.

It also makes the API easier to extend in the future because new nested resources can be added without making one controller class too large.

---

### Part 5.2 - 422 Unprocessable Entity vs 404 Not Found

**Question:**  
Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

**Answer:**  
HTTP `404 Not Found` is suitable when the requested URL resource does not exist.

However, when a client sends a valid JSON request to create a sensor but includes a `roomId` that does not exist, the endpoint itself is valid. The problem is inside the request body.

In this case, HTTP `422 Unprocessable Entity` is more accurate because the server understands the request format, but the data is logically invalid. This helps the client understand that the payload must be corrected.

---

### Part 5.4 - Security Risk of Exposing Java Stack Traces

**Question:**  
From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

**Answer:**  
Exposing Java stack traces is a security risk because it reveals internal implementation details of the application.

An attacker could learn:

- Package names
- Class names
- Method names
- File paths
- Framework or library details
- Internal application flow

This information can help attackers understand the system structure and search for weaknesses. Therefore, the API should return a generic JSON error response to clients while logging full error details only on the server side.

---

### Part 5.5 - JAX-RS Filters for Logging

**Question:**  
Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting `Logger.info()` statements inside every single resource method?

**Answer:**  
JAX-RS filters are better for logging because they provide a centralized solution for all requests and responses.

If logging statements are manually added to every resource method, the code becomes repetitive and harder to maintain. Developers may also forget to add logging to some methods.

Using `ContainerRequestFilter` and `ContainerResponseFilter` keeps logging separate from business logic. It allows the API to log request methods, URIs, and response status codes consistently across the whole application.
