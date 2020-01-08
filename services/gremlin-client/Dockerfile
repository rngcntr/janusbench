ARG VERSION
FROM janusgraph/janusgraph:${VERSION}

COPY --chown=janusgraph:janusgraph src/connect.groovy /data/connect.groovy
COPY --chown=janusgraph:janusgraph src/upsertEdges.groovy /data/upsertEdges.groovy
COPY --chown=janusgraph:janusgraph src/insertVertices.groovy /data/insertVertices.groovy

CMD ./bin/gremlin.sh
