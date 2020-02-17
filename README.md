# JanusBench
### A benchmark framework for JanusGraph backend solutions

## Quick Start
1. Clone the repository
    ```sh
    git clone https://github.com/rngcntr/janusbench.git && cd janusbench
    ```
2. Build the project
    ```sh
    mvn clean install -DskipTests
    ```
    Optionally, you can crate an executable shell script which runs `janusbench`:
    ```sh
    cat res/stub.sh target/janusbench-0.1.0-jar-with-dependencies.jar > target/janusbench && chmod +x target/janusbench
    ```
3. Show available storage/index backends and benchmarks
    ```sh
    janusbench list storage
    janusbench list index
    janusbench list benchmark
    ```
4. Run example benchmarks
    ```sh
    janusbench run -s cassandra -i elasticsearch IndexedEdgeExistenceOnSupernode

## Available Backends
- Storage (one required):
    - cassandra
    - scylla
    - berkeleyje
    - hbase
    - yugabyte
    - foundationdb
    - inmemory
- Index (optional):
    - elasticsearch
    - solr
    - lucene

### Compatibility Matrix
|                   | cassandra | scylla   | berkeleyje | hbase    | yugabyte | foundationdb | inmemory |
|:------------------|:---------:|:--------:|:----------:|:--------:|:--------:|:------------:|:--------:|
| __none__          | &#10004;  | &#10004; | &#10004;   | &#10004; | &#10004; | (&#10004;)   | &#10004; |
| **elasticsearch** | &#10004;  | &#10004; | &#10004;   | &#10004; | &#10004; | (&#10004;)   | &#10008; |
| **solr**          | &#10004;  | &#10004; | &#10004;   | &#10004; | &#10004; | (&#10004;)   | &#10008; |
| **lucene**        | &#10004;  | &#10004; | &#10004;   | &#10004; | &#10004; | (&#10004;)   | &#10008; |

&#10004; functional

(&#10004;) runnable but not suitable for production environments

&#10008; incompatible by design

## Building your own configurations
A lot of services and composed configurations are already available within janusbench.
All of the supported services, which include storage and index backends, are defined and managed by Dockerfiles in their own subdirectory of `docker/services`.
At the moment, new backends need to be registrated within the corresponding backend classes [Storage.java](https://github.com/rngcntr/janusbench/blob/master/src/main/java/de/rngcntr/janusbench/backend/Storage.java) and [Index.java](https://github.com/rngcntr/janusbench/blob/master/src/main/java/de/rngcntr/janusbench/backend/Index.java).

By using these services, more complex scenarios can be built using Docker Compose.
The necessary Docker Compose files are located in [docker/configurations](https://github.com/rngcntr/janusbench/blob/master/docker/configurations) and use a consistent naming scheme where `storage` and `index` refer to the names of backend services:
```
^janusgraph\-(?P<storage>[a-z]+)(\-(?P<index>[a-z]+))?\.yml$
```
The files themselves use the normal Docker Compose format and define combinations of services that can be used when running benchmarks.

## Building your own benchmarks
In janusbench, there are two kinds of benchmarks:

### Simple Benchmarks
Simple Benchmarks -- located within the package [de.rngcntr.janusbench.benchmark.simple](https://github.com/rngcntr/janusbench/blob/master/src/main/java/de/rngcntr/janusbench/benchmark/simple) -- are very basic tasks like adding vertices and edges or testing the existence of such elements.
This kind of benchmark is not thought to be called by the user directly.
Instead, it represents a key task, whose runtime is measured by a composed benchmark (see section below).

All benchmarks in the scope of this package need to be public and extend the abstract class [Benchmark](https://github.com/rngcntr/janusbench/blob/master/src/main/java/de/rngcntr/janusbench/util/Benchmark.java).
The inherited methods `buildUp()` and `tearDown()` can be used to manage local data structures which may be utilized to support the actual performance-measured steps taken in `performAction()`.

### Composed Benchmark
Simple Benchmarks are located within the package [de.rngcntr.janusbench.benchmark.composed](https://github.com/rngcntr/janusbench/blob/master/src/main/java/de/rngcntr/janusbench/benchmark/composed).
Composed benchmarks within this package need to be public and extend the class [ComposedBenchmark](https://github.com/rngcntr/janusbench/blob/master/src/main/java/de/rngcntr/janusbench/util/ComposedBenchmark.java).
As for all benchmarks, the `buildUp()`, `performAction()` and `tearDown()` methods are called within the lifecycle of each benchmark.
For composed benchmarks, we can use the `buildUp()` method to assemble it from various simple benchmarks.
In order to do so, one can instantiate different simple benchmarks, group them into logical units using [ComposedBenchmarks](https://github.com/rngcntr/janusbench/blob/master/src/main/java/de/rngcntr/janusbench/util/ComposedBenchmark.java) and add them to the final composed benchmark by calling `addComponent()`.

#### Parametrizing benchmarks
Composed benchmarks can use parameters, e.g. to control their number of iterations.
In order to keep the CLI clean, these parameters are parsed from a [file](https://github.com/rngcntr/janusbench/blob/master/conf/defaults) which contains default values.
These files are parsed at runtime using [SnakeYAML](https://bitbucket.org/asomov/snakeyaml) and will only be able to set public, non-static fields of your benchmark classes.

## Logging Results
