[![][NUBOMEDIA Logo]][NUBOMEDIA]

Copyright © 2016 [NUBOMEDIA]. Licensed under [Apache 2.0 License].

# NUBOMEDIA Media Client

In order to deploy your application on the NUBOMEDIA Cloud Platform, there are there main libraries to include as dependency to your projects.
These libraries extend the Kurento libraries with functionalities on how to obtain network resources for example the IP address of the Kurento Media Server.
The next sections below provide an overview of each library, how to obtain the library and how to include it into your project.

## NUBOMEDIA Media Client (NMC) Library
The NMC library provides the base functionality to compliment the Kurento Client for auto discovery of the Kurento Media Server (KMS) IP.

### Getting Started
This section explains where to obtian the NMC and how to include it on your project. The assumption here is you are a maven Guru. If not, there are plenty of tutorials online to get you started.

The NMC is distributed via Maven can be found on [Maven central repository](http://search.maven.org/#search%7Cga%7C1%7Cde.fhg).
Simply include it on your project's pom.xml file as describe below, then run the command ```mvn install```.

```
<dependencies>
...
<dependency>
	<groupId>de.fhg.fokus.nubomedia</groupId>
	<artifactId>nubomedia-media-client</artifactId>
	<version>6.5.0/version>
</dependency>
<dependency>
  <!-- Nubomedia client dependency -->
		<groupId>de.fhg.fokus.nubomedia</groupId>
		<artifactId>nubomedia-media-client</artifactId>
		<version>1.0.2</version>
	</dependency>
</dependencies>
```
*NOTE: At the time of writing the release version is 1.0.2. This might change as development evolves, so make sure you have the right version (latest) and replace the version number accordingly.*

### KMS Auto Discovery Process
Kurento Client discovers KMS with the following procedure:

1. If there are a system property with the value “kms.url”, its value will be returned
1. If the file ```~/.kurento/config.properties``` doesn’t exist, the default value ```ws://127.0.0.1:8888/kurento``` will be returned
1. If the file ```“~/.kurento/config.properties”``` exists:
  1. If the property “kms.url” exists in the file, its value will be returned. For example, if the file has the following content:
  ```
  kms.url: ws://4.4.4.4:9999/kurento
  ```
  The value “ws://4.4.4.4:9999/kurento” will be returned.
  
  2. If the property “kms.url.provider” exists in the file, it should contain the name of a class that will be used to obtain the KMS url. 
  In this case:
 
  ```
  kms.url.provider:de.fhg.fokus.nubomedia.kmc.KmsUrlProvider
  ```
  The class ```de.fhg.fokus.nubomedia.kmc.KmsUrlProvider``` will be instantiated with its default constructor. 

This class is a class in the NMC library which implements the interface ```org.kurento.client.internal.KmsProvider```. 
This interface provides the following methods:

```
String reserveKms(String id) throws NotEnoughResourcesException;
String reserveKms(String id, int loadPoints) throws NotEnoughResourcesException;
void releaseKms(String id);
```
The method ```reserveKms()``` will be invoked and its value returned. If ```NotEnoughResourcesException``` exception is thrown, it will be thrown in KurentoClient.create() method.


### REST Interface to NUBOMEDIA Virtual Network Function (VNF)
In conjunction to implementing the ```org.kurento.client.internal.KmsProvider``` this library uses a simple REST client to interact with VNF. This section is not necessary important for developing your application, but so you know how everything glues together, here are a few technical explanations.

During deploying on the NUBOMEDIA PaaS, some envirnoment variables are set by the NUBOMEDIA PaaS Manager on the application container with the address on which the VNF can be reached and also a Virtual Network Function Identifier for your application is created. The ```VNFRService``` interface provides the following API:
```
	/**
	 * Returns a list of VNFRs managed by the Elastic Media Manager
	 * @return a list of all virtual network function records
	 */
	public List<VirtualNetworkFunctionRecord> getListRegisteredVNFR();
	
	/**
	 * Returns a list with detailed information about all apps registered to the VNFR with this identifier
	 * @param vnfrId - the virtual network function record identifier
	 * @return list - the list of application records
	 */
	public List<ApplicationRecord> getListRegisteredApplications(String vnfrId);
	
	/**
	 * Registers a new App to the VNFR with a specific VNFR ID
	 * @param loadPoints - capacity
	 * @param externalAppId - application identifier
	 * @return ApplicationRecord - the application's record
	 */
	public ApplicationRecord registerApplication(String externalAppId, int loadPoints) 
		throws NotEnoughResourcesException;
	
	/**
	 * Unregisters an App to the VNFR with the specific internal application identify
	 * @param internalAppId - identifier of the registered application on the VNFR
	 */
	public void unregisterApplication(String internalAppId) throws NotEnoughResourcesException;
	
	/**
	 * Sends a heart beat to the elastic media manager as a keep alive mechanism for registered sessions
	 * @param internalAppId - the internal application identifier
	 * @param internalAppId
	 */
	public void sendHeartBeat(String internalAppId);
```
And here is the connection:
```kurentoClient.Create().reserveKms()``` -> get instance of ```de.fhg.fokus.nubomedia.kmc.KmsUrlProvider```-> ```VNFRService.registerApplication()``` return KMSURL or throws ```NotEnoughResourcesException```

If return KMSURL = TRUE then ```VNFRService.sendHeartBeat``` every 1 minute to VNF for the KMS.

Subsequently:
```kurentoClient.Create().releaseKms()``` -> get instance of ```de.fhg.fokus.nubomedia.kmc.KmsUrlProvider```-> ```VNFRService.unregisterApplication()``` return void or throws ```NotEnoughResourcesException``` -> stopHeartBeatTask();


# What is NUBOMEDIA

This project is part of [NUBOMEDIA], which is an open source cloud Platform as a
Service (PaaS) which makes possible to integrate Real Time Communications (RTC)
and multimedia through advanced media processing capabilities. The aim of
NUBOMEDIA is to democratize multimedia technologies helping all developers to
include advanced multimedia capabilities into their Web and smartphone
applications in a simple, direct and fast manner. To accomplish that objective,
NUBOMEDIA provides a set of APIs that try to abstract all the low level details
of service deployment, management, and exploitation allowing applications to
transparently scale and adapt to the required load while preserving QoS
guarantees.

# Documentation

The [NUBOMEDIA] project provides detailed documentation including tutorials,
installation and [Development Guide].

# Source

Source code for other NUBOMEDIA projects can be found in the [GitHub NUBOMEDIA
Group].

# News


Follow us on Twitter @[NUBOMEDIA Twitter].

# Issue tracker

Issues and bug reports should be posted to [GitHub Issues].

# Licensing and distribution

Licensed under the Apache License, Version 2.0 (the "License"); you may not use
this file except in compliance with the License. You may obtain a copy of the
License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed
under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.

# Contribution policy

You can contribute to the NUBOMEDIA community through bug-reports, bug-fixes,
new code or new documentation. For contributing to the NUBOMEDIA community,
drop a post to the [NUBOMEDIA Public Mailing List] providing full information
about your contribution and its value. In your contributions, you must comply
with the following guidelines

* You must specify the specific contents of your contribution either through a
  detailed bug description, through a pull-request or through a patch.
* You must specify the licensing restrictions of the code you contribute.
* For newly created code to be incorporated in the NUBOMEDIA code-base, you
  must accept NUBOMEDIA to own the code copyright, so that its open source
  nature is guaranteed.
* You must justify appropriately the need and value of your contribution. The
  NUBOMEDIA project has no obligations in relation to accepting contributions
  from third parties.
* The NUBOMEDIA project leaders have the right of asking for further
  explanations, tests or validations of any code contributed to the community
  before it being incorporated into the NUBOMEDIA code-base. You must be ready
  to addressing all these kind of concerns before having your code approved.

# Support

The NUBOMEDIA community provides support through the [NUBOMEDIA Public Mailing List].

[Apache 2.0 License]: https://www.apache.org/licenses/LICENSE-2.0.txt
[Development Guide]: http://nubomedia.readthedocs.org/
[GitHub Issues]: https://github.com/nubomedia/bugtracker/issues
[GitHub NUBOMEDIA Group]: https://github.com/nubomedia
[NUBOMEDIA Logo]: http://www.nubomedia.eu/sites/default/files/nubomedia_logo-small.png
[NUBOMEDIA Twitter]: https://twitter.com/nubomedia
[NUBOMEDIA Public Mailing list]: https://groups.google.com/forum/#!forum/nubomedia-dev
[NUBOMEDIA]: http://www.nubomedia.eu