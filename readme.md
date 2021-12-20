```diff
- DEFAULT BRANCH IS MAIN INSTEAD OF MASTER. 
- DON'T PUSH CHANGES TO MAIN. 
- CREATE PULL REQUEST ALWAYS.
```

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
```diff
+ Tomcat8 download page: https://tomcat.apache.org/download-80.cgi
! Download tomcat8 64bit windows zip and extract it. 
```
Make sure you successfully finished 1.1. Then go to login-server/target and copy the .war file to tomcat/webapps.
Then go to tomcat/bin and open a cmd. Start with `catalina.bat run` (for linux sh catalina.sh run)

## 1.3 How to run in local using mvn:
Make sure you successfully finished 1.1.
* start your mysql database.
* Then do `mvn spring-boot:run`
* watch the logs in terminal and report if any error.

## 1.4.0 How to access swagger when running using mvn:
* Make sure you are done with 1.3
* Open http://{HOST}:{PORT}/swagger-ui/index.html
* Use {HOST}=localhost and {PORT}=8080. It may vary based upon your deployment.
* Example: http://localhost:8080/swagger-ui/index.html
<img width="1786" alt="Screenshot 2021-12-20 at 8 55 25 AM" src="https://user-images.githubusercontent.com/17001948/146707426-159122eb-b728-4124-b192-2b54916cbebb.png">


## 1.4.1 How to access swagger when running in tomcat:
* Make sure you are done with 1.2.
* Open http://{HOST}:{PORT}/{APP_NAME}/swagger-ui/index.html
* Use {HOST}=localhost and {PORT}=8080. It may vary based upon your deployment.
* Use {APP_NAME}=your war file name which is deployed in tomcat.

Color Coding Text Note:
```diff
- red 
+ green 
! orange 
# gray
@@ blue @@
```


