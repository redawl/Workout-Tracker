-- name: AddUser :exec
INSERT INTO WT_USER (id) VALUES ($1);

-- name: GetUser :one
SELECT * FROM WT_USER WHERE id = $1;


-- name: GetWorkouts :many
SELECT * FROM WORKOUT WHERE user_id = $1;

-- name: GetWorkoutByDate :one
SELECT * FROM WORKOUT WHERE user_id = $1 AND workout_date = $2;

-- name: AddWorkout :one
INSERT INTO WORKOUT (workout_date, user_id) VALUES ($1, $2) RETURNING *;

-- name: RemoveWorkoutByDate :exec
DELETE FROM WORKOUT WHERE user_id = $1 AND workout_date = $2;


-- name: GetExercises :many
SELECT name, lbs, reps, sets FROM EXERCISE 
JOIN WORKOUT ON exercise.workout_id = workout.id
WHERE exercise.user_id = $1 AND (name, workout_date) in (
    SELECT name, MAX(workout_date) from EXERCISE
    JOIN WORKOUT ON exercise.workout_id = workout.id
    GROUP BY name
);

-- name: GetExercisesForWorkout :many
SELECT * FROM EXERCISE WHERE user_id = $1 AND workout_id = $2;

-- name: AddExercise :one
INSERT INTO EXERCISE (workout_id, user_id, name, lbs, reps, sets) VALUES ($1, $2, $3, $4, $5, $6) RETURNING *;

-- name: RemoveExercise :exec 
DELETE FROM EXERCISE WHERE user_id = $1 AND id = $2;

-- name: UpdateExercise :one
UPDATE EXERCISE SET name = $1, lbs = $2, reps = $3, sets = $4 WHERE user_id = $5 AND id = $6 RETURNING *;

