# event-manager
will be added


## API Endpoints - Can be seen in the swagger documentation
// todo, add authenticated

# API 
- **Generated Swagger documentation at: http://localhost:8080/swagger-ui/**
- **Create Event**: `POST /events/create`
- **Retrieve All Events**: `GET /events`
- **Retrieve Event by ID**: `GET /events/{id}`
- **Update Event by ID**: `PUT /events/{id}`
- **Delete Event**: `DELETE /events/{id}`


# Example API Request
-  **all requests needs basic auth with username:ori password; password**
  ~~~ 
  curl... -u ori:password
  ~~~
- **Create Event**: `POST /events/create`
  ~~~ 
  curl --location 'http://localhost:8080/events/create' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Basic b3JpOnBhc3N3b3Jk' \
  --header 'Cookie: JSESSIONID=2B4ACB8A5F5635DAF2076603362EDB48' \
  --data '{
      "name": "Sample Event",
      "information": "This is a sample event description.",
      "location": "israel",
      "date": "2023-01-27T15:30:00",
      "popularity": 0
  }'
  ~~~

- **Get All Events**: `GET /events`
- 
  ~~~
  basic request:
  curl --location 'http://localhost:8080/events' \
  --header 'Cookie: JSESSIONID=26C1A12ADA023554BF6BCE08B5DBC00F' \
  --data ''
  ~~~
- ~~~
  http://localhost:8080/events?location=${LOCATION}&sortBy=${SORT_BY}&sortDirection=${SORT_DIRECTION}
  For example: curl --location 'http://localhost:8080/events?location=israel' \
  --header 'Cookie: JSESSIONID=26C1A12ADA023554BF6BCE08B5DBC00F' \
  --data ''
  ~~~

- **Retrieve Event by ID**: `GET /events/{id}`
  ~~~
  curl --location 'http://localhost:8080/events/1' \
  --header 'Cookie: JSESSIONID=26C1A12ADA023554BF6BCE08B5DBC00F'
  ~~~
- **Update Event by ID**: `PUT /events/{id}`
  ~~~
  curl --location --request PUT 'http://localhost:8080/events/1' \
  --header 'Content-Type: application/json' \
  --header 'Cookie: JSESSIONID=26C1A12ADA023554BF6BCE08B5DBC00F' \
  --data '{
  "name": "Updated Event",
  "information": "This is an updated event description.",
  "location": "Updated Location",
  "date": "2024-01-28T16:45:00",
  "popularity": 3
  }'
  ~~~
- **Delete Event**: `DELETE /events/{id}`
  ~~~
  curl --location --request DELETE 'http://localhost:8080/events/1' \
  --header 'Cookie: JSESSIONID=26C1A12ADA023554BF6BCE08B5DBC00F'
  ~~~
