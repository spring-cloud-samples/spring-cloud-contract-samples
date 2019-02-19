#!/usr/bin/env bash

set -o errexit
set -o errtrace
set -o pipefail

./src/main/bash/download_protoc.sh

echo "Generating proto files"
mkdir -p target/generated-sources/java
target/protoc/bin/protoc --java_out=target/generated-sources/java src/main/resources/proto/beer.proto
echo "Proto files generated!"