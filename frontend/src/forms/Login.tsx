import React, {SyntheticEvent, useState} from "react";
import {Button, Container, Stack, TextField, Typography} from "@mui/material";
import {postLogin} from "../model/api";

interface LoginFormProps {
    onSuccessLogin: () => void;
}

export default function LoginForm({onSuccessLogin}: LoginFormProps) {
    const handleSubmit = (event: SyntheticEvent) => {
        event.preventDefault();
        const credentials: {username: string, password: string} = {
            username: login,
            password: password
        };
        postLogin(credentials, onSuccessLogin)
    };

    const [login, setLogin] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    return(
        <Container maxWidth="xs" className="login_page">
            <form onSubmit={handleSubmit} autoComplete="true">
                <Stack spacing={2}>
                    <Typography variant="h4" gutterBottom align={"center"}>
                        Login
                    </Typography>
                    <TextField
                        id="outlined-basic"
                        label="Username"
                        variant="outlined"
                        value={login}
                        tabIndex={0}
                        onChange={(e) => setLogin(e.target.value)}/>
                    <TextField
                        id="outlined-basic"
                        label="Password"
                        type="password"
                        autoComplete="current-password"
                        variant="outlined"
                        value={password}
                        tabIndex={1}
                        onChange={(e) => setPassword(e.target.value)}/>
                    <Button
                        variant="contained"
                        type="submit"
                        size="large"
                        tabIndex={2}>
                        Login
                    </Button>
                </Stack>
            </form>
        </Container>
    )
}
