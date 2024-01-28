# event-manager
will be added

## API Endpoints - Can be seen in the swagger documentation
// todo, add swagger
// todo, add authenticated

- **Create Event**: `POST /events/create`
- **Retrieve All Events**: `GET /events`
- **Retrieve Event by ID**: `GET /events/{id}`
- **Update Event by ID**: `PUT /events/{id}`
- **Delete Event**: `DELETE /events/{id}`


# Example API Request
- **Create Event**: `POST /events/create`
  ~~~ 
  curl --location 'http://localhost:8080/events/create' \
  --header 'Content-Type: application/json' \
  --header 'Cookie: JSESSIONID=26C1A12ADA023554BF6BCE08B5DBC00F' \
  --data '{
  "name": "Sample Event",
  "information": "This is a sample event description.",
  "location": "Sample Location",
  "date": "2024-01-27T15:30:00",
  "popularity": 0
  }' 
  ~~~

- **Get All Events**: `GET /events`
  ~~~
  curl --location 'http://localhost:8080/events' \
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
