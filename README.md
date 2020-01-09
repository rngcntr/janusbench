# JanusBench
### A benchmark framework for JanusGraph backend solutions

## Getting Started
1. Clone the repository
    ```sh
    git clone https://github.com/rngcntr/janusbench.git && cd janusbench
    ```
2. Build the current configuration ...
    ... with an external index backend:
    ```sh
    make storage=<...> index=<...>
    ```
    ... or without an external index backend:
    ```sh
    make storage=<...>
    ```
    The same syntax applies to all subsequent commands.
3. Start a gremlin client (and the backend services in the background)
    ```sh
    make storage=<...> index=<...> run
    ```
    Multiple gremlin clients can be started by running this command concurrently from different terminals. All required backends will be started once only. Therefore, all concurrent gremlin clients will communicate with the same JanusGraph instances and share the same data.
4. Stop all services
    ```sh
    make storage=<...> index=<...> stop
    ```
5. Reset all services including database storage
    ```sh
    make storage=<...> index=<...> clean
    ```

### Available Backends
- Storage (one required):
    - cassandra
    - scylla
    - berkeleyje
    - hbase
    - foundationdb
    - inmemory
- Index (optional):
    - elasticsearch
    - solr
    - lucene

### Compatibility Matrix
|                   | cassandra | scylla   | berkeleyje | hbase    | foundationdb | inmemory |
|:------------------|:---------:|:--------:|:----------:|:--------:|:------------:|:--------:|
| __none__          | &#10004;  | &#10004; | &#10004;   | &#10004; | (&#10004;)   | &#10004; |
| **elasticsearch** | &#10004;  | &#10004; | &#10004;   | &#10004; | (&#10004;)   | &#10008; |
| **solr**          | &#10004;  | &#10004; | &#10004;   | &#10004; | (&#10004;)   | &#10008; |
| **lucene**        | &#10004;  | &#10004; | &#10004;   | &#10004; | (&#10004;)   | &#10008; |

&#10004; functional

