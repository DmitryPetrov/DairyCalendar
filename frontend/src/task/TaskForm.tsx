import React, {SyntheticEvent, useState} from "react";
import {
    Autocomplete,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControlLabel,
    Rating,
    Stack,
    Switch,
    TextField,
    Typography
} from "@mui/material";
import {Task} from "../model/Task";
import {getTags} from "../model/api";

interface TaskFormProps {
    task: Task | undefined;
    parentTaskId: number | null;
    open: boolean;
    closeTask: (id: number) => void;
    onSave: (task: Task) => void;
    onBack: () => void;
    onDelete: (id: number) => void;
}

export default function TaskForm({task, parentTaskId, open, closeTask, onSave, onBack, onDelete}: TaskFormProps) {

    React.useEffect(() => {
        setTitle(task?.title ?? "");
        setDescription(task?.description ?? "");
        setTags(task?.tags ?? []);
        setDone(task?.done ?? false);
        setPriority(task?.priority ?? 0);
        setReadonly(task?.finishedAt != null);
        getTags(setTagsList)
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
        onSave(new Task({
                id: task?.id ?? undefined,
                title: title,
                description: description,
                priority: priority,
                done: done,
                finishedAt: task?.finishedAt ?? null,
                tags: tags,
                parentId: parentTaskId
        }))
        onBack();
    };

    function deleteTask(id: number | undefined) {
        if (typeof id == 'number') {
            onDelete(id)
        }
        onBack();
    }
    function closeTaskFunc(id: number | undefined) {
        if (typeof id == 'number') {
            closeTask(id)
        }
        onBack();
    }

    return(
        <Dialog open={open} onClose={onBack} fullWidth maxWidth="xl" sx={{height: '100%'}}>
            <DialogTitle align="center">{title}</DialogTitle>
            <br/>
            <DialogContent>
                <Stack direction="row" spacing={2}>
                    <Stack spacing={2} sx={{width: '30%'}}>
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
                            id="outlined-basic"
                            label="Finished at"
                            variant="outlined"
                            value={task?.finishedAt?.toISODate?.() ?? undefined}
                            onChange={() => {}}
                            disabled/>
                        <Autocomplete
                            multiple
                            id="tags-outlined"
                            options={tagsList}
                            getOptionLabel={(option) => option}
                            defaultValue={task?.tags}
                            freeSolo
                            filterSelectedOptions
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Tags"
                                    placeholder="tag"
                                />
                            )}
                            onChange={(e, value) => {setTags(value)}}/>
                    </Stack>
                    <TextField
                        sx={{width: '70%'}}
                        id="outlined-basic"
                        label="Description"
                        multiline
                        rows={35}
                        variant="outlined"
                        disabled={readonly}
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}/>
                </Stack>
            </DialogContent>
            <DialogActions>
                {(typeof task?.id === 'number') ? <Button onClick={() => deleteTask(task?.id)}>Delete task</Button> : <div></div>}
                {(typeof task?.id === 'number') ? <Button onClick={() => closeTaskFunc(task?.id)}>Close task</Button> : <div></div>}
                <Button onClick={handleSubmit}>Save</Button>
                <Button onClick={onBack}>Cancel</Button>
            </DialogActions>
        </Dialog>
    )
}
