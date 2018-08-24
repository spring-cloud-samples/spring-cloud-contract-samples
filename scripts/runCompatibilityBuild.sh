#!/bin/bash

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

ROOT=${ROOT:-`pwd`}
BUILD_COMMON="${BUILD_COMMON:-true}"
SKIP_TESTS="${SKIP_TESTS:-false}"
# TODO: Fetch these automatically
CURRENT_BOOT_VERSION="${CURRENT_BOOT_VERSION:-2.1.0.M2}"
CURRENT_CLOUD_VERSION="${CURRENT_CLOUD_VERSION:-Greenwich.BUILD-SNAPSHOT}"
CURRENT_CONTRACT_VERSION="${CURRENT_CONTRACT_VERSION:-2.1.0.BUILD-SNAPSHOT}"
PREVIOUS_BOOT_VERSION="${PREVIOUS_BOOT_VERSION:-2.0.3.RELEASE}"
PREVIOUS_CLOUD_VERSION="${PREVIOUS_CLOUD_VERSION:-Finchley.BUILD-SNAPSHOT}"
PREVIOUS_CONTRACT_VERSION="${PREVIOUS_CONTRACT_VERSION:-2.0.2.BUILD-SNAPSHOT}"

function clean() {
    rm -rf ~/.m2/repository/com/example/
    rm -rf "${ROOT}"/target/
    rm -rf ~/.gradle/caches/modules-2/files-2.1/com.example/
}

function build() {
    local folder="${1}"
    local bootVersion="${2}"
    local cloudVersion="${3}"
    local verifierVersion="${4}"
    echo -e "\n\nBuilding [${folder}] for boot [${bootVersion}] and cloud [${cloudVersion}] and verifier [${verifierVersion}]\n\n"
    cd "${ROOT}/${folder}"
    if [[ "${SKIP_TESTS}" == "true" ]]; then
        ./gradlew clean build publishToMavenLocal -x test -PBOM_VERSION="${cloudVersion}" -PbootVersion="${bootVersion}" -PverifierVersion="${verifierVersion}"
    else
        ./gradlew clean build publishToMavenLocal -PBOM_VERSION="${cloudVersion}" -PbootVersion="${bootVersion}" -PverifierVersion="${verifierVersion}"
    fi
    cd "${ROOT}"
}

function prepare_for_build() {
    clean

    cd "${ROOT}/beer_contracts"
    ./mvnw clean install -U

    echo -e "\n\nCopying git repo to contract_git/target/git\n\n"
    mkdir -p "${ROOT}/target/contract_git"
    cp -r "${ROOT}/contract_git" "${ROOT}/target/"
    mv "${ROOT}/target/contract_git/git" "${ROOT}/target/contract_git/.git"

    if [[ "${BUILD_COMMON}" == "true" ]]; then
        pushd "${ROOT}/common" && ./gradlew clean build publishToMavenLocal -x test && popd
    fi
}

function build_all_projects() {
    local producerBootVersion="${1}"
    local producerCloudVersion="${2}"
    local producerVerifierVersion="${3}"
    local consumerBootVersion="${4}"
    local consumerCloudVersion="${5}"
    local consumerVerifierVersion="${6}"
    build producer "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
    build producer_webflux "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
    build consumer_pact "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
    build producer_with_git "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
    build producer_yaml "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
    build producer_advanced "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
    build producer_pact "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
    build producer_with_stubs_per_consumer "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
    build producer_with_external_contracts "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
    build producer_with_restdocs "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
    build producer_with_dsl_restdocs "${producerBootVersion}" "${producerCloudVersion}" "${producerVerifierVersion}"
    build consumer "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
    build consumer_pact_stubrunner "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
    build consumer_with_stubs_per_consumer "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
    build consumer_with_restdocs "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
    build consumer_with_discovery "${consumerBootVersion}" "${consumerCloudVersion}" "${consumerVerifierVersion}"
}

function build_gradle() {
    prepare_for_build
    echo "Building current producer, old consumer"
    build_all_projects "${CURRENT_BOOT_VERSION}" "${CURRENT_CLOUD_VERSION}" "${CURRENT_CONTRACT_VERSION}" "${PREVIOUS_BOOT_VERSION}" "${PREVIOUS_CLOUD_VERSION}" "${PREVIOUS_CONTRACT_VERSION}"

    prepare_for_build
    echo "Building old producer, current consumer"
    build_all_projects "${PREVIOUS_BOOT_VERSION}" "${PREVIOUS_CLOUD_VERSION}" "${PREVIOUS_CONTRACT_VERSION}" "${CURRENT_BOOT_VERSION}" "${CURRENT_CLOUD_VERSION}" "${CURRENT_CONTRACT_VERSION}"
}

cat <<'EOF'
 _______  _______  _______  _______  _______ __________________ ______  _________ _       __________________
(  ____ \(  ___  )(       )(  ____ )(  ___  )\__   __/\__   __/(  ___ \ \__   __/( \      \__   __/\__   __/|\     /|
| (    \/| (   ) || () () || (    )|| (   ) |   ) (      ) (   | (   ) )   ) (   | (         ) (      ) (   ( \   / )
| |      | |   | || || || || (____)|| (___) |   | |      | |   | (__/ /    | |   | |         | |      | |    \ (_) /
| |      | |   | || |(_)| ||  _____)|  ___  |   | |      | |   |  __ (     | |   | |         | |      | |     \   /
| |      | |   | || |   | || (      | (   ) |   | |      | |   | (  \ \    | |   | |         | |      | |      ) (
| (____/\| (___) || )   ( || )      | )   ( |   | |   ___) (___| )___) )___) (___| (____/\___) (___   | |      | |
(_______/(_______)|/     \||/       |/     \|   )_(   \_______/|/ \___/ \_______/(_______/\_______/   )_(      \_/

EOF

build_gradle
