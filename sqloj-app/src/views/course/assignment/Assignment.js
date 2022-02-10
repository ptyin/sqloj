import {Descriptions, PageHeader} from "antd";
import React, {useEffect, useState} from "react";
import {Link, Route, Switch, useRouteMatch} from "react-router-dom";
import QuestionList from "./questions/QuestionList";

export default function Assignment(props)
{
    const match = useRouteMatch()
    const [assignment, setAssignment] = useState(null)
    useEffect(() =>
    {
        const demo = {
            uuid: 'abc',
            name: `Assignment mock`,
            description: `This is a mock assignment`,
            createdAt: new Date().toDateString(),
            startedAt: new Date().toDateString(),
            endedAt: new Date().toDateString(),
        }
        setAssignment(demo)
    }, [])

    return (
        <Switch>
            <Route path={`${match.path}/questions`} component={QuestionList}/>
            <Route>
                <PageHeader
                    ghost={false}
                    onBack={() => window.history.back()}
                    title={match.params.assignmentUuid}
                    subTitle={assignment?.name}
                >
                    <Descriptions size="small" column={3}>
                        <Descriptions.Item key="createdAt" label="Created At">{assignment?.createdAt}</Descriptions.Item>
                        <Descriptions.Item key="startedAt" label="Started At">{assignment?.startedAt}</Descriptions.Item>
                        <Descriptions.Item key="endedAt" label="Ended At">{assignment?.endedAt}</Descriptions.Item>
                        <Descriptions.Item key="description" label="description">{assignment?.description}</Descriptions.Item>
                        <Descriptions.Item key="questions" label="questions"><Link to={`${match.url}/questions`}>questions</Link></Descriptions.Item>
                    </Descriptions>
                </PageHeader>
                <QuestionList/>
            </Route>
        </Switch>
    )
}