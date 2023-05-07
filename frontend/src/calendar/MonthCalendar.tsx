import * as React from "react";
import {DateTime} from "luxon";
import {Day} from "../model/Day";
import {Paper, Table, TableBody, TableCell, TableHead, TableRow, Typography} from "@mui/material";
import CloseIcon from '@mui/icons-material/Close';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';

interface RowProps {
    days: Day[]
    year: number,
    month: number
}

export default function MonthCalendar({ days, year, month }: RowProps) {

    let firstDay = DateTime.local(year, month, 1);
    let table: (DateTime | undefined)[][] = [[]];
    for (let i = 1; i < firstDay.weekday; i++) {
        table[0].push(undefined)
    }
    let week = 0;
    for (let day = 1; day <= firstDay.daysInMonth; day++) {
        if (table[week].length === 7) {
            table.push([])
            week++;
        }
        table[week].push(DateTime.local(year, month, day))
    }
    while (table[week].length < 7){
        table[week].push(undefined)
    }

    return (
        <Paper component={Paper} >
            <Table size="small">
                <TableHead>
                    <TableRow>
                        <TableCell align="center" colSpan={4}><Typography>{firstDay.monthLong}</Typography></TableCell>
                        <TableCell align="center" colSpan={3}><Typography>{firstDay.year}</Typography></TableCell>
                    </TableRow>
                    <TableRow>
                        <TableCell align="center"><Typography>Пн</Typography></TableCell>
                        <TableCell align="center"><Typography>Вт</Typography></TableCell>
                        <TableCell align="center"><Typography>Ср</Typography></TableCell>
                        <TableCell align="center"><Typography>Чт</Typography></TableCell>
                        <TableCell align="center"><Typography>Пт</Typography></TableCell>
                        <TableCell align="center"><Typography>Сб</Typography></TableCell>
                        <TableCell align="center"><Typography>Вс</Typography></TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {table.map((week: (DateTime | undefined)[], index) => {
                        return <TableRow key={'week' + index}>
                            {week.map((item, index2) => {
                                let grade = days.find(day => item?.equals(day.date))?.assessment;
                                if (grade === undefined) {
                                    return <TableCell align="center" key={'day'+index2}>{item?.day}</TableCell>
                                } else if (grade > 0) {
                                    return <TableCell align="center" key={'day'+index2} className="calendar_cell">
                                        <span className="calendar_cell_text">{item?.day}</span>
                                        <CheckCircleIcon className="calendar_cell_icon done"/>
                                    </TableCell>
                                }
                                return <TableCell align="center" key={'day'+index2} className="calendar_cell">
                                    <span className="calendar_cell_text">{item?.day}</span>
                                    <CloseIcon className="calendar_cell_icon undone"/>
                                </TableCell>
                            })}
                        </TableRow>
                    })}
                </TableBody>
            </Table>
        </Paper>
    );
}