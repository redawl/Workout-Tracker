//go:build !prod

package main

import (
	"context"
	"fmt"
	"net/http"
	"net/http/httputil"
	"net/url"
	"path/filepath"

	"burchbytes.com/workout_tracker/api"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/rs/cors"
	"github.com/supertokens/supertokens-golang/recipe/session/sessmodels"
	"github.com/testcontainers/testcontainers-go/modules/postgres"
)

func printEnv() {
	fmt.Println("Env: Development")
}

func initCORS(env *Env) *cors.Cors {
	return cors.AllowAll()
}

func initDB(ctx context.Context, env *Env) (*pgxpool.Pool, error) {
	pgContainer, err := postgres.Run(ctx,
		"postgres:16.3-alpine",
		postgres.WithInitScripts(filepath.Join("_postgres", "initdb", "schema.sql")),
		postgres.WithDatabase(env.PostgresDB),
		postgres.WithUsername(env.PostgresUser),
		postgres.WithPassword(env.PostgresPass),
		postgres.WithSQLDriver("pgx"),
		postgres.BasicWaitStrategies(),
	)
	if err != nil {
		return nil, err
	}

	connStr, err := pgContainer.ConnectionString(ctx, "sslmode=disable")
	if err != nil {
		return nil, err
	}

	return pgxpool.New(ctx, connStr)
}

var proxy *httputil.ReverseProxy

func initAuth(env *Env) (func(http.Handler) http.Handler, error) {
	devServer := "http://localhost:5173"

	url, err := url.Parse(devServer)
	if err != nil {
		return nil, err
	}

	proxy = httputil.NewSingleHostReverseProxy(url)

	return func(next http.Handler) http.Handler {
		return next
	}, nil
}

func createAuthMiddleware() api.StrictMiddlewareFunc {
	return func(next api.StrictHandlerFunc, operationID string) api.StrictHandlerFunc {
		return func(ctx context.Context, w http.ResponseWriter, r *http.Request, request any) (any, error) {
			switch operationID {
			// TODO: Add open routes here
			default:
				session := &sessmodels.TypeSessionContainer{
					GetUserID: func() string {
						return "00000000"
					},
				}

				sessionCtx := context.WithValue(ctx, api.SessionKey{}, session)

				return next(sessionCtx, w, r, request)
			}
		}
	}
}

func serveFrontend(response http.ResponseWriter, request *http.Request, path string) {
	proxy.ServeHTTP(response, request)
}
