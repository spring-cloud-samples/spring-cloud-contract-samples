#!/bin/bash -x

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

export MAVEN_WRAPPER_VERSION="${MAVEN_WRAPPER_VERSION:-3.9.4}"

for d in */; do
    echo "$d" && cd "$d" && (mvn wrapper:wrapper -Dmaven="${MAVEN_WRAPPER_VERSION}" || echo "Failed") && cd ..
done
