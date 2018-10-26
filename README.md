node-integration-test
===
Integration test for RSong node.

## Caution
This project is still under active development.  Any changes to the [rchain/models](https://github.com/rchain/rchain/tree/dev/models) will require
- manual re-install of the [lib/models_2.12.jar](./lib/models_2.12.jar)
- project rebuild

## Getting Started

### quick start
run the docker image for integration tests
```aidl
for payload_size in '10000 100000 1000000 10000000';do 
    docker run \
        -e GRPC_PORT_EXTERNAL=>NODE-PORT\
        -e GRPC_SERVER=<NODE-HOST>  \
        $payload_size
done
```

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system

## Install

- Prerequisites
- Clone & Build
- Execute 

### Prerequisites

- [sbt](https://www.scala-sbt.org/)
- [JDK8](http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html?printOnly=1)
- [docker](https://www.docker.com/) 


### Environment variables:
```aidl
export GRPC_SERVER="localhost'
export GRPC_PORT_INTERNAL=40404
```

#### Clone Build &run

```
git clone git@github.com:kayvank/rsong-acquisition.git
cd node-integration-test
sbt compile 
sbt "run 5000000  ## PAYLOAD-SIZE: 5 meg
```

