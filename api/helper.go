package api

import (
	"context"
	"errors"

	"burchbytes.com/workout_tracker/sql"
	"github.com/jackc/pgx/v5"
	"github.com/supertokens/supertokens-golang/recipe/session/sessmodels"
)

type SessionKey struct{}

// getSession retrieves the current SuperTokens session from ctx.
func getSession(ctx context.Context, queries *sql.Queries) (sessmodels.SessionContainer, error) {
	session := ctx.Value(SessionKey{})
	container := session.(sessmodels.SessionContainer)
	err := ensureUser(ctx, queries, container.GetUserID())
	if err != nil {
		return nil, err
	}
	return container, nil
}

func ensureUser(ctx context.Context, queries *sql.Queries, userID string) error {
	_, err := queries.GetUser(ctx, userID)
	if err != nil {
		if errors.Is(err, pgx.ErrNoRows) {
			return queries.AddUser(ctx, userID)
		}
		return err
	}

	return nil
}
