cd umple-orion-server
mvn.bin clean package
cd ..
docker build -t umple_orion .
docker run --name my_orion -p 127.0.0.1:8080:8080 -p 127.0.0.1:4567:4567 -i -t umple_orion