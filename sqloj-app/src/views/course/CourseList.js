import React from "react";
import {Card, List} from "antd";
import {BrowserRouter, Link, Route, Switch} from "react-router-dom";
import {ArrowRightOutlined} from "@ant-design/icons";
import AssignmentList from "./assignment/AssignmentList";
import Course from "./Course";


export default function CourseList(props)
{
    const demo = []
    for(let i=0;i<40;i++)
        demo.push({
            uuid: i,
            name: `Course ${i}`,
            description: `Description ${i}`
        })

    return (
        <BrowserRouter>
            <Switch>
                {/*<Route path="/dashboard/:role/courses/:courseUuid" component={AssignmentList}/>*/}
                <Route>
                    <List
                        grid={{ gutter: 16, column: 4 }}
                        pagination={{pageSize: 12}}
                        dataSource={demo}
                        renderItem={item => (
                            <List.Item key={item.uuid}>
                                <Card title={item.name} extra={<Link to={`${item.uuid}`}><ArrowRightOutlined /></Link>}>{item.description}</Card>
                            </List.Item>
                        )}
                    />
                </Route>
            </Switch>
        </BrowserRouter>
    )
}