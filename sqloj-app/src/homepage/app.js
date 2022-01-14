import React from 'react'
import 'antd/dist/antd.css'
import './app.css'
import Anime from './anime'
import {useHistory} from "react-router-dom";


export default function App()
{
    const history = useHistory();
    return <Anime history={history}/>
}