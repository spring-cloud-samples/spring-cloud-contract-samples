#!/usr/bin/env bash

VERSION="3.0.3"
URL="https://repo1.maven.org/maven2/org/springframework/cloud/spring-cloud-contract-stub-runner-boot/${VERSION}/spring-cloud-contract-stub-runner-boot-${VERSION}.jar"
JAR_NAME="stub-runner-boot-${VERSION}"
JAR_LOCATION="target/${JAR_NAME}.jar"

mkdir -p target
curl -L "${URL}" -o "${JAR_LOCATION}"
echo "Running stub runner"
nohup java -jar "${JAR_LOCATION}" --stubrunner.stubs-mode="local" --stubrunner.ids="com.example:beer-api-producer-advanced" 2>&1 &
# poor man's version to wait for the app to start
sleep 10
echo "Show all running stubs"
curl -s localhost:8083/stubs
echo "Get the port of stub"
STUB_PORT=`curl -s localhost:8083/stubs/beer-api-producer-advanced`
curl "localhost:${STUB_PORT}/stout"
pkill -f ${JAR_NAME}
