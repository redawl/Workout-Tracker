#!/bin/bash

DIR=$(dirname "${0}")

pushd "${DIR}/../frontend" || exit
npm run "build:prod"
popd || exit
pushd "${DIR}/../" || exit
spring_profiles_active=prod ./mvnw spring-boot:run