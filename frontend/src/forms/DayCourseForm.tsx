import React, {useEffect} from "react";
import {Course} from "../model/Course";
import {
    ListItem,
    ListItemText,
    ToggleButton,
    ToggleButtonGroup
} from "@mui/material";
import {Day} from "../model/Day";
import {Moment} from "moment/moment";
import moment from "moment";

interface CourseFormProps {
    date: Moment;
    course: Course;
    handleAssessment: (day: Day) => void;
    setUndefinedAssessmentToZero: boolean;
}

export default function DayCourseForm({date, course, handleAssessment, setUndefinedAssessmentToZero}: CourseFormProps) {
    const handleButtonClick = (value: number) => {
        const day = new Day({
            courseId: course.id,
            courseName: course.name,
            assessment: value,
            date: date.toDate()
        })
        handleAssessment(day)
    };

    const [alignment, setAlignment] = React.useState<number | undefined>(
        course.days.find(day => moment(day.date).isSame(date))?.assessment
    );

    useEffect(() => {
        if (setUndefinedAssessmentToZero && (alignment === undefined)) {
            setAlignment(0)
        }
    }, [setUndefinedAssessmentToZero])

    const handleAlignment = (
        event: React.MouseEvent<HTMLElement>,
        newAlignment: number,
    ) => {
        setAlignment(newAlignment);
        handleButtonClick(newAlignment)
    };

    return(
        <ListItem>
            <ListItemText primary={course.name}/>
            <ToggleButtonGroup
                value={alignment}
                //defaultValue={course.days.find(day => moment(day.date).isSame(date))?.assessment}
                exclusive
                onChange={handleAlignment}
                size="small"
            >
                <ToggleButton value={0}>0</ToggleButton>
                <ToggleButton value={1}>1</ToggleButton>
                <ToggleButton value={2}>2</ToggleButton>
                <ToggleButton value={3}>3</ToggleButton>
                <ToggleButton value={4}>4</ToggleButton>
                <ToggleButton value={5}>5</ToggleButton>
                <ToggleButton value={6}>6</ToggleButton>
                <ToggleButton value={7}>7</ToggleButton>
                <ToggleButton value={8}>8</ToggleButton>
                <ToggleButton value={9}>9</ToggleButton>
            </ToggleButtonGroup>
        </ListItem>

    )
}
