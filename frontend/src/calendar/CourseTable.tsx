import React, {useState} from "react";
import {Course} from "../model/Course";
import {DateTime} from "luxon";
import {getCourses} from "../model/api";
import {GetCoursesRequestParams} from "../model/GetCoursesRequestParams";
import CourseTableView from "./CourseTableView";
import {DayDescription} from "../model/DayDescription";

export default function CourseTable() {
    const [courses, setCourses] = useState<Course[]>([])
    const [daysDescriptions, setDaysDescriptions] = useState<DayDescription[]>([])
    const [dates, setDates] = useState<DateTime[]>([])
    const [reload, setReload] = useState<boolean>(false)

    React.useEffect(() => {
        getCourses(new GetCoursesRequestParams(DateTime.now().minus({days: 28}), DateTime.now()), readPayload)
    }, [reload]);

    function readPayload(courses: Course[], descriptions: DayDescription[], fromDate: DateTime, toDate: DateTime) {
        setCourses(courses);
        setDaysDescriptions(descriptions);
        setDates(getDateList(fromDate, toDate))
    }

    const reloadTable = () => {
        setReload(!reload);
    };

    function getDateList(fromDate: DateTime, toDate : DateTime) {
        if ((fromDate == undefined) || (toDate == undefined)) {
            return []
        }
        let result: DateTime[] = [];
        let date = fromDate;
        while (date <= toDate) {
            result.push(date);
            date = date.plus({days: 1})
        }
        return result
    }

    return <CourseTableView courses={courses} daysDescriptions={daysDescriptions} dates={dates} reloadTable={reloadTable}/>
}