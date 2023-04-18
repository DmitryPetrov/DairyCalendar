import React, {useState} from "react";
import {Checkbox, FormControlLabel, Stack, Switch} from "@mui/material";

interface BooleanFilterProps {
    onChange: (value: boolean | null) => void;
    lable: string
    lable2: string
}

export default function BooleanFilter(props: BooleanFilterProps) {

    const [value, setValue] = useState<boolean>(false);
    const [active, setActive] = useState<boolean>(false);

    const switcher = (on: boolean) => {
        if (on) {
            setActive(true);
            props.onChange(value)
        } else {
            setActive(false);
            props.onChange(null)
        }
    }

    return (
        <Stack direction="row" spacing={2}>
            <FormControlLabel
                sx={{width: '25%'}}
                control={<Switch checked={active} size="small" onChange={(e) => switcher(e.target.checked)}/>}
                label={props.lable}/>
            <FormControlLabel
                sx={{width: '75%'}}
                disabled={!active}
                control={<Checkbox size="small"/>}
                onChange={(e, value) => {
                    setValue(value);
                    props.onChange(value);
                }}
                label={props.lable2}/>
        </Stack>
    );
}
