package api

import (
	"context"
	"errors"
	"fmt"

	"burchbytes.com/workout_tracker/sql"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgtype"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/oapi-codegen/runtime/types"
)

var _ StrictServerInterface = (*WorkoutAPI)(nil)

type WorkoutAPI struct {
	DB      *pgxpool.Pool
	Queries *sql.Queries
}

// AddWorkout Add a new workout
// (POST /api/workout)
func (w *WorkoutAPI) AddWorkout(ctx context.Context, request AddWorkoutRequestObject) (AddWorkoutResponseObject, error) {
	tx, err := w.DB.Begin(ctx)
	if err != nil {
		return AddWorkout500Response{}, err
	}

	queries := w.Queries.WithTx(tx)

	workoutDate := request.Body.Date
	exercises := request.Body.Exercises

	session := getSession(ctx)
	userID := pgtype.Text{String: session.GetUserID(), Valid: true}

	err = ensureUser(ctx, queries, userID)
	if err != nil {
		return AddWorkout500Response{}, err
	}

	params := sql.AddWorkoutParams{
		WorkoutDate: pgtype.Date{Time: workoutDate.Time, Valid: true},
		UserID:      userID,
	}

	resultWorkout, err := queries.AddWorkout(ctx, params)
	if err != nil {
		// TODO: Error if workout exists
		return AddWorkout500Response{}, err
	}

	resultingExercises := make([]sql.Exercise, len(exercises))

	for i, exercise := range exercises {
		params := sql.AddExerciseParams{
			WorkoutID: pgtype.Int8{Int64: resultWorkout.ID, Valid: true},
			UserID:    userID,
			Name:      pgtype.Text{String: exercise.Name, Valid: true},
			Lbs:       pgtype.Int4{Int32: exercise.Lbs, Valid: true},
			Reps:      pgtype.Int4{Int32: exercise.Reps, Valid: true},
			Sets:      pgtype.Int4{Int32: exercise.Sets, Valid: true},
		}

		resultingExercise, err := queries.AddExercise(ctx, params)
		if err != nil {
			return AddWorkout500Response{}, err
		}
		resultingExercises[i] = resultingExercise
	}

	err = tx.Commit(ctx)
	if err != nil {
		return AddWorkout500Response{}, err
	}

	outExercises := make([]Exercise, len(exercises))

	for i, resultingExercise := range resultingExercises {
		outExercises[i] = Exercise{
			Lbs:  resultingExercise.Lbs.Int32,
			Name: resultingExercise.Name.String,
			Reps: resultingExercise.Reps.Int32,
			Sets: resultingExercise.Sets.Int32,
		}
	}

	return AddWorkout201JSONResponse{
		Date:      types.Date{Time: resultWorkout.WorkoutDate.Time},
		Exercises: outExercises,
	}, nil
}

// GetAllWorkouts Retrieve all workouts
// (GET /api/workout/all)
func (w *WorkoutAPI) GetAllWorkouts(ctx context.Context, request GetAllWorkoutsRequestObject) (GetAllWorkoutsResponseObject, error) {
	queries := w.Queries
	session := getSession(ctx)

	userID := pgtype.Text{String: session.GetUserID(), Valid: true}

	workouts, err := queries.GetWorkouts(ctx, userID)
	if err != nil {
		return GetAllWorkouts500Response{}, err
	}

	dtoWorkouts := make([]Workout, len(workouts))

	for i, workout := range workouts {
		exercises, err := queries.GetExercisesForWorkout(ctx, sql.GetExercisesForWorkoutParams{
			UserID: userID, WorkoutID: pgtype.Int8{Int64: workout.ID, Valid: true},
		})
		if err != nil {
			return GetAllWorkouts500Response{}, err
		}

		dtoExercises := make([]Exercise, len(exercises))
		for i, exercise := range exercises {
			dtoExercises[i] = Exercise{
				Lbs:  exercise.Lbs.Int32,
				Name: exercise.Name.String,
				Reps: exercise.Reps.Int32,
				Sets: exercise.Sets.Int32,
			}
		}

		dtoWorkouts[i] = Workout{
			Date:      types.Date{Time: workout.WorkoutDate.Time},
			Exercises: dtoExercises,
		}
	}

	return GetAllWorkouts200JSONResponse(dtoWorkouts), nil
}

