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
import React from 'react';
import { formatDate } from '../helpers/DateHelpers';
import {     
    Thead,
    Tr,
    Th,
    Table,
    Tbody,
    Td
} from '@patternfly/react-table'

import { ApiError, Exercise, ExerciseService, Workout, WorkoutService } from '../workout-api-client';
import { SearchAutocomplete } from '../components/SearchAutocomplete';

export const Workouts = () => {
    const [workoutDate, setWorkoutDate] = React.useState(formatDate(new Date()));
    const [exercises, setExercises] = React.useState([] as Array<Exercise>);
    const [saveSuccess, setSaveSuccess] = React.useState(false);
    const [saveFailureMessage, setSaveFailureMessage] = React.useState('');
    const [unsavedChanges, setUnsavedChanges] = React.useState(false);
    const [exerciseReferences, setExerciseReferences] = React.useState([] as Array<Exercise>);

    const workoutExists = React.useRef(false);

    React.useEffect(() => {
        ExerciseService.getExercises().then((references) => {
            setExerciseReferences([...references]);
        }).catch((apiError: ApiError) => {
            console.error(apiError);
        });
        WorkoutService.getWorkoutByDate({ date: workoutDate })
            .then((workout: Workout) => {
                    workoutExists.current = true;
                setExercises(workout.exercises as Array<Exercise>);
                setUnsavedChanges(false);
            })
            .catch((apiError: ApiError) => {
                if(apiError.status === 404){
                    workoutExists.current = false;
                    setExercises([]);
                } else {
                    console.error(apiError);
                }
            });
    }, [workoutDate]);

    const newExercise = () => {
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

    const removeExercise = (exerciseIndex: number) => {
        setExercises(exercises.filter((_: Exercise, index: number) => index !== exerciseIndex));
    }

    const updateName = (exerciseIndex: number, exerciseName: string) => {
        setExercises(exercises.map(
            (exercise: Exercise, index: number) => index === exerciseIndex ? { ...exercise, name: exerciseName } : exercise
        ));
        setUnsavedChanges(true);
    }

    const updateLbs = (exerciseIndex: number, lbs: number) => {
        setExercises(exercises.map(
            (exercise: Exercise, index: number) => index === exerciseIndex ? { ...exercise, lbs } : exercise
        ));
        setUnsavedChanges(true);
    } 

    const updateSets = (exerciseIndex: number, sets: number) => {
        setExercises(exercises.map(
            (exercise: Exercise, index: number) => index === exerciseIndex ? { ...exercise, sets } : exercise
        ));
        setUnsavedChanges(true);
    }
    
    const updateReps = (exerciseIndex: number, reps: number) => {
        setExercises(exercises.map((exercise: Exercise, index: number) => {
            if(index === exerciseIndex) {
                setUnsavedChanges(true);
                return { ...exercise, reps };
            }

            return exercise;
        }));
    } 

    const updateWorkout = () => {
        if(workoutExists.current){
            WorkoutService.updateWorkout({
                requestBody:{
                    date: workoutDate,
                    exercises
                }
            })
            .then(() => {
                setSaveSuccess(true);
                setUnsavedChanges(false);
            })
            .catch((error: ApiError) => {setSaveFailureMessage(
                error.statusText === undefined ? 
                error.toString() : error.statusText
            )});
        } else {
            WorkoutService.addWorkout({
                requestBody: {
                    date: workoutDate,
                    exercises
                }
            })
            .then(() => {
                setSaveSuccess(true);
                setUnsavedChanges(false);
                workoutExists.current = true;
            })
            .catch((error: ApiError) => {setSaveFailureMessage(
                error.statusText === undefined ? 
                error.toString() : error.statusText
            )});
        }
    }

    const updateExercise = (exercise: Exercise, index: number) => {
        setExercises(exercises.map((currExercise: Exercise, exerciseIndex: number) => {
            if(exerciseIndex === index){
                return {...exercise};
            }
            return currExercise;
        }));
    }

    return (
        <Panel>
            <PanelHeader>
                <Title headingLevel='h2'>{ WORKOUTS }</Title>
            </PanelHeader>
            <Divider />
            <PanelMain>
                <PanelMainBody>
                    <Form>
                        {
                            unsavedChanges && (
                                <FormGroup>
                                    <Alert title="There are unsaved changes. Click 'Save Workout' to save." variant='warning'/>
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
                                                        parentOnChange={(exerciseIndex: number, exerciseName: string) => updateName(exerciseIndex, exerciseName)}
                                                    />
                                                </Td>
                                                <Td>
                                                    <NumberInput 
                                                        value={exercise.lbs} 
                                                        onChange={(event: React.FormEvent<HTMLInputElement>) => updateLbs(index, +(event.target as HTMLInputElement).value)}
                                                        onMinus={() =>  updateLbs(index, exercise.lbs - WEIGHT_STEP >= 0 ? exercise.lbs - WEIGHT_STEP: 0)}
                                                        onPlus={() => updateLbs(index, exercise.lbs + WEIGHT_STEP)}
                                                    />
                                                </Td>
                                                <Td>
                                                    <NumberInput 
                                                        value={exercise.reps} 
                                                        onChange={(event: React.FormEvent<HTMLInputElement>) => updateReps(index, +(event.target as HTMLInputElement).value)}
                                                        onMinus={() => updateReps(index, exercise.reps - REP_STEP >= 0 ? exercise.reps - REP_STEP: 0)}
                                                        onPlus={() => updateReps(index, exercise.reps + REP_STEP)}
                                                    />
                                                </Td>
                                                <Td>
                                                    <NumberInput 
                                                        value={exercise.sets} 
                                                        onChange={(event: React.FormEvent<HTMLInputElement>) => updateSets(index, +(event.target as HTMLInputElement).value)}
                                                        onMinus={() => updateSets(index, exercise.sets - SET_STEP >= 0 ? exercise.sets - SET_STEP: 0)}
                                                        onPlus={() => updateSets(index, exercise.sets + SET_STEP)}
                                                    />
                                                </Td>
                                                <Td>
                                                    <Button 
                                                        variant={ButtonVariant.danger}
                                                        onClick={() => removeExercise(index)}
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
                                        <Button variant={ButtonVariant.primary} onClick={() => newExercise()}>
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
                                { saveFailureMessage }
                            </Alert>
                        }
                        <ActionGroup>
                            <Button 
                                variant={ButtonVariant.primary}
                                onClick={() => updateWorkout()}
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
