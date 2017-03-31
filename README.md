# Umple.Orion

This is a plugin to extend the [Eclipse Orion] IDE to support features that facilitate cloud-based development of [Umple] software.

## Development Setup

### Quick Setup
If you have Docker, then to set up and run the Docker image in a container, run `set_up_and_run_docker` from this directory (the directory containing the `Dockerfile`). You can istall the plugin on your Orion client at `https://nwam.github.io/umple.orion/umplePlugin.html`. If you are encountering issues, you can follow the longer setup steps below.

### Umple-Orion Server Setup
The umple-orion-server only needs to be built. Docker will handle running the server. To build the server, `cd` to `umple-orion-server` and run `mvn package`. The server will be packaged at `target/umple-orion-server-*-jar-with-dependencies.jar`, where `*` is the version number.

### Docker Setup

**Make sure you have built the Umple-Orion server to `umple-orion-server/target/umple-orion-server-*-jar-with-dependencies.jar` before building the docker image.**

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

### Umple Plugin Installation
You need to install the Umple plugin to your Orion client to access the umple-orion server. In your Orion client, navigate to the plugins tab in settings. You can istall the plugin on your Orion client at `https://nwam.github.io/umple.orion/umplePlugin.html`. If you are encountering issues, you can follow the longer setup steps below. The commands contributed from the Umple plugin can be found under the Tools menu while editing a file with the extention `.ump`. For more information about installing Orion plugins, visit [this page](https://wiki.eclipse.org/Orion/How_Tos/Installing_A_Plugin).

## Managing Docker

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


## Resources
- [Eclipse Orion Server guide](http://wiki.eclipse.org/Orion/Server_admin_guide)
- [Docker Cheatsheet](https://github.com/wsargent/docker-cheat-sheet)

[Eclipse Orion]: http://orionhub.org
[Umple]: http://umple.org
[Docker]: http://docker.com
[Docker Toolbox]: https://www.docker.com/products/docker-toolbox
