//go:build prod

package main

import (
	"context"
	"embed"
	"errors"
	"fmt"
	"io"
	"io/fs"
	"log"
	"net/http"
	"time"

	"burchbytes.com/workout_tracker/api"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/rs/cors"
	"github.com/supertokens/supertokens-golang/recipe/dashboard"
	"github.com/supertokens/supertokens-golang/recipe/emailpassword"
	"github.com/supertokens/supertokens-golang/recipe/emailverification"
	"github.com/supertokens/supertokens-golang/recipe/emailverification/evmodels"
	"github.com/supertokens/supertokens-golang/recipe/session"
	"github.com/supertokens/supertokens-golang/supertokens"
)

func printEnv() {
	fmt.Println("Env: Production")
}

func initCORS(env *Env) *cors.Cors {
	return cors.New(cors.Options{
		AllowedOrigins: []string{env.APIDomain},
		AllowedMethods: []string{"*"},
	})
}

func initDB(ctx context.Context, env *Env) (*pgxpool.Pool, error) {
	return pgxpool.New(ctx, fmt.Sprintf(
		"postgresql://%s:%s@postgres:5432/%s",
		env.PostgresUser, env.PostgresPass, env.PostgresDB,
	))
}

func initAuth(env *Env) (func(http.Handler) http.Handler, error) {
	apiBasePath := "/api/auth"
	websiteBasePath := "/auth"
	err := supertokens.Init(supertokens.TypeInput{
		Supertokens: &supertokens.ConnectionInfo{
			ConnectionURI: "http://supertokens-core:3567",
		},
		AppInfo: supertokens.AppInfo{
			AppName:         "WorkoutTracker",
			APIDomain:       env.APIDomain,
			WebsiteDomain:   env.APIDomain,
			APIBasePath:     &apiBasePath,
			WebsiteBasePath: &websiteBasePath,
		},
		RecipeList: []supertokens.Recipe{
			emailverification.Init(evmodels.TypeInput{Mode: evmodels.ModeRequired}),
			emailpassword.Init(nil),
			session.Init(nil),
			dashboard.Init(nil),
		},
	})
	if err != nil {
		return nil, err
	}

	return supertokens.Middleware, nil
}

func createAuthMiddleware() api.StrictMiddlewareFunc {
	return func(next api.StrictHandlerFunc, operationID string) api.StrictHandlerFunc {
		return func(ctx context.Context, w http.ResponseWriter, r *http.Request, request any) (any, error) {
			switch operationID {
			// TODO: Add open routes here
			default:
				session, err := session.GetSession(r, w, nil)
				if err != nil {
					supertokens.ErrorHandler(err, r, w, nil)
					return request, err
				}

				sessionCtx := context.WithValue(ctx, api.SessionKey{}, session)

				return next(sessionCtx, w, r, request)
			}
		}
	}
}

//go:embed staticDir/**
var staticDir embed.FS

func serveFrontend(response http.ResponseWriter, request *http.Request, path string) {
	path = "staticDir" + path
	f, err := staticDir.Open(path)
	if err != nil {
		if errors.Is(err, fs.ErrNotExist) {
			log.Printf("Path: %s not found\n", path)
			http.Error(response, "Not Found", http.StatusNotFound)
			return
		}
		http.Error(response, err.Error(), http.StatusInternalServerError)
		return
	}
	defer f.Close() //nolint:errcheck

	http.ServeContent(response, request, path, time.Now(), f.(io.ReadSeeker))
}
