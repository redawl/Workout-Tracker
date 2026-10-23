#!/bin/bash

set -e
echo "sourcing .profile"
. "/home/github/.profile" || echo "Failed to source .profile"

DIR=$(dirname "${0}")

# Run migrations
docker compose up -d postgres
./goose.sh up
docker compose down

docker compose up -d --build
