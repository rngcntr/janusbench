ARG VERSION
FROM docker.elastic.co/elasticsearch/elasticsearch:${VERSION}

ENV discovery.type=single-node \
    http.host=0.0.0.0 \
    transport.host=127.0.0.1

USER 1000

HEALTHCHECK --interval=1s --timeout=30s --retries=30 CMD curl -f http://localhost:9200
