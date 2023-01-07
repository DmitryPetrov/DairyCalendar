import * as React from 'react';
import Box from '@mui/material/Box';
import {Course} from "../model/Course";
import {DateTime} from "luxon";


interface TableProps {
    courses: Course[];
    onSave: (course: Course) => void;
    fromDate: DateTime;
    toDate: DateTime;
}

export default function TableView({courses, onSave, fromDate, toDate}: TableProps) {


    return (
        <Box sx={{ height: 700, width: '100%' }}>

        </Box>
    );
}