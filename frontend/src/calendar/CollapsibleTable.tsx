import * as React from 'react';
import Box from '@mui/material/Box';
import Collapse from '@mui/material/Collapse';
import IconButton from '@mui/material/IconButton';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import {Course} from "../model/Course";
import {GridColDef} from "@mui/x-data-grid";
import moment from "moment/moment";
import {Divider} from "@mui/material";

interface RowProps {
    name: string;
    description: string;
    tags: string[];
    assessments: (number | null)[];
}

function Row({ name, description, tags, assessments }: RowProps) {
    const [open, setOpen] = React.useState(false);

    return (
        <React.Fragment>
            <TableRow>
                <TableCell>
                    <IconButton
                        aria-label="expand row"
                        size="small"
                        onClick={() => setOpen(!open)}
                    >
                        {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
                    </IconButton>
                </TableCell>
                <TableCell component="th" scope="row">
                    {name}
                </TableCell>
                {assessments.map((assessment, index) => {
                    if (assessment === 0) {
                        return <React.Fragment>
                            <TableCell align="center" key={name + index} style={{backgroundColor:"red"}}>{assessment}</TableCell>
                        </React.Fragment>
                    } else {
                        return <React.Fragment>
                            <TableCell align="center" key={name + index}>{assessment}</TableCell>
                        </React.Fragment>
                    }
                    }
                )}
            </TableRow>
            <TableRow>
                <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
                    <Collapse in={open} timeout="auto" unmountOnExit>
                        <Box sx={{ margin: 1 }}>
                            <Typography variant="h6" gutterBottom component="div">
                                {description}
                            </Typography>
                        </Box>
                    </Collapse>
                </TableCell>
            </TableRow>
        </React.Fragment>
    );
}

interface TableProps {
    courses: Course[];
    onSave: (course: Course) => void;
    fromDate: Date;
    toDate: Date;
}

export default function CollapsibleTable({courses, onSave, fromDate, toDate}: TableProps) {

    function getColumns() {
        let columns: any[] = [];
        columns.push(<TableCell key="Course name">Course name</TableCell>)
        let startDate = moment(fromDate);
        let stopDate = moment(toDate);
        while (startDate.isBefore(stopDate)) {
            startDate.add(1, 'days')
            const date = startDate.toDate().toISOString().split("T")[0];
            columns.push(
                <React.Fragment>
                    <TableCell align="center" key={date}>{date}</TableCell>
                </React.Fragment>
            )
        }
        return columns
    }

    function getDates(course: Course) {
        let result: (number | null)[] = []
        let startDate = moment(fromDate);
        let stopDate = moment(toDate);
        while (startDate.isBefore(stopDate)) {
            startDate.add(1, 'days')
            const dateString = startDate.toDate().toISOString().split("T")[0];

            let days = course.days.filter(day => day.date.toISOString().split("T")[0] === dateString);
            if ((Array.isArray(days)) && (days.length == 1)) {
                result.push(days[0].assessment)
            } else {
                result.push(null)
            }
        }
        console.log(result)
        return result
    }

    return (
        <TableContainer component={Paper}>
            <Table aria-label="collapsible table" size="small">
                <TableHead>
                    <TableRow>
                        <TableCell />
                        {getColumns()}
                    </TableRow>
                </TableHead>
                <TableBody>
                    {courses.map(course =>
                        <Row key={course.id}
                             name={course.name}
                             description={course.description}
                             tags={course.tags}
                             assessments={getDates(course)}
                        />
                    )}
                </TableBody>
            </Table>
        </TableContainer>
    );
}