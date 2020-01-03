config := $(if $(index),"configurations/janusgraph-${storage}-${index}.yml","configurations/janusgraph-${storage}.yml")

.PHONY: all run stop clean

all:
	docker-compose -f $(config) build

run:
	docker-compose -f $(config) run --rm -e GREMLIN_REMOTE_HOSTS=janusgraph gremlin-client ./bin/gremlin.sh

stop:
	docker-compose -f $(config) stop

clean:
	docker-compose -f $(config) down --remove-orphans
