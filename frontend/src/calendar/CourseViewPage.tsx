import * as React from 'react';
import {useState} from 'react';
import {Course} from "../model/Course";
import {Grid, Paper, Typography} from "@mui/material";
import {getCourses} from "../model/api";
import {GetCoursesRequestParams} from "../model/GetCoursesRequestParams";
import {DateTime} from "luxon";
import MonthCalendar from "./MonthCalendar";
import {useParams} from "react-router-dom";
import {DayDescription} from "../model/DayDescription";

export default function CourseViewPage() {
    const [course, setCourse] = useState<Course>()
    const id = useParams().id;

    React.useEffect(() => {
        const params = new GetCoursesRequestParams(
            DateTime.now().minus({year: 1}),
            DateTime.now(),
            id ? [parseInt(id)] : []
        );
        getCourses(params, readPayload)
    }, []);

    function readPayload(courses: Course[], descriptions: DayDescription[], fromDate: DateTime, toDate: DateTime) {
        setCourse(courses[0]);
    }

    return (
        <Paper className="page_container" elevation={3}>
            <Typography variant="h3" className="course_title">{course?.name}</Typography>
            <Typography variant="h6" className="course_description">{course?.description}</Typography>
            <Grid container spacing={2} className="course_calendar">
                {Array.from(Array(12)).map((_, index) => {
                    let date = DateTime.now().minus({month: 11}).plus({month: index})
                    return <Grid item xs={3} key={index}>
                        <MonthCalendar days={course ? course.days.filter(day => day.date.month === date.month) : []}
                                       year={date.year} month={date.month}/>
                    </Grid>
                })}
            </Grid>
        </Paper>
    );
}