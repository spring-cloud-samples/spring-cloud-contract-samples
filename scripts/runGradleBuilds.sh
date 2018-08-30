#!/bin/bash

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

ROOT=${ROOT:-`pwd`}
BUILD_COMMON="${BUILD_COMMON:-true}"
SKIP_TESTS="${SKIP_TESTS:-false}"

function clean() {
    rm -rf ~/.m2/repository/com/example/
    rm -rf "${ROOT}"/target/
    rm -rf ~/.gradle/caches/modules-2/files-2.1/com.example/
}

function build() {
    local folder="${1}"
    echo -e "\n\nBuilding ${folder}\n\n"
    cd "${ROOT}/${folder}"
    if [[ "${SKIP_TESTS}" == "true" ]]; then
        ./gradlew clean build publishToMavenLocal -x test --stacktrace
    else
        ./gradlew clean build publishToMavenLocal  --stacktrace
    fi
    cd "${ROOT}"
}

function build_gradle() {
    clean

    cd "${ROOT}/beer_contracts"
    ./mvnw clean install -U

    echo -e "\n\nCopying git repo to contract_git/target/git\n\n"
    mkdir -p "${ROOT}/target/contract_git"
    cp -r "${ROOT}/contract_git" "${ROOT}/target/"
    mv "${ROOT}/target/contract_git/git" "${ROOT}/target/contract_git/.git"

    if [[ "${BUILD_COMMON}" == "true" ]]; then
        build common
    fi
    build producer
    build producer_webflux
    build consumer_pact
    build producer_with_git
    build producer_yaml
    build producer_advanced
    build producer_pact
    build producer_with_stubs_per_consumer
    build producer_with_external_contracts
    build producer_with_restdocs
    build producer_with_dsl_restdocs
    build consumer
    build consumer_pact_stubrunner
    build consumer_with_stubs_per_consumer
    build consumer_with_restdocs
    build consumer_with_discovery
}

cat <<'EOF'
 .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.
| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |
| |    ______    | || |  _______     | || |      __      | || |  ________    | || |   _____      | || |  _________   | |
| |  .' ___  |   | || | |_   __ \    | || |     /  \     | || | |_   ___ `.  | || |  |_   _|     | || | |_   ___  |  | |
| | / .'   \_|   | || |   | |__) |   | || |    / /\ \    | || |   | |   `. \ | || |    | |       | || |   | |_  \_|  | |
| | | |    ____  | || |   |  __ /    | || |   / ____ \   | || |   | |    | | | || |    | |   _   | || |   |  _|  _   | |
| | \ `.___]  _| | || |  _| |  \ \_  | || | _/ /    \ \_ | || |  _| |___.' / | || |   _| |__/ |  | || |  _| |___/ |  | |
| |  `._____.'   | || | |____| |___| | || ||____|  |____|| || | |________.'  | || |  |________|  | || | |_________|  | |
| |              | || |              | || |              | || |              | || |              | || |              | |
| '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |
 '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'
EOF

build_gradle
