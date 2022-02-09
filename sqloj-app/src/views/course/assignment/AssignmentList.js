import React, {useEffect, useState} from "react";
import {Button, Card, List, Space, Table} from "antd";
import {BrowserRouter, Link, Route, Switch} from "react-router-dom";
import EaseAnim from "../../../components/anim/EaseAnim";


export default function AssignmentList(props)
{
    const [data, setData] = useState([])
    useEffect(() =>
    {
        const demo = []
        for(let i=0;i<40;i++)
            demo.push({
                uuid: i,
                name: `Assignment ${i}`,
                description: `Description ${i}`,
                startedAt: Date.now(),
                endedAt: Date.now()
            })
        setData(demo)
    }, [])

    const columns = [
        {
            title: 'assignment',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'start',
            dataIndex: 'startedAt',
            key: 'start',
        },
        {
            title: 'end',
            dataIndex: 'endedAt',
            key: 'deadline',
        },
        {
            title: 'action',
            key: 'action',
            render: (record) => (
                <Space size="middle">
                    <Link to={record.uuid}>
                        <Button className='button'>{"questions"}</Button>
                    </Link>
                </Space>
            ),
        }
    ];

    return (
        <BrowserRouter>
            <Switch>
                <Route path="/dashboard/:role/courses/:courseUuid/assignments/:assignmentUuid"/>
                <Route>
                    <EaseAnim>
                        <div key="assignments">
                            <Table columns={columns} dataSource={data} pagination={{pageSize: 5}}/>
                        </div>
                    </EaseAnim>
                </Route>
            </Switch>
        </BrowserRouter>
    )
}