# System for gamification of lab work in TDT4100

> Using measures to give indications of qualities in student exercises 

## Project structure

```
e2e         --> Angular end to end testing
libs        --> External jars
postman     --> postman project backup
src
  app         --> Angular
  assets      --> Angular
  environment --> Angular
  main        --> Java Spring backend
  test        --> Java Spring tests
  ```


## Installation

### Prerequisites
- node and npm 
- angular CLI: 6.2.1
- Java 1.8
- MongoDB 3.6.3

### Setup

- Clone this repo to your local machine using https://github.com/mariusmoe/metrics-consumer.git
- Go to `libs/` and run commands from `maven-manual-imports`

### Authentication provider
> The application uses spring security oauth2 implicit flow. 
- Create an account at an authentication provider or set up an Authorization Server. 
  - An example is Feide dataporten
- Create the Spring configuration file at `metrics-consumer/src/main/resources/` and fill in the provided fields from the authentication provider:
```security:
  oauth2:
    client:
      accessTokenUri: 
      userAuthorizationUri: 
      clientId: 
      clientSecret: 
    resource:
      userInfoUri: 
mom:
  mongo:
    address: 127.0.0.1
    database: measures
  staticResource: 
logging:
  level:
    com.moe.metricsconsumer: info
metricsProviders:
  - no.hal.learning.exercise.jdt.metrics
```
### Build
- Run `ng build --prod` to build Angular static files
  - The target path for Angular production build is set to `target/classes/static`
- Run `mvn clean package` and deploy war to tomcat 


