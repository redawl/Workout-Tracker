CREATE TABLE IF NOT EXISTS EXERCISE (
    name varchar(200) primary key,
    lbs  int,
    reps int,
    sets int
);

CREATE TABLE IF NOT EXISTS WORKOUT (
    id BIGSERIAL primary key,
    workout_date date,
)

CREATE TABLE IF NOT EXISTS WORKOUT_EXERCISE (
    id BIGSERIAL primary key,
    constraint fk_exercise_id  foreign key (exercise_name) references EXERCISE (name),
    constraint fk_workout_id foreign key (workout_id) references WORKOUT (id)
)