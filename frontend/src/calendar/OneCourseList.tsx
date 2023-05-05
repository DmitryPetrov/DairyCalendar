import * as React from 'react';
import {useState} from 'react';
import {Course} from "../model/Course";
import {Dialog, DialogContent, DialogTitle, Grid} from "@mui/material";
import {getCourses} from "../model/api";
import {GetCoursesRequestParams} from "../model/GetCoursesRequestParams";
import {DateTime} from "luxon";
import MonthCalendar from "./MonthCalendar";

interface RowProps {
    open: boolean;
    handleClose: () => void
    courseId: number | undefined;
}

export default function OneCourseList({ open, handleClose, courseId}: RowProps) {
    const [course, setCourse] = useState<Course|undefined>(undefined)

    React.useEffect(() => {
        if (open) {
            getCourses(new GetCoursesRequestParams(
                    DateTime.now().minus({year: 1}),
                    DateTime.now(),
                    courseId ? [courseId] : []
                ),
                readPayload
            )
        }
    }, [open]);

    function readPayload(courses: Course[], fromDate: DateTime, toDate: DateTime) {
        setCourse(courses[0]);
    }

    return (
        <Dialog open={open} onClose={handleClose} fullWidth maxWidth="xl" sx={{height: '100%'}}>
            <DialogTitle>{course?.name}</DialogTitle>
            <DialogContent>
                <Grid container spacing={2}>
                    {Array.from(Array(12)).map((_, index) => {
                        let date = DateTime.now().minus({month: 11}).plus({month: index})
                        return <Grid item xs={3} key={index}>
                            <MonthCalendar days={course ? course.days.filter(day => day.date.month === date.month) : []}
                                           year={date.year} month={date.month}/>
                        </Grid>
                    })}
                </Grid>
            </DialogContent>
        </Dialog>
    );
}