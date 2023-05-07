import React from 'react';
import {Link, Outlet} from "react-router-dom";
import {Breadcrumbs, Typography} from "@mui/material";

export default function NavigationTabs() {
    return (
        <>
            <Breadcrumbs aria-label="breadcrumb" className="navigation_panel" separator={<Typography variant="h4">/</Typography>}>
                <Link to={"/course"} className="navigation_link"><Typography variant="h4">Courses</Typography></Link>
                <Link to={"/course/new"} className="navigation_link"><Typography variant="h4">New Course</Typography></Link>
                <Link to={"/task"} className="navigation_link"><Typography variant="h4">Tasks</Typography></Link>
            </Breadcrumbs>
            <Outlet />
        </>
    );
}