import React, {useState} from "react";
import {Form, Input, Menu, message, Modal} from 'antd';
import {useHistory} from "react-router-dom";
import '../common/font.css'
import {
    CalendarOutlined,
    DatabaseOutlined,
    LogoutOutlined,
    ToolOutlined,
    UserAddOutlined,
    UserOutlined
} from '@ant-design/icons';
import axios from "axios";

const FormItem = Form.Item;
export default function GuideTeacher(props)
{
    const {SubMenu} = Menu;
    const history = useHistory();
    const [isChangePasswordVisible, setIsChangePasswordVisible] = useState(false);
    const [isAddUserVisible, setIsAddUserVisible] = useState(false);
    const [studentUsername, setStudentUsername] = useState('');
    const [studentPassword, setStudentPassword] = useState('');
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
            <Menu selectedKeys={[props.item]} mode="inline" style={{height: '100%', borderRight: 0}}>
                <Menu.Item key="assignments" icon={<CalendarOutlined/>} onClick={(event) =>
                {
                    // window.localStorage.current = event.key
                    history.push('/teacherAssignments')
                }}>
                    Assignments
                </Menu.Item>
                <Modal title="Change Password" visible={isChangePasswordVisible}
                       onOk={() =>
                       {
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
                                   setIsChangePasswordVisible(false)
                               } else
                               {
                                   message.error('Invalid authentication information.');
                               }
                           })
                       }}
                       onCancel={() => setIsChangePasswordVisible(false)}>
                    <Form className='change-form'
                          name="basic"
                          initialValues={{remember: true}}>
                        <FormItem {...singleLayout} style={{textAlign: "center"}}>
                            <p className="form_title">{"user: " + window.sessionStorage.username}</p>
                        </FormItem>
                        <FormItem  {...doubleLayout} label="old password">
                            <Input placeholder="Please enter your old password."
                                   maxLength={30} type={"password"}
                                   onChange={(event) =>
                                   {
                                       setOldPassword(event.target.value)
                                   }}/>
                        </FormItem>
                        <FormItem  {...doubleLayout} label="new password">
                            <Input placeholder="Please enter your new password."
                                   maxLength={30} type={"password"}
                                   onChange={(event) =>
                                   {
                                       setNewPassword(event.target.value)
                                   }}/>
                        </FormItem>
                    </Form>
                </Modal>
                <Modal title="Add Student" visible={isAddUserVisible}
                       onOk={() =>
                       {
                           axios.defaults.withCredentials = true;
                           axios.post('/api/register',
                               {
                                   username: studentUsername,
                                   password: studentPassword
                               }).then((response) =>
                           {
                               if (response.data.success)
                               {
                                   message.success('Add successfully.');
                                   setIsAddUserVisible(false)
                               } else
                               {
                                   message.error('Fail to add, please retry.');
                               }
                           })
                       }}
                       onCancel={() => setIsAddUserVisible(false)}>
                    <Form className='add-form'
                          name="basic"
                          initialValues={{remember: true}}>
                        <FormItem  {...doubleLayout} label="student username">
                            <Input placeholder="Please enter the student username."
                                   maxLength={10}
                                   onChange={(event) =>
                                   {
                                       setStudentUsername(event.target.value)
                                   }}/>
                        </FormItem>
                        <FormItem  {...doubleLayout} label="student password">
                            <Input placeholder="Please enter the student password."
                                   maxLength={30} type={"password"}
                                   onChange={(event) =>
                                   {
                                       setStudentPassword(event.target.value)
                                   }}/>
                        </FormItem>
                    </Form>
                </Modal>
                <SubMenu key="user" icon={<UserOutlined/>} title="user">
                    <Menu.ItemGroup title={'Current teacher: ' + window.sessionStorage.username}>
                        <Menu.Item key="change_pwd" icon={<ToolOutlined/>} onClick={(event) =>
                        {
                            setIsChangePasswordVisible(true);
                            // window.localStorage.current = event.key
                            // history.push('/change_pwd_teacher')
                        }}>edit password</Menu.Item>
                        <Menu.Item key="add_student" icon={<UserAddOutlined/>} onClick={(event) =>
                        {
                            setIsAddUserVisible(true);
                        }}>add student</Menu.Item>
                    </Menu.ItemGroup>
                </SubMenu>
                <Menu.Item key="databases" icon={<DatabaseOutlined/>} onClick={(event) =>
                {
                    // window.localStorage.current = event.key
                    history.push('/databases')
                }}>
                    Databases
                </Menu.Item>
                <Menu.Item key="out" icon={<LogoutOutlined/>} onClick={(event) =>
                {
                    // window.localStorage.current = 'assignments'
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
