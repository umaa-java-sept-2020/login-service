 mvn spring-boot:run -f application/login-server/pom.xml \
-Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"

