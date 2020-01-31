ARG VERSION
FROM foundationdb/foundationdb:${VERSION}

COPY /src/custom_init.sh /var/fdb/scripts/custom_init.sh
RUN chmod +x /var/fdb/scripts/custom_init.sh

CMD ["/bin/sh", "-c", "/var/fdb/scripts/custom_init.sh"]
