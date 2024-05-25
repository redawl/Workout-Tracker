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
spring_profiles_active=prod ./mvnw spring-boot:run