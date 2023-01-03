import React, {useState} from "react";
import {Course} from "../model/Course";
import {Box, Button, Stack, TextField} from "@mui/material";
import {Day} from "../model/Day";
import DayForm from "./DayForm";
import {getCourseList, postDays} from "../model/api"
import {DateTime} from "luxon";


interface CourseFormProps {
    courses: Course[];
    onSave: (days: Day[]) => void;
    onCancel: () => void;
}

export default function DaysForm({onSave, onCancel}: CourseFormProps) {

    function toString(date: Date) {
        return date.toISOString().split("T")[0]
    }

    const [date, setDate] = useState(toString(new Date()));
    const [days, setDays] = useState<Day[]>([]);
    const [courses, setCourses] = useState<Course[]>([]);

    const handleSubmit = () => {postDays(days)};

    React.useEffect(() => {setCourses(getCourseList())}, []);

    const handleButton = (day: Day) => {
        day.date = DateTime.fromISO(date);
        let updatedDays;
        if (days.filter(e => e.courseId === day.courseId).length > 0) {
            updatedDays = days.map(old => old.courseId === day.courseId ? day : old);
        } else {
            updatedDays = [...days, day];
        }
        setDays(updatedDays)
    };

    return(
        <Box sx={{ width: '100%' }}>
            <Stack direction="row" spacing={2}>
                <div>
                    <Button variant="contained" onClick={handleSubmit}>Save</Button>
                </div>
                <TextField
                    id="outlined-basic"
                    label="Date"
                    variant="outlined"
                    value={date}
                    onChange={(e) => setDate(e.target.value)}/>
                <DayForm courses={courses} date={DateTime.fromISO(date)} onSave={handleButton} onCancel={() => {}}/>
            </Stack>
        </Box>
    )
}
