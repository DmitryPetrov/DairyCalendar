import * as React from 'react';
import {useState} from 'react';
import Box from '@mui/material/Box';
import {Course} from "../model/Course";
import {List, ListItem, ListItemButton, ListItemText,} from "@mui/material";
import DialogDayForm from "./DialogDayForm";
import {DateTime} from "luxon";

interface RowProps {
    date: DateTime;
    courses: Course[];
    course: Course;
}

const courseInfoSize = "20%"
const cellSize = "5%"

function Row({date, course}: RowProps) {
    const [open, setOpen] = React.useState(false);
    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    function a() {
        let day = course.days.filter(day => day.date.hasSame(date, 'day'));
        const data = day[0]?.assessment ?? "?";
        const color = data > 0 ? "#81c784" : "#ffb74d";
        return <ListItemText
            primary={data}
            key={course.name + date.toISODate()}
            style={{backgroundColor: color, width: cellSize, textAlign: "center"}}/>
    }
    return (
            a()
    );
}

interface TableProps {
    courses: Course[];
    fromDate: (DateTime | undefined);
    toDate: (DateTime | undefined);
}

export default function ListTable2({courses, fromDate, toDate}: TableProps) {

    const [dates, setDates] = useState<DateTime[]>([])
    const [date, setDate] = useState<DateTime>(DateTime.now())
    const [open, setOpen] = React.useState(false);
    const handleClickOpen = (date: DateTime) => {
        setDate(date)
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
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
            <List dense={true}>
                <ListItem >
                    <ListItemText primary="Course name" sx={{ width: courseInfoSize}} />
                    {dates.map(date =>
                        <ListItemButton key={date.toISODate()} onClick={() => handleClickOpen(date)}>
                            <ListItemText
                                primary={date.toISODate().split("-")[2]}
                                sx={{ width: cellSize, textAlign: "center" }}
                            />
                        </ListItemButton>)}
                </ListItem>

                {courses.map(course => {
                    return <ListItemButton key={course.name}>
                        <ListItemText primary={course.name}  sx={{ width: courseInfoSize }}/>
                        {dates.map(date => <Row key={date.toISODate()} date={date} course={course} courses={courses}/>)}
                    </ListItemButton>
                })}
                    <DialogDayForm
                        open={open}
                        handleClose={handleClose}
                        date={date}
                        courses={courses}/>

            </List>
        </Box>
    )

}