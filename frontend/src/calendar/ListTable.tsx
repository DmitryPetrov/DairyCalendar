import * as React from 'react';
import {useState} from 'react';
import Box from '@mui/material/Box';
import {Course} from "../model/Course";
import {List, ListItem, ListItemButton, ListItemText, Stack,} from "@mui/material";
import DialogDayForm from "./DialogDayForm";
import {DateTime} from "luxon";

interface RowProps {
    date: DateTime;
    courses: Course[];
    tags: string[];
    assessments: (number | null)[];
}

function Row({ date, courses}: RowProps) {
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
                    <ListItemText primary={date.toISODate().split("-")[2]} />
                </ListItemButton>
                {courses.map(course => {
                    let day = course.days.filter(day => day.date.hasSame(date, 'day'));
                    const data = day[0]?.assessment ?? "?";
                    const color = data > 0 ? "#81c784" : "#ffb74d";
                    return <ListItem key={course.name + date.toISODate()} style={{backgroundColor: color}}>
                        <ListItemText primary={data} />
                    </ListItem>
                })}
                <DialogDayForm open={open} handleClose={handleClose} date={date} courses={courses}/>
            </List>
    );
}

interface TableProps {
    courses: Course[];
    fromDate: (DateTime | undefined);
    toDate: (DateTime | undefined);
}

export default function ListTable({courses, fromDate, toDate}: TableProps) {

    const [dates, setDates] = useState<DateTime[]>([])

    React.useEffect(() => {setDates(getDateList())}, [courses, fromDate, toDate])

    function getDateList() {
        if ((fromDate == undefined) || (toDate == undefined)) {
            return []
        }
        let result: DateTime[] = [];
        let date = fromDate;
        while (date <= toDate) {
            result.push(date);
            date = date.plus({days: 1})
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
                            <ListItemText primary={course.name}  sx={{ width: '20%' }}/>
                            <ListItemText primary={course.tags} />
                        </ListItem>
                    })}
                </List>
                {dates.map(date => <Row key={date.toISODate()} date={date} courses={courses} tags={[]} assessments={[]}/>)}
            </Stack>
        </Box>
    )

}