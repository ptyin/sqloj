import React, {useEffect, useState} from "react";
import {useHistory} from "react-router-dom";
import GuideTeacher from "../../components/guideTeacher";
import logo from '../../common/images/logo.png';
import '../../common/css/layout.css';
import {Badge, Button, Card, Collapse, Input, Layout, message, Select} from "antd";
import axios from "axios";
import QueueAnim from "rc-queue-anim";
import 'github-markdown-css'
import '../../common/css/otto.css'
// import Editor from "../components/editor";
import {Controlled as CodeMirror} from 'react-codemirror2';
import 'codemirror/lib/codemirror.css';
import 'codemirror/mode/sql/sql';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/addon/hint/show-hint.js';
import 'codemirror/addon/hint/sql-hint.js';
import 'codemirror/theme/solarized.css';
import BraftEditor from 'braft-editor'
import 'braft-editor/dist/index.css'


export default function UpdateQuestion()
{
    // const [timer,setTimer] = useState(0)
    const [data, setData] = useState([])
    const [databaseId, setDatabaseId] = useState('')
    const [assignmentId, setAssignmentId] = useState('')
    // const [show] = useState(true)
    const [questionId, setQuestionId] = useState('');
    const [questionName, setQuestionName] = useState('');
    const [questionDescription, setQuestionDescription] = useState('')
    const [questionOutput, setQuestionOutput] = useState('')
    const [questionType, setQuestionType] = useState('sql')
    const [code, setCode] = useState('');

    const {Header, Content, Sider} = Layout;
    const {Option} = Select;
    const {Panel} = Collapse
    const history = useHistory();
    const editorProps = {
        contentStyle: {height: 300},
        contentFormat: 'html',
        // defaultValue: '<p>Hello World!</p>',
        // onChange: this.handleChange
    }
    const id = window.sessionStorage.update_question_id
    useEffect(() =>
    {
        axios.get('/api/teacher/QuestionDetail', {
            params: {
                question_id: id
            }
        }).then((response) =>
        {
            console.log(response.data)
            setDatabaseId(response.data.db_id)
            setAssignmentId(response.data.assignment_id)
            setQuestionId(response.data.question_id)
            setQuestionName(response.data.question_name)
            setQuestionDescription(response.data.question_description)
            setQuestionOutput(response.data.question_output)
            setQuestionType(response.data.question_type)
            setCode(response.data.question_answer)
        })

        axios.get('/api/teacher/DatabaseListQuery').then((response) =>
        {
            setData(response.data)
        })
    }, [])

    return <Layout>
        <Header className="header">
            <img src={logo} style={{height: '45px'}} alt=""/>
        </Header>
        <Layout>
            <Sider width={200} className="site-layout-content"><GuideTeacher item="assignments"/></Sider>
            <Layout style={{padding: '0 24px 24px'}}>
                <Content className="default_font" style={{margin: '24px 0'}}>
                    <QueueAnim
                        key="demo"
                        type={['right', 'left']}
                        duration="2000"
                        ease={['easeOutQuart', 'easeInOutQuart']}>
                        <Card key="demo1" title="Update Question">
                            {
                                questionType === 'sql' ?
                                    [
                                        <div>
                                            <Badge status="processing" text="Corresponding Database"/>
                                        </div>,
                                        <Select
                                            showSearch
                                            style={{width: 200}}
                                            key={databaseId}
                                            defaultValue={databaseId}
                                            // placeholder={databaseId}
                                            // value={databaseId}
                                            // placeholder="Search to Select"
                                            optionFilterProp="children"
                                            filterOption={(input, option) =>
                                                option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                            }
                                            filterSort={(optionA, optionB) =>
                                                optionA.children.toLowerCase().localeCompare(optionB.children.toLowerCase())
                                            }
                                            onChange={(value) =>
                                            {
                                                setDatabaseId(value)
                                            }}
                                        >
                                            {
                                                data.map((v) => (
                                                    <Option value={v.db_id}>{v.db_name}</Option>
                                                ))
                                            }
                                        </Select>
                                    ] : null
                            }

                            <div style={{height: "10px"}}/>
                            <div>
                                <Badge status="processing" text="Question Name"/>
                            </div>
                            <Input key="name" style={{width: "200px"}} placeholder={questionName}
                                   onChange={value =>
                                   {
                                       setQuestionName(value.target.value)
                                   }}/>
                            <div style={{height: "10px"}}/>
                            <div style={{padding: '3px'}}/>
                            <Collapse key="detail" defaultActiveKey={[]}>
                                <Panel key="description" header="Question Description">
                                    <BraftEditor {...editorProps}
                                                 defaultValue={BraftEditor.createEditorState(questionDescription)}
                                                 onChange={(content) =>
                                                 {
                                                     setQuestionDescription(content.toHTML())
                                                 }}/>
                                </Panel>
                                {/*<div style={{height: "5px"}}/>*/}
                                {/*<div style={{padding: '3px'}}/>*/}
                                {/*<Panel key="output" header="Output">*/}
                                {/*    <BraftEditor {...editorProps} defaultValue={BraftEditor.createEditorState(questionOutput)} onChange={(content) =>*/}
                                {/*    {*/}
                                {/*        setQuestionOutput(content.toHTML())*/}
                                {/*    }}/>*/}
                                {/*</Panel>*/}
                                {
                                    questionType === 'sql' ?
                                        [
                                            <div style={{height: "5px"}}/>,
                                            <div style={{padding: '3px'}}/>,
                                            <Panel key="answer" header="Standard Answer">
                                                <CodeMirror
                                                    key='editor'
                                                    // defaultValue={code}
                                                    value={code}
                                                    // value='# press Ctrl to autocomplete'
                                                    onBeforeChange={(editor, data, value) => setCode(value)}
                                                    options={{
                                                        lineNumbers: true,
                                                        mode: {name: "text/x-mysql"},
                                                        extraKeys: {"Ctrl": "autocomplete"},
                                                        // autofocus: true,
                                                        styleActiveLine: true,
                                                        lineWrapping: true,
                                                        foldGutter: true,
                                                        theme: "solarized",
                                                    }}
                                                />
                                            </Panel>
                                        ] : null
                                }
                            </Collapse>
                            <div style={{height: "20px"}}/>
                            <div>
                                <Badge status="processing" text="Question Type"/>
                            </div>

                            <Select key={questionType} defaultValue={questionType} onChange={(value) =>
                            {
                                setQuestionType(value)
                            }}>
                                <Option value="sql">SQL</Option>
                                <Option value="text">Text</Option>
                            </Select>

                            <div style={{height: "20px"}}/>
                            <Button style={{width: "90px"}} type="primary" onClick={() =>
                            {
                                console.log({
                                    question_name: questionName,
                                    question_description: questionDescription,
                                    question_output: questionOutput,
                                    question_answer: code,
                                    question_type: questionType,
                                    assignment_id: assignmentId,
                                    db_id: databaseId
                                })
                                axios.patch('/api/teacher/QuestionDetail',
                                    {
                                        question_id: questionId,
                                        question_name: questionName,
                                        question_description: questionDescription,
                                        question_output: questionOutput,
                                        question_answer: code,
                                        question_type: questionType,
                                        assignment_id: assignmentId,
                                        db_id: databaseId
                                    }).then((response) =>
                                {
                                    if (response.data.success)
                                    {
                                        message.success('update successfully.');
                                        history.push('/teacherQuestions')
                                    } else
                                    {
                                        message.error('Fail to update, please retry.');
                                    }
                                })
                            }}>update</Button>
                        </Card>
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}
