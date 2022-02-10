import React, {useEffect, useState} from "react";
import {Route, useRouteMatch} from "react-router-dom";
import 'github-markdown-css'
import {Controlled as CodeMirror} from 'react-codemirror2';
import 'codemirror/lib/codemirror.css';
import 'codemirror/mode/sql/sql';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/addon/hint/show-hint.js';
import 'codemirror/addon/hint/sql-hint.js';
import 'codemirror/theme/solarized.css';
import EaseAnim from "../../components/anim/EaseAnim";
import {Card, Descriptions, PageHeader, Tag} from "antd";
import {colorMap} from "../../config/StatusConfig";
import DataTable from "../../components/data/DataTable";

export default function Record(props)
{
    const match = useRouteMatch()
    const [question, setQuestion] = useState()
    const [record, setRecord] = useState()
    useEffect(() =>
    {
        const demoQuestion = {
            name: 'Question mock',
            type: 'SQL',
            finished: true,
            description: 'This is a mocked question',
        }
        const demoRecord = {
            uuid: '43d43ab2-0725-4dae-a581-90f2c3da45f9',
            runAt: new Date().toLocaleString().replaceAll('/', '-'),
            status: 'RUNNING',
            duration: '10s',
            code: '# This is submitted code',
            header: ['name', 'age', 'department'],
            output: [
                {
                    name: 'yxk',
                    age: 21,
                    department: 'CS'
                },
                {
                    name: 'lh',
                    age: 21,
                    department: 'MATH'
                },
            ]
        }
        const tempHeader = []
        for(let column of demoRecord.header)
            tempHeader.push({title: column, dataIndex: column, key: column, align: "center"})
        let i = 0
        for(let row of demoRecord.output)
            row.uuid = i++
        demoRecord.header = tempHeader
        setQuestion(demoQuestion)
        setRecord(demoRecord)
    }, [])
    return (
        <Route>
            <EaseAnim>
                {[
                    <div key="pageHeader">
                        <PageHeader
                            ghost={false}
                            onBack={() => window.history.back()}
                            title={match.params.recordUuid}
                            subTitle={question?.name}
                        >
                            <Descriptions size="small" column={3}>
                                <Descriptions.Item key="type" label="type">{question?.type}</Descriptions.Item>
                                <Descriptions.Item key="finished" label="finished">
                                    <Tag color={question?.finished ? 'green' : 'volcano'}>
                                        {question?.finished ? 'YES' : 'NO'}
                                    </Tag>
                                </Descriptions.Item>
                                <Descriptions.Item key="status" label="status">
                                    <Tag color={colorMap[record?.status]} key={record?.status}>
                                        {record?.status}
                                    </Tag>
                                </Descriptions.Item>
                                <Descriptions.Item key="runAt" label="run at">{record?.runAt}</Descriptions.Item>
                                <Descriptions.Item key="duration" label="duration">{record?.duration}</Descriptions.Item>
                            </Descriptions>
                        </PageHeader>
                    </div>,
                    <div key="description">
                        <Card className="info-card" title="description">
                            <div dangerouslySetInnerHTML={{__html: question?.description}}/>
                        </Card>
                    </div>,
                    <div key="code">
                        <Card className="info-card" title="code">
                            <CodeMirror
                                value={record?.code}
                                options={{
                                    readOnly: true,
                                    lineNumbers: true,
                                    mode: {name: "text/x-mysql"},
                                    lineWrapping: true,
                                    foldGutter: true,
                                    theme: "solarized",
                                }}
                            />
                        </Card>
                    </div>,
                    <div key="recordOutput">
                        <Card className="info-card" title="output">
                            <DataTable columns={record?.header} data={record?.output}/>
                        </Card>
                    </div>
                ]}
            </EaseAnim>
        </Route>
    )
}