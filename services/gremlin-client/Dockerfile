ARG VERSION
FROM janusgraph/janusgraph:${VERSION}

ADD --chown=janusgraph:janusgraph src /data/
ADD --chown=janusgraph:janusgraph src /data/
ADD --chown=janusgraph:janusgraph src /data/

CMD ./bin/gremlin.sh
