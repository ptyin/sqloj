import React, {useEffect, useState} from "react";
import {useHistory} from "react-router-dom";
import GuideTeacher from "../components/guideTeacher";
import logo from '../common/images/logo.png';
import '../common/layout.css';
import '../common/question.css'
import {Button, Card, Layout, Space, Table} from "antd";
import axios from "axios";
import QueueAnim from "rc-queue-anim";
import 'github-markdown-css'
import {Controlled as CodeMirror} from "react-codemirror2";
import 'codemirror/lib/codemirror.css';
import 'codemirror/mode/sql/sql';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/addon/hint/show-hint.js';
import 'codemirror/addon/hint/sql-hint.js';
import 'codemirror/theme/solarized.css';


export default function ()
{
    // const [timer,setTimer] = useState(0)
    // const [show] = useState(true)
    const [data, setData] = useState([])
    const [questionName, setQuestionName] = useState('');
    const [description, setDescription] = useState('');
    const [output, setOutput] = useState('');
    const [answer, setAnswer] = useState('');
    const {Header, Content, Sider} = Layout;
    const id = window.sessionStorage.detail_question_id;
    const history = useHistory();
    const columns = [
        {
            title: 'Student Name',
            dataIndex: 'username',
            key: 'username',
            // render: text => <a>{text}</a>
        },
        {
            title: 'Submit Time',
            dataIndex: 'submit_time',
            key: 'submit_time'
        },
        {
            title: 'Action',
            key: 'action',
            render: (record) => (
                <Space size="middle">
                    <Button className='button' onClick={() =>
                    {
                        window.sessionStorage.record_id = record.record_id;
                        history.push("/teacherRecordDetail");
                    }}>{"detail"}</Button>
                </Space>
            ),
        }
    ]
    useEffect(() =>
    {
        axios.get('/api/teacher/QuestionDetail', {
            params: {
                question_id: id
            }
        }).then((response) =>
        {
            setQuestionName(response.data.question_name);
            setDescription(response.data.question_description)
            setOutput(response.data.question_output)
            setAnswer(response.data.question_answer)
        })

        function query_record_list()
        {
            axios.get('/api/teacher/RecordListQuery', {
                params: {
                    question_id: id
                }
            }).then((response) =>
            {
                const temp = response.data
                for (let i = 0; i < temp.length; i++)
                {
                    temp[i].key = temp[i].username;
                    // let newTime = new Date(temp[i].assignment_start_time);
                    // temp[i].assignment_start_time = strftime("%y/%m/%d %H:%M:%S", newTime)
                    // newTime = new Date(temp[i].assignment_end_time);
                    // temp[i].assignment_end_time = strftime("%y/%m/%d %H:%M:%S", newTime)
                }
                for (let k = 0; k < temp.length; k++)
                {
                    for (let j = k; j < temp.length; j++)
                    {
                        if (Date.parse(temp[k].assignment_end_time) > Date.parse(temp[j].assignment_end_time))
                        {
                            const op = temp[k];
                            temp[k] = temp[j];
                            temp[j] = op;
                        }
                    }
                }
                setData(response.data)
            })
        }

        query_record_list()
        const timer = setInterval(query_record_list, 3000)

        return () =>
        {
            clearInterval(timer)
        }
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
                        {/*<div id="detail">*/}

                        <div key="question_name">
                            <Card className="info-card" title="Question Name">
                                <div dangerouslySetInnerHTML={{__html: questionName}}/>
                            </Card>
                        </div>
                        <div key="question_description">
                            <Card className="info-card" title="Question Description">
                                <div dangerouslySetInnerHTML={{__html: description}}/>
                            </Card>
                        </div>
                        {/*<div key="output">*/}
                        {/*    <Card className="info-card" title="Output">*/}
                        {/*        <div dangerouslySetInnerHTML={{__html: output}}/>*/}
                        {/*    </Card>*/}
                        {/*</div>*/}
                        <div key="answer">
                            <Card className="info-card" title="Standard Answer">
                                <CodeMirror
                                    key='editor'
                                    value={answer}
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
                        </div>
                        <div key="Completion">
                            <Card className="info-card" title="Completion">
                                <Table columns={columns} dataSource={data}/>
                            </Card>
                        </div>
                        {/*<div style={{padding:'3px'}}/>*/}
                        <div key="return" className="button-container">
                            <Button style={{width: "90px"}} type="primary" onClick={() =>
                            {
                                history.push('/teacherQuestions')
                            }}>return</Button>
                        </div>
                        {/*</div>*/}
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}
