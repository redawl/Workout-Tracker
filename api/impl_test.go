package api

import (
	"context"
	"log"
	"path/filepath"
	"testing"
	"time"

	_ "embed"

	"burchbytes.com/workout_tracker/sql"
	"github.com/jackc/pgx/v5/pgxpool"
	_ "github.com/jackc/pgx/v5/stdlib"
	"github.com/oapi-codegen/runtime/types"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"github.com/supertokens/supertokens-golang/recipe/session/sessmodels"
	"github.com/testcontainers/testcontainers-go"
	"github.com/testcontainers/testcontainers-go/modules/postgres"
)

type PostgresContainer struct {
	*postgres.PostgresContainer
	ConnectionString string
}

var (
	DB            *PostgresContainer
	ParentContext *context.Context
)

func mustBindDate(t *testing.T, input string) types.Date {
	output := types.Date{}

	err := output.Bind(input)

	require.Nil(t, err)

	return output
}

func CreatePostgresContainer(ctx context.Context) (*PostgresContainer, error) {
	pgContainer, err := postgres.Run(ctx,
		"postgres:16.3-alpine",
		postgres.WithInitScripts(filepath.Join("..", "_postgres", "initdb", "schema.sql")),
		postgres.WithDatabase("wt"),
		postgres.WithUsername("username"),
		postgres.WithPassword("password"),
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

	return &PostgresContainer{
		PostgresContainer: pgContainer,
		ConnectionString:  connStr,
	}, nil
}

func CreateWorkoutAPI(t *testing.T, ctx context.Context) *WorkoutAPI {
	pool, err := pgxpool.New(ctx, DB.ConnectionString)
	require.Nil(t, err)

	queries := sql.New(pool)

	workoutAPI := &WorkoutAPI{
		DB:      pool,
		Queries: queries,
	}

	return workoutAPI
}

func TestMain(m *testing.M) {
	ctx := context.Background()
	session := &sessmodels.TypeSessionContainer{
		GetUserID: func() string {
			return "00000000"
		},
	}
	ctx = context.WithValue(ctx, SessionKey{}, session)
	ParentContext = &ctx
	container, err := CreatePostgresContainer(*ParentContext)
	if err != nil {
		panic(err)
	}

	DB = container

	err = DB.Snapshot(ctx)
	if err != nil {
		panic(err)
	}

	m.Run()

	err = testcontainers.TerminateContainer(DB)
	if err != nil {
		panic(err)
	}
}

func TestGetWorkout(t *testing.T) {
	date := mustBindDate(t, "2026-09-17")
	ctx := *ParentContext
	for _, tt := range []struct {
		Name              string
		ExpectedDate      types.Date
		ExpectedExercises []Exercise
	}{
		{
			Name:         "1 exercise",
			ExpectedDate: date,
			ExpectedExercises: []Exercise{
				{Name: "Bicep Curl", Lbs: 40, Reps: 8, Sets: 4},
			},
		},
		{
			Name:         "2 exercises",
			ExpectedDate: date,
			ExpectedExercises: []Exercise{
				{Name: "Bicep Curl", Lbs: 40, Reps: 8, Sets: 4},
				{Name: "Bent Over Row", Lbs: 45, Reps: 8, Sets: 4},
			},
		},
		{
			Name:              "No exercises",
			ExpectedDate:      date,
			ExpectedExercises: []Exercise{},
		},
	} {
		t.Run(tt.Name, func(t *testing.T) {
			t.Cleanup(func() {
				err := DB.Restore(ctx)
				assert.Nil(t, err)
			})

			workoutAPI := CreateWorkoutAPI(t, ctx)

			_, err := workoutAPI.AddWorkout(ctx, AddWorkoutRequestObject{
				Body: &AddWorkoutJSONRequestBody{
					Date:      tt.ExpectedDate,
					Exercises: tt.ExpectedExercises,
				},
			})
			require.Nil(t, err)

			workout, err := workoutAPI.GetWorkoutByDate(ctx, GetWorkoutByDateRequestObject{
				Params: GetWorkoutByDateParams{
					Date: tt.ExpectedDate,
				},
			})
			require.Nil(t, err)

			if workout200Response, ok := workout.(GetWorkoutByDate200JSONResponse); ok {
				assert.Equal(t, tt.ExpectedDate, workout200Response.Date)
				assert.Equal(t, tt.ExpectedExercises, workout200Response.Exercises)
			} else {
				log.Fatalf("Error: non 200 response: %T", workout)
			}
		})
	}
}

func TestRemoveWorkout(t *testing.T) {
	date := mustBindDate(t, "2026-09-17")
	ctx := *ParentContext
	for _, tt := range []struct {
		Name         string
		ExpectedDate types.Date
	}{
		{Name: "Test 1", ExpectedDate: date},
	} {
		t.Run(tt.Name, func(t *testing.T) {
			t.Cleanup(func() {
				err := DB.Restore(ctx)
				require.Nil(t, err)
			})
			ctx := *ParentContext
			workoutAPI := CreateWorkoutAPI(t, ctx)

			_, err := workoutAPI.AddWorkout(ctx, AddWorkoutRequestObject{
				Body: &AddWorkoutJSONRequestBody{
					Date: tt.ExpectedDate,
				},
			})
			require.Nil(t, err)

			_, err = workoutAPI.RemoveWorkoutByDate(ctx, RemoveWorkoutByDateRequestObject{
				Params: RemoveWorkoutByDateParams{
					Date: tt.ExpectedDate,
				},
			})

			require.Nil(t, err)

			workout, err := workoutAPI.GetWorkoutByDate(ctx, GetWorkoutByDateRequestObject{
				Params: GetWorkoutByDateParams{
					Date: tt.ExpectedDate,
				},
			})

			assert.Nil(t, err)

			assert.IsType(t, GetWorkoutByDate404Response{}, workout)
		})
	}
}

func TestUpdateWorkout(t *testing.T) {
	ctx := *ParentContext
	date := mustBindDate(t, "2026-09-17")

	for _, tt := range []struct {
		Name          string
		AddWorkouts   []Workout
		UpdateWorkout Workout
	}{
		{
			Name: "updating single exercise",
			AddWorkouts: []Workout{
				{
					Date: date,
					Exercises: []Exercise{
						{40, "Bicep Curl", 8, 4},
					},
				},
			},
			UpdateWorkout: Workout{
				Date: date,
				Exercises: []Exercise{
					{50, "Chest Press", 8, 4},
				},
			},
		},
		{
			Name: "add exercise to existing workout",
			AddWorkouts: []Workout{
				{
					Date: date,
					Exercises: []Exercise{
						{40, "Bicep Curl", 8, 4},
					},
				},
			},
			UpdateWorkout: Workout{
				Date: date,
				Exercises: []Exercise{
					{40, "Bicep Curl", 8, 4},
					{50, "Chest Press", 8, 4},
				},
			},
		},
		{
			Name: "remove exercise from existing workout",
			AddWorkouts: []Workout{
				{
					Date: date,
					Exercises: []Exercise{
						{40, "Bicep Curl", 8, 4},
						{50, "Chest Press", 8, 4},
					},
				},
			},
			UpdateWorkout: Workout{
				Date: date,
				Exercises: []Exercise{
					{50, "Chest Press", 8, 4},
				},
			},
		},
		{
			Name: "remove all exercises from existing workout",
			AddWorkouts: []Workout{
				{
					Date: date,
					Exercises: []Exercise{
						{40, "Bicep Curl", 8, 4},
						{50, "Chest Press", 8, 4},
					},
				},
			},
			UpdateWorkout: Workout{
				Date:      date,
				Exercises: []Exercise{},
			},
		},
		{
			Name: "add multiple exercises to existing workout",
			AddWorkouts: []Workout{
				{
					Date:      date,
					Exercises: []Exercise{},
				},
			},
			UpdateWorkout: Workout{
				Date: date,
				Exercises: []Exercise{
					{40, "Bicep Curl", 8, 4},
					{50, "Chest Press", 8, 4},
				},
			},
		},
		{
			Name: "no changes to existing workout",
			AddWorkouts: []Workout{
				{
					Date: date,
					Exercises: []Exercise{
						{40, "Bicep Curl", 8, 4},
						{50, "Chest Press", 8, 4},
					},
				},
			},
			UpdateWorkout: Workout{
				Date: date,
				Exercises: []Exercise{
					{40, "Bicep Curl", 8, 4},
					{50, "Chest Press", 8, 4},
				},
			},
		},
		{
			Name: "add two workouts, update 1 workout",
			AddWorkouts: []Workout{
				{
					Date: date,
					Exercises: []Exercise{
						{40, "Bicep Curl", 8, 4},
						{50, "Chest Press", 8, 4},
					},
				},
				{
					Date: types.Date{Time: date.Add(24 * time.Hour)},
					Exercises: []Exercise{
						{50, "Bicep Curl", 8, 4},
						{60, "Chest Press", 8, 4},
					},
				},
			},
			UpdateWorkout: Workout{
				Date: date,
				Exercises: []Exercise{
					{40, "Bicep Curl", 8, 4},
					{50, "Chest Press", 8, 4},
				},
			},
		},
	} {
		t.Run(tt.Name, func(t *testing.T) {
			t.Cleanup(func() {
				err := DB.Restore(ctx)
				require.Nil(t, err)
			})
			workoutAPI := CreateWorkoutAPI(t, ctx)
			for _, workout := range tt.AddWorkouts {
				addResponse, err := workoutAPI.AddWorkout(ctx, AddWorkoutRequestObject{
					Body: &AddWorkoutJSONRequestBody{
						workout.Date,
						workout.Exercises,
					},
				})

				require.Nil(t, err)

				require.IsType(t, AddWorkout201JSONResponse{}, addResponse)

				createdResponse := addResponse.(AddWorkout201JSONResponse)
				require.Equal(t, workout.Date, createdResponse.Date)
				require.ElementsMatch(t, workout.Exercises, createdResponse.Exercises)
			}

			updateResponse, err := workoutAPI.UpdateWorkout(ctx, UpdateWorkoutRequestObject{
				Body: &UpdateWorkoutJSONRequestBody{
					tt.UpdateWorkout.Date,
					tt.UpdateWorkout.Exercises,
				},
			})

			assert.Nil(t, err)
			assert.IsType(t, UpdateWorkout200JSONResponse{}, updateResponse)
			okResponse := updateResponse.(UpdateWorkout200JSONResponse)
			assert.Equal(t, tt.UpdateWorkout.Date, okResponse.Date)
			assert.ElementsMatch(t, tt.UpdateWorkout.Exercises, okResponse.Exercises)
		})
	}
}

func TestUpdateWorkout_NoWorkout(t *testing.T) {
	ctx := *ParentContext
	t.Cleanup(func() {
		err := DB.Restore(ctx)
		require.Nil(t, err)
	})
	workoutAPI := CreateWorkoutAPI(t, ctx)

	workout, err := workoutAPI.UpdateWorkout(ctx, UpdateWorkoutRequestObject{
		Body: &UpdateWorkoutJSONRequestBody{
			Date: mustBindDate(t, "2026-09-21"),
		},
	})
	assert.Nil(t, err)
	assert.IsType(t, UpdateWorkout404Response{}, workout)
}

func TestGetExercises(t *testing.T) {
	ctx := *ParentContext
	date := mustBindDate(t, "2026-09-17")
	for _, tt := range []struct {
		Name              string
		ExpectedWorkouts  []Workout
		ExpectedExercises []Exercise
	}{
		{
			Name: "1 Workout",
			ExpectedWorkouts: []Workout{
				{
					Date: date,
					Exercises: []Exercise{
						{40, "Bicep Curl", 8, 4},
						{50, "Chest Press", 8, 4},
					},
				},
			},
			ExpectedExercises: []Exercise{
				{40, "Bicep Curl", 8, 4},
				{50, "Chest Press", 8, 4},
			},
		},
		{
			Name: "2 Workouts",
			ExpectedWorkouts: []Workout{
				{
					Date: date,
					Exercises: []Exercise{
						{30, "Bicep Curl", 8, 4},
						{40, "Chest Press", 8, 4},
					},
				},
				{
					Date: types.Date{Time: date.Add(time.Hour * 24)},
					Exercises: []Exercise{
						{40, "Bicep Curl", 8, 4},
						{50, "Chest Press", 8, 4},
					},
				},
			},
			ExpectedExercises: []Exercise{
				{40, "Bicep Curl", 8, 4},
				{50, "Chest Press", 8, 4},
			},
		},
	} {
		t.Run(tt.Name, func(t *testing.T) {
			t.Cleanup(func() {
				err := DB.Restore(ctx)
				require.Nil(t, err)
			})
			workoutAPI := CreateWorkoutAPI(t, ctx)

			for _, workout := range tt.ExpectedWorkouts {
				workout, err := workoutAPI.AddWorkout(ctx, AddWorkoutRequestObject{
					Body: &AddWorkoutJSONRequestBody{
						Date:      workout.Date,
						Exercises: workout.Exercises,
					},
				})
				require.Nil(t, err)
				require.IsType(t, AddWorkout201JSONResponse{}, workout)
			}

			exercises, err := workoutAPI.GetExercises(ctx, GetExercisesRequestObject{})
			assert.Nil(t, err)
			assert.IsType(t, GetExercises200JSONResponse{}, exercises)
			assert.ElementsMatch(t, tt.ExpectedExercises, exercises)
		})
	}
}

func TestGetExercises_NoExercises(t *testing.T) {
	ctx := *ParentContext

	t.Cleanup(func() {
		err := DB.Restore(ctx)
		require.Nil(t, err)
	})
	workoutAPI := CreateWorkoutAPI(t, ctx)

	exercises, err := workoutAPI.GetExercises(ctx, GetExercisesRequestObject{})
	assert.Nil(t, err)
	assert.IsType(t, GetExercises200JSONResponse{}, exercises)

	assert.Len(t, exercises, 0)
}

func TestGetAllWorkouts(t *testing.T) {
	ctx := *ParentContext
	for _, tt := range []struct {
		Name             string
		ExpectedWorkouts []Workout
	}{
		{
			Name: "1 empty workout",
			ExpectedWorkouts: []Workout{
				{Date: mustBindDate(t, "2026-09-21"), Exercises: []Exercise{}},
			},
		},
		{
			Name: "1 workout with exercises",
			ExpectedWorkouts: []Workout{
				{
					Date: mustBindDate(t, "2026-09-21"),
					Exercises: []Exercise{
						{Name: "Bicep Curl", Lbs: 40, Reps: 8, Sets: 4},
						{Name: "Bent Over Row", Lbs: 45, Reps: 10, Sets: 4},
					},
				},
			},
		},
		{
			Name: "2 workouts with exercises",
			ExpectedWorkouts: []Workout{
				{
					Date: mustBindDate(t, "2026-09-21"),
					Exercises: []Exercise{
						{Name: "Bicep Curl", Lbs: 40, Reps: 8, Sets: 4},
						{Name: "Bent Over Row", Lbs: 45, Reps: 10, Sets: 4},
					},
				},
				{
					Date: mustBindDate(t, "2026-09-23"),
					Exercises: []Exercise{
						{Name: "Bicep Curl", Lbs: 45, Reps: 10, Sets: 4},
						{Name: "Bent Over Row", Lbs: 50, Reps: 10, Sets: 4},
					},
				},
			},
		},
	} {
		t.Run(tt.Name, func(t *testing.T) {
			t.Cleanup(func() {
				err := DB.Restore(ctx)
				require.Nil(t, err)
			})
			workoutAPI := CreateWorkoutAPI(t, ctx)

			for _, workout := range tt.ExpectedWorkouts {
				workout, err := workoutAPI.AddWorkout(ctx, AddWorkoutRequestObject{
					Body: &AddWorkoutJSONRequestBody{
						Date:      workout.Date,
						Exercises: workout.Exercises,
					},
				})
				require.Nil(t, err)
				require.IsType(t, AddWorkout201JSONResponse{}, workout)
			}

			workouts, err := workoutAPI.GetAllWorkouts(ctx, GetAllWorkoutsRequestObject{})
			assert.Nil(t, err)
			assert.IsType(t, GetAllWorkouts200JSONResponse{}, workouts)

			successResponse := workouts.(GetAllWorkouts200JSONResponse)

			assert.ElementsMatch(t, tt.ExpectedWorkouts, successResponse)
		})
	}
}
