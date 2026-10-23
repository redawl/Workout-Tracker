import {
    Title,
    Panel,
    PanelHeader,
    Divider,
    PanelMain,
    PanelMainBody,
    DatePicker,
    Form,
    FormGroup,
    ActionGroup,
    Button,
    ButtonVariant,
    NumberInput,
    Alert
} from '@patternfly/react-core';
import { REP_STEP, SET_STEP, WEIGHT_STEP, WORKOUTS } from '../components/constants';
import { useEffect, useRef, useState } from 'react';
import { formatDate } from '../helpers/DateHelpers';
import {
    Thead,
    Tr,
    Th,
    Table,
    Tbody,
    Td
} from '@patternfly/react-table'

import { addWorkout, Exercise, getExercises, getWorkoutByDate, updateWorkout } from '../workout-api-client';
import { SearchAutocomplete } from '../components/SearchAutocomplete';

export const Workouts = () => {
    const [workoutDate, setWorkoutDate] = useState(formatDate(new Date()));
    const [exercises, setExercises] = useState([] as Exercise[]);
    const [saveSuccess, setSaveSuccess] = useState(false);
    const [saveFailureMessage, setSaveFailureMessage] = useState('');
    const [unsavedChanges, setUnsavedChanges] = useState(false);
    const [exerciseReferences, setExerciseReferences] = useState([] as Array<Exercise>);

    const workoutExists = useRef(false);

    useEffect(() => {
        const fetchData = async () => {
            const { data: exercises, error: exerciseError } = await getExercises();

            if (exerciseError) {
                console.error(exerciseError);
                return;
            }
            setExerciseReferences([...exercises!]);

            const { data: workout, error: workoutError, response } = await getWorkoutByDate({ query: { date: workoutDate } });

            if (workoutError) {
                if (response?.status === 404) {
                    workoutExists.current = false;
                    setExercises([]);
                } else {
                    console.error(workoutError);
                }
                return;
            }
            workoutExists.current = true;
            setExercises(workout!.exercises);
            setUnsavedChanges(false);
        }

        fetchData();
    }, [workoutDate]);

    const handleAddExercise = () => {
        setExercises([...exercises,
        {
            name: "",
            lbs: 0,
            sets: 0,
            reps: 0
        }
        ])
        setUnsavedChanges(true);
    }

    const handleRemoveExercise = (exerciseIndex: number) => {
        setExercises(exercises.filter((_: Exercise, index: number) => index !== exerciseIndex));
    }

    const handleUpdateName = (exerciseIndex: number, exerciseName: string) => {
        setExercises(exercises.map(
            (exercise: Exercise, index: number) => index === exerciseIndex ? { ...exercise, name: exerciseName } : exercise
        ));
        setUnsavedChanges(true);
    }

    const handleUpdateLbs = (exerciseIndex: number, lbs: number) => {
        setExercises(exercises.map(
            (exercise: Exercise, index: number) => index === exerciseIndex ? { ...exercise, lbs } : exercise
        ));
        setUnsavedChanges(true);
    }

    const handleUpdateSets = (exerciseIndex: number, sets: number) => {
        setExercises(exercises.map(
            (exercise: Exercise, index: number) => index === exerciseIndex ? { ...exercise, sets } : exercise
        ));
        setUnsavedChanges(true);
    }

    const updateReps = (exerciseIndex: number, reps: number) => {
        setExercises(exercises.map((exercise: Exercise, index: number) => {
            if (index === exerciseIndex) {
                setUnsavedChanges(true);
                return { ...exercise, reps };
            }

            return exercise;
        }));
    }

    const handleUpdateWorkout = async () => {
        if (workoutExists.current) {
            const { error } = await updateWorkout({
                body: {
                    date: workoutDate,
                    exercises
                }
            })
            if (error) {
                setSaveFailureMessage(error.toString());
                return;
            }
            setSaveSuccess(true);
            setUnsavedChanges(false);
            workoutExists.current = true;
        } else {
            const { error } = await addWorkout({
                body: {
                    date: workoutDate,
                    exercises
                }
            })
            if (error) {
                setSaveFailureMessage(error.toString());
                return;
            }
            setSaveSuccess(true);
            setUnsavedChanges(false);
            workoutExists.current = true;
        }
    }

    const updateExercise = (exercise: Exercise, index: number) => {
        setExercises(exercises.map((currExercise: Exercise, exerciseIndex: number) => {
            if (exerciseIndex === index) {
                return { ...exercise };
            }
            return currExercise;
        }));
    }

    return (
        <Panel>
            <PanelHeader>
                <Title headingLevel='h2'>{WORKOUTS}</Title>
            </PanelHeader>
            <Divider />
            <PanelMain>
                <PanelMainBody>
                    <Form>
                        {
                            unsavedChanges && (
                                <FormGroup>
                                    <Alert title="There are unsaved changes. Click 'Save Workout' to save." variant='warning' />
                                </FormGroup>
                            )
                        }
                        {
                            saveSuccess &&
                            <FormGroup>
                                <Alert
                                    title="Workout Saved"
                                    variant="success"
                                    timeout={10000}
                                    onTimeout={() => setSaveSuccess(false)}
                                />
                            </FormGroup>
                        }
                        <FormGroup label="Workout Date">
                            <DatePicker
                                onChange={(_event, str) => setWorkoutDate(str)}
                                value={workoutDate}
                            />
                        </FormGroup>
                        <FormGroup label="Exercises">
                            <Table>
                                <Thead>
                                    <Tr>
                                        <Th>Name</Th>
                                        <Th>Lbs</Th>
                                        <Th>Reps</Th>
                                        <Th>Sets</Th>
                                        <Th>Action</Th>
                                    </Tr>
                                </Thead>
                                <Tbody>
                                    {
                                        exercises.map((exercise: Exercise, index: number) => (
                                            <Tr key={index}>
                                                <Td>
                                                    <SearchAutocomplete
                                                        exercises={exerciseReferences}
                                                        index={index}
                                                        startingValue={exercise.name}
                                                        parentOnSelect={(exercise: Exercise, index: number) => updateExercise(exercise, index)}
                                                        parentOnChange={(exerciseIndex: number, exerciseName: string) => handleUpdateName(exerciseIndex, exerciseName)}
                                                    />
                                                </Td>
                                                <Td>
                                                    <NumberInput
                                                        value={exercise.lbs}
                                                        onChange={(event: React.FormEvent<HTMLInputElement>) => handleUpdateLbs(index, +(event.target as HTMLInputElement).value)}
                                                        onMinus={() => handleUpdateLbs(index, Math.max(exercise.lbs - WEIGHT_STEP, 0))}
                                                        onPlus={() => handleUpdateLbs(index, exercise.lbs + WEIGHT_STEP)}
                                                    />
                                                </Td>
                                                <Td>
                                                    <NumberInput
                                                        value={exercise.reps}
                                                        onChange={(event: React.FormEvent<HTMLInputElement>) => updateReps(index, +(event.target as HTMLInputElement).value)}
                                                        onMinus={() => updateReps(index, Math.max(exercise.reps - REP_STEP, 0))}
                                                        onPlus={() => updateReps(index, exercise.reps + REP_STEP)}
                                                    />
                                                </Td>
                                                <Td>
                                                    <NumberInput
                                                        value={exercise.sets}
                                                        onChange={(event: React.FormEvent<HTMLInputElement>) => handleUpdateSets(index, +(event.target as HTMLInputElement).value)}
                                                        onMinus={() => handleUpdateSets(index, exercise.sets - SET_STEP >= 0 ? exercise.sets - SET_STEP : 0)}
                                                        onPlus={() => handleUpdateSets(index, exercise.sets + SET_STEP)}
                                                    />
                                                </Td>
                                                <Td>
                                                    <Button
                                                        variant={ButtonVariant.danger}
                                                        onClick={() => handleRemoveExercise(index)}
                                                    >
                                                        Delete
                                                    </Button>
                                                </Td>
                                            </Tr>
                                        ))
                                    }
                                    <Tr>
                                        <Td colSpan={4}></Td>
                                        <Td>
                                            <Button variant={ButtonVariant.primary} onClick={() => handleAddExercise()}>
                                                Add
                                            </Button>
                                        </Td>
                                    </Tr>
                                </Tbody>
                            </Table>
                        </FormGroup>
                        {
                            saveFailureMessage.length !== 0 &&
                            <Alert
                                isInline
                                isExpandable
                                title="Workout failed to save"
                                variant='danger'
                                timeout={10000}
                                onTimeout={() => setSaveFailureMessage('')}
                            >
                                {saveFailureMessage}
                            </Alert>
                        }
                        <ActionGroup>
                            <Button
                                variant={ButtonVariant.primary}
                                onClick={() => handleUpdateWorkout()}
                            >
                                Save Workout
                            </Button>
                        </ActionGroup>
                    </Form>
                </PanelMainBody>
            </PanelMain>
        </Panel>
    )
}
