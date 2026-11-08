#!/bin/bash

set -e

DIR=$(dirname "${0}")

# Run migrations
docker compose -f compose-dev.yaml up -d --wait postgres

./goose.sh up

docker compose down

docker compose -f compose-dev.yaml up --build -d
