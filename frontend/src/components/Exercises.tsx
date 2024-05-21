import { 
    Panel, 
    PanelHeader, 
    Title,
    Divider,
    PanelMain,
    PanelMainBody
} from '@patternfly/react-core';

import {     
    Thead,
    Tr,
    Th,
    Table,
    Tbody,
    Td
} from '@patternfly/react-table';

import React, { useEffect } from 'react';

import { EXERCISES } from './constants';

import { ApiError, Exercise, ExerciseService } from '../workout-api-client';

export const Exercises = () => {
    const [ exercises, setExercises ] = React.useState([] as Array<Exercise>);

    useEffect(() => {
        ExerciseService.getExercises()
        .then((resp) => {
            setExercises(resp);
        })
        .catch((error: ApiError) => console.error(error));
    }, []);

    return (
        <Panel>
            <PanelHeader>
                <Title headingLevel='h2'>{ EXERCISES }</Title>
            </PanelHeader>
            <Divider />
            <PanelMain>
                <PanelMainBody>
                    <Table>
                        <Thead>
                            <Tr>
                                <Th>Name</Th>
                                <Th>Lbs</Th>
                                <Th>Reps</Th>
                                <Th>Sets</Th>
                            </Tr>
                        </Thead>
                        <Tbody>
                        {
                            exercises.map((exercise: Exercise, index: number) => (
                                <Tr key={index}>
                                    <Td>{exercise.name}</Td>
                                    <Td>{exercise.lbs}</Td>
                                    <Td>{exercise.reps}</Td>
                                    <Td>{exercise.sets}</Td>
                                </Tr>
                            ))
                        }
                        </Tbody>
                    </Table>
                </PanelMainBody>
            </PanelMain>
        </Panel>
    );
}
