// This file is auto-generated by @hey-api/openapi-ts

import type { CancelablePromise } from './core/CancelablePromise';
import { OpenAPI } from './core/OpenAPI';
import { request as __request } from './core/request';
import type { GetWorkoutByDateData, GetWorkoutByDateResponse, UpdateWorkoutData, UpdateWorkoutResponse, AddWorkoutData, AddWorkoutResponse, RemoveWorkoutByDateData, RemoveWorkoutByDateResponse, GetAllWorkoutsResponse, GetExerciseByNameData, GetExerciseByNameResponse, GetExercisesResponse } from './types.gen';

export class WorkoutService {
    /**
     * Retrieve a workout by date
     * @param data The data for the request.
     * @param data.date
     * @returns Workout Ok - Workout was found for given date
     * @throws ApiError
     */
    public static getWorkoutByDate(data: GetWorkoutByDateData): CancelablePromise<GetWorkoutByDateResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/workout',
            query: {
                date: data.date
            },
            errors: {
                404: 'Not Found - Workout was not found for given date'
            }
        });
    }
    
    /**
     * Update a workout by date
     * @param data The data for the request.
     * @param data.requestBody
     * @returns unknown Ok - Workout was successfully updated
     * @throws ApiError
     */
    public static updateWorkout(data: UpdateWorkoutData): CancelablePromise<UpdateWorkoutResponse> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/workout',
            body: data.requestBody,
            mediaType: 'application/json'
        });
    }
    
    /**
     * Add a new workout
     * @param data The data for the request.
     * @param data.requestBody
     * @returns unknown Created - Workout was created successfully
     * @throws ApiError
     */
    public static addWorkout(data: AddWorkoutData): CancelablePromise<AddWorkoutResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/workout',
            body: data.requestBody,
            mediaType: 'application/json',
            errors: {
                400: 'Bad Request - Workout already exists for specified date'
            }
        });
    }
    
    /**
     * Remove a workout by date
     * @param data The data for the request.
     * @param data.date
     * @returns void No Content - Workout was removed successfully
     * @throws ApiError
     */
    public static removeWorkoutByDate(data: RemoveWorkoutByDateData): CancelablePromise<RemoveWorkoutByDateResponse> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/api/workout',
            query: {
                date: data.date
            },
            errors: {
                400: 'Bad Request - Date was missing or invalid',
                404: 'Not Found - Workout was not found for specified date'
            }
        });
    }
    
    /**
     * Retrieve all workouts
     * @returns Workout Ok - All workouts were retrieved successfully
     * @throws ApiError
     */
    public static getAllWorkouts(): CancelablePromise<GetAllWorkoutsResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/workout/all'
        });
    }
    
}

export class ExerciseService {
    /**
     * Retrieve exercise by name
     * @param data The data for the request.
     * @param data.name
     * @returns unknown Ok - exercise was retrieved successfully
     * @throws ApiError
     */
    public static getExerciseByName(data: GetExerciseByNameData): CancelablePromise<GetExerciseByNameResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/exercise',
            query: {
                name: data.name
            },
            errors: {
                404: 'Not Found - Exercise with that name does not exist'
            }
        });
    }
    
    /**
     * Retrieve all existing exercises
     * @returns Exercise Ok - exercise were received successfully
     * @throws ApiError
     */
    public static getExercises(): CancelablePromise<GetExercisesResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/exercise/all'
        });
    }
    
}