#!/bin/bash -x

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

export ROOT=`pwd`

function clean() {
    rm -rf ~/.m2/repository/com/example/
    rm -rf "${ROOT}"/target/
    rm -rf ~/.gradle/caches/modules-2/files-2.1/com.example/
    clearDocker
}

function clearDocker() {
    pushd "${ROOT}"/docker
		./stop.sh
    popd
}

clean