import React from "react";
import {Button, Stack} from "@mui/material";

interface TasksProps {
    createTask: () => void;
}


export default function TasksPanel({createTask}: TasksProps) {

    return (
        <Stack direction="row" spacing={2}>
            <Button variant="contained" onClick={e => createTask()}>Save</Button>
        </Stack>
    );
}
