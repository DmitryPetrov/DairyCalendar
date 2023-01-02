export class Day {

    courseId: number | undefined;
    courseName: string = '';
    assessment: number = 0;
    date: Date = new Date();

    constructor(item: any) {
        if (!item) return;
        if (item.courseId) this.courseId = item.courseId;
        if (item.courseName) this.courseName = item.courseName;
        if (item.assessment) this.assessment = item.assessment;
        if (item.date) this.date = new Date(item.date);
    }

    public toPostPayload() {
        return {
            "courseId": this.courseId,
            "date": this.date.toISOString().split("T")[0],
            "assessment": this.assessment
        }
    }
}