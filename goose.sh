#/bin/sh
source .env

export GOOSE_DRIVER="postgres"
export GOOSE_DBSTRING="postgres://${POSTGRES_USER}:${POSTGRES_PASS}@127.0.0.1:5432/${POSTGRES_DB}"
export GOOSE_MIGRATION_DIR="./_migrations"
export GOOSE_TABLE="goose_migrations"
# echo $@
goose $@