(&#10004;) runnable but not suitable for production environments

&#10008; incompatible by design

## What to do from here?

### Show logs of different services

Docker provides an easy way to show log output of running (and stopped) services.
After deploying the services (e.g. via `make storage=scylla index=solr run`), `docker ps` should show something similar to this:

```
CONTAINER ID        IMAGE                           COMMAND                  CREATED             STATUS          PORTS                                                                                                                                                                                NAMES
6093ed5570d6        configurations_gremlin-client   "docker-entrypoint.s…"   32 seconds ago      Up 31 seconds   8182/tcp                                                                                                                                                                             configurations_gremlin-client_run_671eeb90f2cd
37bcecf57464        configurations_janusgraph       "docker-entrypoint.s…"   33 seconds ago      Up 31 seconds   0.0.0.0:8182->8182/tcp                                                                                                                                                               configurations_janusgraph_1
2634c8ee8223        configurations_solr             "docker-entrypoint.s…"   33 seconds ago      Up 32 seconds   0.0.0.0:8983->8983/tcp                                                                                                                                                               configurations_solr_1
09b6b16f0eb4        configurations_zookeeper        "/docker-entrypoint.…"   34 seconds ago      Up 33 seconds   2888/tcp, 3888/tcp, 0.0.0.0:2181->2181/tcp, 8080/tcp                                                                                                                                 configurations_zookeeper_1
327fb9251398        configurations_scylla           "/docker-entrypoint.…"   34 seconds ago      Up 32 seconds   0.0.0.0:7000-7001->7000-7001/tcp, 0.0.0.0:7199->7199/tcp, 0.0.0.0:9042->9042/tcp, 0.0.0.0:9100->9100/tcp, 0.0.0.0:9160->9160/tcp, 0.0.0.0:9180->9180/tcp, 0.0.0.0:10000->10000/tcp   configurations_scylla_1
```

The rightmost colum, `NAMES` shows the name of each container which can be used to reference it.
So in order to view the logs produced by our JanusGraph instance, we use `docker logs configurations_janusgraph_1`.
As a more interactive solution, we can also use `docker logs --follow`.

### Connect to the gremlin server
```groovy
gremlin> :load /data/connect.groovy
```

Depending on whether or not the background services are already running, this process might take a while.
The warnings which appear every 3 seconds indicate that the server is not yet ready to serve the client.
As you can see below, it may take several seconcs until the connection is established.

<details><summary>Message Log</summary><p>

```groovy
gremlin> :load /data/connect.groovy
12:36:39 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
==>Configured janusgraph/172.25.0.4:8182-[336b6daf-d68e-47e6-a956-a7a3eec6c806]
==>All scripts will now be sent to Gremlin Server - [janusgraph/172.25.0.4:8182]-[336b6daf-d68e-47e6-a956-a7a3eec6c806] - type ':remote console' to return to local mode
gremlin> 12:36:40 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:36:43 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:36:43 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:36:46 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:36:46 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:36:49 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:36:49 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:36:52 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:36:52 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:36:55 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:36:55 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:36:58 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:36:58 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:01 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:01 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:04 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:04 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:07 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:07 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:10 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:10 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:13 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:13 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:16 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:16 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:19 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:19 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.
12:37:22 WARN  org.apache.tinkerpop.gremlin.driver.Host  - Marking Host{address=janusgraph/172.25.0.4:8182, hostUri=ws://janusgraph:8182/gremlin} as unavailable. Trying to reconnect.

gremlin>
```

</p></details>

### Send queries to the server

Gremlin has two built-in methods of benchmarking query processing performance.
An easy way is to use `clockWithResult(n) {...}` where `n` is the number of replications to run.
After following the instructions to load the [air-routes graph](https://github.com/krlawrence/graph/tree/master/sample-data), the following command returns the average runtime in milliseconds of 100 replications of the contained query (which searches for all single stop flights from Düsseldorf to Ålesund).

Info: The air-routes graph is not included in this project.
In order to use it, make sure the `janusgraph` container has access to the required `.graphml` file.

```groovy
gremlin> clockWithResult(100) {g.V().has('code','DUS').out().out().has('code','AES').path().by('city').toList()}
==>65.01110942999999
==>[path[Dusseldorf, London, Ålesund], path[Dusseldorf, Alicante, Ålesund], path[Dusseldorf, Oslo, Ålesund], path[Dusseldorf, Amsterdam, Ålesund], path[Dusseldorf, Copenhagen, Ålesund], path[Dusseldorf, Riga, Ålesund]]
e
```

A more sophisticated performance measurement is provided by `.profile()`.
For instance, this is the profile of a query that returns all airports directly reachable from Düsseldorf:

```groovy
gremlin> g.V().has('code','DUS').out().profile()
==>Traversal Metrics
Step                                                               Count  Traversers       Time (ms)    % Dur
=============================================================================================================
JanusGraphStep([],[code.eq(DUS)])                                      1           1           0.360    41.96
    \_condition=(code = DUS)
    \_orders=[]
    \_isFitted=true
    \_isOrdered=true
    \_query=multiKSQ[1]@2147483647
    \_index=airportIndex
  optimization                                                                                 0.016
  optimization                                                                                 0.093
JanusGraphVertexStep(OUT,vertex)                                     166         166           0.498    58.04
    \_condition=(EDGE AND visibility:normal)
    \_orders=[]
    \_isFitted=false
    \_isOrdered=true
    \_query=org.janusgraph.diskstorage.keycolumnvalue.SliceQuery@801a60ee
    \_vertices=1
  optimization                                                                                 0.002
                                            >TOTAL                     -           -           0.859        -
```

### Build your own graph

This project includes two example scripts which provide the functions `insertVertices(N)` and `upsertEdges(N)`.
Both functions assume a schema of a social graph with persons represented by vertices and relations represented by edges.
A person has a name and an age and knows other persons.
Each "knows" edge has a property which denotes the latest time these persons have seen each other.

- `insertVertices(N)` inserts N randomly generated Vertices into the graph.
- `upsertEdges(N)` randomly chooses N pairs of Vertices and creates an edge between them using the current date as a value for `lastSeen`. In case an edge already exists, the `lastSeen` property is updated without modifying the edge in any other way.

All scripts provided in `/services/gremlin-client/src/` can be loaded using `:load /data/<filename>.groovy`
You are encouraged to use the given ones as a starting point to write your own scripts and define your own graph!
