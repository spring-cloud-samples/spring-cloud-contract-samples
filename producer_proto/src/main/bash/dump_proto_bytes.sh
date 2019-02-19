#!/usr/bin/env bash

# Generates the binary representation of request and response.
# Those are required to be passed to the request and response contracts.

set -o errexit
set -o errtrace
set -o pipefail

./src/main/bash/download_protoc.sh

echo "Generating request"
echo age : 17 | target/protoc/bin/protoc --encode=beer.PersonToCheck src/main/resources/proto/beer.proto > target/PersonToCheck_too_young.bin
echo "Generated proto"
echo age : 23 | target/protoc/bin/protoc --encode=beer.PersonToCheck src/main/resources/proto/beer.proto > target/PersonToCheck_old_enough.bin
echo "Generated proto"

echo "Generating response"
echo status : NOT_OK | target/protoc/bin/protoc --encode=beer.Response src/main/resources/proto/beer.proto > target/Response_too_young.bin
echo "Generated proto"
echo status : OK | target/protoc/bin/protoc --encode=beer.Response src/main/resources/proto/beer.proto > target/Response_old_enough.bin
echo "Generated proto"

echo "All binary files generated!"

