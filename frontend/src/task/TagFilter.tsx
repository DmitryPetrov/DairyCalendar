import React, {useState} from "react";
import {Autocomplete, FormControlLabel, Stack, Switch, TextField} from "@mui/material";
import {getTags} from "../model/api";

interface TagFilterProps {
    onChange: (tags: string[]) => void;
    lable: string
}

export default function TagFilter(props: TagFilterProps) {

    React.useEffect(() => {getTags(setTagsList)}, []);
    const [tagsList, setTagsList] = useState<string[]>([]);
    const [tags, setTags] = useState<string[]>([]);
    const [active, setActive] = useState<boolean>(true);

    const switcher = (on: boolean) => {
        if (on) {
            setActive(true);
            props.onChange(tags)
        } else {
            setActive(false);
            props.onChange([])
        }
    }

    return (
        <Stack direction="row" spacing={2}>
            <FormControlLabel
                sx={{width: '20%'}}
                control={<Switch checked={active} size="small" onChange={(e) => switcher(e.target.checked)}/>}
                label={props.lable}/>
            <Autocomplete
                size="small"
                sx={{width: '80%'}}
                multiple
                id="tags-outlined"
                options={tagsList}
                getOptionLabel={(option) => option}
                disabled={!active}
                filterSelectedOptions
                renderInput={(params) => <TextField {...params} label="Tags" placeholder="tag"/>}
                onChange={(e, value) => {
                    setTags(value);
                    props.onChange(value);
                }}/>
        </Stack>
    );
}
