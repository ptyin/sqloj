import React, {useEffect, useState} from 'react'
import {Button, Checkbox, Form, Input, message} from "antd";
import {useHistory} from "react-router-dom";
import axios from 'axios';
import './LoginPanel.css'
import '../../components/anim/HomepageAnim.css'
import '../../common/css/font.css'
import EaseAnim from "../../components/anim/EaseAnim";
import {login} from "../../feed/authentication";

const FormItem = Form.Item;
axios.defaults.withCredentials = true
export default function LoginPanel()
{
    const doubleLayout = {
        labelCol: {span: 8},
        wrapperCol: {span: 16},
    };
    const singleLayout = {
        wrapperCol: {span: 24},
    };
    const tailLayout = {
        wrapperCol: {offset: 8, span: 16},
    };
    // const [show] = useState(true)
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [remember, setRemember] = useState(false);
    const history = useHistory();

    useEffect(() =>
    {
        setRemember(window.localStorage.remember === 'true')
        if (window.localStorage.remember === 'true')
        {
            setUsername(window.localStorage.username)
            setPassword(window.localStorage.password)
            // refCheckBox.current.state.checked = true
        } else
        {
            window.localStorage.removeItem("username")
            window.localStorage.removeItem("password")
        }
    }, [])

    // noinspection JSValidateTypes
    return <div className="combined-wrapper">
        {/*<div style={{height: 200}}/>*/}
        <EaseAnim>
            <div className="frame" key="demo1">
                <Form className='login-form'
                      name="basic"
                      initialValues={{remember: true}}>
                    <FormItem {...singleLayout} style={{textAlign: "center"}}>
                        <p className="form_title">login</p>
                    </FormItem>
                    <FormItem {...doubleLayout} label="username">
                        <Input placeholder="Please enter the user name"
                               maxLength={10}
                               defaultValue={username}
                               onChange={(event) =>
                               {
                                   setUsername(event.target.value)
                               }}/>
                    </FormItem>
                    <FormItem  {...doubleLayout} label="password">
                        <Input placeholder="Please enter your password."
                               defaultValue={password}
                               maxLength={30} type={"password"}
                               onChange={(event) =>
                               {
                                   setPassword(event.target.value)
                               }}/>
                    </FormItem>
                    <FormItem  {...tailLayout} style={{textAlign: "left"}}>
                        <Checkbox defaultChecked={remember} onChange={(e) =>
                        {
                            setRemember(e.target.checked)
                        }}>Remember me</Checkbox>
                    </FormItem>
                    <FormItem  {...singleLayout} style={{textAlign: "center"}}>
                        <Button type="primary" style={{width: "40%"}} onClick={
                            function ()
                            {
                                login(username, password, remember).then((response) =>
                                {
                                    if (response.data.success === true)
                                    {
                                        message.success('Log in successfully.');
                                        if (response.data.data === 'TEACHER')
                                            history.push('/dashboard/teacher')
                                        else if (response.data.data === 'STUDENT')
                                            history.push('/dashboard/student');
                                    }
                                    else
                                        message.error('Invalid authentication information.');
                                })
                            }
                        }>OK</Button>
                    </FormItem>
                </Form>
            </div>
        </EaseAnim>
    </div>
}
