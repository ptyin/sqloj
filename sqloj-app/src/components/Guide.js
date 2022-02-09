import React from "react";
import {Menu} from 'antd';
import {Link, useRouteMatch} from "react-router-dom";
import {GuideIconMap} from "../config/IconConfig";


export default function Guide(props)
{
    const match = useRouteMatch()
    const menuItemList = props.allItems.map((item) =>
        <Menu.Item key={item} icon={GuideIconMap[item]} onClick={() => props.callback(item)}>
            <Link to={`/dashboard/${match.params.role}/${item}`}>{item}</Link>
        </Menu.Item>
    )

    return (
        <Menu selectedKeys={[props.selectedItem]} theme="light" mode="inline" style={{minHeight: "100%"}}>
            {menuItemList}
        </Menu>
    )
}
