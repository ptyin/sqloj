import React from "react";
import {Menu} from 'antd';
import {Link, useLocation, useRouteMatch} from "react-router-dom";
import {GuideIconMap} from "../config/IconConfig";


export default function Guide(props)
{
    const location = useLocation()
    const match = useRouteMatch()
    const pathSnippets = location.pathname.split('/').filter( i => i !== '');
    const menuItemStyle = {}
    const menuItemList = props.allItems.map((item) =>
        <Menu.Item key={item} style={menuItemStyle} icon={GuideIconMap[item]}>
            <Link to={`/dashboard/${match.params.role}/${item}`}>
                {item}
            </Link>
        </Menu.Item>
    )
    const subMenuList = props.allSubMenus?.map(subMenu =>
        <Menu.SubMenu key={subMenu.title} style={menuItemStyle} title={subMenu.title} icon={GuideIconMap[subMenu.title]}>
            {subMenu.items.map(item =>
                <Menu.Item key={item} style={menuItemStyle} icon={GuideIconMap[item]}>
                    <Link to={`/dashboard/${match.params.role}/${item}`}>
                        {item}
                    </Link>
                </Menu.Item>
            )}
        </Menu.SubMenu>
    )

    return (
        <Menu selectedKeys={[pathSnippets[2]]} theme='dark' mode="horizontal"
              style={
                  {
                      minHeight: "100%",
                      backgroundColor: 'initial',
                      float: 'left',
                      // position: 'relative',
                      // display: 'flex',
                      // justifyContent: 'center'
                  }
              }>
            {menuItemList}
            {subMenuList}
            {props.children}
        </Menu>
    )
}
