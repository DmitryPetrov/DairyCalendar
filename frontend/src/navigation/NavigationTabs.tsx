import React from 'react';
import {Link, Outlet} from "react-router-dom";
import {Breadcrumbs, Typography} from "@mui/material";
import HorizontalRuleIcon from '@mui/icons-material/HorizontalRule';

export default function NavigationTabs() {
    return (
        <>
            <Breadcrumbs aria-label="breadcrumb" className="navigation_panel" separator={<HorizontalRuleIcon fontSize="small" />}>
                <Link to={"/course"}><Typography variant="h4">Courses</Typography></Link>
                <Link to={"/course/new"}><Typography variant="h4">New Course</Typography></Link>
                <Link to={"/task"}><Typography variant="h4">Tasks</Typography></Link>
            </Breadcrumbs>
            <Outlet />
        </>
    );
}