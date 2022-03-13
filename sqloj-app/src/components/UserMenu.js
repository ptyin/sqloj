import {Menu} from "antd";
import {InfoCircleOutlined, SettingOutlined, UserOutlined} from "@ant-design/icons";
import React, {useState} from "react";
import ChangePasswordPop from "./pop/ChangePasswordPop";
import {Link, useRouteMatch} from "react-router-dom";


export default function UserMenu()
{
    const match = useRouteMatch()
    const matchInformation = useRouteMatch('/dashboard/:role(student|teacher)/information')
    const [changePasswordVisible, setChangePasswordVisible] = useState(false)
    return (
        <Menu theme='dark' mode='horizontal'
              style={{backgroundColor: 'initial'}}
              selectedKeys={matchInformation?.isExact ? ['info'] : []}
              onClick={({item, key}) =>
              {
                  if (key === 'password')
                  {
                      setChangePasswordVisible(true)
                  }
              }}
        >
            <Menu.SubMenu key="user" icon={<UserOutlined />} title="user" style={{float: "right"}}>
                <Menu.ItemGroup key="username" title={`username: ${window.sessionStorage.username}`}>
                    <Menu.Item key="password" icon={<SettingOutlined/>}>
                        password
                    </Menu.Item>
                    <Menu.Item key="info" icon={<InfoCircleOutlined/>}>
                        <Link to={`/dashboard/${match.params.role}/information`}>
                            information
                        </Link>
                    </Menu.Item>
                </Menu.ItemGroup>
            </Menu.SubMenu>

            <ChangePasswordPop visible={changePasswordVisible} setVisible={setChangePasswordVisible}/>
        </Menu>
    )
}