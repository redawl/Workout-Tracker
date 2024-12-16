#!/bin/bash

set -e

DIR=$(dirname "${0}")

pushd "${DIR}/../frontend"
npm i
npm run build
popd
pushd "${DIR}/../"
spring_profiles_active=local ./mvnw spring-boot:run

