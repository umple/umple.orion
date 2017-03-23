To manually install umple maven dependency into local repository:
`mvn install:install-file -Dfile=umple-1.25.0-963d2bd.jar -DgroupId=cruise.umple -DartifactId=umple -Dversion=1.25.0-963d2bd -Dpackaging=jar`

Build with
`mvn package`

Run with
`java -jar target/umple*with-dependencies.jar`
