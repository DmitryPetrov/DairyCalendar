import React, {useState} from "react";
import {Task} from "../model/Task";
import {closeTask, deleteTask, getTasks, postTask, putTask} from "../model/api";
import {
    Button,
    Chip,
    Fab,
    Paper,
    Rating,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography
} from "@mui/material";
import TaskForm from "./TaskForm";
import DoneIcon from '@mui/icons-material/Done';
import EditIcon from '@mui/icons-material/Edit';
import AddIcon from '@mui/icons-material/Add';
import TaskFilters from "./TaskFilters";
import Box from "@mui/material/Box";

export default function TaskTableView() {

    const [tasks, setTasks] = useState<Task[]>([])
    const [filters, setFilters] = useState<{}>({})
    const [reload, setReload] = useState<boolean>(false)
    React.useEffect(() => {
        getTasks(filters, readPayload)
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

    const onApplyFilters = (filters: {}) => {
        setFilters(filters);
        getTasks(filters, readPayload)
    }

    return (
        <div>
            <Fab color="primary" aria-label="add" onClick={createTask} className="add_element_button">
                <AddIcon />
            </Fab>
            <Box sx={{mb:'16px'}}>
                <TaskFilters onApply={onApplyFilters} />
            </Box>
            <TableContainer component={Paper} >
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell><Typography variant="h6">Title</Typography></TableCell>
                            <TableCell align="center"><Typography variant="h6">Priority</Typography></TableCell>
                            <TableCell align="center"><Typography variant="h6">Done</Typography></TableCell>
                            <TableCell align="center"><Typography variant="h6">Finished at</Typography></TableCell>
                            <TableCell align="center"><Typography variant="h6">Tags</Typography></TableCell>
                            <TableCell align="center"><Typography variant="h6">Edit</Typography></TableCell>
                            <TableCell align="center"><Typography variant="h6">Show parent</Typography></TableCell>
                            <TableCell align="center"><Typography variant="h6">Add child</Typography></TableCell>
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
                                <TableCell align="center">
                                    <Stack spacing={1} direction="row" >
                                        {task.tags.map(tag => <Chip key={tag} label={tag}/>)}
                                    </Stack>
                                </TableCell>
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
        </div>
    );
}
