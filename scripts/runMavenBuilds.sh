#!/bin/bash

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

export ROOT=${ROOT:-`pwd`}

function clean() {
    rm -rf ~/.m2/repository/com/example/
    rm -rf "${ROOT}"/target/
    rm -rf ~/.gradle/caches/modules-2/files-2.1/com.example/
}

function build_maven() {
    clean
    echo -e "\n\nCopying git repo to /target/contract_git\n\n"
    mkdir -p "${ROOT}/target/contract_git"
    cp -r "${ROOT}/contract_git/" "${ROOT}/target/contract_git/"
    mv "${ROOT}/target/contract_git/git" "${ROOT}/target/contract_git/.git"

    echo -e "\n\nInstalling common\n\n"
    cd ${ROOT}/common
    ./mvnw clean install -U
    cd ${ROOT}

    echo -e "\n\nBuilding everything\n\n"
    ./mvnw clean install -Ptest -U
}

build_maven
