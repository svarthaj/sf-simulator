# Smart Factory Simulation Module

The SF Simulation Module is a simple data generator based on function concatenation that is used to model different Smart Devices and the data they generate. This module is part of a bigger project about managing and modeling custom Smart Factory scenarios.  

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

This project is writen in Java and uses Maven as a dependency manager, so to have it up and running you only need a Java 8 installation and the Maven package. For simplicity this was developed inside the Eclipse IDE and used its package manager. However this guide is made to be used without any IDE and works purely with a CLI.

```
sudo apt-get install maven
```

The last step is to set the MongoDB Replica Set used for data storage. First you need to install MongoDB. On Ubuntu, the best option is to install the package maintained by MongoDB Inc. itself. This can be easily done following their [tutorial page](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/). After all is done, you need to create the directories for your DB. On Ubuntu, run
```
sudo mkdir -p /data/db /data/log
```

Finally you can deploy the database with
```
sudo mongod --replSet rs0
```

### Installing

Building a Maven project is really easy with the pom.xml. You just need to open the project file and run
```
mvn package
```

This will run a script that downloads and packages all dependencies in one executable. If everything is correct there will be a ```.jar``` file inside ```/target``` containing everything needed to deploy the simulator.

## Deployment

Since our
If the installation was successful you can run the ```.jar``` file with
```
java -jar simulator-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

## Project details
This section is reserved for explaining the file organization of the Project.

###  Adding new function types
When adding new functions some steps must be taken:
1. Add the type to the function enum class ```src/main/java/sfp/simulator/SensorType.java```.
1. Add the switch key to the JSON parser ```src/main/java/sfp/simulator/FactoryJSONParser.java```.
1. Add the class with the new function. Don't forget to make it extend the abstract class *Sensor* and to implement all its methods.

### To-do's
1. Make the data publishing on MQTT more accessible without Telegraph (timestamping, for example)

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Paho](https://www.eclipse.org/paho/) - MQTT Messaging Protocol
* [MongoDB](https://www.mongodb.com/) - Object Database

## Authors

* **Mathias Mormul** - *Project Leader* - [Mormulms](https://github.com/mormulms)
* **Lucas Leal** - *Initial work* - [Svarthaj](https://github.com/Svarthaj)

