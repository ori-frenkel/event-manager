# Event-Manager

RESTful API that manages events, offering users the ability to schedule, retrieve,
update, delete, and be reminded of events with additional advanced features

------

## Notes

- at the start of my work, I tried to set up my machine environment with docker and docker-compose (and k8s) with
  kafka/rabbitmw deployment,
but I stumble into time-consuming problems and as such I decided to run the project locally without docker/k8s.
as such, I had to change the design of the project and remove the kafka/rabbitmq and the microservices architecture.
- My expected design was to have 3 microservices:
    - Event-Manager: responsible for the event management.
        - Event-Manager would have a kafka/rabbitmq producer that would send messages to the Reminder-Manager.
        - Event-Manager would have a kafka/rabbitmq producer that would send messages to the Subscription-Manager.
    - Reminder-Manager: responsible for the reminder management.
        - Reminder-Manager would have a kafka/rabbitmq (this is a simple example, for this scenario I would choose
          rabbitMQ)
          consumer that would receive messages from the Event-Manager.
    - Subscription-Manager: responsible for the subscription management.
        - Subscription-Manager would have a kafka/rabbitmq consumer that would receive messages from the Event-Manager.
    - DB: PostgreSQL (or any other SQL DB) for event management, possible secondary DB - elasticsearch
        - elasticsearch support fast geo-search, so we could have secondary DB as elastic that would be indexed based on
          location (in this exercise the location is simple string, but if it had been lat/lon with elastic we could
          fast
          and easily search for location with the option to search for X meters for location).
          we would need to add sync mechanism between the PostgreSQL and elastic.
          anyway (the above may be overkill for exercise), please review my code and documentation and I will be happy to answer
        any question.

## Tests

**Please review these tests:**
    wrote for some (but not all) components unit tests and integration tests.
    I think that for the sake of home-exercise, this two tests are enough to show my testing skills.
- integration: ../integ/**EventControllerTest.java**
- unit: ../unit/**EventServiceTest.java**

-----

## API Endpoints

It is recommended to look at the Swagger documentation for a more detailed description of the API endpoints.

- **basic auth credential is ori:password (i.e, user=ori, password=password)**
- **Generated Swagger documentation at: http://localhost:8080/swagger-ui/** (run the main application on your local
  machine)
- **Create Event**: `POST /event`
- **Retrieve All Events**: `GET /event/all`
- **Retrieve Event by ID**: `GET /event/{id}`
- **Update Event**: `PUT /events`
- **Delete Event by ID**: `DELETE /event/{id}`
- **Batch Create Event**: `POST /batch/events`
- **Batch Update Events**: `PUT /batch/events`
- **Batch Delete Event by ID**: `DELETE /batch/events`

## Example API Request

- **all requests needs basic auth with username:ori password; password**

  ~~~ 
  curl... -u ori:password
  ~~~

- **Create Event**: `POST /event/create`
  ~~~ 
  curl --location 'http://localhost:8080/event/create' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Basic b3JpOnBhc3N3b3Jk' \
  --header 'Cookie: JSESSIONID=CA45E0FFA2D6454CB9D0C525AFD24F6D' \
  --data '{
  "name": "Sample Event",
  "information": "This is a sample event description.",
  "location": "israel",
  "date": "2023-01-27T15:30:00",
  "popularity": 0
  }'
  ~~~

- **Get All Events**: `GET /events/all`
-
~~~
basic request:
curl --location 'http://localhost:8080/event/all' \
--header 'Authorization: Basic b3JpOnBhc3N3b3Jk' \
--header 'Cookie: JSESSIONID=398300FB1F1CA54815468656E113852D' \
--data ''
~~~
- ~~~
  with param: 
  curl --location 'http://localhost:8080/event/all?location=israel' \
  --header 'Authorization: Basic b3JpOnBhc3N3b3Jk' \
  --header 'Cookie: JSESSIONID=398300FB1F1CA54815468656E113852D' \
  --data ''
  ~~~

- **Retrieve Event by ID**: `GET /event/{id}`
  ~~~
  curl --location 'http://localhost:8080/event/1' \
  --header 'Cookie: JSESSIONID=398300FB1F1CA54815468656E113852D'
  ~~~
- **Update Event**: `PUT /events`
  ~~~
  curl --location --request PUT 'http://localhost:8080/event' \
  --header 'Content-Type: application/json' \
  --header 'Cookie: JSESSIONID=398300FB1F1CA54815468656E113852D' \
  --data '{
  "creationTime": "2024-01-29T09:46:30.924Z",
  "date": "2024-01-29T09:46:30.924Z",
  "id": 1,
  "information": "new info",
  "location": "England",
  "name": "new name",
  "popularity": 0
  }'
  ~~~
- **Delete Event**: `DELETE /event/{id}`
  ~~~
  curl --location --request DELETE 'http://localhost:8080/event/1' \
  --header 'Cookie: JSESSIONID=398300FB1F1CA54815468656E113852D'
  ~~~
- **Batch Create Event**: `POST /batch/events`
  ~~~
  curl --location 'http://localhost:8080/batch/events' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Basic b3JpOnBhc3N3b3Jk' \
  --header 'Cookie: JSESSIONID=398300FB1F1CA54815468656E113852D' \
  --data '[
  {
  "name": "Event 1",
  "information": "Information 1",
  "location": "Location 1",
  "date": "2024-02-01T18:00:00",
  "popularity": 100
  },
  {
  "name": "Event 2",
  "information": "Information 2",
  "location": "Location 2",
  "date": "2024-02-15T20:30:00",
  "popularity": 75
  }
  ]'
  ~~~
- **Batch Update Events**: `PUT /events/batch`
  ~~~
  curl --location --request PUT 'http://localhost:8080/batch/events' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Basic b3JpOnBhc3N3b3Jk' \
  --header 'Cookie: JSESSIONID=398300FB1F1CA54815468656E113852D' \
  --data '[
  {
  "id": 4,
  "name": "Event 1",
  "information": "batch update 1",
  "location": "Location 1",
  "date": "2024-02-01T18:00:00",
  "creationTime": "2024-01-29T11:49:39.94903",
  "popularity": 100
  },
  {
  "id": 5,
  "name": "Event 2",
  "information": "batch update 2",
  "location": "Location 2",
  "date": "2024-02-15T20:30:00",
  "creationTime": "2024-01-29T11:49:39.951057",
  "popularity": 75
  }
  ]'
  ~~~
- **Batch Delete Event by ID**: `DELETE /events/batch`
  ~~~
  curl --location --request DELETE 'http://localhost:8080/batch/events' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Basic b3JpOnBhc3N3b3Jk' \
  --header 'Cookie: JSESSIONID=398300FB1F1CA54815468656E113852D' \
  --data '[4, 5]'
  ~~~

## Database Configuration

* The application utilizes an H2 in-memory database during development, eliminating the necessity to connect to a
  SQL server. Configuration to establish connections with any SQL server, if needed, is easily accomplished through
  the config file located at src/main/resources/application.properties