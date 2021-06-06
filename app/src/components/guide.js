import React from "react";
import { Menu } from 'antd';
import {useHistory} from "react-router-dom";
import '../common/font.css'
import { UserOutlined,SnippetsOutlined,CalendarOutlined,ToolOutlined,LogoutOutlined } from '@ant-design/icons';


export default function Guide(props) {
        const { SubMenu } = Menu;
    const history = useHistory();
    return (<div className="default_font">
            {/*<Menu selectedKeys={[window.sessionStorage.current]}*/}
            <Menu selectedKeys={[props.item]}
                  mode="inline"
                  style={{ height: '100%', borderRight: 0 }}>
                <Menu.Item key="assignments" icon={<CalendarOutlined />} onClick={(event)=>{
                    // window.sessionStorage.current = event.key
                    history.push('/assignments')
                }}>
                    Assignments
                </Menu.Item>
                <Menu.Item key="records" icon={<SnippetsOutlined />} onClick={(event)=>{
                    // window.sessionStorage.current = event.key
                    history.push('/records')
                }}>
                    Records
                </Menu.Item>
                <SubMenu key="user" icon={<UserOutlined />} title="user">
                    <Menu.ItemGroup title={'The current user'+window.sessionStorage.id}>
                        <Menu.Item key="change_pwd" icon={<ToolOutlined />} onClick={(event)=>{
                            // window.sessionStorage.current = event.key
                            history.push('/change_pwd')
                        }}>edit password</Menu.Item>
                        {/*<Menu.Item key="assignments_grade" icon={<ProfileOutlined />} onClick={(event)=>{*/}
                        {/*    info()*/}
                        {/*    window.sessionStorage.current = event.key*/}
                        {/*    history.push('/users')*/}
                        {/*}}>查看assignment分数</Menu.Item>*/}
                    </Menu.ItemGroup>
                </SubMenu>
                <Menu.Item key="out" icon={<LogoutOutlined />} onClick={(event)=>{
                    // window.sessionStorage.current = 'assignments'
                    history.push('/')
                }}>
                    Log out
                </Menu.Item>
            </Menu>
        </div>
        );
}
