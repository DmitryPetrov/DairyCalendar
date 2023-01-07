import React, {SyntheticEvent, useState} from "react";
import {Box, Button, Stack, TextField} from "@mui/material";
import {postLogin} from "../model/api";

export default function LoginForm() {
    const handleSubmit = (event: SyntheticEvent) => {
        event.preventDefault();
        const credentials: {username: string, password: string} = {
            username: login,
            password: password
        };
        postLogin(credentials)
    };

    const [login, setLogin] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    return(
        <Box>
            <Stack spacing={2}>
                <TextField
                    id="outlined-basic"
                    label="Login"
                    variant="outlined"
                    value={login}
                    onChange={(e) => setLogin(e.target.value)}/>
                <TextField
                    id="outlined-basic"
                    label="Password"
                    variant="outlined"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}/>
                <div>
                    <Button variant="contained" onClick={handleSubmit}>Login</Button>
                </div>
            </Stack>
        </Box>

    )
}
