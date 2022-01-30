```diff
- DEFAULT BRANCH IS MAIN INSTEAD OF MASTER. 
- DON'T PUSH CHANGES TO MAIN. 
- CREATE PULL REQUEST ALWAYS.
```

## 1.5 Add ssh keys to git
* Download and Install Git
* Read document: https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent
* Watch Video: https://www.youtube.com/watch?v=jfi9n4y-WFo

## 1.6 Clone project
* Finish step 1.5
* Go to any directory and open a cmd or terminal. 
* Do cloning of code: git clone git@github.com:umaa-java-sept-2020/login-service.git
<img width="1792" alt="Screenshot 2021-12-21 at 9 12 13 AM" src="https://user-images.githubusercontent.com/17001948/146867311-8273edae-d4fa-4a51-8dab-6bd54675cb1b.png">

## 1.7 Open project in Intellij
* File > New > Project From Existing Source. Locate the login-service > application > login-build > pom.xml. Then click open.
<img width="1792" alt="Screenshot 2021-12-21 at 9 15 06 AM" src="https://user-images.githubusercontent.com/17001948/146867636-c089963d-00f7-4732-a69e-24f168099789.png">
<br/>
<img width="1792" alt="Screenshot 2021-12-21 at 9 15 48 AM" src="https://user-images.githubusercontent.com/17001948/146867685-3d67d6ca-af8f-4774-a0a1-6081ce73404d.png">

* This will open in IntelliJ as below:

<img width="1792" alt="Screenshot 2021-12-21 at 9 18 44 AM" src="https://user-images.githubusercontent.com/17001948/146867836-3c65b484-c000-481e-ab16-c2c00831437d.png">


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
* Go to login-server. start your mysql database.
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
* Use {HOST}=localhost and {PORT}=8085. It may vary based upon your deployment.
* Use {APP_NAME}=your war file name which is deployed in tomcat.

## 1.8 Build the code using shell script
* This is alternate to step 1.1. Go to login-service
* Open a terminal
* Run `sh build.sh`

## 1.9 Run the code using shell script
* This is alternate to 1.3. Go to login-service
* Open a terminal.
* Run `sh mvnSpringBootRun.sh`

## 2.0 Datasource configuration
* Open login-security > pom.xml and add below maven dependency
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
```
* Create a file database.properties under login-security/src/main/resources
* Add database configuration to database.properties file as below:
```diff
db.datasource.url= jdbc:mysql://localhost:3306/testdb?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false
db.datasource.username= root
db.datasource.password= root_pass
db.datasource.driver-classname= com.mysql.jdbc.Driver
```
* You may need to change the password accordingly
* Create DatabaseConfig class that defines dataSource bean.
* Create a dao class that provides jdbcTemplate using dataSource bean.

## Self Evaluation:
* What is datasource in JDBC?
* What is JDBCTemplate in Spring JDBC?
* What is a bean in spring and what does it do?
* What are the lifeccyles of spring bean using annotation?

Color Coding Text Note:
```diff
- red 
+ green 
! orange 
# gray
@@ blue @@
```


