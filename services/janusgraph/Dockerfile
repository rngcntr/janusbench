ARG VERSION
FROM janusgraph/janusgraph:${VERSION}

ENV janusgraph.gremlin.graph=org.janusgraph.core.JanusGraphFactory \
    janusgraph.graph.graphname=graph \
    JANUS_STORAGE_TIMEOUT=30

# only needed for berkeley
RUN mkdir -p /db/berkeley && chmod 777 /db/berkeley

COPY --chown=janusgraph:janusgraph src/gremlin-server.yaml ${JANUS_HOME}/conf/gremlin-server/gremlin-server.yaml
COPY --chown=janusgraph:janusgraph src/initialize-server.groovy /opt/initialize-server.groovy

COPY --chown=janusgraph:janusgraph src/air-routes.graphml /data/air-routes.graphml
COPY --chown=janusgraph:janusgraph src/loadGraph.groovy /docker-entrypoint-initdb.d/loadGraph.groovy
