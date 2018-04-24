#!/bin/bash

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

ROOT=`pwd`

function clean() {
    rm -rf ~/.m2/repository/com/example/
    rm -rf ~/.gradle/caches/modules-2/files-2.1/com.example/
}

clean

. ${ROOT}/scripts/runMavenBuilds.sh

. ${ROOT}/scripts/runManual.sh

. ${ROOT}/scripts/runGradleBuilds.sh

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
cd ${ROOT} && mkdir test-results -p
find . -type f -regex ".*/target/.*-reports/.*" -exec cp {} test-results/junit/ \;
find . -type f -regex ".*/build/test-results/.*" -exec cp {} test-results/junit/ \;

echo "Preparing for docs"
cd ${ROOT} && ./gradlew prepareForWorkshops

echo "Compiling the whole project again after preparing for docs"
export BUILD_COMMON=false
export SKIP_TESTS=true

. ${ROOT}/scripts/runMavenBuilds.sh

. ${ROOT}/scripts/runGradleBuilds.sh
