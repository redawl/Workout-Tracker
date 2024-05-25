#!/bin/bash

set -e
. "/home/github/.profile"

DIR=$(dirname "${0}")

pushd "${DIR}/../frontend"
npm run "build:prod"
popd
pushd "${DIR}/../"
spring_profiles_active=prod ./mvnw spring-boot:run