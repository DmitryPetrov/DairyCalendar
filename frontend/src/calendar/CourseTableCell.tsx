import React from "react";
import {ListItemText} from "@mui/material";

interface CellProps {
    grade: number | undefined
}

export default function CourseTableCell({grade}: CellProps) {
    if (grade === undefined) {
        return <ListItemText primary="?" className="course_table_cell"/>
    } else if (grade > 0) {
        return <ListItemText primary={grade} className="course_table_cell course_table_cell_positiv"/>
    }
    return <ListItemText primary={grade} className="course_table_cell course_table_cell_zero"/>
}