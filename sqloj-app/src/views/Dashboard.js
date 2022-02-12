import logo from "../common/images/logo.png";
import {Layout, Menu} from "antd";
import React from "react";
import NavigationBar from "../components/NavigationBar";
import {ToolOutlined, UserOutlined} from "@ant-design/icons";
import {Redirect, Route, Switch, useRouteMatch} from "react-router-dom";
import CourseList from "./course/CourseList";
import Guide from "../components/Guide";
import RecordList from "./record/RecordList";


export default function Dashboard(props)
{
    const match = useRouteMatch()
    const {Header, Sider, Content, Footer} = Layout

    return (
        <Layout style={{height: "100%"}}>
            <Header className="header">
                <div id="logo" style={{float: "left"}}>
                    <a href="/"><img src={logo} style={{height: '45px'}} alt=""/></a>
                </div>
                <Menu theme="dark" mode="horizontal">
                    <Menu.SubMenu key="user" icon={<UserOutlined />} title="user" style={{float: "right"}}>
                        <Menu.ItemGroup key="username" title={`username: ${window.sessionStorage.username}`}>
                            <Menu.Item key="change" icon={<ToolOutlined/>}>
                                change password
                            </Menu.Item>
                        </Menu.ItemGroup>
                    </Menu.SubMenu>
                </Menu>
            </Header>
            <Layout>
                <Sider style={{height: "100%", width: '300px'}}>
                    {
                        <Guide
                            allItems={props.match.params.role === 'student' ? ["courses", "records"] : ["courses"]}
                            defaultSelectedKeys={['courses']}
                        />
                    }
                </Sider>
                <Layout style={{ padding: '0 24px 0 24px' }}>
                    <NavigationBar style={{margin: '16px 0'}}/>
                    <Content style={{
                        padding: "24px",
                        height: "100%",
                        backgroundColor: "#fff",
                        overflowY: "auto"
                    }}>
                        <Switch>
                            <Route path="/dashboard/:role/courses" component={CourseList}/>
                            <Route path="/dashboard/:role/records" component={RecordList}/>
                            {/*Default fallbacks to courses*/}
                            <Route exact path="/dashboard/:role">
                                <Redirect to={`/dashboard/${match.params.role}/courses`}/>
                            </Route>
                        </Switch>
                    </Content>
                    <Footer style={{ textAlign: 'center' }}>SQL OpenJudge ©2021 Created by <a target="_blank" href="//github.com/PTYin">PTYin</a></Footer>
                </Layout>
            </Layout>
        </Layout>
    )
}