ARG VERSION
FROM janusgraph/janusgraph:${VERSION}

COPY --chown=janusgraph:janusgraph src/connect.groovy /data/connect.groovy
COPY --chown=janusgraph:janusgraph src/benchmark.groovy /data/benchmark.groovy
COPY --chown=janusgraph:janusgraph src/insertEdges.groovy /data/insertEdges.groovy

CMD ./bin/gremlin.sh
