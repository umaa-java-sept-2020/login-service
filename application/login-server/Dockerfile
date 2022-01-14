FROM tomcat:8.5.66-jdk8-corretto
LABEL maintainer="rabindrapatra825@gmail.com"

COPY setenv.sh /usr/local/tomcat/bin/
COPY target/login-server.war /usr/local/tomcat/ROOT.war

EXPOSE 8080 8000
