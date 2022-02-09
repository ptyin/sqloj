import logo from "../common/images/logo.png";
import {Layout, Menu} from "antd";
import React, {useState} from "react";
import NavigationBar from "../components/NavigationBar";
import {ToolOutlined, UserOutlined} from "@ant-design/icons";
import {BrowserRouter, Redirect, Route, Switch, useLocation, useRouteMatch} from "react-router-dom";
import CourseList from "./course/CourseList";
import StudentGuide from "../components/StudentGuide";
import TeacherGuide from "../components/TeacherGuide";
import Guide from "../components/Guide";


export default function Dashboard(props)
{
    const match = useRouteMatch()
    const [navigation, setNavigation] = useState(['courses'])
    const {Header, Sider, Content, Footer} = Layout

    function handleGuideChange(item)
    {
        console.log(props.history)
        setNavigation([item])
        // console.log(item)
    }

    return <Layout style={{height: "100%"}}>
        <Header className="header">
            <div id="logo" style={{float: "left"}}>
                <img src={logo} style={{height: '45px'}} alt=""/>
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
                        selectedItem={navigation[0]}
                        callback={handleGuideChange}
                    />
                }
            </Sider>
            <Layout style={{ padding: '0 24px 0 24px' }}>
                <BrowserRouter>
                    <NavigationBar style={{margin: '16px 0'}}/>
                    <Content style={{padding: "24px", height: "100%", backgroundColor: "#fff"}}>
                            <Switch>
                                <Route path="/dashboard/:role/courses" component={CourseList}/>
                                {/*Default fallbacks to courses*/}
                                <Route path="/dashboard/:role">
                                    <Redirect to={`/dashboard/${match.params.role}/courses`}/>
                                </Route>
                            </Switch>
                    </Content>
                </BrowserRouter>
                <Footer style={{ textAlign: 'center' }}>SQL OpenJudge ©2021 Created by PTYin</Footer>
            </Layout>
        </Layout>
    </Layout>
}