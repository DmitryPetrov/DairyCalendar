import * as React from 'react';
import {useState} from 'react';
import Box from '@mui/material/Box';
import {Course} from "../model/Course";
import {Divider, List, ListItem, ListItemButton, ListItemText,} from "@mui/material";
import DialogDayForm from "../forms/DialogDayForm";
import {DateTime} from "luxon";
import CourseTableRow from "./CourseTableRow";

interface TableProps {
    courses: Course[];
    dates: DateTime[]
    reloadTable: () => void
}

export default function CourseTableView({courses, dates, reloadTable}: TableProps) {

    const [date, setDate] = useState<DateTime>(DateTime.now())
    const [open, setOpen] = React.useState(false);

    const handleClickOpen = (date: DateTime) => {
        setDate(date)
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
        reloadTable();
    };

    return (
        <Box sx={{width: '100%'}}>
            <List dense={true}>
                <ListItem>
                    <ListItemText primary="Course name" className="course_table_title"/>
                    {dates.map(date =>
                        <ListItemButton key={date.toISODate()} onClick={() => handleClickOpen(date)}>
                            <ListItemText
                                primary={date.toISODate().split("-")[2]}
                                className="course_table_cell"/>
                        </ListItemButton>
                    )}
                </ListItem>
                <Divider />
                {courses
                    .sort((a, b) => a.position - b.position)
                    .map(course => <CourseTableRow key={course.id} course={course} dates={dates}/>)}
                <DialogDayForm
                    open={open}
                    handleClose={handleClose}
                    date={date}
                    courses={courses}/>
            </List>
        </Box>
    )

}