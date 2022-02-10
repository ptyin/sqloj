import React, {useEffect, useState} from "react";
import {useRouteMatch} from "react-router-dom";
import {Button, Card, Descriptions, PageHeader, Tag} from "antd";
import 'github-markdown-css'
import {Controlled as CodeMirror} from 'react-codemirror2';
import 'codemirror/lib/codemirror.css';
import 'codemirror/mode/sql/sql';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/addon/hint/show-hint.js';
import 'codemirror/addon/hint/sql-hint.js';
import 'codemirror/theme/solarized.css';
import EaseAnim from "../../../../components/anim/EaseAnim";
import {SaveOutlined, UploadOutlined} from "@ant-design/icons";


export default function Question()
{
    const match = useRouteMatch()
    const [question, setQuestion] = useState({})
    const [code, setCode] = useState('');

    useEffect(() =>
    {
        const demo = {
            name: 'Question mock',
            type: 'SQL',
            finished: true,
            description: 'This is a mocked question',
        }
        const demoSavedCode = '# Saved code from server'

        setQuestion(demo)
        setCode(demoSavedCode)
    }, [])

    return (
        <EaseAnim>
            {[
                <div key="pageHeader">
                    <PageHeader
                        ghost={false}
                        onBack={() => window.history.back()}
                        title={match.params.questionUuid}
                        subTitle={question?.name}
                    >
                        <Descriptions size="small" column={3}>
                            <Descriptions.Item key="type" label="type">{question?.type}</Descriptions.Item>
                            <Descriptions.Item key="finished" label="finished">
                                <Tag color={question?.finished ? 'green' : 'volcano'}>
                                    {question?.finished ? 'YES' : 'NO'}
                                </Tag>
                            </Descriptions.Item>
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
                            value={code}
                            onBeforeChange={(editor, data, value) => setCode(value)}
                            options={{
                                lineNumbers: true,
                                mode: {name: "text/x-mysql"},
                                styleActiveLine: true,
                                lineWrapping: true,
                                foldGutter: true,
                                theme: "solarized",
                            }}
                        />
                    </Card>
                </div>,
                <div key="buttons" className="button-container">
                    <Button icon={<SaveOutlined />} style={{width: "90px", margin: "0 15px"}}>save</Button>
                    <Button type="primary" icon={<UploadOutlined />} style={{width: "90px", margin: "0 15px"}}>submit</Button>
                </div>
            ]}
        </EaseAnim>
    )
}
