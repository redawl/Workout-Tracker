#!/bin/bash

set -e
. "/home/github/.profile"

DIR=$(dirname "${0}")

pushd "${DIR}/../frontend"
npm run build
popd
pushd "${DIR}/../"
spring_profiles_active=local ./mvnw spring-boot:run
