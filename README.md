# JanusBench
### A benchmark framework for JanusGraph backend solutions

## Getting Started
1. Clone the repository
    ```sh
    git clone https://github.com/rngcntr/janusbench.git && cd janusbench
    ```
2. Build the current configuration
    ```sh
    make CONFIG=janusgraph-<storage>-<index>
    ```
3. Start the gremlin client (and the backend services in the background)
    ```sh
    make CONFIG=janusgraph-<storage>-<index> run
    ```
4. Stop all services
    ```sh
    make CONFIG=janusgraph-<storage>-<index> stop
    ```
5. Reset all services including database storage
    ```sh
    make CONFIG=janusgraph-<storage>-<index> clean
    ```

## What to do from the gremlin console

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
The following command returns the average runtime in milliseconds of 100 replications of the contained query (which searches for all single stop flights from Düsseldorf to Ålesund).

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
