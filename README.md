Ad-Astra 3
==========

This application is part of Ad-Astra 3, a content management system developed for the Belgian astronomy museum MIRA (NPO). Its task is to manager storage and distribution of multimedia presentations which are to be displayed on kiosks throughout the museum, while simplifying administrative tasks to manage kiosks and presentations.


Terminology
-----------

* Server: the (thick) system responsible for storage, distribution and management of the presentations
* Kiosk: the (thin) system which is to display the presentations, as well as process any user input
* Presentation: a collection of files to be displayed at one or more kiosks


Architecture
------------

The main objectives of the system are:

* Easy administration (user-friendly interface, minimal configuration)
* Robustness
* Future-proof technologies

To keep the kiosk code (which is run on a very lightweight client) fast and lean, we opted to put as much logic as possible in the server application. Concretely, this means that when a kiosk comes on-line, it'll advertise its presence and functionality on the local network, after which all will be managed by the server: the configuration will be loaded from storage, the kiosk will get configured using an RPC mechanism, and the location of the relevant presentation files will be pushed as well. For more details, see the descriptions below.


### Presentation format

HTML5 + Javascript

TODO: clarify (future proof, easy to render client-side, compact, compatible with a SCM)


### Storage

Subversion SCM

TODO: clarify (allows versioning, easy-to-use as it's file based)


### Network communication

We chose to use UPnP, as it takes care of several tedious aspects:

* Identification: each kiosk has to have a unique ID
* Configuration: discovery enables the server to _discover_ the kiosks, which means that the kiosks itself won't need any local configuration
* Eventing: allowing the server to respond to network (device entering, leaving, updating its status, ...) and custom events
* RPC: through actions, the server is able to execute methods on each of the kiosks


### Administrative interface

Web interface

TODO: clarify (user-friendly, easy to access)




Server application
==================

Libraries
---------

Libraries used to implement the essential technologies used within the system:

* Subversion: official JavaHL bindings
* UPnP: Cling
* Web interface: JWt

Auxiliary libraries:

* Servlet engine: Jetty
* Logging: log4j (+ slj4j for Jetty)
* Testing: TestNG
* XML parsing: XPP
* Configuration library from Apache Commons
