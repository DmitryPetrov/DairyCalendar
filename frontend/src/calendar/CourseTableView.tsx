import * as React from 'react';
import {useState} from 'react';
import {Course} from "../model/Course";
import {
    Divider,
    List,
    ListItem,
    ListItemButton,
    ListItemText,
    Paper,
    styled,
    Tooltip, tooltipClasses, TooltipProps,
} from "@mui/material";
import DialogDayForm from "../forms/DialogDayForm";
import {DateTime} from "luxon";
import CourseTableRow from "./CourseTableRow";
import {DayDescription} from "../model/DayDescription";

interface TableProps {
    courses: Course[];
    dates: DateTime[];
    reloadTable: () => void;
    daysDescriptions: DayDescription[];
}


const CustomTooltip = styled(({ className, ...props }: TooltipProps) => (
    <Tooltip {...props} classes={{ popper: className }} />
))(({ theme }) => ({
    [`& .${tooltipClasses.tooltip}`]: {
        backgroundColor: '#f5f5f9',
        color: 'rgba(0, 0, 0, 0.87)',
        maxWidth: 500,
        minWidth: 200,
        fontSize: theme.typography.pxToRem(14),
        border: '1px solid #dadde9',
        whiteSpace: 'pre-wrap'
    },
}));

export default function CourseTableView({courses, dates, reloadTable, daysDescriptions}: TableProps) {

    const [date, setDate] = useState<DateTime>(DateTime.now())
    const [description, setDescription] = useState<string>("")
    const [open, setOpen] = React.useState(false);

    const handleClickOpen = (date: DateTime) => {
        setDate(date)
        setDescription(getDayDescriptionByDate(date))
        setOpen(true);
    };

    const getDayDescriptionByDate = (date: DateTime) => {
        const dayDescription : DayDescription = daysDescriptions
            .filter((description) => date.hasSame(description.date, 'day'))[0];
        return dayDescription?.description ?? ""
    }

    const handleClose = () => {
        setOpen(false);
        setDescription("");
        reloadTable();
    };

    return (
        <Paper className="page_container" elevation={3}>
            <List dense={true}>
                <ListItem>
                    <ListItemText primary="Course name" className="course_table_title"/>
                    {dates.map(date =>
                        <CustomTooltip key={date.toISODate()}
                                 title={getDayDescriptionByDate(date)}
                                 enterDelay={400}
                                 enterNextDelay={400}
                                 leaveDelay={200}>
                            <ListItemButton key={date.toISODate()} onClick={() => handleClickOpen(date)}>
                                <ListItemText
                                    primary={date.toISODate().split("-")[2]}
                                    className="course_table_cell"/>
                            </ListItemButton>
                        </CustomTooltip>
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
                    courses={courses}
                    savedDescription={description}/>
            </List>
        </Paper>
    )

}