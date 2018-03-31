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

function build() {
    local folder="${1}"
    echo -e "\n\nBuilding ${folder}\n\n"
    cd "${ROOT}/${folder}"
    ./gradlew clean build publishToMavenLocal
    cd "${ROOT}"
}

function build_gradle() {
    clean

    echo -e "\n\nCopying git repo to /target/contract_git\n\n"
    cd "${ROOT}/beer_contracts"
    ./mvnw clean install -U

    echo -e "\n\nCopying git repo to contract_git/target/git\n\n"
    mkdir -p "${ROOT}/target/contract_git"
    cp -r "${ROOT}/contract_git/" "${ROOT}/target/contract_git/"
    mv "${ROOT}/target/contract_git/git" "${ROOT}/target/contract_git/.git"

    build common
    build producer
    build producer_with_git
    build producer_yaml
    build producer_advanced
    build producer_with_stubs_per_consumer
    build producer_with_external_contracts
    build producer_with_restdocs
    build consumer
    build consumer_with_stubs_per_consumer
    build consumer_with_restdocs
    build consumer_with_discovery
}

build_gradle
