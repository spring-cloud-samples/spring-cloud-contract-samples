#!/usr/bin/env bash

set -o errexit

./stop.sh

docker-compose up -d
echo "Waiting for 30 seconds for Broker to boot up"
sleep 30