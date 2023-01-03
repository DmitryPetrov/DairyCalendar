import axios from "axios";
import {Course} from "./Course";
import {Day} from "./Day";
import {GetCoursesRequestParams} from "./GetCoursesRequestParams";
import {DateTime} from "luxon";

const URL = 'http://localhost:8181';
export const DATE_FORMAT = "yyyy-MM-dd"

export const getCourses = (
    params: GetCoursesRequestParams,
    readPayload: (courses: Course[], fromDate: DateTime, toDate: DateTime) => void
) => {
    axios
        .get(URL + '/course', {params: params})
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
    const url = URL + `/courseList`;
    let courses: Course[] = [];
    axios
        .get(url)
        .then(response => {courses = response.data.map((item: any) => new Course(item))})
        .catch((error: TypeError) => {handleError(error)});
    return courses;
}

export const postDays = (days: Day[]) => {
    const url = URL + `/day`;
    axios
        .post(url, days.map(item => item.toPostPayload()))
        .then(response => console.log(response))
        .catch((error: TypeError) => {handleError(error)});
}

export const postCourse = (course: Course) => {
    const url = URL + `/course`;
    axios
        .post(url, course.toPostPayload())
        .catch((error: TypeError) => {handleError(error)});
}

function handleError(error: TypeError) {
    console.log('log client error ' + error);
    throw new Error(
        'There was an error retrieving the projects. Please try again.'
    );
}