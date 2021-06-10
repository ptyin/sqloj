import React from "react";
import {Menu} from 'antd';
import {useHistory} from "react-router-dom";
import '../common/font.css'
import {UserOutlined, CalendarOutlined, ToolOutlined, LogoutOutlined, DatabaseOutlined} from '@ant-design/icons';


export default function GuideTeacher(props)
{
    const {SubMenu} = Menu;
    const history = useHistory();
    return (<div className="default_font">
            <Menu selectedKeys={[props.item]} mode="inline" style={{height: '100%', borderRight: 0}}>
                <Menu.Item key="assignments" icon={<CalendarOutlined/>} onClick={(event) =>
                {
                    // window.localStorage.current = event.key
                    history.push('/teacherAssignments')
                }}>
                    Assignments
                </Menu.Item>
                <SubMenu key="user" icon={<UserOutlined/>} title="user">
                    <Menu.ItemGroup title={'Current teacher : ' + window.sessionStorage.username}>
                        <Menu.Item key="change_pwd" icon={<ToolOutlined/>} onClick={(event) =>
                        {
                            // window.localStorage.current = event.key
                            history.push('/change_pwd_teacher')
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
