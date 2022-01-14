JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=container"
rm -rf /usr/local/tomcat/webapps/ROOT
rm -rf /usr/local/tomcat/webapps/ROOT.war
cp /usr/local/tomcat/ROOT.war /usr/local/tomcat/webapps/ROOT.war