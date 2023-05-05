import {DateTime} from "luxon";
import {Course} from "../model/Course";
import React from "react";
import {ListItemButton, ListItemText} from "@mui/material";
import CourseTableCell from "./CourseTableCell";
import OneCourseList from "./OneCourseList";

interface RowProps {
    dates: DateTime[];
    course: Course;
}

export default function CourseTableRow({dates, course}: RowProps) {
    const [open, setOpen] = React.useState(false);

    const handleClick = () => {
        setOpen(!open);
    };

    return (
        <div>
            <ListItemButton onClick={handleClick}>
                <ListItemText primary={course.name} className="course_table_title"/>
                {dates.map(date =>
                    <CourseTableCell
                        key={date.toISODate()}
                        grade={course.days.find(day => date.equals(day.date))?.assessment}/>
                )}
            </ListItemButton>
            <OneCourseList
                open={open}
                handleClose={handleClick}
                courseId={course.id}/>
        </div>
    );
}