#!/bin/bash -x

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

export ROOT=$(pwd)
export CI="${CI:-false}"

source "${ROOT}"/scripts/clean.sh

trap 'clean' EXIT

clean

export SKIP_BUILD="${SKIP_BUILD:-false}"

if [[ "${SKIP_BUILD}" != "true" ]]; then
	. ${ROOT}/scripts/runMavenBuilds.sh

	. ${ROOT}/scripts/runManual.sh

	. ${ROOT}/scripts/runGradleBuilds.sh
fi

export SKIP_COMPATIBILITY="${SKIP_COMPATIBILITY:-false}"

if [[ "${SKIP_COMPATIBILITY}" != "true" ]]; then
	echo -e "\n\nWill run compatibility build\n\n"
	# TODO: Go back to snapshots one day
	export CURRENT_BOOT_VERSION="2.7.5"
	. ${ROOT}/scripts/runCompatibilityBuild.sh
fi

export SKIP_DOCS="${SKIP_DOCS:-false}"

if [[ "${CI}" == "true" ]]; then
	echo "Skipping docs for CI build. Can't tweak Gradle's memory."
	SKIP_DOCS="true"
fi

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
	export PREPARE_FOR_WORKSHOPS=true

	clean

	. ${ROOT}/scripts/runMavenBuilds.sh

	. ${ROOT}/scripts/runGradleBuilds.sh

else
	echo -e "\n\nSkipping docs generation\n\n"
fi
