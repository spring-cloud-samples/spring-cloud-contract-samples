#!/bin/bash

set -e

function clean() {
    rm -rf ~/.m2/repository/com/example/
    rm -rf ~/.gradle/caches/modules-2/files-2.1/com.example/
}

RETRIES=3