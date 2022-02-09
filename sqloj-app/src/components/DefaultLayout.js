import logo from "../common/images/logo.png";
import {Layout, Menu} from "antd";
import React from "react";
import NavigationBar from "./NavigationBar";
import {UserOutlined} from "@ant-design/icons";

/**
 *
 * @param {{"guide": JSX.Element, "navigationBar": [string], "children": JSX.Element}} props
 * @returns {JSX.Element}
 * @constructor
 */
export default function DefaultLayout(props)
{
    const {Header, Sider, Content, Footer} = Layout
    return <Layout style={{height: "100%"}}>
        <Header className="header">
            <img src={logo} style={{height: '45px', float: "left"}} alt=""/>
            <Menu theme="dark" mode="horizontal">
                <Menu.Item key="user" icon={<UserOutlined />}>change password</Menu.Item>
            </Menu>
        </Header>
        <Layout>
            <Sider style={{height: "100%", width: '300px'}}>
                {props.guide}
            </Sider>
            <Layout style={{ padding: '0 24px 0 24px' }}>
                <NavigationBar style={{margin: '16px 0'}}>
                    {props.navigationBar}
                </NavigationBar>
                <Content style={{padding: "24px", height: "100%", backgroundColor: "#fff"}}>
                    {props.children}
                </Content>
                <Footer style={{ textAlign: 'center' }}>SQL OpenJudge ©2021 Created by PTYin</Footer>
            </Layout>
        </Layout>
    </Layout>
}