import {DateTime} from "luxon";

export class GetCoursesRequestParams {
    fromDate: (string | null) = null;
    toDate: (string | null) = null;
    tags: string[] = [];

    constructor(fromDate: DateTime, toDate: DateTime) {
        this.fromDate = fromDate.toISODate();
        this.toDate = toDate.toISODate();
    }
}