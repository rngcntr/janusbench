ARG VERSION
FROM janusgraph/janusgraph:${VERSION}

ENV JANUS_STORAGE_TIMEOUT=30

COPY --chown=janusgraph:janusgraph src/gremlin-server.yaml ${JANUS_HOME}/conf/gremlin-server/gremlin-server.yaml
COPY --chown=janusgraph:janusgraph src/initialize-server.groovy /opt/initialize-server.groovy

COPY --chown=janusgraph:janusgraph src/janusgraph-configurationmanagement.properties /etc/opt/janusgraph/janusgraph-configurationmanagement.properties
COPY --chown=janusgraph:janusgraph src/janusgraph.properties /etc/opt/janusgraph/janusgraph.properties

COPY --chown=janusgraph:janusgraph src/air-routes.graphml /data/air-routes.graphml
COPY --chown=janusgraph:janusgraph src/loadGraph.groovy /docker-entrypoint-initdb.d/loadGraph.groovy
