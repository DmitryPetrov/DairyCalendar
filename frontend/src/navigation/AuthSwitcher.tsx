import React from "react";
import LoginForm from "../forms/Login";
import {isLoggedIn} from "../model/api";
import Routes from "./Routes";

export default function AuthSwitcher() {
    const [loggedIn, setLoggedIn] = React.useState<boolean>(false);
    const onSuccessLogin = () => {
        setLoggedIn(true);
    };
    const onSuccessLogout = () => {
        setLoggedIn(false);
    };

    React.useEffect(() => {
        isLoggedIn(onSuccessLogin, onSuccessLogout)
    }, []);

    if (loggedIn) {
        return <Routes onSuccessLogout={onSuccessLogout}/>
    } else {
        return <LoginForm onSuccessLogin={onSuccessLogin}/>
    }
}