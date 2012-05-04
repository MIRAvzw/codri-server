Codri
=====

This application is part of Codri, a content management system developed for
the Belgian astronomy museum MIRA (an NPO). Its task is to manager storage and
distribution of multimedia presentations which are to be displayed on kiosks
throughout the museum, while simplifying administrative tasks to manage kiosks
and presentations.


Terminology
-----------

* Server: the (thick) system responsible for storage, distribution and
  management of the presentations
* Kiosk: the (thin) system which is to display the presentations, as well as
  process any user input
* Presentation: a collection of files to be displayed at one or more kiosks


Architecture
------------

The main objectives of the system are:

* Easy administration (user-friendly interface, minimal configuration)
* Robustness
* Future-proof technologies

To keep the kiosk code (which is run on a very lightweight client) fast and
lean, we opted to put as much logic as possible in the server application. In
practice, this means that when a kiosk comes on-line, it'll advertise its
presence and functionality on the local network, after which all will be
managed by the server: the configuration will be loaded from storage, the
kiosk will get configured using an RPC mechanism, and the location of the
relevant presentation files will be pushed as well. For more details, see the
descriptions below.


### Presentation format

HTML5 + Javascript

TODO: clarify (future proof, easy to render client-side, compact, compatible
with a SCM, compatible with current format)


### Storage

Subversion SCM

TODO: clarify (allows versioning, easy-to-use as it's file based)


### Network communication

We use a variety of lightweight protocols, each taking care of one specific aspect of the network communication.

#### Discovery

Each of the Codri servers are discovered through mDNS, at the following static
hostnames:
* control.codri.local: control endpoint, service the REST interface (see below)
* data.codri.local: where the data is to be fetched
* mail.codri.local: SMTP server
* log.codri.local: UDP Syslog server
* www.codri.local: human-friendly webinterface

After registering itself with the control server, each kiosk is reachable at
an .codri.local endpoint as well. The exact endpoint (composed of the hostname
and aforementioned .codri.local suffix) is to be chosen freely, but should
obviously be unique. Current client software generates something like
"efikamx-$uid.codri.local", with the $uid composed of the 3 machine-specific
bytes from the MAC address.


#### Control

Server as well as kiosk provide a REST-interface, allowing to acquire and
configure several parameters of the system.

TODO: document the interface.


#### Data distribution

As we use Subversion, the SVN protocol (served by svnserve) is used to push
data.



### Administrative interface

WIP



History
-------

The Codri project originates in the master's dissertation of Tim Besard
(2011, Hogeschool Gent), replacing the then DVD-based content format used
within the MIRA museum.




Server application
==================

Libraries
---------

Libraries used to implement the essential technologies used within the system:

* Subversion: official JavaHL bindings
* Enterprise framework: Spring

Auxiliary libraries:

* Logging: log4j & logback
* XML parsing: XPP
* Configuration library from Apache Commons
* Building: Maven
* Servlet engine: Tomcat
