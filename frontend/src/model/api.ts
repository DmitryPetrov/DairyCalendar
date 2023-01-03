import axios from "axios";
import {Course} from "./Course";
import {Day} from "./Day";

const URL = 'http://localhost:8181';

export const DATE_FORMAT = "yyyy-MM-dd"

export const getCourseList = () => {
    const url = URL + `/courseList`;
    let courses: Course[] = [];
    axios
        .get(url)
        .then(response => {
            courses = response.data.map((item: any) => new Course(item))
        })
        .catch((error: TypeError) => {
            console.log('log client error ' + error);
            throw new Error(
                'There was an error retrieving the projects. Please try again.'
            );
        });

    return courses;
}

export const postDays = (days: Day[]) => {
    const url = URL + `/day`;
    axios
        .post(url, days.map(item => item.toPostPayload()))
        .then(response => console.log(response))
        .catch((error: TypeError) => {
            console.log('log client error ' + error);
            throw new Error(
                'There was an error retrieving the projects. Please try again.'
            );
        });
}