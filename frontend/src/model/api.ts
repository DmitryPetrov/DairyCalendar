import axios from "axios";
import {Course} from "./Course";
import {Day} from "./Day";
import {GetCoursesRequestParams} from "./GetCoursesRequestParams";
import {DateTime} from "luxon";
import {Task} from "./Task";
import * as Qs from "qs";


const URL = process.env.REACT_APP_API_ENDPOINT;
const client = axios.create({
    withCredentials: true,
    baseURL: URL
});

const anonymous = axios.create({
    withCredentials: true,
    baseURL: URL
});

export const DATE_FORMAT = "yyyy-MM-dd"

export const getCourses = (
    params: GetCoursesRequestParams,
    readPayload: (courses: Course[], fromDate: DateTime, toDate: DateTime) => void
) => {
    client
        .get('/api/course', {
            params: params,
            paramsSerializer: { serialize: (params: any) => Qs.stringify(params, {arrayFormat: 'comma'}) }
        })
        .then(response => {
            readPayload(
                response.data.courses.map((item: any) => new Course(item)),
                DateTime.fromISO(response.data.fromDate),
                DateTime.fromISO(response.data.toDate)
            )
        })
        .catch((error: TypeError) => {handleError(error)});
}

export const getCourseList = () => {
    let courses: Course[] = [];
    client
        .get(`/api/courseList`)
        .then(response => {courses = response.data.map((item: any) => new Course(item))})
        .catch((error: TypeError) => {handleError(error)});
    return courses;
}

export const postDays = (days: Day[]) => {
    client
        .post(`/api/day`, days.map(item => item.toPostPayload()))
        .then(response => {
            window.location.reload();
        })
        .catch((error: TypeError) => {handleError(error)});
}

export const postCourse = (course: Course) => {
    client
        .post(`/api/course`, course.toPostPayload())
        .catch((error: TypeError) => {handleError(error)});
}

export const postTask = (task: Task, readPayload: (id: number) => void) => {
    client
        .post(`/api/task`, task.toPostPayload())
        .then(response => readPayload(response.data.id))
        .catch((error: TypeError) => {handleError(error)});
}

export const putTask = (task: Task, readPayload: (id: number) => void) => {
    client
        .put(`/api/task/` + task.id, task.toPostPayload())
        .then(response => readPayload(response.data.id))
        .catch((error: TypeError) => {handleError(error)});
}

export const closeTask = (id: number, readPayload: (id: number) => void) => {
    client
        .put(`/api/task/` + id + '/close')
        .then(response => readPayload(response.data.id))
        .catch((error: TypeError) => {handleError(error)});
}

export const deleteTask = (id: number, onSuccess: () => void) => {
    client
        .delete(`/api/task/` + id)
        .then(response => onSuccess())
        .catch((error: TypeError) => {handleError(error)});
}

export const getTasks = (
    params: object,
    readPayload: (tasks: Task[]) => void
) => {
    client
        .get('/api/task', {
            params: params,
            paramsSerializer: { serialize: (params: any) => Qs.stringify(params, {arrayFormat: 'comma'}) }
        })
        .then(response => {
            readPayload(response.data.map((item: any) => new Task(item)))
        })
        .catch((error: TypeError) => {handleError(error)});
}

export const getTask = (id: number, readPayload: (task: Task) => void) => {
    client
        .get(`/api/task/` + id)
        .then(response => readPayload(new Task(response.data)))
        .catch((error: TypeError) => {handleError(error)});
}

export const isLoggedIn = (ifLoggedIn: () => void, ifNotLoggedIn: () => void, ) => {
    client
        .get('/login/check')
        .then(response => {
            if (response.data === true) {
                ifLoggedIn();
            } else {
                ifNotLoggedIn()
            }
        })
        .catch((error: TypeError) => {
            ifNotLoggedIn()
        });
}

export const postLogin = (credentials: {username: string, password: string}, onSuccessLogin: () => void) => {
    anonymous
        .post('/login/process', null,{params:credentials})
        .then(response => {
            console.log(response)
            onSuccessLogin();
        })
        .catch((error: TypeError) => {handleError(error)});
}

export const postLogout = (onSuccessLogout: () => void) => {
    anonymous
        .post('/logout', )
        .then(response => {
            console.log(JSON.stringify(response));
            onSuccessLogout();
        })
        .catch((error: TypeError) => {handleError(error)});
}

export const getTags = (readPayload: (tags: string[]) => void) => {
    client
        .get('/api/tag')
        .then(response => {
            readPayload(response.data)
        })
        .catch((error: TypeError) => {handleError(error)});
}

function handleError(error: TypeError) {
    console.log(error);
    throw new Error(
        'There was an error retrieving the projects. Please try again.'
    );
}