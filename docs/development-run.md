# Local Development Environment
This document provides steps for setting up a development environment for developing and running this module locally.


## Prerequisites
Refer to [Development Setup](development-setup.md) for prerequisite software and local environment setup information.

## Installing
```text
git clone git@github.com:doubletuck/gym-roster-parser.git
cd gym-roster-parser
```

## Building
```shell
mvn install 
```

## Running
### Running locally
```shell
mvn spring-boot:run
```

or

```shell
java -jar target/gym-roster-parser.jar
```

To stop the server, issue a Control-C command in the shell where the service is running.
