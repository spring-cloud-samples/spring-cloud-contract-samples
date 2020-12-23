#!/bin/bash -x

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

export ROOT="${ROOT:-`pwd`}"
export BUILD_COMMON="${BUILD_COMMON:-true}"
export SKIP_TESTS="${SKIP_TESTS:-false}"
export PREPARE_FOR_WORKSHOPS="${PREPARE_FOR_WORKSHOPS:-false}"
export PARALLEL="${PARALLEL:-false}"

. "${ROOT}/scripts/common.sh"

function clean() {
    rm -rf ~/.m2/repository/com/example/
    rm -rf "${ROOT}"/target/
    rm -rf ~/.gradle/caches/modules-2/files-2.1/com.example/
}

function setup_git() {
    local git_name
    git_name="${1}"
    echo -e "\n\nCopying git repo to /target/${git_name}\n\n"
    mkdir -p "${ROOT}/target/${git_name}"
    cp -R "${ROOT}/${git_name}" "${ROOT}/target/"
    mv "${ROOT}/target/${git_name}/git" "${ROOT}/target/${git_name}/.git"
}

function build() {
    local folder="${1}"
    echo -e "\n\nBuilding [${folder}] skipping tests? [${SKIP_TESTS}] after prepare for workshops? [${PREPARE_FOR_WORKSHOPS}]\n\n"
    cd "${ROOT}/${folder}"
    if [[ "${PARALLEL}" == "true" ]]; then
        if [[ "${SKIP_TESTS}" == "true" ]]; then
            ./gradlew clean build publishToMavenLocal -x test -PSKIP_TESTS=true -Dspring.cloud.contract.verifier.skip=true --stacktrace --refresh-dependencies --console=plain &
        else
            ./gradlew clean build publishToMavenLocal  --stacktrace --refresh-dependencies --console=plain &
        fi
        addPid "Building [${folder}]" $!
    else
        if [[ "${SKIP_TESTS}" == "true" ]]; then
            ./gradlew clean build publishToMavenLocal -x test -PSKIP_TESTS=true -Dspring.cloud.contract.verifier.skip=true --stacktrace --refresh-dependencies --console=plain
        else
            ./gradlew clean build publishToMavenLocal  --stacktrace --refresh-dependencies --console=plain
        fi
    fi
    cd "${ROOT}"
}

function build_gradle() {
    clean

    cd "${ROOT}/beer_contracts"
    ./mvnw clean install -U

    prepare_git

    if [[ "${BUILD_COMMON}" == "true" ]]; then
        build common
    fi
    build producer
    build producer_testng
    build producer_jaxrs
    build producer_jaxrs_spring
    build producer_webflux
    build producer_router_function
    build producer_webflux_webtestclient
#    FIXME
#    build consumer_pact
    waitPids
    kill_java

    build producer_webflux_security
    build producer_with_git
    build producer_with_empty_git
    build producer_yaml
    build producer_advanced
#    FIXME
#    build producer_pact
    waitPids
    kill_java

    build producer_proto
    build producer_kotlin
    build producer_with_stubs_per_consumer
    build producer_with_external_contracts
    build producer_with_restdocs
    waitPids
    kill_java

    build producer_with_webtestclient_restdocs
    build producer_with_dsl_restdocs
    build producer_with_spock
    build producer_with_junit4
    build producer_with_xml
    waitPids
    kill_java

    build producer_security
    build producer_with_latest_2_2_features
    build producer_with_latest_3_0_features_gradle
    build producer_java
    build producer_kotlin_ftw
    build producer_kafka
    build producer_kafka_middleware
    build producer_rabbit_middleware
    build producer_jms_middleware
    build producer_graphql
    build producer_grpc
    waitPids
    kill_java

    build consumer
    build consumer_kotlin
    build consumer_proto
#    FIXME
#    build consumer_pact_stubrunner
    build consumer_with_stubs_per_consumer
    build consumer_with_restdocs
    waitPids
    kill_java

    build consumer_with_discovery
    build consumer_with_junit4
    build consumer_security
    build consumer_with_latest_2_2_features
    build consumer_with_latest_3_0_features_gradle
    waitPids
    kill_java

    build consumer_java
    build consumer_kotlin_ftw
    build consumer_kafka
    build consumer_kafka_middleware
    build consumer_rabbit_middleware
    build consumer_jms_middleware
    build consumer_with_secured_webflux
    build consumer_grpc
    waitPids
    kill_java
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
echo -e "\n\nFinished Gradle build!!!\n\n"
