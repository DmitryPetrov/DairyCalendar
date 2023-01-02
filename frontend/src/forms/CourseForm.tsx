import React, {SyntheticEvent, useState} from "react";
import {Course} from "../model/Course";
import {Box, Button, Stack, TextField} from "@mui/material";

interface CourseFormProps {
    onSave: (course: Course) => void;
    onCancel: () => void;
}

export default function CourseForm({onSave, onCancel}: CourseFormProps) {

    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [tags, setTags] = useState<string[]>([]);

    const handleSubmit = (event: SyntheticEvent) => {
        event.preventDefault();
        onSave(new Course({name, description, tags}))
    };

    return(
        <Box sx={{ width: '100%' }}>
            <Stack spacing={2}>
                <TextField
                    id="outlined-basic"
                    label="Course Name"
                    variant="outlined"
                    value={name}
                    onChange={(e) => setName(e.target.value)}/>
                <TextField
                    id="outlined-multiline-flexible"
                    label="Course Description"
                    multiline
                    maxRows={4}
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}/>
                <TextField
                    id="outlined-multiline-flexible"
                    label="Tags"
                    multiline
                    maxRows={4}
                    value={tags.join(" ")}
                    onChange={(e) => setTags(e.target.value.split(" "))}/>
                <div>
                    <Button variant="contained" onClick={handleSubmit}>Save</Button>
                </div>
            </Stack>
        </Box>
    )
}
