# Umple.Orion
This is a plugin to extend the [Eclipse Orion](https://wiki.eclipse.org/Orion) IDE to support features that facilitate cloud-based development of [Umple](http://cruise.eecs.uottawa.ca/umple/) software. The main feature of this plugin gives Orion users the ability to generate code from Umple code.

# Contents
 - [Features](#features)
 - [Architecture](#architecture)
 - [Development Setup](#development-setup)
 - [Resources](#resources)

# Features
## Code Generation
As it is the core feature of Umple, code generation is also the core feature of the plugin. In Orion, after installing the Umple plugin, you can find all of the code generation features in the `Tools` menu while editing a `.ump` file. These options are hidden otherwise because they are of no use to other filetypes. **Note**: In order to see the generated files, the user must refresh the page after the processing is complete.

## Syntax Hilighting
When editing an Umple file (`.ump`) in Orion, keywords will be highlighted according to [Umple's grammar](http://cruise.eecs.uottawa.ca/umple/UmpleGrammar.html).

## Umple Icons
Users can more easily locate and distinguish Umple files because they have their own custom icons.

# Architecture
The following is a simplified architecture of the [code generation](#code-generation):

![Umple-Orion-Architecture](https://nwam.github.io/umple.orion/Umple-Orion-Architecture.png)

#### Components
 - **Umple Server**: Machine containing the Orion Server and umple-orion-server.
 - **Orion Client**: The Orion client, as accessed via browser by an Orion user. ([example](http://orionhub.org/))
 - **Orion Server**: The server side of Orion. Source available [here](http://download.eclipse.org/orion/).
 - **user files**: The files of the user who is logged into the Orion Client. Orion's architecture stores user files in the Orion Server. Each user has their own directory with their projects' files.
 - **umple-orion-plugin** and **umple-orion-server**: Main components which make up this project.

A more detailed description of Orion's architecture (on it's own) can be found [here](https://wiki.eclipse.org/Orion/Documentation/Developer_Guide/Architecture)

#### Actions
 - **Request**: A Request is initiated by the user through the Orion Client's UI when a user clicks on an Umple command in the Tools menu. A Request contains the username of the requestee, the requested file for Umple generation, and the language requested for generation. 
 - **Execute Umple.jar**: This action is initiated by a successful Request to the umple-orion-server. The umple-orion-server executes Umple's generation features in the requested language, on the requested file. This is facilitated by the fact that the umple-orion-server and the Orion Server are on the same machine (Umple Server).

# Development Setup

 - [0.0. Windows Development](#00-windows-development)
 - [0.1. Dependencies](#01-dependencies)
 - [0.2 Quick Setup](#021-quick-setup)
 - [0.3. Quick Cleanup (Unix)](#03-quick-cleanup-unix)
 - [1. umple-orion-server Setup](#1-umple-orion-server-setup)
 - [2. Docker Setup](#2-docker-setup)
 - [3. umple-orion-plugin Setup](#3-umple-orion-plugin-setup)
 - [4. Managing Docker](#4-managing-docker)

## 0.0 Windows Development
A developemnt enviornemnt for umple.orion is supported on both Windows and Unix. If available, we reccomend to use Linux to develop the umple.orion project because of the rigorus extra configuration required to set up and work with a Windows enviornment. Similarly, for anyone deploying this system, it highly recomended to deploy on Linux servers.  

## 0.1. Dependencies
 - [Docker](#2-docker-setup)
 - [Maven](https://maven.apache.org/install.html)
 - [Umple.jar is installed to your local Maven repository](#installing-umple-to-your-local-maven-repository)

## 0.2 Quick Setup
####Unix
If all of your dependencies are in place, then to set up and run the Docker image in a container, run 

```
setup
````

You can istall the plugin on your Orion client at `https://nwam.github.io/umple.orion/umplePlugin.html`.

####Windows
If all of your dependencies are in place and your maven path is part of your PATH variable, while docker is running, run `setup.bat`. This will set up and run the Docker image in a container.

**Importatnt:** If you are encountering an error running the Docker image, replace the line-endings in the file `run` with Unix line-endings. For unknown reasons, the line-endings in this file get converted to Windows line endings, **even if you never opened the file**.

##Both 
See [managing docker](#4-managing-docker) to work with the docker image once it is setup and running. If you are encountering issues, you can follow the longer setup steps below.

## 0.3 Quick Cleanup (Unix)
Run `cleanup` to completely clean up your enviornment.`cleanup` will delete your docker image, so elements of the image, such as ubuntu, will have to reintall. `cleanup` is best used to test if the system builds from scratch. For a faster workflow, see [managing docker](#4-managing-docker). `cleanup` might output error messages. That is okay since errors only mean that some workspace elements are already clean. 

## 1. umple-orion-server Setup
### Installing Umple to your local Maven repository
Umple does not exist in Apache's central repositories, so we have to add it to our own local repository to use it in our server. To do so, run: 

```
mvn install:install-file \
-Dfile=/path/to/umple-1.25.0-963d2bd.jar \
-DgroupId=cruise.umple \
-DartifactId=umple \
-Dversion=1.25.0-963d2bd \
-Dpackaging=jar
```
Be sure to replace `/path/to/umple-1.25.0-963d2bd.jar` with the actual path to your umple jar. If there is a newer version of Umple, the version of `umple` in `pom.xml` and in this command may be able to be changed accordingly.

For more information on installing 3rd party jars to your local repository, see [Apache's documentation](https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html).

### Building umple-orion-server
The umple-orion-server only needs to be built. Docker will handle running the server. To build the server, `cd` to `umple-orion-server` and run `mvn package`. The server will be packaged to `target/umple-orion-server-jar-with-dependencies.jar`

**Windows developers**: run `C:\path\to\mvn.cmd clean package` to build the server. For more information about running Maven on Windows, check [Apache's documentation](https://maven.apache.org/guides/getting-started/windows-prerequisites.html). 

**Non-developers**: You can download a (possibly outdated) version of the server [here](https://drive.google.com/open?id=0ByO4l0WBF7WAblgwaEhibE1kZ3c) and place it in `umple.orion/umple-orion-server/target/umple-orion-server-jar-with-dependencies.jar` instead of building from source. 

## 2. Docker Setup

**Windows developers:** Windows seems to unexpectedly change the line-endings of some files. If you are having issues with running the Docker image, make sure all of your line-endings are **Unix** line-endings as the image is built on Ubuntu. This is especially important for the `run` file.

Umple.Orion leverages [Docker] containers to allow developers to quickly setup their local development environment and get the application up and running on their system, regardless of operating system. 

To install Docker, please visit the following links below to download the appropriate installer and installation instructions. Please read the installation instructions carefully, as certain systems may not meet the requirements for a native Docker installation, and must use [Docker Toolbox] instead. 

| Operating System | Download Link                                         |
|------------------|-------------------------------------------------------|
| Windows          | https://docker.github.io/engine/installation/windows/ |
| Mac OS           | https://docker.github.io/engine/installation/mac/     |
| Linux            | https://docs.docker.com/engine/installation/linux/    |

Once Docker/Docker Toolbox has been installed, be sure to run the following command in a terminal (Docker QuickStart Terminal for Windows) to ensure your Docker was installed successfully:

```
docker run hello-world
```

With a working Docker installation, clone the repository and run the following Docker command in a terminal to build the image for Eclipse Orion (this should take a few minutes depending on your connection speed):

```
docker build -t umple_orion .
```

This command will build an image from the Dockerfile and name it `umple_orion`. You can find this image by executing the command `docker images` in a Docker terminal, which lists all of the available Docker images on your system. Once the image is successfully built, you may run the Eclipse Orion server by executing the following Docker command in a terminal:

```
docker run --name my_orion -p 127.0.0.1:8080:8080 -p 127.0.0.1:4567:4567 -i -t umple_orion
```

This command will run the image you built in the previous step and create a container named `my_orion`. If everything is working correctly, you should see the Eclipse Orion server boot up in your terminal and your command prompt should look like the following:

```
osgi>
```

Visit `http://localhost:8080` in your web browser and you should be able to see the Eclipse Orion up and running on your system! Sometimes, upon first visiting `http://localhost:8080` you will encounter a HTTP server error 500. Refresh to fix this.

## 3. umple-orion-plugin Setup

### Installation
You need to install the Umple plugin to your Orion client to access the umple-orion server. In your Orion client, navigate to the plugins tab in settings. You can istall the plugin on your Orion client through `https://nwam.github.io/umple.orion/umplePlugin.html`. 

The commands contributed from the Umple plugin can be found under the `Tools` menu while editing a file with the extention `.ump`. One the files have been generated, to see the new files, you must refresh the page. For more information about installing Orion plugins, visit [Orion's documentation](https://wiki.eclipse.org/Orion/How_Tos/Installing_A_Plugin).

### Troubleshooting Connection Security Issues
Because of security measures (and lack of a CA signed certificate), when testing on localhost, the umple-orion-server gets connections blocked. To fix this, when the umple-orion-server is running, visit `https://localhost:4567/UmpleGenerate` and confirm security exceptions for this address.

Some have reported issues downloading the plugin. It is currently hosted on nwam's GitHub pages, which, for unknown reasons, gets blocked in some browsers (on Windows). If you encounter issues downloading the plugin, visit the [plugin page](ttps://nwam.github.io/umple.orion/umplePlugin.html) and add the security exception to your browser.

## 4. Managing Docker

To kill a running Docker container instance, execute the following command in a terminal:

```
docker kill <container-name>
```

In our example above, we would execute `docker kill my_orion` to kill our running Docker container instance. To check for any running Docker instances, execute `docker ps` in a Docker terminal to list all containers.

To restart a Docker container that was killed, simply execute the following command:

```
docker restart <container-name>
```

Continuing our example, we would execute `docker restart my_orion` to bring our Eclipse Orion Docker instance back up again. Please refer to the Docker Cheatsheet in the Resources section for more Docker commands.

Sometimes, when debugging, it is useful to explore the contents of the docker container. To do so execute

```
docker exec -i -t <container-name> bash
```

With our example, we would execute `docker exec -i -t my_orion bash` which would let us explore the contents of the container with bash.

For more information about managing docker, check out the [docker cheatsheet](https://github.com/wsargent/docker-cheat-sheet).


# Resources
- [Eclipse Orion Wiki](https://wiki.eclipse.org/Orion): Your main resrouce on all things Orion. A great place to start your research.
  - [Orion Developer Guide](https://wiki.eclipse.org/Orion/Documentation/Developer_Guide): The developer's best friend to learn about Orion and Orion plugins.
  - [Orion Forums](https://www.eclipse.org/forums/index.php/f/227/): Attempt to reach out for help on these forums or read pervious discussions. Thew board is pretty unresponsive so be patient or find information elsewhere.
  - Orion IRC Channel: `#eclipse-orion` on the Freenode network. Also very unresponsive. Be patient, and wait around for a response (don't sign out!). IRC tip for beginners: don't ask to ask, just ask your question and wait (for long).
- [Orion Hub](http://orionhub.org): A sample Orion that is set up for public use. 
- [Umple](http://umple.org): More information about Umple.
