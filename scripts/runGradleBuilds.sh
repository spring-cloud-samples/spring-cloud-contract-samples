#!/bin/bash -x

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

export ROOT="${ROOT:-`pwd`}"
export BUILD_COMMON="${BUILD_COMMON:-true}"
export SKIP_TESTS="${SKIP_TESTS:-false}"
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
    echo -e "\n\nBuilding [${folder}] skipping tests? [${SKIP_TESTS}]\n\n"
    cd "${ROOT}/${folder}"
    if [[ "${PARALLEL}" == "true" ]]; then
        if [[ "${SKIP_TESTS}" == "true" ]]; then
            ./gradlew clean build publishToMavenLocal -x test -PSKIP_TESTS=true -Dspring.cloud.contract.verifier.skip=true --stacktrace --console=plain &
        else
            ./gradlew clean build publishToMavenLocal  --stacktrace --console=plain &
        fi
        addPid "Building [${folder}]" $!
    else
        if [[ "${SKIP_TESTS}" == "true" ]]; then
            ./gradlew clean build publishToMavenLocal -x test -PSKIP_TESTS=true -Dspring.cloud.contract.verifier.skip=true --stacktrace --console=plain
        else
            ./gradlew clean build publishToMavenLocal  --stacktrace --console=plain
        fi
    fi
    cd "${ROOT}"
}

function build_gradle() {
    clean

    cd "${ROOT}/beer_contracts"
    ./mvnw clean install

    prepare_git

    if [[ "${BUILD_COMMON}" == "true" ]]; then
        build common
    fi
    build standalone/dsl/http-server
    build standalone/dsl/http-client
    build standalone/restdocs/http-server
    build standalone/restdocs/http-client
    build standalone/webclient/http-server
    build standalone/webclient/http-client
    build producer
    build producer_testng
#    build producer_jaxrs # TODO: Fix me
#    build producer_jaxrs_spring # TODO: Fix me
    build producer_webflux
    build producer_router_function
    build producer_webflux_webtestclient

#    build producer_webflux_security # TODO: Kotlin broken
    build producer_with_git
    build producer_with_empty_git
    build producer_yaml
    build producer_advanced

#    build producer_proto # TODO: Fix me
#    build producer_kotlin  # TODO: Kotlin broken
    build producer_with_stubs_per_consumer
    build producer_with_external_contracts
#    build producer_with_restdocs # TODO: Fix me

    build producer_with_webtestclient_restdocs
    build producer_with_dsl_restdocs
   # build producer_with_spock # TODO: Fix me
    build producer_with_xml

#    build producer_security # TODO: Fix me
    build producer_with_latest_2_2_features
#    build producer_with_latest_3_0_features_gradle # TODO: Fix me
    build producer_java
#    build producer_kotlin_ftw  # TODO: Kotlin broken
    build producer_kafka_middleware
    build producer_rabbit_middleware
#    build producer_jms_middleware # TODO: Fix me
#    build producer_graphql # TODO: Migrate to Spring GraphQL
#    build producer_grpc # TODO:  Java 17??

    build consumer
#    build consumer_kotlin # TODO: Kotlin broken
#    build consumer_proto  # TODO: Fix me
    build consumer_with_stubs_per_consumer
#    build consumer_with_restdocs # TODO: Fix me

    build consumer_with_discovery
#    build consumer_security # TODO: Fix me
    build consumer_with_latest_2_2_features
    # build consumer_with_latest_3_0_features_gradle # TODO: Fix me

    build consumer_java
#    build consumer_kotlin_ftw # TODO: Kotlin broken
    build consumer_kafka_middleware
    build consumer_rabbit_middleware
#    build consumer_jms_middleware # TODO: Fix me
#    build consumer_with_secured_webflux # TODO: Fix me
#    build consumer_grpc # TODO: Fix me
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
