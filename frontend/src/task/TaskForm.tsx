import React, {SyntheticEvent, useState} from "react";
import {
    Autocomplete,
    Button,
    Divider,
    FormControlLabel,
    Grid,
    Rating,
    Stack,
    Switch,
    TextField,
    Typography
} from "@mui/material";
import {Task} from "../model/Task";
import {closeTask, deleteTask, getTags, postTask, putTask} from "../model/api";
import {useNavigate} from "react-router-dom";

interface TaskFormProps {
    task: Task;
}

export default function TaskForm({task}: TaskFormProps) {
    const navigate = useNavigate();
    React.useEffect(() => getTags(setTagsList), []);
    React.useEffect(() => {
        setTitle(task?.title ?? "");
        setDescription(task?.description ?? "");
        setTags(task?.tags ?? []);
        setDone(task?.done ?? false);
        setPriority(task?.priority ?? 0);
        setReadonly(task?.finishedAt != null);
    }, [task]);

    const [tagsList, setTagsList] = useState<string[]>([]);
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [tags, setTags] = useState<string[]>([]);
    const [done, setDone] = useState<boolean>(false);
    const [priority, setPriority] = useState<number>(0);
    const [readonly, setReadonly] = useState<boolean>(false);

    const handleSubmit = (event: SyntheticEvent) => {
        event.preventDefault();
        saveTask(new Task({
            id: task?.id,
            title: title,
            description: description,
            priority: priority,
            done: done,
            finishedAt: task?.finishedAt?.toISO?.(),
            tags: tags,
            parentId: task?.parentId
        }))
    };
    function saveTask(task: Task) {
        if (isEmpty(task.id)) {
            postTask(task, (id) => openTask(id));
        } else {
            putTask(task, () => navigate(0));
        }
    }

    function removeTask(id: number | undefined) {
        if (typeof id == 'number') {
            deleteTask(id, () => navigate('/task'));
        }
    }
    function closeTaskFunc(id: number | undefined) {
        if (typeof id == 'number') {
            closeTask(id, () => navigate(0))
        }
    }
    function openTask(id: number | null | undefined) {
        //ID приходит строкой от кнопки "перейти к родительской задаче" на странице новой задачи
        //ХЗ за херня, МБ неявное преобразование типов string->object a null==object
        if ((typeof id == 'number') || (typeof id == 'string')) {
            navigate('/task/' + id);
        }
    }
    function isEmpty(element: any) {
        if (element == null) {
            return true;
        }
        return typeof element == 'undefined';
    }

    return(
        <Stack spacing={3}>
            <Stack direction="row" spacing={2}>
                <Button variant="outlined" onClick={handleSubmit}>Save</Button>
                <Button disabled={isEmpty(task?.id) || !isEmpty(task?.finishedAt)}
                        variant="outlined" onClick={() => closeTaskFunc(task?.id)}>
                    Close task
                </Button>
                <Button disabled={isEmpty(task?.id)} variant="outlined" onClick={() => removeTask(task?.id)}>
                    Delete task
                </Button>
            </Stack>

            <Grid container spacing={2}>
                <Grid item xs={3}>
                    <Stack spacing={2} sx={{width: '100%'}}>
                        <TextField
                            id="outlined-basic"
                            label="Title"
                            variant="outlined"
                            value={title}
                            disabled={readonly}
                            onChange={(e) => setTitle(e.target.value)}/>
                        <span>
                            <Typography component="legend">Priority</Typography>
                            <Rating name="customized-10"
                                    value={priority}
                                    disabled={readonly}
                                    onChange={(e, value) => setPriority(value ?? 0)}
                                    max={5}/>
                        </span>

                        <FormControlLabel
                            control={<Switch
                                disabled={readonly}
                                checked={done}
                                onChange={(e) => setDone(e.target.checked)}/>}
                            label="Done" />
                        <TextField
                            label="Finished at"
                            variant="outlined"
                            value={task?.finishedAt?.toISODate?.() ?? ""}
                            disabled/>
                        <Autocomplete
                            multiple
                            options={tagsList}
                            getOptionLabel={(option) => option}
                            value={tags}
                            freeSolo
                            filterSelectedOptions
                            renderInput={params => <TextField {...params} label="Tags" placeholder="tag"/>}
                            onChange={(e, value) => {setTags(value)}}/>
                    </Stack>
                </Grid>
                <Grid item xs={6}>
                    <TextField
                        sx={{width: '100%'}}
                        id="outlined-basic"
                        label="Description"
                        multiline
                        rows={20}
                        variant="outlined"
                        disabled={readonly}
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}/>
                </Grid>
                <Grid item xs={3}>
                    <Stack spacing={2} sx={{width: '100%'}}>
                        <Button disabled={isEmpty(task?.parentId)}
                                variant="outlined"
                                onClick={() => openTask(task?.parentId)}>
                            Parent: {task?.parent?.title}
                        </Button>
                        <Divider/>
                        {task?.children.map((child: Task) => {
                            return (
                                <Button variant="outlined" onClick={() => openTask(child?.id)} key={child?.id}>
                                    Child: {child?.title}
                                </Button>
                            )
                        })}
                        {isEmpty(task?.id) ? '' :
                            <Button variant="outlined"
                                    onClick={() => navigate('/task/new?parent=' + task.id)}>
                                Add subtask ...
                            </Button>}
                    </Stack>
                </Grid>
            </Grid>
        </Stack>
    )

}
