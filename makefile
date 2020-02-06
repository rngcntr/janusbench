all:
	mvn clean install -DskipTests

run:
	java -jar target/janusbench-0.0.2-jar-with-dependencies.jar run -s $(storage) $(if $(index), -i $(index), )

list:
	java -jar target/janusbench-0.0.2-jar-with-dependencies.jar list

test:
	mvn clean test

clean:
	mvn clean
