import {DateTime} from "luxon";

export class Task {
    id: number | undefined;
    title: string = '';
    description: string = '';
    position: number = 100;
    priority: number = 0;
    done: boolean = false;
    finishedAt: DateTime | null = null;
    parentId: number | null = null;
    tags: string[] = [];

    constructor(item: any) {
        if (!item) return;
        if (item.id) this.id = item.id;
        if (item.title) this.title = item.title;
        if (item.description) this.description = item.description;
        if (item.position) this.position = item.position;
        if (item.priority) this.priority = item.priority;
        if (item.done) this.done = item.done;
        if (item.finishedAt) this.finishedAt = DateTime.fromISO(item.finishedAt);
        if (item.parentId) this.parentId = item.parentId;
        if (item.tags) this.tags = item.tags;
    }

    public toPostPayload() {
        return this
    }
}