// GetExercises Retrieve all existing exercises
// (GET /api/exercise/all)
func (w *WorkoutAPI) GetExercises(ctx context.Context, request GetExercisesRequestObject) (GetExercisesResponseObject, error) {
	queries := w.Queries

	session := getSession(ctx)

	userID := pgtype.Text{String: session.GetUserID(), Valid: true}

	exercises, err := queries.GetExercises(ctx, userID)
	if err != nil {
		if errors.Is(err, pgx.ErrNoRows) {
			return GetExercises200JSONResponse{}, nil
		}
		return nil, err
	}

	respExercises := make(GetExercises200JSONResponse, len(exercises))

	for i, exercise := range exercises {
		newExercise := Exercise{
			Name: exercise.Name.String,
			Lbs:  exercise.Lbs.Int32,
			Reps: exercise.Reps.Int32,
			Sets: exercise.Sets.Int32,
		}

		respExercises[i] = newExercise
	}

	return respExercises, nil
}

// GetWorkoutByDate Retrieve a workout by date
// (GET /api/workout)
func (w *WorkoutAPI) GetWorkoutByDate(ctx context.Context, request GetWorkoutByDateRequestObject) (GetWorkoutByDateResponseObject, error) {
	queries := w.Queries

	session := getSession(ctx)

	userID := pgtype.Text{String: session.GetUserID(), Valid: true}

	workout, err := queries.GetWorkoutByDate(ctx, sql.GetWorkoutByDateParams{
		UserID:      userID,
		WorkoutDate: pgtype.Date{Time: request.Params.Date.Time, Valid: true},
	})
	if err != nil {
		if errors.Is(err, pgx.ErrNoRows) {
			return GetWorkoutByDate404Response{}, nil
		}
		return nil, err
	}

	exercises, err := queries.GetExercisesForWorkout(ctx, sql.GetExercisesForWorkoutParams{
		UserID:    userID,
		WorkoutID: pgtype.Int8{Int64: workout.ID, Valid: true},
	})
	if err != nil {
		return nil, err
	}

	responseExercises := make([]Exercise, len(exercises))

	for i, exercise := range exercises {
		responseExercise := Exercise{
			Name: exercise.Name.String,
			Lbs:  exercise.Lbs.Int32,
			Reps: exercise.Reps.Int32,
			Sets: exercise.Sets.Int32,
		}

		responseExercises[i] = responseExercise
	}

	return GetWorkoutByDate200JSONResponse{
		Date:      types.Date{Time: workout.WorkoutDate.Time},
		Exercises: responseExercises,
	}, nil
}

// RemoveWorkoutByDate Remove a workout by date
// (DELETE /api/workout)
func (w *WorkoutAPI) RemoveWorkoutByDate(ctx context.Context, request RemoveWorkoutByDateRequestObject) (RemoveWorkoutByDateResponseObject, error) {
	tx, err := w.DB.Begin(ctx)
	if err != nil {
		return nil, err
	}

	queries := w.Queries.WithTx(tx)

	session := getSession(ctx)

	userID := pgtype.Text{String: session.GetUserID(), Valid: true}

	err = queries.RemoveWorkoutByDate(ctx, sql.RemoveWorkoutByDateParams{
		UserID:      userID,
		WorkoutDate: pgtype.Date{Time: request.Params.Date.Time, Valid: true},
	})
	if err != nil {
		return nil, err
	}

	err = tx.Commit(ctx)
	if err != nil {
		return nil, err
	}

	return RemoveWorkoutByDate204Response{}, nil
}

