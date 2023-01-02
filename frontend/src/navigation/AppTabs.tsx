import React, {useState} from 'react';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
import TabPanel from "./TabPanel";
import Table from "../calendar/Table";
import CourseForm from "../forms/CourseForm";
import axios from "axios";
import {Course} from "../model/Course";
import DaysForm from "../forms/DaysForm";

export default function AppTabs() {
    const [courses, setCourses] = useState<Course[]>([])

    const [value, setValue] = React.useState(0);

    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setValue(newValue);
    };

    const url = `http://localhost:8181/course`;
    function saveNewCourse(course: Course) {
        axios
            .post(url, course.toPostPayload())
            .catch((error: TypeError) => {
                console.log('log client error ' + error);
                throw new Error(
                    'There was an error retrieving the projects. Please try again.'
                );
            });
    };

    return (
        <Box sx={{ width: '100%' }}>
            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tabs value={value} onChange={handleChange} aria-label="basic tabs example">
                    <Tab label="Calendar" id="0" />
                    <Tab label="Add Course" id="1" />
                    <Tab label="Add Day" id="2" />
                </Tabs>
            </Box>
            <TabPanel value={value} index={0}>
                <Table saveCourses={setCourses}/>
            </TabPanel>
            <TabPanel value={value} index={1}>
                <CourseForm onSave={saveNewCourse} onCancel={() => {}}/>
            </TabPanel>
            <TabPanel value={value} index={2}>
                <DaysForm courses={courses} onSave={() => {}} onCancel={() => {}}/>
            </TabPanel>
        </Box>
    );
}