import React, { useEffect } from "react";

import {
    Panel,
    PanelHeader,
    Title,
    Divider,
    PanelMain,
    PanelMainBody
} from '@patternfly/react-core';

import { REPORTS } from "./constants";
import { Workout, WorkoutService } from "../workout-api-client";
import { LineChart } from "./charts/LineChart";

export const Reports = () => {
    const [ workouts, setWorkouts ] = React.useState([] as Array<Workout>);

    useEffect(() => {
        WorkoutService.getAllWorkouts()
        .then((workouts: Array<Workout>) => {
            setWorkouts(workouts);
        })
    }, []);
    
    return (
        <Panel>
            <PanelHeader>
                <Title headingLevel='h2'>{ REPORTS }</Title>
            </PanelHeader>
            <Divider />
            <PanelMain>
                <PanelMainBody>
                    <LineChart workouts={ workouts } />
                </PanelMainBody>
            </PanelMain>
        </Panel>
    );
}