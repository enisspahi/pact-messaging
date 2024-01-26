# Pact Messaging Example

This repository contains a `consumer driven contract` based Consumer and Provider implementation using [pact](https://docs.pact.io/implementation_guides/jvm).

## Starting Pact Broker and verify on [http://localhost:9292/](http://localhost:9292/)
````
docker compose -f .docker/docker-compose.yml up -d
````

### 1. Consumer implements and performs consumer contract testing
``
./gradlew :pact-consumer:test
``

### 2. Consumer publishes generated pacts to broker
``
./gradlew :pact-consumer:pactPublish
``

### 3. Provider verify API implementation

At this point provider implementation may still be in-progress.

``
./gradlew :pact-provider:test
``

### 4. Provider performs Pact Verify against deployed application and publishes verification results 
``
./gradlew :pact-provider:pactVerify -Dpact.verifier.publishResults=true
``

### 5. Consumer performs `Can I deploy?` check

At this point provider has fulfilled the Pact and deployed the API. Therefore, the consumer is good-to-go with client deployment

``
./gradlew :pact-consumer:canIDeploy -Ppacticipant='greetingConsumer' -Platest=true
``
