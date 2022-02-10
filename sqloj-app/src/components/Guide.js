import React from "react";
import {Menu} from 'antd';
import {Link, useLocation, useRouteMatch} from "react-router-dom";
import {GuideIconMap} from "../config/IconConfig";


export default function Guide(props)
{
    const location = useLocation()
    const match = useRouteMatch()
    const pathSnippets = location.pathname.split('/').filter( i => i !== '');
    const menuItemList = props.allItems.map((item) =>
        <Menu.Item key={item} icon={GuideIconMap[item]}>
            <Link to={`/dashboard/${match.params.role}/${item}`}>{item}</Link>
        </Menu.Item>
    )

    return (
        <Menu selectedKeys={[pathSnippets[2]]} theme="light" mode="inline" style={{minHeight: "100%"}}>
            {menuItemList}
        </Menu>
    )
}
