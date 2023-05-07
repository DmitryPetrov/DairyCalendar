import React, {useState} from "react";
import {Task} from "../model/Task";
import {getTasks} from "../model/api";
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
import DoneIcon from '@mui/icons-material/Done';
import EditIcon from '@mui/icons-material/Edit';
import AddIcon from '@mui/icons-material/Add';
import TaskFilters from "./TaskFilters";
import Box from "@mui/material/Box";
import {useNavigate} from "react-router-dom";

export default function TaskTableView() {
    const navigate = useNavigate();
    const [tasks, setTasks] = useState<Task[]>([])
    const [filters, setFilters] = useState<{}>({})
    React.useEffect(() => getTasks(filters, readPayload), []);
    function readPayload(tasks: Task[]) {
        setTasks(tasks);
    }
    const onApplyFilters = (filters: {}) => {
        setFilters(filters);
        getTasks(filters, readPayload)
    }

    return (
        <Paper className="page_container" elevation={3}>
            <Fab color="primary"
                 aria-label="add"
                 onClick={() => navigate('/task/new')}
                 className="add_element_button">
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
                            <TableCell align="center"><Typography variant="h6">Parent</Typography></TableCell>
                            <TableCell align="center"><Typography variant="h6">Add subtask</Typography></TableCell>
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
                                    <Stack spacing={1} direction="row" justifyContent="center">
                                        {task.tags.map(tag => <Chip key={tag} label={tag}/>)}
                                    </Stack>
                                </TableCell>
                                <TableCell align="center">
                                    <Button onClick={() => navigate('/task/' + task.id)}><EditIcon/></Button>
                                </TableCell>
                                <TableCell align="center">
                                    <Button>{task.parentId}</Button>
                                </TableCell>
                                <TableCell align="center">
                                    <Button onClick={() => navigate('/task/new?parent=' + task.id)}>
                                        Add subtask ...
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Paper>
    );
}
