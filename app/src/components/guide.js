import React, {useState} from "react";
import {Button, Checkbox, Form, Input, Menu, message, Modal} from 'antd';
import {useHistory} from "react-router-dom";
import '../common/font.css'
import {UserOutlined, SnippetsOutlined, CalendarOutlined, ToolOutlined, LogoutOutlined} from '@ant-design/icons';
import axios from "axios";


const FormItem = Form.Item;
export default function Guide(props)
{
    const {SubMenu} = Menu;
    const history = useHistory();
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');

    const singleLayout = {
        // wrapperCol: {offset: 8, span: 16},
        wrapperCol: {span: 24},
    };
    const doubleLayout = {
        labelCol: {span: 8},
        wrapperCol: {span: 16},
    };

    return (<div className="default_font">
            {/*<Menu selectedKeys={[window.sessionStorage.current]}*/}
            <Menu selectedKeys={[props.item]}
                  mode="inline"
                  style={{height: '100%', borderRight: 0}}>
                <Menu.Item key="assignments" icon={<CalendarOutlined/>} onClick={(event) =>
                {
                    // window.sessionStorage.current = event.key
                    history.push('/assignments')
                }}>
                    Assignments
                </Menu.Item>
                <Menu.Item key="records" icon={<SnippetsOutlined/>} onClick={(event) =>
                {
                    // window.sessionStorage.current = event.key
                    history.push('/records')
                }}>
                    Records
                </Menu.Item>
                <Modal title="Change Password" visible={isModalVisible}
                       onOk={() =>
                       {
                           console.log("aaaaaaaaaaa")
                           axios.defaults.withCredentials = true;
                           axios.get('/api/ChangePassword',
                               {
                                   params: {
                                       current_pw: oldPassword,
                                       new_pw: newPassword
                                   }
                               }).then((response) =>
                           {
                               if (response.data.success)
                               {
                                   message.success('Change successfully.');
                                   setIsModalVisible(false)
                               }
                               else
                               {
                                   message.error('Invalid authentication information.');
                               }
                           })
                       }}
                       onCancel={() => setIsModalVisible(false)}>
                    <Form className='change-form'
                          name="basic"
                          initialValues={{remember: true}}>
                        <FormItem {...singleLayout} style={{textAlign: "center"}}>
                            <p className="form_title">{"user: " + window.sessionStorage.username}</p>
                        </FormItem>
                        <FormItem  {...doubleLayout} label="old password">
                            <Input placeholder="Please enter your old password."
                                   maxLength={10} type={"password"}
                                   onChange={(event) =>
                                   {
                                       setOldPassword(event.target.value)
                                   }}/>
                        </FormItem>
                        <FormItem  {...doubleLayout} label="new password">
                            <Input placeholder="Please enter your new password."
                                   maxLength={10} type={"password"}
                                   onChange={(event) =>
                                   {
                                       setNewPassword(event.target.value)
                                   }}/>
                        </FormItem>
                    </Form>
                </Modal>
                <SubMenu key="user" icon={<UserOutlined/>} title="user">
                    <Menu.ItemGroup title={'The current student: ' + window.sessionStorage.username}>
                        <Menu.Item key="change_pwd" icon={<ToolOutlined/>} onClick={(event) =>
                        {
                            // window.sessionStorage.current = event.key
                            setIsModalVisible(true);
                            // history.push('/change_pwd')
                        }}>edit password</Menu.Item>
                        {/*<Menu.Item key="assignments_grade" icon={<ProfileOutlined />} onClick={(event)=>{*/}
                        {/*    info()*/}
                        {/*    window.sessionStorage.current = event.key*/}
                        {/*    history.push('/users')*/}
                        {/*}}>查看assignment分数</Menu.Item>*/}
                    </Menu.ItemGroup>
                </SubMenu>
                <Menu.Item key="out" icon={<LogoutOutlined/>} onClick={(event) =>
                {
                    // window.sessionStorage.current = 'assignments'
                    window.sessionStorage.removeItem('username')
                    axios.get('/api/logout').then((response) =>
                    {
                        if (response.data.success)
                        {
                            message.success('Log out successfully.');
                            history.push('/')
                        }
                    })
                }}>
                    Log out
                </Menu.Item>
            </Menu>
        </div>
    );
}
