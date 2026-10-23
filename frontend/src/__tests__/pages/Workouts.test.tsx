import { render, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import { Workouts } from '../../pages/Workouts';

vi.mock('../../workout-api-client', () => {
    return {
        Exercise: '',
        getExercises: vi.fn(() => Promise.resolve({ data: [], error: null })),
        getWorkoutByDate: vi.fn(() => Promise.resolve({
            error: {},
            response: {
                status: 404
            },
        })),
    };
})

describe('Workouts', () => {
    it('Matches snapshot', async () => {
        const { container } = render(<Workouts />);

        await waitFor(() => expect(container).toMatchSnapshot());
    })
})
