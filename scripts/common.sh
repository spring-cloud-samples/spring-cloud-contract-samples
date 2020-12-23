#!/bin/bash -x

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

export ROOT="${ROOT:-`pwd`}"
export PARALLEL="${PARALLEL:-false}"
export KILL_JAVA="${KILL_JAVA:-false}"

function setup_git() {
    local git_name
    git_name="${1}"
    echo -e "\n\nCopying git repo to ${ROOT}/target/${git_name}\n\n"
    mkdir -p "${ROOT}/target/${git_name}"
    cp -R "${ROOT}/${git_name}" "${ROOT}/target/"
    mv "${ROOT}/target/${git_name}/git" "${ROOT}/target/${git_name}/.git"
    ls -al "${ROOT}/target/${git_name}"
}

function prepare_git() {
	setup_git contract_git
	setup_git contract_empty_git
}

function kill_java() {
    if [[ "${KILL_JAVA}" == "true" ]]; then
        pkill java -9 || echo "Failed to kill java processes"
        pkill gradle -9 || echo "Failed to kill gradle processes"
    fi
}

declare -a pids

function waitPids() {
    if [[ "${PARALLEL}" != "true" ]]; then
        echo "Not running a parallel build"
        return 0
    fi
    while [ ${#pids[@]} -ne 0 ]; do
        echo "Waiting for pids: ${pids[@]}"
        local range=$(eval echo {0..$((${#pids[@]}-1))})
        local i
        for i in $range; do
            if ! kill -0 ${pids[$i]} 2> /dev/null; then
                echo "Done -- ${pids[$i]}"
                unset pids[$i]
            fi
        done
        pids=("${pids[@]}") # Expunge nulls created by unset.
        sleep 1
    done
    echo "Done!"
}

function addPid() {
    desc=$1
    pid=$2
    echo "$desc -- $pid"
    pids=(${pids[@]} $pid)
}
