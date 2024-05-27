CREATE TABLE IF NOT EXISTS WT_USER (
    id varchar(36) primary key
);

CREATE TABLE IF NOT EXISTS WORKOUT (
    id BIGSERIAL primary key,
    workout_date date,
    user_id varchar(36),
    constraint fk_user_id foreign key (user_id) references WT_USER (id),
    UNIQUE (workout_date, user_id)
);

CREATE TABLE IF NOT EXISTS EXERCISE_REFERENCE (
    id BIGSERIAL primary key,
    name varchar(200),
    user_id varchar(36),
    constraint fk_user_id foreign key (user_id) references WT_USER (id),
    UNIQUE(name, user_id)
);

CREATE TABLE IF NOT EXISTS EXERCISE (
    id BIGSERIAL primary key,
    workout_id BIGSERIAL,
    user_id varchar(36),
    exercise_reference_id BIGSERIAL,
    lbs  int,
    reps int,
    sets int,
    constraint fk_workout_id foreign key (workout_id) references WORKOUT (id),
    constraint fk_user_id foreign key (user_id) references WT_USER (id),
    constraint fk_exercise_id foreign key (exercise_reference_id) references EXERCISE_REFERENCE (id)
);

CREATE DATABASE supertokens;