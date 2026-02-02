export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
export PATH=$JAVA_HOME/bin:$PATH

cd ./tarea1/

mvn clean
mvn package

cd ../tarea2/

mvn clean
mvn package

cd ../tarea3mobile/

mvn clean
mvn package

cd ../

mv ./tarea1/target/*.jar ./tarea2/target/*.war ./tarea3mobile/target/*.war ./
