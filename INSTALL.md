Servlet container
=================

To run Codri, you need a servlet container like Tomcat or Jetty. Although it
shouldn't matter exactly which container you use, it should at least support
servler API v2.5 (e.g. Tomcat 6 or higher).

However, there tend to be slight differences between several containers. E.g.
Tomcat doesn't provide a JSTL implementation, while other containers might.
The default Maven build process is ready to use with Tomcat 6, so if you use
any other container you might need to change some stuff in order to get it
working.

Obviously, you'll need to have the proper rights to deploy the application to
the Tomcat container. More specifically, the Maven deploy plugin will need the
"manager-script" role. Create a user, grant him the "manager-script" role, and
save the credentials in your Maven settings file (see
[this page](http://mojo.codehaus.org/tomcat-maven-plugin/configuration.html)).


Content storage
===============

Data and related directives are stored in a external repository, read out by Codri's
RepositoryReaders. Currently, there is only one reader, using a external Subversion
repository.

### Setup

Create a SVN repository (supported protocols: file://, http(s)://, svn://), and configure
the repository.location in Codri's server configuration file.


### Commit hook

Codri does not monitor the repository, but expects you to notify in case of changes.
To do that, create a post-commit hook sending a HTTP PUT request on the repository
endpoint of the REST API:
> $ cat post-commit  
> \# \#!/bin/sh  
> \# curl --silent -X PUT http://localhost:8080/codri/repository  


Subversion JavaHL
=================

Codri makes use of the native JavaHL bindings Subversion provides. This means that
you need to install these libraries, and make sure they can be accessed properly.

### install the bindings

Debian: aptitude install libsvn-java


### add the native library path to java.properties.path

Debian: nothing to do, /usr/lib/jni already in java.properties.path


### add the java library to Tomcat's classpath

Debian (assuming shared.loader contains /var/lib/tomcat6/shared/*.jar):
> ln -s /usr/share/java/svn-javahl.jar /var/lib/tomcat6/shared/svn-javahl.jar
