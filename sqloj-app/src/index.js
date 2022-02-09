import React from 'react';
import ReactDOM from 'react-dom';
import Entry from "./views/entry";
import axios from 'axios'
import {message} from "antd";
import IRouter from "./router";

function setup(axios)
{
    axios.interceptors.response.use(function (response)
    {
        return response;
    }, function (error)
    {
        if (error.response.status === 401)
        {
            message.error('Unauthorized Access!');
            // window.location.href="/";
            // setTimeout(() =>
            // {
            // }, 3000)
            return Promise.reject(error);
        }
        // return error
        return Promise.reject(error);

    });
}

axios.defaults.crossDomain = true;
axios.defaults.withCredentials = true
setup(axios)
// ReactDOM.render(<Router/>, document.body);
ReactDOM.render(<Entry/>, document.getElementById('root'));
