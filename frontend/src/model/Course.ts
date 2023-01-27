import {Day} from "./Day";

export class Course {
    id: number | undefined;
    name: string = '';
    description: string = '';
    position: number = 100;
    tags: string[] = [];
    days: Day[] = [];

    constructor(item: any) {
        if (!item) return;
        if (item.id) this.id = item.id;
        if (item.name) this.name = item.name;
        if (item.description) this.description = item.description;
        if (item.position) this.position = item.position;
        if (item.tags) this.tags = item.tags;
        if (item.days)
            this.days = item.days.map((dayObj: any) => {return new Day(dayObj)});
    }
    public toPostPayload() {
        return {
            "name": this.name,
            "description": this.description,
            "tags": this.tags
        }
    }
}