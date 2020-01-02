all:

run:
	docker-compose -f docker/docker-compose.yml run --rm -e GREMLIN_REMOTE_HOSTS=janusgraph gremlin-client ./bin/gremlin.sh

clean:
	docker-compose down --remove-orphans
