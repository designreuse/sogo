JAVA_OPTS="-server -Xms1048m -Xmx1048m -XX:PermSize=556m -XX:MaxPermSize=556M"
mvn clean  package -s maven-settings.xml   -U -B  -T 1C -Dmaven.test.skip=true -Dmaven.compile.fork=true
