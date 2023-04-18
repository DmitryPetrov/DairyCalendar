import React, {useState} from "react";
import {FormControlLabel, Stack, Switch, TextField,} from "@mui/material";

interface StringFilterProps {
    onChange: (value: string | null) => void;
    lable: string
    lable2: string
}

export default function StringFilter(props: StringFilterProps) {

    const [value, setValue] = useState<string>("");
    const [active, setActive] = useState<boolean>(true);

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
                sx={{width: '20%'}}
                control={<Switch checked={active} size="small" onChange={(e) => switcher(e.target.checked)}/>}
                label={props.lable}/>
            <TextField
                size="small"
                sx={{width: '80%'}}
                id="outlined-basic"
                variant="outlined"
                value={value}
                disabled={!active}
                onChange={(e) => {
                    setValue(e.target.value);
                    props.onChange(e.target.value);
                }}
                label={props.lable2}/>
        </Stack>
    );
}
