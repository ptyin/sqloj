import React from "react";
import {Menu} from 'antd';
import {useHistory} from "react-router-dom";
import '../style/font.css'
import {CalendarOutlined, DatabaseOutlined, LogoutOutlined} from '@ant-design/icons';


export default function GuideTeacher()
{
    const history = useHistory();
    return (<div className="defult_font">
            <Menu selectedKeys={[window.localStorage.current]} mode="inline" style={{height: '100%', borderRight: 0}}>
                <Menu.Item key="admin_users" icon={<CalendarOutlined/>} onClick={(event) =>
                {
                    window.localStorage.current = event.key
                    history.push('/admin_users')
                }}>
                    users
                </Menu.Item>
                <Menu.Item key="admin_chet" icon={<DatabaseOutlined/>} onClick={(event) =>
                {
                    window.localStorage.current = event.key
                    history.push('/admin_chet')
                }}>
                    chet
                </Menu.Item>
                <Menu.Item key="out" icon={<LogoutOutlined/>} onClick={(event) =>
                {
                    window.localStorage.current = 'assignments'
                    history.push('/')
                }}>
                    Log out
                </Menu.Item>
            </Menu>
        </div>
    );
}
