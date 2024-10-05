# Yoshida
![Yoshida from manga Ryuu to Yuusha to Haitatsunin](https://github.com/ankasatu/yoshida/blob/main/.github/yoshida.gif?raw=true)

This project is a proof of concept how to:
- Set up a consumer based on a registered topic name.
- Render consumed data using a saved template.
- Send the results to a registered producer topic.

### Stack
- Framework: Spring Boot
- Database: PostgreSQL
- Messaging: Apache Kafka
- Template Engine: Apache FreeMarker

## Getting Started
Make sure to update the `application.properties`  file or set _env-vars_ with your environment.

```bash
# Database settings
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres?currentSchema=yoshida
spring.datasource.username=postgres
spring.datasource.password=postgres

# Set this to "update" for the first time to migrate the database
spring.jpa.hibernate.ddl-auto=update

# Kafka settings
spring.kafka.bootstrap-servers=localhost:9094
```

Once everything's set, run the app and try to access http://localhost:8080/swagger-ui/index.html to check app is running.

## Sample Test
Let's walk through an example to generate text output from simple template, based on consumed data from `source-topic`, and then publish result to `target-topic`.

First, makesure to check both topic above is exist. By default `auto.create.topics.enable` is on, so we can skip the step. Otherwise, we should create manually.

### Setting up template
Use the following api to create a template

```bash
curl -X 'POST' \
  'http://localhost:8080/template/config/save' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "example",
  "source": "Hello ${user}!"
}'
```
> the template is written in the [FreeMarker Template Language](https://try.freemarker.apache.org/)

check if the template is saved

```bash
curl -X 'GET' \
  'http://localhost:8080/template/config/ec10b5be-1747-4300-9af0-d66bad6a6f46' \
  -H 'accept: */*'
```

### Attach topic to the template

```bash
curl -X 'POST' \
  'http://localhost:8080/template/config/ec10b5be-1747-4300-9af0-d66bad6a6f46/topic/attach' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "category": "example-category",
  "sourceTopic": "source-topic",
  "targetTopic": "target-topic"
}'
```
> After attaching a topic to template, restart the service to rebuild the consumer instance.

### Flight 🛩️ 
Now that everything's set up. Let's publish a message to the `source-topic`
```json
{
  "id": "b21ec196-d325-49c1-829a-b80013ad0c66",
  "user": "yoshida"
}
```
The result should be published to the `target-topic`, and it will look something like this:

```json
{
  "result": "Hello yoshida!",
  "sourceEventId": "b21ec196-d325-49c1-829a-b80013ad0c66",
  "sourceTopicName": "source-topic",
  "templateId": "ec10b5be-1747-4300-9af0-d66bad6a6f46"
}
```

