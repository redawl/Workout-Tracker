#!/bin/bash

set -e
echo "sourcing .profile"
. "/home/github/.profile" || echo "Failed to source .profile"

DIR=$(dirname "${0}")

pushd "${DIR}/../frontend"
npm i
npm run "build:prod"
popd
pushd "${DIR}/../"
./mvnw clean package
docker compose up -d --build
