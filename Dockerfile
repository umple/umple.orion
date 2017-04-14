# Dockerfile to build Eclipse Orion 12.0 container images
# Based on Ubuntu 16.04 with Java 8 installed

FROM ubuntu:16.04
MAINTAINER Edmund Luong <edmundvmluong@gmail.com>

# Install Java 8.
RUN apt-get update && \
    apt-get install -y unzip curl software-properties-common && \
    echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
    add-apt-repository -y ppa:webupd8team/java && \
    apt-get update && \
    apt-get install -y oracle-java8-installer && \
    rm -rf /var/lib/apt/lists/* && \
    rm -rf /var/cache/oracle-jdk8-installer

# Install Eclipse Orion
RUN curl -o /tmp/orion.zip http://mirror.csclub.uwaterloo.ca/eclipse/orion/drops/R-12.0-201606220105/eclipse-orion-12.0-linux.gtk.x86_64.zip && \
    cd /opt && \
    unzip /tmp/orion.zip && \
    rm -rf /tmp/orion.zip && \
    chmod +x /opt/eclipse/orion

# Add the Eclipse Orion configuration file
ADD orion.conf /opt/eclipse/orion.conf

# Copy over umple-orion-server
RUN mkdir /opt/umple-orion-server
ADD umple-orion-server/target/umple-orion-server-jar-with-dependencies.jar /opt/umple-orion-server/umple-orion-server.jar
ADD umple-orion-server/deploy/keystore.jks /opt/umple-orion-server/deploy/keystore.jks

# Expose ports
EXPOSE 8080 4567

# Set the working directory
WORKDIR /opt/eclipse

# Run the servers
ADD run /opt/run
RUN chmod +x /opt/run
CMD /opt/run
