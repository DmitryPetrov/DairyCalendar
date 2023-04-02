import axios from "axios";
import {Course} from "./Course";
import {Day} from "./Day";
import {GetCoursesRequestParams} from "./GetCoursesRequestParams";
import {DateTime} from "luxon";


const URL = process.env.REACT_APP_API_ENDPOINT;
const client = axios.create({
    withCredentials: true,
    baseURL: URL + '/api'
});

export const DATE_FORMAT = "yyyy-MM-dd"

export const getCourses = (
    params: GetCoursesRequestParams,
    readPayload: (courses: Course[], fromDate: DateTime, toDate: DateTime) => void
) => {
    client
        .get('/course', {params: params})
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
        .get(`/courseList`)
        .then(response => {courses = response.data.map((item: any) => new Course(item))})
        .catch((error: TypeError) => {handleError(error)});
    return courses;
}

export const postDays = (days: Day[]) => {
    client
        .post(`/day`, days.map(item => item.toPostPayload()))
        .then(response => console.log(response))
        .catch((error: TypeError) => {handleError(error)});
}

export const postCourse = (course: Course) => {
    client
        .post(`/course`, course.toPostPayload())
        .catch((error: TypeError) => {handleError(error)});
}

export const isLoggedIn = (ifLoggedIn: () => void, ifNotLoggedIn: () => void, ) => {
    client
        .get('/login/check')
        .then(response => {
            console.log(response)
            ifLoggedIn();
        })
        .catch((error: TypeError) => {
            ifNotLoggedIn()
            //handleError(error)
        });
}

export const postLogin = (credentials: {username: string, password: string}, onSuccessLogin: () => void) => {
    client
        .post('/login/process', null,{params:credentials})
        .then(response => {
            console.log(response)
            onSuccessLogin();
        })
        .catch((error: TypeError) => {handleError(error)});
}

export const postLogout = (onSuccessLogout: () => void) => {
    client
        .post('/logout', )
        .then(response => {
            console.log(JSON.stringify(response));
            onSuccessLogout();
        })
        .catch((error: TypeError) => {handleError(error)});
}

function handleError(error: TypeError) {
    console.log('log client error ' + error);
    throw new Error(
        'There was an error retrieving the projects. Please try again.'
    );
}