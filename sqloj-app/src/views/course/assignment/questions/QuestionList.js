import React, {useEffect, useState} from "react";
import {Button, Space, Tag} from "antd";
import {Link, Route, Switch, useRouteMatch} from "react-router-dom";
import {ArrowRightOutlined} from "@ant-design/icons";
import Question from "./Question";
import DataTable from "../../../../components/data/DataTable";


export default function QuestionList(props)
{
    const match = useRouteMatch()
    const [data, setData] = useState([])
    useEffect(() =>
    {
        const demo = []
        for(let i=0;i<40;i++)
            demo.push({
                uuid: `${i}`,
                name: `Question ${i}`,
                type: `SQL`,
                finished: true
            })
        setData(demo)
    }, [])

    const columns = [
        {
            title: 'question',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'type',
            dataIndex: 'type',
            key: 'type',
        },
        {
            title: 'finished',
            dataIndex: 'finished',
            key: 'finished',
            render: finished =>
            {
                const color = finished ? 'green' : 'volcano'
                return (
                    <Tag color={color} key={finished}>
                        {finished ? 'YES' : 'NO'}
                    </Tag>
                )
            }
        },
        {
            title: 'action',
            key: 'action',
            render: (question) => (
                <Space size="middle">
                    <Link to={`/dashboard/${match.params.role}/courses/${match.params.courseUuid}/assignments/${match.params.assignmentUuid}/questions/${question.uuid}`}>
                        <Button className='button'><ArrowRightOutlined /></Button>
                    </Link>
                </Space>
            ),
        }
    ];

    return (
        <Switch>
            <Route path={`${match.path}/:questionUuid`} component={Question}/>
            <Route>
                <DataTable columns={columns} data={data}/>
            </Route>
        </Switch>
    )
}