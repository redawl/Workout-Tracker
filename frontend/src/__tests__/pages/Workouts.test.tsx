import { findByText, render, waitFor } from '@testing-library/react';
import { describe, test, vi } from 'vitest';
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
    test('Matches snapshot', async ({ expect }) => {
        vi.setSystemTime(new Date(2026, 9, 23))
        const { container } = render(<Workouts />);

        await waitFor(() => expect(container).toMatchSnapshot());
    })
})
