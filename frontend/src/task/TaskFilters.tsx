import React, {useState} from "react";
import {Accordion, AccordionDetails, AccordionSummary, Button, Grid, Paper, Stack, Typography} from "@mui/material";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import TagFilter from "./TagFilter";
import BooleanFilter from "./BooleanFilter";
import StringFilter from "./StringFilter";

interface TaskFiltersProps {
    onApply: (filters: {}) => void;
}

export default function TaskFilters({onApply}: TaskFiltersProps) {

    const [filters, setFilters] = useState<{}>({})
    const [expand, setExpand] = useState<boolean>(true)

    return (
            <Accordion expanded={expand} onChange={()=>{setExpand(!expand)}}>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                    aria-controls="panel1a-content"
                    id="panel1a-header">
                    <Typography variant="h6">Filters</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Grid container spacing={4}>
                        <Grid item xs={6}>
                            <Paper elevation={2} sx={{padding: '16px'}}>
                                <TagFilter lable="With tags" onChange={(value) => setFilters({...filters, tags: value})}/>
                            </Paper>
                        </Grid>
                        <Grid item xs={6}>
                            <Paper elevation={2} sx={{padding: '16px'}}>
                                <TagFilter lable="Without tags" onChange={(value) => setFilters({...filters, noTags: value})}/>
                            </Paper>
                        </Grid>
                        <Grid item xs={6}>
                            <Paper elevation={2} sx={{padding: '16px'}}>
                                <StringFilter lable="With title" lable2="Title" onChange={(value) => setFilters({...filters, title: value})}/>
                            </Paper>
                        </Grid>
                        <Grid item xs={3}>
                            <Paper elevation={2} sx={{padding: '16px'}}>
                                <BooleanFilter lable="Completed" lable2="Done" onChange={(value) => setFilters({...filters, done: value})}/>
                            </Paper>
                        </Grid>
                        <Grid item xs={3}>
                            <Paper elevation={2} sx={{padding: '16px'}}>
                                <BooleanFilter lable="Closed" lable2="Closed" onChange={(value) => setFilters({...filters, closed: value})}/>
                            </Paper>
                        </Grid>
                    </Grid>
                    <Stack spacing={3} direction="row" sx={{mt: '16px'}}>
                        <Button variant="outlined" size="small" sx={{width: '10%'}} onClick={() => onApply(filters)}>Apply</Button>
                        <Button variant="outlined" size="small" sx={{width: '10%'}} color="error" onClick={() => onApply({})}>Cancel</Button>
                    </Stack>
                </AccordionDetails>
            </Accordion>
    );
}
