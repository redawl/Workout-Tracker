import React, { useEffect, useState } from "react";

import {
    Panel,
    PanelHeader,
    Title,
    Divider,
    PanelMain,
    PanelMainBody,
    Spinner,
    EmptyState
} from '@patternfly/react-core';

import { REPORTS } from "../components/constants";
import { Workout, getAllWorkouts } from "../workout-api-client";
import { LineChart } from "../components/charts/LineChart";

export const Reports = () => {
    const [workouts, setWorkouts] = useState([] as Workout[]);
    const [loaded, setLoaded] = useState(false);

    useEffect(() => {
        getAllWorkouts()
            .then(({ data: workouts, error }) => {
                if (error || !workouts) {
                    console.error(error, workouts)
                    return
                }
                setWorkouts(workouts);
            })
            .finally(() => setLoaded(true));
    }, []);

    return (
        <Panel>
            <PanelHeader>
                <Title headingLevel='h2'>{REPORTS}</Title>
            </PanelHeader>
            <Divider />
            <PanelMain>
                <PanelMainBody>
                    {loaded ? <LineChart workouts={workouts} /> : <EmptyState titleText="Loading" headingLevel="h4" icon={Spinner} />}
                </PanelMainBody>
            </PanelMain>
        </Panel>
    );
}
