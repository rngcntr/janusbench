#! /bin/sh

/var/fdb/scripts/fdb.bash &
sleep 2
/usr/bin/fdbcli --exec "configure new single ssd"

wait %1
