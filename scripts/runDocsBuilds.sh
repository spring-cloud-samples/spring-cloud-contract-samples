#!/bin/bash -x

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

export ROOT=${ROOT:-`pwd`}
export SKIP_COMPATIBILITY="true"
export SKIP_BUILD="true"
export SKIP_DOCS="false"
export CI="false"

${ROOT}/scripts/runAcceptanceTests.sh