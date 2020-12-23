#!/usr/bin/env bash

# Generates the binary representation of request and response.
# Those are required to be passed to the request and response contracts.

set -o errexit
set -o errtrace
set -o pipefail

./src/main/bash/download_protoc.sh

echo "GENERATING GRPC COMPATIBLE MESSAGES"

function grpc_envelope() {
	local file="${1}"
	local fullPath="$( pwd )/${file}"
	groovy src/main/bash/grpcEnvelope.groovy "${fullPath}"
}

echo "Generating request"
echo age : 17 | target/protoc/bin/protoc --encode=beer.PersonToCheck src/main/proto/beer.proto > target/PersonToCheck_too_young.bin
grpc_envelope "target/PersonToCheck_too_young.bin"
echo "Generated grpc"

echo age : 23 | target/protoc/bin/protoc --encode=beer.PersonToCheck src/main/proto/beer.proto > target/PersonToCheck_old_enough.bin
grpc_envelope "target/PersonToCheck_old_enough.bin"
echo "Generated grpc"

echo "Generating response"

echo status : NOT_OK | target/protoc/bin/protoc --encode=beer.Response src/main/proto/beer.proto > target/Response_too_young.bin
grpc_envelope "target/Response_too_young.bin"
echo "Generated grpc"

echo status : OK | target/protoc/bin/protoc --encode=beer.Response src/main/proto/beer.proto > target/Response_old_enough.bin
grpc_envelope "target/Response_old_enough.bin"
echo "Generated grpc"

echo "All binary files generated!"

