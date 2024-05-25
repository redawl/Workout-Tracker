import { Chart, ChartAxis, ChartGroup, ChartLine, ChartThemeColor } from '@patternfly/react-charts';
import { Exercise, Workout } from '../../workout-api-client';

export const LineChart = ({ workouts }: { workouts: Array<Workout> }) => {
    const legendData = [{ childName: 'lbs', name: 'Lbs' }];

    return (
        <Chart
            legendData={legendData}
            legendPosition="bottom"
            height={200}
            minDomain={{ y: 40 }}
            themeColor={ChartThemeColor.green}
            width={800}
        >
            <ChartAxis />
            <ChartAxis dependentAxis/>
            <ChartGroup>
                <ChartLine
                    data={workouts}
                    name="lbs"
                    x={(datum: Workout) => datum.date}
                    y={(datum: Workout) => datum.exercises.reduce((prev: number, next: Exercise) => prev + next.lbs * next.reps * next.sets, 0)}
                />
            </ChartGroup>
        </Chart>
    );
}
