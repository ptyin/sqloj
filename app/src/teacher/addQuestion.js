import React, {useEffect, useState} from "react";
import {useHistory} from "react-router-dom";
import GuideTeacher from "../components/guideTeacher";
import logo from '../common/images/logo.png';
import '../common/layout.css';
import {Layout, Card, Input, Button, Select, InputNumber, Checkbox, Badge, Collapse, message} from "antd";
import axios from "axios";
import QueueAnim from "rc-queue-anim";
import 'github-markdown-css'
import '../common/otto.css'
// import Editor from "../components/editor";
import CodeMirror from 'react-codemirror';
import 'codemirror/lib/codemirror.css';
import 'codemirror/mode/sql/sql';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/addon/hint/show-hint.js';
import 'codemirror/addon/hint/sql-hint.js';
import 'codemirror/theme/solarized.css';
import BraftEditor from 'braft-editor'
import 'braft-editor/dist/index.css'


export default function AddQuestion()
{
    // const [timer,setTimer] = useState(0)
    const [data, setData] = useState([])
    // const [show] = useState(true)
    const [databaseId, setDatabaseId] = useState('')
    const [questionName, setQuestionName] = useState('');
    const [questionDescription, setQuestionDescription] = useState('');
    const [questionOutput, setQuestionOutput] = useState('');
    const [questionType, setQuestionType] = useState('SQL')

    const [code, setCode] = useState('');
    // const [operate, setOperate] = useState('query');
    // const [isOrder, setIsOrder] = useState('')
    const {Header, Content, Sider} = Layout;
    const {Option} = Select;
    const { Panel } = Collapse;
    const history = useHistory();
    const editorProps = {
        contentStyle: {height: 300},
        contentFormat: 'html',
        // defaultValue: '<p>Hello World!</p>',
        // onChange: this.handleChange
    }
    const assignment_id = window.sessionStorage.assignment_id;

    useEffect(() =>
    {

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

                        <Card key="demo1" title="Add Question">
                            <div>
                                <Badge status="processing" text="Corresponding Database"/>
                            </div>
                            <Select
                                showSearch
                                style={{width: 200}}
                                placeholder="Search to Select"
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
                            <div style={{height: "10px"}}/>
                            <div>
                                <Badge status="processing" text="Question Name"/>
                            </div>
                            <Input style={{width: "200px"}} placeholder="Question Name" onChange={value =>
                            {
                                setQuestionName(value.target.value)
                            }}/>
                            <div style={{height: "10px"}}/>
                            <div style={{padding: '3px'}}/>

                            {/*<Collapse key="detail" defaultActiveKey={["description"]}>*/}
                            <Collapse key="detail" defaultActiveKey={[]}>
                                <Panel key="description" header="Question Description">
                                    <BraftEditor {...editorProps} onChange={(content) =>
                                    {
                                        setQuestionDescription(content.toHTML())
                                    }}/>
                                </Panel>
                                <div style={{height: "5px"}}/>
                                <div style={{padding: '3px'}}/>
                                <Panel key="output" header="Output">
                                    <BraftEditor {...editorProps} onChange={(content) =>
                                    {
                                        setQuestionOutput(content.toHTML())
                                    }}/>
                                </Panel>
                                <div style={{height: "5px"}}/>
                                <div style={{padding: '3px'}}/>
                                <Panel key="answer" header="Standard Answer">
                                    <CodeMirror
                                        key='editor'
                                        value='# press Ctrl to autocomplete'
                                        onChange={(value) => setCode(value)}
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
                            </Collapse>
                            <div style={{height:"20px"}}/>
                            <div>
                                <Badge status="processing" text="Question Type" />
                            </div>

                            <Select key="type" defaultValue="SQL"  onChange={(value)=>{
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
                                    assignment_id: assignment_id,
                                    db_id: databaseId
                                })
                                axios.post('/api/teacher/QuestionDetail',
                                {
                                    question_name: questionName,
                                    question_description: questionDescription,
                                    question_output: questionOutput,
                                    question_answer: code,
                                    question_type: questionType,
                                    assignment_id: assignment_id,
                                    db_id: databaseId
                                }).then((response) =>
                                {
                                    if (response.data.success)
                                    {
                                        message.success('Upload successfully.');
                                        history.push('/teacherQuestions')
                                    } else
                                    {
                                        message.error('Fail to upload, please retry.');
                                    }
                                })
                            }}>submit</Button>
                        </Card>
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}
