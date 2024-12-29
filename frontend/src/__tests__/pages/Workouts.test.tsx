import { render, waitFor } from '@testing-library/react';
import { Workouts } from '../../pages/Workouts';

jest.mock('../../workout-api-client', () => {
    return {
        ApiError: '',
        Exercise: '',
        ExerciseService: {
            getExercises: jest.fn(() => Promise.resolve([])),
        },
        Workout: '',
        WorkoutService: {
            getWorkoutByDate: jest.fn(() => Promise.reject({
                status: 404
            })),
        },
    };
})

describe('Workouts', () => {
    it('Matches snapshot', async () => {
        const { container } = render(<Workouts />);

        await waitFor(() => expect(container).toMatchSnapshot());
    })
})