// UpdateWorkout Update a workout by date
// (PUT /api/workout)
func (w *WorkoutAPI) UpdateWorkout(ctx context.Context, request UpdateWorkoutRequestObject) (UpdateWorkoutResponseObject, error) {
	tx, err := w.DB.Begin(ctx)
	if err != nil {
		return nil, err
	}
	queries := w.Queries.WithTx(tx)

	session := getSession(ctx)

	userID := pgtype.Text{String: session.GetUserID(), Valid: true}

	workout, err := queries.GetWorkoutByDate(ctx, sql.GetWorkoutByDateParams{
		UserID:      userID,
		WorkoutDate: pgtype.Date{Time: request.Body.Date.Time, Valid: true},
	})
	if err != nil {
		if errors.Is(err, pgx.ErrNoRows) {
			return UpdateWorkout404Response{}, nil
		}

		return nil, fmt.Errorf("retrieving workout: %w", err)
	}

	exercises, err := queries.GetExercisesForWorkout(ctx, sql.GetExercisesForWorkoutParams{
		UserID:    userID,
		WorkoutID: pgtype.Int8{Int64: workout.ID, Valid: true},
	})
	if err != nil {
		return nil, fmt.Errorf("retrieving exercise: %w", err)
	}

	resultingExercises := make([]Exercise, len(request.Body.Exercises))
	for i, exercise := range request.Body.Exercises {
		var existingExercise *sql.Exercise

		for _, dbExercise := range exercises {
			if dbExercise.Name.String == exercise.Name {
				existingExercise = &dbExercise
				break
			}
		}

		if existingExercise == nil {
			savedExercise, err := queries.AddExercise(ctx, sql.AddExerciseParams{
				UserID:    userID,
				WorkoutID: pgtype.Int8{Int64: workout.ID, Valid: true},
				Name:      pgtype.Text{String: exercise.Name, Valid: true},
				Lbs:       pgtype.Int4{Int32: exercise.Lbs, Valid: true},
				Reps:      pgtype.Int4{Int32: exercise.Reps, Valid: true},
				Sets:      pgtype.Int4{Int32: exercise.Sets, Valid: true},
			})
			if err != nil {
				return nil, fmt.Errorf("adding exercise: %w", err)
			}

			resultingExercises[i] = Exercise{
				Lbs:  savedExercise.Lbs.Int32,
				Name: savedExercise.Name.String,
				Reps: savedExercise.Reps.Int32,
				Sets: savedExercise.Sets.Int32,
			}
		} else {
			savedExercise, err := queries.UpdateExercise(ctx, sql.UpdateExerciseParams{
				UserID: userID,
				ID:     workout.ID,
				Name:   pgtype.Text{String: exercise.Name, Valid: true},
				Lbs:    pgtype.Int4{Int32: exercise.Lbs, Valid: true},
				Reps:   pgtype.Int4{Int32: exercise.Reps, Valid: true},
				Sets:   pgtype.Int4{Int32: exercise.Sets, Valid: true},
			})
			if err != nil {
				return nil, fmt.Errorf("updating exercise: %w", err)
			}

			resultingExercises[i] = Exercise{
				Lbs:  savedExercise.Lbs.Int32,
				Name: savedExercise.Name.String,
				Reps: savedExercise.Reps.Int32,
				Sets: savedExercise.Sets.Int32,
			}
		}
	}

	for _, exercise := range exercises {
		found := false
		for _, requestExercise := range request.Body.Exercises {
			if requestExercise.Name == exercise.Name.String {
				found = true
				break
			}
		}

		if !found {
			err := queries.RemoveExercise(ctx, sql.RemoveExerciseParams{
				UserID: userID,
				ID:     exercise.ID,
			})
			if err != nil {
				return nil, fmt.Errorf("removing exercise: %w", err)
			}
		}
	}

	err = tx.Commit(ctx)
	if err != nil {
		return nil, fmt.Errorf("committing transactions: %w", err)
	}

	return UpdateWorkout200JSONResponse{
		Date:      types.Date{Time: workout.WorkoutDate.Time},
		Exercises: resultingExercises,
	}, nil
}
