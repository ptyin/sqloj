import React from 'react'
import 'antd/dist/antd.css'
import Homepage from './views/entry/Homepage'
import {useHistory} from "react-router-dom";


export default function App()
{
    const history = useHistory();
    return <Homepage history={history}/>
}