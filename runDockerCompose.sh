#!/bin/bash
./mvnw clean package -DskipTests
docker-compose up --build
#chmod +x runDockerCompose.sh
#The bash commmand above must be executed in the terminal before this file can be used