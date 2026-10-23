-- +goose Up
SET LOCAL lock_timeout = '2s';
ALTER TABLE EXERCISE ADD COLUMN name varchar(200);
UPDATE EXERCISE SET name = e.name
FROM EXERCISE_REFERENCE e WHERE e.id = exercise_reference_id;
ALTER TABLE EXERCISE DROP COLUMN exercise_reference_id;
DROP TABLE EXERCISE_REFERENCE;
ALTER TABLE EXERCISE DROP CONSTRAINT fk_workout_id;
ALTER TABLE EXERCISE ADD CONSTRAINT fk_workout_id foreign key (workout_id) references WORKOUT (id) ON DELETE CASCADE;

ALTER TABLE WORKOUT DROP CONSTRAINT fk_user_id;
ALTER TABLE WORKOUT ADD CONSTRAINT fk_user_id foreign key (user_id) references WT_USER (id) ON DELETE CASCADE;


-- +goose Down
ALTER TABLE WORKOUT DROP CONSTRAINT fk_user_id;
ALTER TABLE WORKOUT ADD CONSTRAINT fk_user_id foreign key (user_id) references WT_USER (id);

ALTER TABLE EXERCISE DROP CONSTRAINT fk_workout_id;
ALTER TABLE EXERCISE ADD CONSTRAINT fk_workout_id foreign key (workout_id) references WORKOUT (id) ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS EXERCISE_REFERENCE (
    id BIGSERIAL primary key,
    name varchar(200),
    user_id varchar(36),
    constraint fk_user_id foreign key (user_id) references WT_USER (id),
    UNIQUE(name, user_id)
);

ALTER TABLE EXERCISE ADD COLUMN exercise_reference_id BIGSERIAL;
ALTER TABLE EXERCISE ADD CONSTRAINT fk_exercise_id foreign key (exercise_reference_id) references EXERCISE_REFERENCE (id);
INSERT INTO EXERCISE_REFERENCE (name, user_id) SELECT name, user_id from EXERCISE;
ALTER TABLE EXERCISE DROP COLUMN name;
