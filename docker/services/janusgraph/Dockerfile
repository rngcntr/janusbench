ARG VERSION
FROM janusgraph/janusgraph:${VERSION}

ENV JANUS_STORAGE_TIMEOUT=30
ENV janusgraph.graph.replace-instance-if-exists=true

COPY --chown=janusgraph:janusgraph src/log4j-server.properties ${JANUS_HOME}/conf/gremlin-server/
COPY --chown=janusgraph:janusgraph src/gremlin-server.yaml ${JANUS_HOME}/conf/gremlin-server/gremlin-server.yaml
COPY --chown=janusgraph:janusgraph src/initialize-server.groovy /opt/initialize-server.groovy
