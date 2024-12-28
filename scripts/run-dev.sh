#!/bin/bash

set -e

DIR=$(dirname "${0}")

pushd "${DIR}/../frontend"
npm ci
npm run build
popd
pushd "${DIR}/../"
./mvnw clean package
docker compose up --build
