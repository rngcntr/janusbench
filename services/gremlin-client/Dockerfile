ARG VERSION
FROM janusgraph/janusgraph:${VERSION}

ENV GREMLIN_REMOTE_HOSTS=janusgraph

COPY --chown=janusgraph:janusgraph src/connect.groovy /data/connect.groovy

CMD ./bin/gremlin.sh
