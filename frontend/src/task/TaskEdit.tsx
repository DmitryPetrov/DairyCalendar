import React, {useState} from "react";
import {Task} from "../model/Task";
import {getTask} from "../model/api";
import {useParams} from "react-router-dom";
import TaskForm from "./TaskForm";

export default function TaskEdit() {
    const id = useParams().id;
    const [task, setTask] = useState<Task>(new Task({}));

    React.useEffect(() => {
        getTask(id ? parseInt(id) : -1, task => setTask(task))
    }, [id]);

    return <TaskForm task={task}/>
}
