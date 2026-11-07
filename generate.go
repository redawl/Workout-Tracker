//go:generate oapi-codegen -config config/oapi-codegen.yaml config/schema.json
//go:generate sqlc -f sql/sqlc.yaml generate
package main
