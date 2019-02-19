#!/usr/bin/env bash

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

./src/main/bash/download_protoc.sh

export LD_LIBRARY_PATH="${LD_LIBRARY_PATH}"
[[ -z "${LD_LIBRARY_PATH}" ]] && LD_LIBRARY_PATH="$( whereis libatomic.so.1 | awk '{ print $2 }' )"

echo "Generating proto files"
mkdir -p target/generated-sources/java
target/protoc/bin/protoc --java_out=target/generated-sources/java src/main/resources/proto/beer.proto
echo "Proto files generated!"