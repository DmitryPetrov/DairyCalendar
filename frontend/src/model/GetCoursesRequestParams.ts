import {DateTime} from "luxon";

export class GetCoursesRequestParams {
    fromDate: (string | null) = null;
    toDate: (string | null) = null;
    tags: string[] = [];
    courses: (number[] | null) = [];

    constructor(fromDate: DateTime, toDate: DateTime, courses: number[]) {
        this.fromDate = fromDate.toISODate();
        this.toDate = toDate.toISODate();
        this.courses = courses;
    }
}