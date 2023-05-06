import {DateTime} from "luxon";
import {Course} from "../model/Course";
import React from "react";
import {ListItemButton, ListItemText} from "@mui/material";
import CourseTableCell from "./CourseTableCell";
import {useNavigate} from "react-router-dom";

interface RowProps {
    dates: DateTime[];
    course: Course;
}

export default function CourseTableRow({dates, course}: RowProps) {

    const navigate = useNavigate();
    const handleClick = () => {
        navigate('/course/' + course.id);
    };

    return (
        <ListItemButton onClick={handleClick}>
            <ListItemText primary={course.name} className="course_table_title"/>
            {dates.map(date =>
                <CourseTableCell
                    key={date.toISODate()}
                    grade={course.days.find(day => date.equals(day.date))?.assessment}/>
            )}
        </ListItemButton>
    );
}