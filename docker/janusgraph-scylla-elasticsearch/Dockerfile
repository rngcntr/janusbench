ARG VERSION
FROM janusgraph/janusgraph:${VERSION}

ENV janusgraph.gremlin.graph=org.janusgraph.core.JanusGraphFactory \
    janusgraph.graph.graphname=graph \
    janusgraph.storage.backend=cql \
    janusgraph.storage.hostname=scylla \
    janusgraph.storage.keyspace=airlines \
    janusgraph.cache.db-cache=true \
    janusgraph.index.search.backend=elasticsearch \
    janusgraph.index.search.hostname=elasticsearch \
    janusgraph.index.search.index-name=airlines \
    janusgraph.index.search.elasticsearch.client-only=true

COPY --chown=janusgraph:janusgraph src/gremlin-server.yaml ${JANUS_HOME}/conf/gremlin-server/gremlin-server.yaml
COPY --chown=janusgraph:janusgraph src/initialize-server.groovy /opt/initialize-server.groovy

COPY --chown=janusgraph:janusgraph src/air-routes.graphml /data/air-routes.graphml
