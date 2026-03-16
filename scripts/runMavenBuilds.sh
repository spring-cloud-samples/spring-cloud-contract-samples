#!/bin/bash -x

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

export ROOT=${ROOT:-`pwd`}
export BUILD_COMMON="${BUILD_COMMON:-true}"
export SKIP_TESTS="${SKIP_TESTS:-false}"
export CI="${CI:-false}"

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
        ./mvnw clean install -B
    fi
    cd ${ROOT}

    echo -e "\n\nBuilding everything skipping tests? [${SKIP_TESTS}]\n\n"
    # Build in two batches to avoid SCC plugin classrealm accumulation
    # causing BasicAuthCache ClassCastException after ~25 modules
    if [[ "${SKIP_TESTS}" == "true" ]]; then
        ./mvnw clean install -Ptest-batch1 -B -DskipTests -DfailIfNoTests=false -Dspring.cloud.contract.verifier.skip=true -Dspring.cloud.contract.verifier.jar.skip=true
        ./mvnw clean install -Ptest-batch2 -B -DskipTests -DfailIfNoTests=false -Dspring.cloud.contract.verifier.skip=true -Dspring.cloud.contract.verifier.jar.skip=true
    else
        ./mvnw clean install -Ptest-batch1 -B
        ./mvnw clean install -Ptest-batch2 -B
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
