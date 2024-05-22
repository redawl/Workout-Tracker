CREATE TABLE IF NOT EXISTS WORKOUT (
    workout_date date primary key,
    user_jwt varchar
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

CREATE TABLE IF NOT EXISTS EXERCISE_REFERENCE (
    name varchar(200) primary key,
    exercise_id BIGSERIAL UNIQUE,
    constraint fk_exercise_id foreign key (exercise_id) references EXERCISE (id)
);

CREATE FUNCTION UPDATE_EXERCISE_REFERENCE()
    RETURNS TRIGGER AS $$
    BEGIN
        INSERT INTO EXERCISE_REFERENCE (name, exercise_id)
        VALUES (NEW.name, NEW.id)
        ON CONFLICT(name) DO UPDATE SET exercise_id = NEW.id;
        RETURN NULL;
    END
    $$ LANGUAGE plpgsql
;

CREATE TRIGGER UPDATE_EXERCISE_REFERENCE_TRGR
    AFTER UPDATE OR INSERT ON EXERCISE
    FOR EACH ROW
    EXECUTE PROCEDURE UPDATE_EXERCISE_REFERENCE()
;