import React from "react";
import {Course} from "../model/Course";
import {List} from "@mui/material";
import {Day} from "../model/Day";
import {DateTime} from "luxon";

interface CourseFormProps {
    date: DateTime
    courses: Course[];
    onSave: (day: Day) => void;
    onCancel: () => void;
}

export default function DayForm({courses, onSave, onCancel, date}: CourseFormProps) {
    return(
        <List>
{/*
            {courses.map(course =><DayCourseForm key={course.id} date={date} course={course} handleAssessment={onSave} />)}
*/}
        </List>
    )
}
