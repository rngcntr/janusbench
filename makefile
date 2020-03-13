all:
	mvn clean install -DskipTests
	cat res/stub.sh target/janusbench-0.1.0-jar-with-dependencies.jar > target/janusbench && chmod +x target/janusbench
	java -cp "target/janusbench-0.1.0-jar-with-dependencies.jar" picocli.AutoComplete -n janusbench -f de.rngcntr.janusbench.JanusBench -o target/janusbench_completion

run:
	java -jar target/janusbench-0.1.0-jar-with-dependencies.jar run -s $(storage) $(if $(index), -i $(index), )

test:
	mvn clean test

clean:
	mvn clean

doc:
	mvn javadoc:javadoc
