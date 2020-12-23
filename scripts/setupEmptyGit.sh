#!/bin/bash -x

set -o errexit
set -o errtrace
set -o nounset
set -o pipefail

export ROOT="${ROOT:-`pwd`}"

function setup_git() {
    local git_name
    git_name="${1}"
    echo -e "\n\nCopying git repo to ${ROOT}/target/${git_name}\n\n"
    mkdir -p "${ROOT}/target/${git_name}"
    rm -rf "${ROOT}/target/${git_name}"
    cp -R "${ROOT}/${git_name}" "${ROOT}/target/"
    mv "${ROOT}/target/${git_name}/git" "${ROOT}/target/${git_name}/.git"
    ls -al "${ROOT}/target/${git_name}"
}

function prepare_git() {
	setup_git contract_empty_git
}

prepare_git