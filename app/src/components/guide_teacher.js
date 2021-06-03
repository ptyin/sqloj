import React from "react";
import { Menu} from 'antd';
import {useHistory} from "react-router-dom";
import '../style/font.css'
import { UserOutlined,CalendarOutlined,ToolOutlined,LogoutOutlined,DatabaseOutlined } from '@ant-design/icons';


export default function GuideTeacher() {
    const { SubMenu } = Menu;
    const history = useHistory();
    return (<div className="defult_font">
            <Menu selectedKeys={[window.localStorage.current]} mode="inline" style={{ height: '100%', borderRight: 0 }}>
                <Menu.Item key="assignments" icon={<CalendarOutlined />} onClick={(event)=>{
                    window.localStorage.current = event.key
                    history.push('/teacher_assignment')
                }}>
                    Assignments
                </Menu.Item>
                <SubMenu key="user" icon={<UserOutlined />} title="user">
                    <Menu.ItemGroup title={'Current teacher user'+window.localStorage.id}>
                        <Menu.Item key="change_pwd" icon={<ToolOutlined />} onClick={(event)=>{
                            window.localStorage.current = event.key
                            history.push('/change_pwd_teacher')
                        }}>edit password</Menu.Item>
                    </Menu.ItemGroup>
                </SubMenu>
                <Menu.Item key="database" icon={<DatabaseOutlined />} onClick={(event)=>{
                    window.localStorage.current = event.key
                    history.push('/database')
                }}>
                    Databases
                </Menu.Item>
                <Menu.Item key="out" icon={<LogoutOutlined />} onClick={(event)=>{
                    window.localStorage.current = 'assignments'
                    history.push('/')
                }}>
                    Log out
                </Menu.Item>
            </Menu>
        </div>
    );
}
