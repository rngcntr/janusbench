all:
	mvn clean install -DskipTests

run:
	java -jar target/janusbench-0.0.1-jar-with-dependencies.jar

test:
	mvn clean test

clean:
	mvn clean
