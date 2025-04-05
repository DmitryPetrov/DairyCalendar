import {DateTime} from "luxon";
import {DATE_FORMAT} from "./api";

export class DayDescription {

    description: string = '';
    date: DateTime = DateTime.now();

    constructor(item: any) {
        if (!item) return;
        if (item.description) this.description = item.description;
        if (item.date) this.date = DateTime.fromFormat(item.date, DATE_FORMAT);
    }

    public toPostPayload() {
        return {
            "date": this.date.toISODate(),
            "description": this.description
        }
    }
}