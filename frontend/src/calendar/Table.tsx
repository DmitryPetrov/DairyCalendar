import React, {useState} from "react";
import {Course} from "../model/Course";
import ListTable from "./ListTable";
import {DateTime} from "luxon";
import {getCourses} from "../model/api";
import {GetCoursesRequestParams} from "../model/GetCoursesRequestParams";

function Table() {
    const [courses, setCourses] = useState<Course[]>([])
    const [fromDate, setFromDate] = useState<DateTime | undefined>(undefined)
    const [toDate, setToDate] = useState<DateTime | undefined>(undefined)

    React.useEffect(() => {getCourses(new GetCoursesRequestParams(), readPayload)}, []);

    function readPayload(courses: Course[], fromDate: DateTime, toDate: DateTime) {
        setCourses(courses);
        setFromDate(fromDate);
        setToDate(toDate);
    }

    return <ListTable courses={courses} fromDate={fromDate} toDate={toDate}/>}

export default Table;