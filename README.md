# Umple.Orion

This is a plugin to extend the [Eclipse Orion] IDE to support features that facilitate cloud-based development of [Umple] software. The main feature gives Orion users the ability to generate code from Umple code.

# Development Setup

## 0.0 Windows Development
A developemnt enviornemnt for umple.orion is supported on both Windows and Unix. If available, we reccomend to use Linux to develop the umple.orion project because of the rigorus extra configuration required to set up and work with a Windows enviornment. Similarly, for anyone deploying this system, it highly recomended to deploy on Linux servers.  

## 0.1. Dependencies
 - [Docker](#2-docker-setup)
 - Maven
 - [Umple.jar is installed to your local Maven repository](#installing-umple-to-your-local-maven-repository)

## 0.2. Quick Setup (Linux)
If all of your dependencies are in place, then to set up and run the Docker image in a container, run `set_up_and_run_docker` from this directory (the directory containing the `Dockerfile`). You can istall the plugin on your Orion client at `https://nwam.github.io/umple.orion/umplePlugin.html`. If you are encountering issues, you can follow the longer setup steps below.

## 1. Umple-Orion Server Setup
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
The umple-orion-server only needs to be built. Docker will handle running the server. To build the server, `cd` to `umple-orion-server` and run `mvn package`. The server will be packaged at `target/umple-orion-server-*-jar-with-dependencies.jar`, where `*` is the version number. 

**Important:** If you are not using the Quick Setup, remove the version number from the file name, renaming the file to `umple-orion-server-jar-with-dependencies.jar`

**Windows developers**: run `C:\path\to\mvn.cmd clean package` to build the server. For more information about running Maven on Windows, check [Apache's documentation](https://maven.apache.org/guides/getting-started/windows-prerequisites.html). 

**Non-developers**: You can download a (possibly outdated) version of the server [here](https://drive.google.com/open?id=0ByO4l0WBF7WAblgwaEhibE1kZ3c) and place it in `umple.orion/umple-orion-server/target/umple-orion-server-jar-with-dependencies.jar` instead of building from source. 

## 2. Docker Setup

**Windows developers:** Windows seems to unexpectedly change the line-endings of some files. If you are having issues with running the Docker image, make sure all of your line-endings are **Unix** line-endings as the image is built on Ubuntu. This is especially important for the `run` file.

**Make sure you have built the Umple-Orion server to `umple-orion-server/target/umple-orion-server-jar-with-dependencies.jar` before building the docker image.**

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
docker run --name my_orion -p 127.0.0.1:8080:8080 -i -t umple_orion
```

This command will run the image you built in the previous step and create a container named `my_orion`. If everything is working correctly, you should see the Eclipse Orion server boot up in your terminal and your command prompt should look like the following:

```
osgi>
```

Visit `http://localhost:8080` in your web browser and you should be able to see the Eclipse Orion up and running on your system! 

## 3. Umple Plugin

### Installation
You need to install the Umple plugin to your Orion client to access the umple-orion server. In your Orion client, navigate to the plugins tab in settings. You can istall the plugin on your Orion client through `https://nwam.github.io/umple.orion/umplePlugin.html`. 

The commands contributed from the Umple plugin can be found under the Tools menu while editing a file with the extention `.ump`. For more information about installing Orion plugins, visit [Orion's documentation](https://wiki.eclipse.org/Orion/How_Tos/Installing_A_Plugin).

### Troubleshooting Connection Security Issues
Because of security measures (and lack of a CA signed certificate), when testing on localhost, the umple-orion-server gets connections blocked. To fix this, when the umple-orion-server is running, visit `https://localhost:4567/UmpleGenerate` and confirm security exceptions for this address.

Some have reported issues downloading the plugin. It is currently hosted on `nwam`'s GitHub pages, which, for unknown reasons, also get blocked from some browsers. To successfully download the plugin, visit the page and add the security exception to your browser.

**Windows developers:** some Windows builds add additional security over your browser's security (firewalls, etc.). Make sure to disable these features to be able to disable the security settings properly. 

# Managing Docker

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


# Resources
- [Eclipse Orion Wiki](https://wiki.eclipse.org/Orion)
- [Docker Cheatsheet](https://github.com/wsargent/docker-cheat-sheet)

[Eclipse Orion]: http://orionhub.org
[Umple]: http://umple.org
[Docker]: http://docker.com
[Docker Toolbox]: https://www.docker.com/products/docker-toolbox
