import React, {useEffect, useState} from "react";
import {Button, Space, Tag} from "antd";
import {Link, Route, Switch, useRouteMatch} from "react-router-dom";
import {ArrowRightOutlined} from "@ant-design/icons";
import DataTable from "../../components/data/DataTable";
import Record from "./Record";
import {colorMap} from "../../config/StatusConfig";


export default function RecordList(props)
{
    const match = useRouteMatch()
    const [data, setData] = useState([])
    useEffect(() =>
    {
        const demo = []
        for(let i=0;i<40;i++)
            demo.push({
                uuid: `${i}`,
                runAt: new Date().toLocaleString().replaceAll('/', '-'),
                courseUuid: `${i}`,
                courseName: `Course 1`,
                assignmentUuid: `${i}`,
                assignmentName: "Assignment 1",
                questionUuid: `${i}`,
                questionName: "Question 1",
                status: 'RUNNING',
                duration: '10s',
            })
        setData(demo)
    }, [])

    const columns = [
        {
            title: 'course name',
            dataIndex: 'courseName',
            key: 'courseName',
            sorter: (a, b) => a.courseName > b.courseName ? 1 : -1,
            // sortDirections: ['descend'],
        },
        {
            title: 'assignment name',
            dataIndex: 'assignmentName',
            key: 'assignmentName',
            sorter: (a, b) => a.assignmentName > b.assignmentName ? 1 : -1,
        },
        {
            title: 'question name',
            dataIndex: 'questionName',
            key: 'questionName',
            sorter: (a, b) => a.questionName > b.questionName ? 1 : -1,
        },
        {
            title: 'run at',
            dataIndex: 'runAt',
            key: 'runAt',
            sorter: (a, b) => new Date(a.runAt) - new Date(b.runAt),
            defaultSortOrder: 'descend'
        },
        {
            title: 'status',
            dataIndex: 'status',
            key: 'status',
            render: tag =>
            {
                tag = tag.toUpperCase()
                return (
                    <Tag color={colorMap[tag]} key={tag}>
                        {tag}
                    </Tag>
                );
            }
        },
        {
            title: 'duration',
            dataIndex: 'duration',
            key: 'duration',
        },
        {
            title: 'action',
            key: 'action',
            render: (record) => (
                <Space size="middle">
                    <Link to={`${match.url}/${record.uuid}`}>
                        <Button className='button'><ArrowRightOutlined /></Button>
                    </Link>
                </Space>
            )
        }
    ];

    return (
        <Switch>
            <Route path={`${match.path}/:recordUuid`} component={Record}/>
            <Route>
                <DataTable columns={columns} data={data}/>
            </Route>
        </Switch>
    )
}