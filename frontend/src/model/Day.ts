import {DateTime} from "luxon";
import {DATE_FORMAT} from "./api";

export class Day {

    courseId: number | undefined;
    courseName: string = '';
    assessment: number = 0;
    date: DateTime = DateTime.now();

    constructor(item: any) {
        if (!item) return;
        if (item.courseId) this.courseId = item.courseId;
        if (item.courseName) this.courseName = item.courseName;
        if (item.assessment) this.assessment = item.assessment;
        if (item.date) this.date = DateTime.fromFormat(item.date, DATE_FORMAT);
    }

    public toPostPayload() {
        return {
            "courseId": this.courseId,
            "date": this.date.toISODate(),
            "assessment": this.assessment
        }
    }
}