import logo from "../common/images/logo.png";
import {Layout} from "antd";
import React from "react";
import NavigationBar from "../components/NavigationBar";
import {GithubOutlined} from "@ant-design/icons";
import {Redirect, Route, Switch, useLocation, useRouteMatch} from "react-router-dom";
import CourseList from "./course/CourseList";
import Guide from "../components/Guide";
import RecordList from "./record/RecordList";
import UserMenu from "../components/UserMenu";
import UserInformation from "./user/UserInformation";


export default function Dashboard(props)
{
    const location = useLocation()
    const match = useRouteMatch()
    const {Header, Content, Footer} = Layout
    let allItems = [], allSubMenus = []
    if (props.match.params.role === 'student')
    {
        allItems = ["courses", "records"]
    }
    else if(props.match.params.role === 'teacher')
    {
        allItems = ["courses", "questions", "databases"]
        allSubMenus = [
            {title: 'manage', items: ['students', '']}
        ]
    }

    return (
        <Layout style={{height: "100%"}}>
            <Header style={{backgroundColor: '#001529'}} className="header">
                <div id="logo" style={{float: "left", marginRight: '5rem'}}>
                    <a href="/"><img src={logo} style={{height: '45px'}} alt=""/></a>
                </div>
                <Guide
                    allItems={allItems}
                    allSubMenus={allSubMenus}
                    defaultSelectedKeys={['courses']}
                >
                </Guide>
                <UserMenu />

            </Header>
            <Layout>
                <Layout style={{ padding: '0 24px 0 24px' }}>
                    <NavigationBar style={{margin: '16px 0'}}/>
                    <Content style={{
                        padding: "24px",
                        height: "100%",
                        backgroundColor: "#fff",
                        overflowY: "auto"
                    }}>
                        <Switch>
                            {/*Shared*/}
                            <Route path={`/dashboard/:role(student|teacher)/information`} component={UserInformation}/>
                            <Route path="/dashboard/:role(student|teacher)/courses" component={CourseList}/>

                            {/*Teacher*/}
                            {/*<Route path="/dashboard/teacher/manage" component={CourseList}/>*/}

                            {/*Student*/}
                            <Route path="/dashboard/student/records" component={RecordList}/>

                            {/*Default fallbacks to courses*/}
                            <Route exact path="/dashboard/:role(student|teacher)">
                                <Redirect to={`/dashboard/${match.params.role}/courses`}/>
                            </Route>
                        </Switch>
                    </Content>
                    <Footer style={{ textAlign: 'center' }}>SQL OpenJudge ©2021 Created by <a target="_blank" href="//github.com/PTYin"><GithubOutlined /> PTYin</a></Footer>
                </Layout>
            </Layout>
        </Layout>
    )
}