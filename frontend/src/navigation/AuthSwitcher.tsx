import React from "react";
import LoginForm from "../forms/Login";
import AppTabs from "./AppTabs";

export default function AuthSwitcher() {
    const [loggedIn, setLoggedIn] = React.useState<boolean>(false);
    const onSuccessLogin = () => {
        setLoggedIn(true);
    };
    const onSuccessLogout = () => {
        setLoggedIn(false);
    };
    if (loggedIn) {
        return <AppTabs onSuccessLogout={onSuccessLogout}/>
    } else {
        return <LoginForm onSuccessLogin={onSuccessLogin}/>
    }
}