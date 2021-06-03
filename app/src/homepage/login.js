import React, {useState} from 'react'
import {Form, Input, Button, message, Checkbox} from "antd";
import {useHistory} from "react-router-dom";
import axios from 'axios';
import QueueAnim from 'rc-queue-anim';
import '../common/login.css'
import './anime.css'
import '../common/font.css'
import {QuestionCircleTwoTone} from '@ant-design/icons';

const FormItem = Form.Item;
axios.defaults.withCredentials = true
export default function Login()
{
    // const [value4,setValue4] = useState('Student')
    const layout = {
        labelCol: {span: 8},
        wrapperCol: {span: 16},
    };
    const tailLayout = {
        wrapperCol: {offset: 8, span: 16},
    };
    const [show] = useState(true)
    const [id, setId] = useState('');
    const [password, setPassword] = useState('');
    const history = useHistory();
    const error = () =>
    {
        message.error('Invalid authentication information');
    };
    const success = () =>
    {
        message.success('Log in successfully');
    };
    // const options = [
    //         { label: 'Student', value: 'Student' },
    //         { label: 'Teacher', value: 'Teacher' },
    //         { label: 'Admin', value: 'Admin' },
    // ];
    return <div className="combined-wrapper">
        <div style={{height: 200}}/>
        <QueueAnim
            key="demo"
            type={['top', 'bottom']}
            duration="1400"
            ease={['easeOutQuart', 'easeInOutQuart']}>
            {show ? [
                <div className="frame" key="demo1">
                    <Form className='login-form'
                          name="basic"
                          initialValues={{remember: true}}>
                        {/*<FormItem><Tag style={{ height:"100%" }} color="purple">登录</Tag></FormItem>*/}
                        <FormItem {...tailLayout} style={{textAlign: "left", marginLeft: "75px"}}>
                            <p className="form_title">login</p>
                        </FormItem>
                        {/*<FormItem style={{marginLeft:"75px"}}>*/}
                        {/*        <Radio.Group*/}
                        {/*            options={options}*/}
                        {/*            onChange={(e)=>{*/}
                        {/*                    setValue4(e.target.value);*/}
                        {/*            }}*/}
                        {/*            value={value4}*/}
                        {/*            optionType="button"*/}
                        {/*            buttonStyle="solid"*/}
                        {/*        />*/}
                        {/*</FormItem>*/}
                        <FormItem {...layout} label="account">
                            <Input placeholder="Please enter the user name"
                                   maxLength={10}
                                   onChange={(event) =>
                                   {
                                       setId(event.target.value)
                                   }}/>
                        </FormItem>
                        <FormItem  {...layout} label="password">
                            <Input placeholder="Please enter your password." maxLength={10} type={"password"}
                                   onChange={(event) =>
                                   {
                                       setPassword(event.target.value)
                                   }}/>
                        </FormItem>
                        <FormItem  {...tailLayout} style={{textAlign: "left"}}><Checkbox>Remember me</Checkbox>
                        </FormItem>
                        <FormItem  {...tailLayout} style={{textAlign: "left"}}>
                            <Button type="primary" style={{width: "30%"}} onClick={() =>
                            {
                                login(id, password).then((response) =>
                                {
                                    console.log(response.data)
                                    if (response.data.success)
                                    {
                                        success()
                                        if (response.data.data === 'teacher')
                                        {
                                            history.push('/Teacher_assignment')
                                        } else if (response.data.data === 'admin')
                                        {
                                            history.push('/admin_chet')
                                        } else
                                            history.push('/assignments');
                                    } else
                                    {
                                        error()
                                    }
                                })
                            }}>login</Button>
                            <Button style={{left: "20%"}} title={'Forgot password'} type="dashed" shape="circle"
                                    icon={<QuestionCircleTwoTone/>} onClick={() =>
                            {
                                history.push('forget')
                            }}/>
                        </FormItem>
                    </Form>
                </div>
            ] : null}
        </QueueAnim>
    </div>
}

function login(id, password)
{
    window.localStorage.id = id;
    window.localStorage.current = 'assignments'
    const params = new URLSearchParams();
    params.append('username', id);
    params.append('password', password);
    axios.defaults.withCredentials = true;
    return axios.post('/api/login', params);
}
