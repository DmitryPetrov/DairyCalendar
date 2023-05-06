import React from "react";
import {Task} from "../model/Task";
import {useSearchParams} from "react-router-dom";
import TaskForm from "./TaskForm";

export default function TaskCreate() {
    const [searchParams, setSearchParams] = useSearchParams();
    const parent = searchParams.get("parent")
    const task = new Task({parentId: parent});
    return <TaskForm task={task}/>
}
