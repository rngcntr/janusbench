all:
	docker-compose -f configurations/${CONFIG}.yml build

run:
	docker-compose -f configurations/${CONFIG}.yml run --rm -e GREMLIN_REMOTE_HOSTS=janusgraph gremlin-client ./bin/gremlin.sh

clean:
	docker-compose -f configurations/${CONFIG}.yml down --remove-orphans
