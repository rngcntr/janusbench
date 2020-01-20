config := $(if $(index),"configurations/janusgraph-${storage}-${index}.yml","configurations/janusgraph-${storage}.yml")

HOSTFILE=hosts.txt
host=`head -n1 $(HOSTFILE)`

.PHONY: all run stop clean

all:
	docker-compose -f $(config) build

up:
	docker-compose -f $(config) up

run:
	docker-compose -f $(config) run --rm gremlin-client ./bin/gremlin.sh

stop:
	docker-compose -f $(config) stop

clean:
	docker-compose -f $(config) down --remove-orphans

push:
	docker-compose -f $(config) push

deploy:
	echo $(host)
	rsync $(config) $(host):~/docker-compose.yml
	ssh $(host) docker stack deploy --compose-file docker-compose.yml janusgraph-configuration

deploy-clean:
	echo $(host)
	ssh $(host) docker stack rm janusgraph-configuration
