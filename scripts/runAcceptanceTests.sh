#!/bin/bash

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

export ROOT=`pwd`

source "${ROOT}"/scripts/clean.sh

function startDockerCompose() {
    pushd "${ROOT}"/docker
    docker-compose pull
    docker-compose up -d
    popd
}

clean

startDockerCompose
. ${ROOT}/scripts/runMavenBuilds.sh
clearDocker

startDockerCompose
. ${ROOT}/scripts/runManual.sh
clearDocker

startDockerCompose
. ${ROOT}/scripts/runGradleBuilds.sh
clearDocker

cat <<'EOF'
 .----------------.  .----------------.  .----------------.  .----------------.
| .--------------. || .--------------. || .--------------. || .--------------. |
| |  ________    | || |     ____     | || |     ______   | || |    _______   | |
| | |_   ___ `.  | || |   .'    `.   | || |   .' ___  |  | || |   /  ___  |  | |
| |   | |   `. \ | || |  /  .--.  \  | || |  / .'   \_|  | || |  |  (__ \_|  | |
| |   | |    | | | || |  | |    | |  | || |  | |         | || |   '.___`-.   | |
| |  _| |___.' / | || |  \  `--'  /  | || |  \ `.___.'\  | || |  |`\____) |  | |
| | |________.'  | || |   `.____.'   | || |   `._____.'  | || |  |_______.'  | |
| |              | || |              | || |              | || |              | |
| '--------------' || '--------------' || '--------------' || '--------------' |
 '----------------'  '----------------'  '----------------'  '----------------'
EOF

echo "Generating docs"
cd ${ROOT} && ./gradlew generateDocumentation

echo "Copying test results"
cd ${ROOT} && mkdir test-results/junit -p
find . -type f -regex ".*/target/.*-reports/.*" -exec cp {} test-results/junit/ \;
find . -type f -regex ".*/build/test-results/.*" -exec cp {} test-results/junit/ \;

echo "Preparing for docs"
cd ${ROOT} && ./gradlew prepareForWorkshops

echo "Building the whole project again after preparing for docs"
export BUILD_COMMON=false
export SKIP_TESTS=true

clean

startDockerCompose
. ${ROOT}/scripts/runMavenBuilds.sh
clearDocker

startDockerCompose
. ${ROOT}/scripts/runGradleBuilds.sh
clearDocker
