# Welcome to the JuliGamesCore!

**Please check the Wiki for detailed information**

## Intoduction

The JuliGamesCore is the software used for communication, standardization, and coordination on
the [Juligames Network](https://juligames.net).
The Core offers access to a [hazelcast cluster](https://hazelcast.com/), MySQL, and centralized configuration files. The
System is very easy to set up and does not require a massive load of companion software to run. The only thing to set up
prior to this is a [MariaDB Server](https://mariadb.org/documentation/#getting-started).

## What does the current version of the Core offer to you?

* Hazelcast cluster
* Central Database access and login distribution
* Configuration accessible on all cores
* Support for Paper / Velocity (can be expanded easily)
* A separate Java Application called "Master" that manages and runs the cluster (no external software required)
* Easy-to-use API for all Platforms

### Primary features bundled with the core:

* Message API (uses [MiniMessage](https://docs.adventure.kyori.net/minimessage/index.html) and database)
* Sending data and commands between cores
* Storing and loading from central configurations
* Access a database via [JDBI](https://jdbi.org/)
*
    + Easy way to add your own features to the network
