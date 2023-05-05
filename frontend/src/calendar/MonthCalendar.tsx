import * as React from "react";
import {DateTime} from "luxon";
import {Day} from "../model/Day";
import {Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography} from "@mui/material";

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
    while (table[week].length < 8){
        table[week].push(undefined)
    }

    return (
        <TableContainer component={Paper} >
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
                                    return <TableCell align="center" key={'day'+index2} className="course_table_cell_positiv">{item?.day}</TableCell>
                                }
                                return <TableCell align="center" key={'day'+index2} className="course_table_cell_zero">{item?.day}</TableCell>
                            })}
                        </TableRow>
                    })}
                </TableBody>
            </Table>
        </TableContainer>
    );
}