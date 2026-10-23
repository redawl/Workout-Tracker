#!/bin/bash

set -e

DIR=$(dirname "${0}")

# Run migrations
docker compose up -d postgres
./goose.sh up
docker compose down

docker compose up --build
