ARG VERSION
FROM janusgraph/janusgraph:${VERSION}

COPY --chown=janusgraph:janusgraph src/connect.groovy /data/connect.groovy

CMD ./bin/gremlin.sh
