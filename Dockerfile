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

# Expose port 8080
EXPOSE 8080

# Set the working directory
WORKDIR /opt/eclipse

# Add the Eclipse Orion configuration file
ADD orion.conf /opt/eclipse/orion.conf

# Run the server
CMD ["/opt/eclipse/orion"]