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

function build() {
    local folder="${1}"
    echo -e "\n\nBuilding ${folder}\n\n"
    cd "${ROOT}/${folder}"
    ./gradlew clean build publishToMavenLocal
    cd "${ROOT}"
}

function build_gradle() {
    clean

    echo -e "\n\nBuilding the external contracts jar\n\n"
    cd "${ROOT}/beer_contracts"
    ./mvnw clean install -U

    build common
    build producer
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
