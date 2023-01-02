import * as React from 'react';
import Box from '@mui/material/Box';
import {Course} from "../model/Course";
import {DataGrid, GridCellParams, GridColDef} from '@mui/x-data-grid';
import moment from "moment";
import axios from "axios/index";


interface TableProps {
    courses: Course[];
    onSave: (course: Course) => void;
    fromDate: Date;
    toDate: Date;
}

export default function TableView({courses, onSave, fromDate, toDate}: TableProps) {

    function getColumns(fromDate: Date, toDate: Date) {
        let columns: GridColDef[] = [
            {
                field: 'name',
                headerName: 'Course name',
                width: 300,
                editable: true,
            }
        ];
        let startDate = moment(fromDate);
        let stopDate = moment(toDate);
        while (startDate.isBefore(stopDate)) {
            startDate.add(1, 'days')
            const date = startDate.toDate().toISOString().split("T")[0];
            columns.push(
                {
                    field: date,
                    headerName: date,
                    width: 90,
                    editable: false
                }
            )
        }
        return columns
    }

    function getRows() {
        let res = [];
        for (let i = 0; i < courses.length; i++) {
            let row: any = {};
            row.id = courses[i].id;
            row.name = courses[i].name
            let startDate = moment(fromDate);
            let stopDate = moment(toDate);
            while (startDate.isBefore(stopDate)) {
                startDate.add(1, 'days')
                const dateString = startDate.toDate().toISOString().split("T")[0];

                let dateObj: any;
                let days = courses[i].days.filter(day => day.date.toISOString().split("T")[0] === dateString);
                if ((Array.isArray(days)) && (days.length == 1)) {
                    dateObj = { [dateString]: days[0].assessment}
                } else {
                    dateObj = { [dateString]: "null"}
                }

                let temp = {...row, ...dateObj}
                row = temp;
            }
            res.push(row)
        }
        return res;
    }

    return (
        <Box sx={{ height: 700, width: '100%' }}>
            <DataGrid
                rowHeight={25}
                rows={getRows()}
                columns={getColumns(fromDate, toDate)}
                pageSize={courses.length}
                rowsPerPageOptions={[courses.length]}
                checkboxSelection
                disableSelectionOnClick
                experimentalFeatures={{ newEditingApi: true }}
                getCellClassName={(params: GridCellParams<number>) => {
                    if (params.field === 'city' || params.value == null) {
                        return '';
                    }
                    return params.value >= 15 ? 'hot' : 'cold';
                }}
            />
        </Box>
    );
}