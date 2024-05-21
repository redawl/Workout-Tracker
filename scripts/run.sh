#!/bin/bash

DIR=$(dirname "${0}")

pushd "${DIR}/../frontend" || exit
npm run build
popd || exit
pushd "${DIR}/../" || exit
./mvnw spring-boot:run