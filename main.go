package main

import (
	"context"
	"log"
	"net/http"

	"burchbytes.com/workout_tracker/api"
	"burchbytes.com/workout_tracker/sql"
)

func main() {
	printEnv()

	ctx := context.Background()

	env := GetEnv()

	authHandler, err := initAuth(env)
	if err != nil {
		log.Fatal("Error initializing auth: %w", err)
	}

	c := initCORS(env)

	pool, err := initDB(ctx, env)
	if err != nil {
		log.Fatal(err)
	}
	defer pool.Close()

	queries := sql.New(pool)

	handler := api.Handler(api.NewStrictHandler(&api.WorkoutAPI{
		DB:      pool,
		Queries: queries,
	}, []api.StrictMiddlewareFunc{createAuthMiddleware()}))

	// TODO: Does this need to be configurable? It's internal only
	listenURI := "0.0.0.0:8080"
	log.Printf("Listening on %s", listenURI)
	err = http.ListenAndServe(listenURI, c.Handler(handleStatic(authHandler(handler))))
	if err != nil {
		log.Fatal(err)
	}
}
