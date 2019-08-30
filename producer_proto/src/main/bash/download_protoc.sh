#!/usr/bin/env bash

set -o errexit
set -o errtrace
set -o pipefail

export PROTOC_VERSION="3.9.1"
export PROTOC_TAG="v${PROTOC_VERSION}"

if [ ! -f target/protoc/bin/protoc ]; then
	rm -rf target/protoc
	mkdir -p target/protoc
	echo "Fetching protoc"
	wget https://github.com/protocolbuffers/protobuf/releases/download/"${PROTOC_TAG}"/protoc-"${PROTOC_VERSION}"-linux-x86_64.zip -O target/protoc.zip
	unzip target/protoc.zip -d target/protoc
	echo "Protoc fetched!"
else
	echo "Protoc already downloaded"
fi
