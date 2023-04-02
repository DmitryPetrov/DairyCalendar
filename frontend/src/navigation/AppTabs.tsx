import React from 'react';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
import TabPanel from "./TabPanel";
import CourseTable from "../calendar/CourseTable";
import CourseForm from "../forms/CourseForm";
import {Course} from "../model/Course";
import {postCourse} from "../model/api";
import LogoutForm from "../forms/Logout";
import TaskTableView from "../task/TaskTableView";

interface AppTabsProps {
    onSuccessLogout: () => void;
}

export default function AppTabs({onSuccessLogout}: AppTabsProps) {
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
                    <Tab label="Tasks" id="2" />
                    <Tab label="Logout" id="3" />
                </Tabs>
            </Box>
            <TabPanel value={value} index={0}>
                <CourseTable/>
            </TabPanel>
            <TabPanel value={value} index={1}>
                <CourseForm onSave={saveNewCourse} onCancel={() => {}}/>
            </TabPanel>
            <TabPanel value={value} index={2}>
                <TaskTableView/>
            </TabPanel>
            <TabPanel value={value} index={3}>
                <LogoutForm onSuccessLogout={onSuccessLogout}/>
            </TabPanel>
        </Box>
    );
}