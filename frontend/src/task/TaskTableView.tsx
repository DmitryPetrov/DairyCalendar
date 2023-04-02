import React, {useState} from "react";
import {Task} from "../model/Task";
import {closeTask, deleteTask, getTasks, postTask, putTask} from "../model/api";
import {
    Button,
    Chip,
    Paper,
    Rating,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow
} from "@mui/material";
import TaskForm from "./TaskForm";
import DoneIcon from '@mui/icons-material/Done';
import EditIcon from '@mui/icons-material/Edit';

export default function TaskTableView() {

    const [tasks, setTasks] = useState<Task[]>([])

    const [reload, setReload] = useState<boolean>(false)
    React.useEffect(() => {
        getTasks({}, readPayload)
    }, [reload]);
    function readPayload(tasks: Task[]) {
        setTasks(tasks);
    }

    function saveTask(task: Task) {
        if (typeof task.id == 'undefined') {
            postTask(task)
        } else {
            putTask(task);
        }
        handleClose()
    }

    const [task, setTask] = useState<Task | undefined>(undefined);
    const [parentTaskId, setPatentTaskId] = useState<number | null>(null);
    const [open, setOpen] = React.useState(false);
    const createTask = () => {
        setTask(undefined);
        setPatentTaskId(null);
        setOpen(true);
    };
    const openTask = (task: Task) => {
        setTask(task);
        setPatentTaskId(task.parentId);
        setOpen(true);
    };
    const addChild = (task: Task) => {
        if (typeof task.id == 'undefined') {
            return
        }
        setTask(undefined);
        setPatentTaskId(task.id);
        setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
        setReload(!reload);
    };


    return (
        <TableContainer component={Paper}>
            <Button variant="contained" onClick={createTask}>Create task</Button>

            <Table sx={{ minWidth: 650 }} aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell>Title</TableCell>
                        <TableCell align="center">Priority</TableCell>
                        <TableCell align="center">Done</TableCell>
                        <TableCell align="center">Finished at</TableCell>
                        <TableCell align="center">Tags</TableCell>
                        <TableCell align="center">Edit</TableCell>
                        <TableCell align="center">Show parent</TableCell>
                        <TableCell align="center">Add child</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {tasks.map((task) => (
                        <TableRow
                            key={task.id}
                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                        >
                            <TableCell component="th" scope="row">
                                {task.title}
                            </TableCell>
                            <TableCell align="center">
                                <Rating value={task.priority} readOnly/>
                            </TableCell>
                            <TableCell align="center">{task.done ? <DoneIcon /> : ''}</TableCell>
                            <TableCell align="center">{task.finishedAt?.toISODate?.()}</TableCell>
                            <TableCell align="center">{task.tags.map(tag => <Chip key={tag} label={tag}/>)}</TableCell>
                            <TableCell align="center">
                                <Button onClick={() => openTask(task)}><EditIcon /></Button>
                            </TableCell>
                            <TableCell align="center">
                                <Button>{task.parentId}</Button>
                            </TableCell>
                            <TableCell align="center">
                                <Button onClick={() => addChild(task)}>Add child</Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
            <TaskForm
                open={open}
                task={task}
                parentTaskId={parentTaskId}
                closeTask={closeTask}
                onSave={saveTask}
                onBack={handleClose}
                onDelete={deleteTask}
            />

        </TableContainer>
    );
}
