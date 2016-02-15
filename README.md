# Client Uniform System Kolaer

Client Uniform System Kolaer - Module system that is used in the organization KolAER.

## About of client
Client use all employees in KolAER organization. Located .jar file of client on file server, thus one copy used by all employees. In this many advantages, but have one problem - update this client so problematic. Therefore modular architecture has been designed.

## Architecture (v0.0.1 and higher)

Architecture designed in such a way that reading modules on folder and have link on class loader modules. If you need to update the module, close it class loader and delete his dependencies. Why i'm not use OSGi framework? Because the familiarization would take more time than it (development speed was the priority). In future i plan to user OSGi framework.

## Links
1. [Architecture](https://yadi.sk/i/AO8dxN-RobNxA "https://yadi.sk/i/AO8dxN-RobNxA")
