#!/bin/bash

set -e

ROOT=${ROOT:-`pwd`}

function clean() {
    rm -rf ~/.m2/repository/com/example/
    rm -rf ~/.gradle/caches/modules-2/files-2.1/com.example/
}

RETRIES=3

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
    return 0
}

SUCCESS=false

for i in $( seq 1 "${RETRIES}" ); do
    echo "Attempt #$i/${RETRIES}..."
    if build_gradle; then
    echo "Tests succeeded!"
        SUCCESS=true
        break
    else
        echo "Fail #$i/${RETRIES}... will try again"
    fi
done

if [ ${SUCCESS} == "false" ]; then
    echo "Failed to make the build work"
    exit 1
fi