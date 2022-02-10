import React, {useEffect, useState} from "react";
import {Button, Space, Table} from "antd";
import {Link, Route, Switch, useLocation, useRouteMatch} from "react-router-dom";
import EaseAnim from "../../../components/anim/EaseAnim";
import {ArrowRightOutlined} from "@ant-design/icons";
import Assignment from "./Assignment";
import DataTable from "../../../components/data/DataTable";


export default function AssignmentList(props)
{
    const match = useRouteMatch()
    const location = useLocation()
    const [data, setData] = useState([])
    useEffect(() =>
    {
        const demo = []
        for(let i=0;i<40;i++)
            demo.push({
                uuid: `${i}`,
                name: `Assignment ${i}`,
                description: `Description ${i}`,
                createdAt: new Date().toDateString(),
                startedAt: new Date().toDateString(),
                endedAt: new Date().toDateString(),
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
            render: (assignment) => (
                <Space size="middle">
                    <Link to={`/dashboard/${match.params.role}/courses/${match.params.courseUuid}/assignments/${assignment.uuid}`}>
                        <Button className='button'><ArrowRightOutlined /></Button>
                    </Link>
                </Space>
            ),
        }
    ];

    return (
        <Switch>
            <Route path={`${match.path}/:assignmentUuid`} component={Assignment}/>
            <Route>
                <DataTable columns={columns} data={data}/>
            </Route>
        </Switch>
    )
}