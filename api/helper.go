package api

import (
	"context"
	"errors"

	"burchbytes.com/workout_tracker/sql"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgtype"
	"github.com/supertokens/supertokens-golang/recipe/session/sessmodels"
)

type SessionKey struct{}

// getSession retrieves the current SuperTokens session from ctx.
func getSession(ctx context.Context) sessmodels.SessionContainer {
	session := ctx.Value(SessionKey{})
	container := session.(sessmodels.SessionContainer)
	return container
}

func ensureUser(ctx context.Context, queries *sql.Queries, userID pgtype.Text) error {
	_, err := queries.GetUser(ctx, userID.String)
	if err != nil {
		if errors.Is(err, pgx.ErrNoRows) {
			return queries.AddUser(ctx, userID.String)
		}
		return err
	}

	return nil
}
