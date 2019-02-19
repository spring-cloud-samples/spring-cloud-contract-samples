#!/usr/bin/env bash

set -o errexit
set -o errtrace
set -o pipefail

export PROTOC_TAG="v3.7.0rc2"
export PROTOC_VERSION="3.7.0-rc-2"

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
