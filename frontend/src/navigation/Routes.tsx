import React from 'react';
import CourseTable from "../calendar/CourseTable";
import CourseForm from "../forms/CourseForm";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import CourseViewPage from "../calendar/CourseViewPage";
import NavigationTabs from "./NavigationTabs";
import TaskTableView from "../task/TaskTableView";

interface AppTabsProps {
    onSuccessLogout: () => void;
}

export default function Routes({onSuccessLogout}: AppTabsProps) {

    const router = createBrowserRouter([{
        element: <NavigationTabs />,
        children: [
            {
                path: "/",
                element: <CourseTable/>,
            },
            {
                path: "course",
                element: <CourseTable/>
            },
            {
                path: "course/new",
                element: <CourseForm/>
            },
            {
                path: "course/:id",
                element: <CourseViewPage/>
            },
            {
                path: "task",
                element: <TaskTableView/>
            }
        ]
    }]);

    return <RouterProvider router={router} />
}