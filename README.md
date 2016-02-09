# Orion.Umple plugin

This is a plugin to extend the [Orion] text editor to support features allowing cloud-based development of [Umple] software.

## Current Features

- Context-free highlighting of Umple and extra code

## Work-in-Progress Features

- Context-sensitive highlight
- Generation of Umple code directly in the Orion editor
- Live generation of UML diagrams of Umple code
- Links to edit code in Orion (a la JSFiddle)

## Setup

To set up this plugin, you must first download and host `plugin.html` and `orion.umple.js` onto a web server that an instance of Orion can pull it from. In Orion itself (either local or [hosted][Orion]), go to {Settings > Plug-ins} and click the 'Install' button on the top right. Enter the URL for `plugin.html` (with `orion.umple.js` in the same directory) and it should install without any issues.

[Orion]: http://orionhub.org
[Umple]: http://umple.org
