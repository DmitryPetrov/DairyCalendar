import {DateTime} from "luxon";

export class GetCoursesRequestParams {
    fromDate: (string | null) = null;
    toDate: (string | null) = null;
    tags: string[] = [];
    courses: (number[] | null) = [];

    constructor(
        fromDate?: DateTime,
        toDate?: DateTime,
        courses?: number[],
        tags?: string[],
    ) {
        this.fromDate = fromDate ? fromDate.toISODate() : null;
        this.toDate = toDate ? toDate.toISODate() : null;
        this.tags = tags ? tags : []
        this.courses = courses ? courses : [];
    }
}