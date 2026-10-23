CREATE TABLE IF NOT EXISTS WT_USER (
    id varchar(36) primary key
);

CREATE TABLE IF NOT EXISTS WORKOUT (
    id BIGSERIAL primary key,
    workout_date date,
    user_id varchar(36),
    constraint fk_user_id foreign key (user_id) references WT_USER (id) ON DELETE CASCADE,
    UNIQUE (workout_date, user_id)
);

CREATE TABLE IF NOT EXISTS EXERCISE (
    id BIGSERIAL primary key,
    workout_id BIGSERIAL,
    user_id varchar(36),
    name varchar(200),
    lbs  int,
    reps int,
    sets int,
    constraint fk_workout_id foreign key (workout_id) references WORKOUT (id) ON DELETE CASCADE,
    constraint fk_user_id foreign key (user_id) references WT_USER (id)
);

CREATE DATABASE supertokens;

