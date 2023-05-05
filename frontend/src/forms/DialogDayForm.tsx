import * as React from 'react';
import {useState} from 'react';
import {Course} from "../model/Course";
import {DateTime} from "luxon";
import {Button, Dialog, DialogActions, DialogContent, DialogTitle, List} from "@mui/material";
import DayCourseForm from "./DayCourseForm";
import {postDays} from "../model/api"
import {Day} from "../model/Day";

interface RowProps {
    open: boolean;
    handleClose: () => void;
    date: DateTime;
    courses: Course[];
}

export default function DialogDayForm({ open, handleClose, date, courses}: RowProps) {

    const [days, setDays] = useState<Day[]>([]);
    function sendDays() {
        postDays(days)
        closeDialog();
    }
    function closeDialog() {
        setEmptyToZero(false);
        handleClose();
    }

    const handleAssessmentButton = (day: Day) => {
        let updatedDays;
        if (days.filter(e => e.courseId === day.courseId).length > 0) {
            updatedDays = days.map(old => old.courseId === day.courseId ? day : old);
        } else {
            updatedDays = [...days, day];
        }
        setDays(updatedDays)
    };

    const [emptyToZero, setEmptyToZero] = useState<boolean>(false);

    const handleEmptyToZero = () => {
        const days: Day[] = courses.flatMap(course => {
            const day = course.days.find(day => day.date.hasSame(date, 'day'));
            if (day == undefined) {
                return [new Day({
                    courseId: course.id,
                    courseName: course.name,
                    assessment: 0,
                    date: date.toISODate()
                })];
            } else {
                return []
            }
        })
        //эта функция отрабатывает только для курсов к которых оценка не проставлена
        //для проставленных оценок функция просто копирует значения
        setDays(days);
    }

    return (
        <Dialog open={open} onClose={closeDialog}>
            <DialogTitle>{date.toISODate()}</DialogTitle>
            <DialogContent>
                <List dense={true}>
                    {courses.map(course =>
                        <DayCourseForm
                            key={course.id}
                            course={course}
                            date={date}
                            handleAssessment={handleAssessmentButton}
                            setUndefinedAssessmentToZero={emptyToZero} />
                    )}
                </List>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleEmptyToZero}>Empty to ZERO</Button>
                <Button onClick={closeDialog}>Cancel</Button>
                <Button onClick={sendDays}>Sand</Button>
            </DialogActions>
        </Dialog>
    );
}