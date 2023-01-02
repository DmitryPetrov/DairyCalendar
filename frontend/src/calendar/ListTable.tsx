import * as React from 'react';
import Box from '@mui/material/Box';
import {Course} from "../model/Course";
import moment, {Moment} from "moment/moment";
import {
    List,
    ListItem,
    ListItemButton,
    ListItemText,
    Stack,
} from "@mui/material";
import {useState} from "react";
import DayCourseForm from "../forms/DayCourseForm";
import DialogDayForm from "./DialogDayForm";

interface RowProps {
    date: Moment;
    courses: Course[];
    tags: string[];
    assessments: (number | null)[];
}

function Row({ date, courses, tags, assessments }: RowProps) {
    const [open, setOpen] = React.useState(false);
    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
    return (
            <List dense={true}>
                <ListItemButton onClick={handleClickOpen}>
                    <ListItemText primary={date.toISOString().split("T")[0]} />
                </ListItemButton>
                {courses.map(course => {
                    let days2 = course.days.filter(day => moment(day.date).isSame(date));

                    if ((Array.isArray(days2)) && (days2.length === 1)) {
                        return <ListItem key={course.name + date.toISOString()}>
                            <ListItemText primary={days2[0].assessment} />
                        </ListItem>
                    } else {
                        return <ListItem key={course.name + date.toISOString()}>
                            <ListItemText primary="{null}" />
                        </ListItem>
                    }
                })}
                <DialogDayForm open={open} handleClose={handleClose} date={date} courses={courses}/>
            </List>
    );
}

interface TableProps {
    courses: Course[];
    onSave: (course: Course) => void;
    fromDate: Date;
    toDate: Date;
}

export default function ListTable({courses, onSave, fromDate, toDate}: TableProps) {

    const [dates, setDates] = useState<Moment[]>([])

    React.useEffect(() => {
        setDates(getDateList())
    }, [fromDate, toDate])

    function getDateList() {
        if ((fromDate == null) || (toDate == null)) {
            return []
        }
        let result: Moment[] = []
        let startDate = moment(new Date(fromDate));
        let stopDate = moment(toDate);
        while (startDate.isBefore(stopDate)) {
            result.push(moment(startDate))
            startDate.add(1, 'days')
        }
        return result
    }

    return (
        <Box sx={{ width: '100%' }}>
            <Stack direction="row" spacing={2}>
                <List dense={true}>
                    <ListItem>
                        <ListItemText primary="Course name" />
                    </ListItem>
                    {courses.map(course => {
                        return <ListItem key={course.name}>
                            <ListItemText primary={course.name} />
                        </ListItem>
                    })}
                </List>
                {dates.map(date => <Row key={date.toISOString()} date={date} courses={courses} tags={[]} assessments={[]}/>)}
            </Stack>
        </Box>
    )

}