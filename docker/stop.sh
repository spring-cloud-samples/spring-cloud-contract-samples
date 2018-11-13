#!/usr/bin/env bash

set -o errexit

docker-compose kill || echo "Failed to kill apps"
yes | docker-compose rm -v || echo "Failed to kill apps"
docker stop $(docker ps | grep pact-broker | awk -F' ' '{print $1}') || echo "Failed to stop pact-broker"
docker stop $(docker ps | grep postgres | awk -F' ' '{print $1}') || echo "Failed to stop postgres"