#!/bin/bash -x

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

export ROOT=${ROOT:-`pwd`}
export BUILD_COMMON="${BUILD_COMMON:-true}"
export SKIP_TESTS="${SKIP_TESTS:-false}"
export CI="${CI:-false}"
export PREPARE_FOR_WORKSHOPS="${PREPARE_FOR_WORKSHOPS:-false}"

. ${ROOT}/scripts/common.sh

function clean() {
    rm -rf ~/.m2/repository/com/example/
    rm -rf "${ROOT}"/target/
    rm -rf ~/.gradle/caches/modules-2/files-2.1/com.example/
}

function build_maven() {
    clean
    
    prepare_git

    if [[ "${BUILD_COMMON}" == "true" ]]; then
        echo -e "\n\nInstalling common\n\n"
        cd ${ROOT}/common
        ./mvnw clean install -U -B
    fi
    cd ${ROOT}

    echo -e "\n\nBuilding everything skipping tests? [${SKIP_TESTS}] after prepare for workshops? [${PREPARE_FOR_WORKSHOPS}]\n\n"
    if [[ "${SKIP_TESTS}" == "true" ]]; then
        ./mvnw clean install -Ptest -U -B -DskipTests -DfailIfNoTests=false -Dspring.cloud.contract.verifier.skip=true -Dspring.cloud.contract.verifier.jar.skip=true
    else
        ./mvnw clean install -Ptest -U -B
    fi
    if [[ "${CI}" == "true" ]]; then
        echo "Skipping high mem projects for CI build"
        return 0
    fi
    echo -e "Running the high memory requirement projects"
    if [[ "${SKIP_TESTS}" == "true" ]]; then
        ./mvnw clean install -Phighmem -pl producer_java -U -B -DskipTests -DfailIfNoTests=false -Dspring.cloud.contract.verifier.skip=true -Dspring.cloud.contract.verifier.jar.skip=true
        ./mvnw clean install -Phighmem -pl consumer_java -U -B -DskipTests -DfailIfNoTests=false -Dspring.cloud.contract.verifier.skip=true -Dspring.cloud.contract.verifier.jar.skip=true
    else
        ./mvnw clean install -Phighmem -U -B -pl producer_java
        ./mvnw clean install -Phighmem -U -B -pl consumer_java
    fi
}

cat <<'EOF'
 .----------------.  .----------------.  .----------------.  .----------------.  .-----------------.
| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |
| | ____    ____ | || |      __      | || | ____   ____  | || |  _________   | || | ____  _____  | |
| ||_   \  /   _|| || |     /  \     | || ||_  _| |_  _| | || | |_   ___  |  | || ||_   \|_   _| | |
| |  |   \/   |  | || |    / /\ \    | || |  \ \   / /   | || |   | |_  \_|  | || |  |   \ | |   | |
| |  | |\  /| |  | || |   / ____ \   | || |   \ \ / /    | || |   |  _|  _   | || |  | |\ \| |   | |
| | _| |_\/_| |_ | || | _/ /    \ \_ | || |    \ ' /     | || |  _| |___/ |  | || | _| |_\   |_  | |
| ||_____||_____|| || ||____|  |____|| || |     \_/      | || | |_________|  | || ||_____|\____| | |
| |              | || |              | || |              | || |              | || |              | |
| '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |
 '----------------'  '----------------'  '----------------'  '----------------'  '----------------'
EOF

build_maven
echo -e "\n\nFinished Maven build!!!\n\n"