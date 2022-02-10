import {Badge, Button, Descriptions, message, PageHeader} from "antd";
import AssignmentList from "./assignment/AssignmentList";
import React, {useEffect, useState} from "react";
import {LikeOutlined} from "@ant-design/icons";
import {Link, Route, Switch, useRouteMatch} from "react-router-dom";

export default function Course(props)
{
    const match = useRouteMatch()
    const [course, setCourse] = useState(null)
    useEffect(() =>
    {
        const demo = {
            uuid: 'abc',
            name: `Course mock`,
            description: `This is a mock course`,
            createdAt: new Date().toDateString(),
            startedAt: new Date().toDateString(),
            endedAt: new Date().toDateString(),
        }
        setCourse(demo)
    }, [])

    return (
        <Switch>
            <Route path={`${match.path}/assignments`} component={AssignmentList}/>
            <Route>
                <PageHeader
                    ghost={false}
                    onBack={() => window.history.back()}
                    title={match.params.courseUuid}
                    subTitle={course?.name}
                    extra={[
                        <Badge key="like" count={0}>
                            <Button onClick={() => message.warn("not developed yet.")}>
                                <LikeOutlined />
                            </Button>
                        </Badge>
                    ]}
                >
                    <Descriptions size="small" column={3}>
                        <Descriptions.Item key="createdAt" label="created at">{course?.createdAt}</Descriptions.Item>
                        <Descriptions.Item key="startedAt" label="started at">{course?.startedAt}</Descriptions.Item>
                        <Descriptions.Item key="endedAt" label="ended at">{course?.endedAt}</Descriptions.Item>
                        <Descriptions.Item key="description" label="description">{course?.description}</Descriptions.Item>
                        <Descriptions.Item key="assignments" label="assignments"><Link to={`${match.url}/assignments`}>assignments</Link></Descriptions.Item>
                    </Descriptions>
                </PageHeader>
                <AssignmentList/>
            </Route>
        </Switch>
    )
}