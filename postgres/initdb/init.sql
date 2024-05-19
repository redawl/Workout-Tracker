CREATE TABLE IF NOT EXISTS WORKOUT (
    workout_date date primary key
);

CREATE TABLE IF NOT EXISTS EXERCISE (
    id BIGSERIAL primary key,
    name varchar(200),
    lbs  int,
    reps int,
    sets int,
    workout_id date,
    constraint fk_workout_id foreign key (workout_id) references WORKOUT (workout_date)
);