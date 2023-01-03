import React, {useState} from "react";
import {Course} from "../model/Course";
import axios from "axios";
import ListTable from "./ListTable";
import {DateTime} from "luxon";

const params ='fromDate=2022-12-30&toDate=2022-12-31';
const url = `http://localhost:8181/course`;


interface TableProps {
    saveCourses: (course: Course[]) => void;
}
function Table({saveCourses}: TableProps) {
    const [courses, setCourses] = useState<Course[]>([])
    const [fromDate, setFromDate] = useState<DateTime | null>(null)
    const [toDate, setToDate] = useState<DateTime | null>(null)

    React.useEffect(() => {
        axios
            .get(url)
            .then(response => {
                const courses2 = response.data.courses.map((item: any) => new Course(item))
                setCourses(courses2)
                saveCourses(courses2)
                setFromDate(DateTime.fromISO(response.data.fromDate))
                setToDate(DateTime.fromISO(response.data.toDate))
                console.log(response)
            })
            .catch((error: TypeError) => {
                console.log('log client error ' + error);
                throw new Error(
                    'There was an error retrieving the projects. Please try again.'
                );
            });
    }, []);

    // @ts-ignore
    return <ListTable courses={courses} onSave={() => {}} fromDate={fromDate} toDate={toDate}/>
}

export default Table;