ARG VERSION
FROM scylladb/scylla:${VERSION}

HEALTHCHECK --interval=3s --timeout=10s --retries=5 \
    CMD '[ $$(nodetool statusgossip) = running ]'
