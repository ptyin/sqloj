import React, {useState} from "react";
import {Form, Input, Menu, message, Modal} from 'antd';
import {useHistory} from "react-router-dom";
import '../common/font.css'
import {UserOutlined, CalendarOutlined, ToolOutlined, LogoutOutlined, DatabaseOutlined} from '@ant-design/icons';
import axios from "axios";

const FormItem = Form.Item;
export default function GuideTeacher(props)
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
            <Menu selectedKeys={[props.item]} mode="inline" style={{height: '100%', borderRight: 0}}>
                <Menu.Item key="assignments" icon={<CalendarOutlined/>} onClick={(event) =>
                {
                    // window.localStorage.current = event.key
                    history.push('/teacherAssignments')
                }}>
                    Assignments
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
                    <Menu.ItemGroup title={'Current teacher : ' + window.sessionStorage.username}>
                        <Menu.Item key="change_pwd" icon={<ToolOutlined/>} onClick={(event) =>
                        {
                            setIsModalVisible(true);
                            // window.localStorage.current = event.key
                            // history.push('/change_pwd_teacher')
                        }}>edit password</Menu.Item>
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
                    history.push('/')
                }}>
                    Log out
                </Menu.Item>
            </Menu>
        </div>
    );
}
