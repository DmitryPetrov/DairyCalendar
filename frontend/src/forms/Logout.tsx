import React, {SyntheticEvent} from "react";
import {Button} from "@mui/material";
import {postLogout} from "../model/api";

interface LogoutFormProps {
    onSuccessLogout: () => void;
}
export default function LogoutForm({onSuccessLogout}: LogoutFormProps) {
    const handleLogout = (event: SyntheticEvent) => {
        event.preventDefault();
        postLogout(onSuccessLogout)
    };

    return <Button variant="contained" onClick={handleLogout}>Logout</Button>
}
