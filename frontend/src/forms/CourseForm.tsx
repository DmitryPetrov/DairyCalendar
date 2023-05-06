import React, {SyntheticEvent, useState} from "react";
import {Course} from "../model/Course";
import {Box, Button, Stack, TextField} from "@mui/material";
import {postCourse} from "../model/api";

export default function CourseForm() {

    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [tags, setTags] = useState<string[]>([]);

    const handleSubmit = (event: SyntheticEvent) => {
        event.preventDefault();
        postCourse(new Course({name, description, tags}));
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
