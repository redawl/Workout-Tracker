import React, { useEffect } from "react";

import {
    Panel,
    PanelHeader,
    Title,
    Divider,
    PanelMain,
    PanelMainBody
} from '@patternfly/react-core';

import { REPORTS } from "../components/constants";
import { Workout, getAllWorkouts } from "../workout-api-client";
import { LineChart } from "../components/charts/LineChart";

export const Reports = () => {
    const [workouts, setWorkouts] = React.useState([] as Array<Workout>);

    useEffect(() => {
        getAllWorkouts()
            .then(({ data: workouts, error }) => {
                if (error || !workouts) {
                    console.error(error, workouts)
                    return
                }
                setWorkouts(workouts);
            })
    }, []);

    return (
        <Panel>
            <PanelHeader>
                <Title headingLevel='h2'>{REPORTS}</Title>
            </PanelHeader>
            <Divider />
            <PanelMain>
                <PanelMainBody>
                    <LineChart workouts={workouts} />
                </PanelMainBody>
            </PanelMain>
        </Panel>
    );
}
