#!/bin/bash

set -e

source common.sh || source scripts/common.sh

function build_maven() {
    echo -e "\n\nInstalling common\n\n"
    cd ${ROOT}/common
    ./mvnw clean install -U
    cd ${ROOT}

    echo -e "\n\nBuilding everything\n\n"
    ./mvnw clean install -Ptest -U
}

SUCCESS=false

for i in $( seq 1 "${RETRIES}" ); do
    echo "Attempt #$i/${RETRIES}..."
    if build_maven; then
    echo "Tests succeeded!"
        SUCCESS="true"
        break;
    else
        echo "Fail #$i/${RETRIES}... will try again"
    fi
done

if [ ${SUCCESS} == "false" ]; then
    echo "Failed to make the build work"
    exit 1
fi