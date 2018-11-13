#!/bin/bash

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

export ROOT=`pwd`

source "${ROOT}"/scripts/clean.sh

trap 'clean && clearDocker' EXIT

function startDockerCompose() {
    pushd "${ROOT}"/docker
    docker-compose pull
    docker-compose up -d
    popd
}

clean
clearDocker

. ${ROOT}/scripts/runMavenBuilds.sh

. ${ROOT}/scripts/runManual.sh

. ${ROOT}/scripts/runGradleBuilds.sh

export SKIP_COMPATIBILITY="${SKIP_COMPATIBILITY:-false}"

if [[ "${SKIP_COMPATIBILITY}" != "true" ]]; then
	startDockerCompose
  # TODO: Go back to snapshots one day
  export CURRENT_BOOT_VERSION="2.1.0.RELEASE"
	. ${ROOT}/scripts/runCompatibilityBuild.sh
fi

export SKIP_DOCS="${SKIP_DOCS:-false}"

if [[ "${SKIP_DOCS}" != "true" ]]; then
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

	echo "Preparing for docs"
	cd ${ROOT} && ./gradlew prepareForWorkshops

	echo "Building the whole project again after preparing for docs"
	export BUILD_COMMON=false
	export SKIP_TESTS=true

	clean
	clearDocker

	. ${ROOT}/scripts/runMavenBuilds.sh

	. ${ROOT}/scripts/runGradleBuilds.sh
fi
