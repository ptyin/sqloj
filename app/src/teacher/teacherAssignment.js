import React from 'react';
import Guide from "../components/guideTeacher";
import Assignments from "../components/assignments";


export default function()
{
    return <Assignments action='addAssignment' guide={Guide({item: "assignments"})}/>
}