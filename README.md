# event-manager
will be added


## API Endpoints - Can be seen in the swagger documentation

# API 
- **Generated Swagger documentation at: http://localhost:8080/swagger-ui/**
- **Create Event**: `POST /event`
- **Retrieve All Events**: `GET /event/all`
- **Retrieve Event by ID**: `GET /event/{id}`
- **Update Event**: `PUT /events`
- **Delete Event by ID**: `DELETE /event/{id}`
- **Batch Create Event**: `POST /batch/events`
- **Batch Update Events**: `PUT /batch/events`
- **Batch Delete Event by ID**: `DELETE /batch/events`

# Example API Request
-  **all requests needs basic auth with username:ori password; password**
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