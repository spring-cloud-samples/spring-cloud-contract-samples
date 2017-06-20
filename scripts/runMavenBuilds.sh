#!/bin/bash

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

ROOT=${ROOT:-`pwd`}

function clean() {
    rm -rf ~/.m2/repository/com/example/
    rm -rf ~/.gradle/caches/modules-2/files-2.1/com.example/
}

function build_maven() {
    echo -e "\n\nInstalling common\n\n"
    cd ${ROOT}/common
    ./mvnw clean install -U
    cd ${ROOT}

    echo -e "\n\nBuilding everything\n\n"
    ./mvnw clean install -Ptest -U
}

build_maven
