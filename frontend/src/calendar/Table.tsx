import React, {useEffect, useState} from "react";
import {Course} from "../model/Course";
import CollapsibleTable from "./CollapsibleTable";
import axios from "axios";
import TableView from "./TableView";
import ListTable from "./ListTable";

const url = `http://localhost:8181/course`;

interface TableProps {
    saveCourses: (course: Course[]) => void;
}
function Table({saveCourses}: TableProps) {
    const [courses, setCourses] = useState<Course[]>([])
    const [fromDate, setFromDate] = useState<Date | null>(null)
    const [toDate, setToDate] = useState<Date | null>(null)

    React.useEffect(() => {
        axios
            .get(url)
            .then(response => {
                const courses2 = response.data.courses.map((item: any) => new Course(item))
                setCourses(courses2)
                saveCourses(courses2)
                setFromDate(response.data.fromDate)
                setToDate(response.data.toDate)
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
    /*
    <ListTable courses={courses} onSave={() => {}} fromDate={fromDate} toDate={toDate}/>
    <TableView courses={courses} onSave={() => {}} fromDate={fromDate} toDate={toDate}/>
    <CollapsibleTable courses={courses} onSave={() => {}} fromDate={fromDate} toDate={toDate}/>
    */

}

export default Table;