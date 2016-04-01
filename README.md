# Orion.Umple plugin

This is a plugin to extend the [Orion] text editor to support features allowing cloud-based development of [Umple] software.

## Current Features

- Context-free highlighting of Umple and extra code
- Generation of Umple code directly in the Orion editor (Partial - see below)

## Work-in-Progress Features

- Context-sensitive highlight
- Live generation of UML diagrams of Umple code
- Links to edit code in Orion (a la JSFiddle)

## User Plugin Setup

To set up this plugin, you must first download and host `plugin.html` and `orion.umple.js` onto a web server that an instance of Orion can retrieve it from. In Orion itself (either local or [hosted][Orion]), go to {Settings > Plug-ins} and click the 'Install' button on the top right. Enter the URL for `plugin.html` (with `orion.umple.js` in the same directory) and it should install without any issues.

Do note that this plugin will not allow compilation of code using any arbitrary instance of Orion. In order to compile code in Orion, it must be prebuilt with Umple compiler support.

## Server Plugin Setup

For server admins who would like Umple compilation built into their Orion server instance, the following instructions will add that functionality.

### For those who want to add Umple to an unmodified server

Clone the orion client:

    git clone https://git.eclipse.org/r/orion/org.eclipse.orion.client.git

Copy the Umple.Orion files into the cloned repo

    cp -r umple.orion/org.eclipse.orion.client/* org.eclipse.orion.client/

Clone and build the server repo (note: the Orion Client and Server repositories must be in the same directory for Orion to build.)

    git clone https://git.eclipse.org/r/orion/org.eclipse.orion.server.git
    cd org.eclipse.orion.server
    mvn clean install -P platform-kepler,local-build -DskipTests

The resulting binaries will be in a zip folder in the server repository.

    cd org.eclipse.orion.server/releng/org.eclipse.orion.server.repository/target/products/

Once there, the binaries will be in zipped files. Unzip the file containing the binaries for your target platform, then

    cd eclipse
    ./orion

And Orion will run locally on port 8080. Additional configuration will be required with Apache and Nginx for things to work on a web server.

### For those who want to add Umple to an modified server

In your Orion client repository, add the line

    "plugins/orion.umple.html": true,

to `org.eclipse.orion.client/bundles/org.eclipse.orion.ui/web/defaults.pref` inside the `"/plugins":{ }` section. Additionally, add the line

    { name : "plugins/orion.umple" }

to `org.eclipse.orion.client/releng/org.eclipse.orion.releng/builder/scripts/orion.build.js` in the modules section (around line 120). Then, in the directory containing the client and Umple.Orion repositories:

    cp umple.orion/org.eclipse.orion.client/bundles/org.eclipse.orion.ui/web/plugins/* org.eclipse.orion.client/bundles/org.eclipse.orion.ui/web/plugins/

And then build the server as usual.

## License

See the Umple license for copying details.

[Orion]: http://orionhub.org
[Umple]: http://umple.org
