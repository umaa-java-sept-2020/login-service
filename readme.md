# DEFAULT BRANCH IS MAIN INSTEAD OF MASTER. DON'T PUSH CHANGES TO MAIN. CREATE PULL REQUEST ALWAYS.

## Understand the project structure:
* application
  * login-build
  * login-server: an war application contains controllers only. use(s) the jars defined as part of the framework.
* framework
  * login-client: have code which can be shared with other modules. login-client use this as dependency.
  * login-security: actual implementation of security logic. login-server use this as dependency.

## 1.1 How to build:
Go to login-build and open a terminal. Then do `mvn clean install -DskipTests`

## 1.2 How to deploy in tomcat:
Make sure you successfully finished 1.1. Then go to login-server/target and copy the .war file to tomcat/webapps

## 1.3 How to run in local:
Make sure you successfully finished 1.1.
* start your mysql database.
* Then do `mvn spring-boot:run`
* watch the logs in terminal and report if any error.


