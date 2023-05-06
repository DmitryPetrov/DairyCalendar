import * as React from 'react';
import {useState} from 'react';
import {Course} from "../model/Course";
import {Grid, Typography} from "@mui/material";
import {getCourses} from "../model/api";
import {GetCoursesRequestParams} from "../model/GetCoursesRequestParams";
import {DateTime} from "luxon";
import MonthCalendar from "./MonthCalendar";
import Box from "@mui/material/Box";
import {useParams} from "react-router-dom";

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

    function readPayload(courses: Course[], fromDate: DateTime, toDate: DateTime) {
        setCourse(courses[0]);
    }

    return (
        <Box>
            <Typography variant="h3">{course?.name}</Typography>
            <Typography variant="h6">{course?.description}</Typography>
            <Grid container spacing={2}>
                {Array.from(Array(12)).map((_, index) => {
                    let date = DateTime.now().minus({month: 11}).plus({month: index})
                    return <Grid item xs={3} key={index}>
                        <MonthCalendar days={course ? course.days.filter(day => day.date.month === date.month) : []}
                                       year={date.year} month={date.month}/>
                    </Grid>
                })}
            </Grid>
        </Box>
    );
}