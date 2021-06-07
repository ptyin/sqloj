import React from 'react';
import Guide from "../components/guide";
import Assignments from "../components/assignments";

export default function()
{
    return <Assignments action='questions' guide={Guide({item: 'assignments'})} />
}