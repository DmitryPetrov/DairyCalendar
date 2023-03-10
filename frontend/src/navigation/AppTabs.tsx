import React from 'react';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
import TabPanel from "./TabPanel";
import CourseTable from "../calendar/CourseTable";
import CourseForm from "../forms/CourseForm";
import {Course} from "../model/Course";
import {postCourse} from "../model/api";
import LoginForm from "../forms/Login";

export default function AppTabs() {
    const [value, setValue] = React.useState(0);

    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setValue(newValue);
    };

    function saveNewCourse(course: Course) {
        postCourse(course);
    }

    return (
        <Box className="main_screen">
            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tabs value={value} onChange={handleChange} aria-label="basic tabs example">
                    <Tab label="Calendar" id="0" />
                    <Tab label="Add Course" id="1" />
                    <Tab label="Login" id="2" />
                </Tabs>
            </Box>
            <TabPanel value={value} index={0}>
                <CourseTable/>
            </TabPanel>
            <TabPanel value={value} index={1}>
                <CourseForm onSave={saveNewCourse} onCancel={() => {}}/>
            </TabPanel>
            <TabPanel value={value} index={2}>
                <LoginForm/>
            </TabPanel>
        </Box>
    );
}