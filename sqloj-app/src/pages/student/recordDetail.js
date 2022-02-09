import React, {useEffect, useState} from "react";
import {useHistory} from "react-router-dom";
import GuideStudent from "../../components/guideStudent";
import logo from '../../common/images/logo.png';
import '../../common/css/layout.css';
import {Button, Card, Layout, message, Table} from "antd";
import axios from "axios";
import QueueAnim from "rc-queue-anim";
import 'github-markdown-css'
import '../../common/css/otto.css'
import 'codemirror/lib/codemirror.css';
import 'codemirror/mode/sql/sql';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/addon/hint/show-hint.js';
import 'codemirror/addon/hint/sql-hint.js';
import 'codemirror/theme/solarized.css';
import {Controlled as CodeMirror} from "react-codemirror2";

export default function Submit()
{
    // const [timer,setTimer] = useState(0)
    // const [show] = useState(true)
    const [questionName, setQuestionName] = useState('');
    const [description, setDescription] = useState('');
    const [code, setCode] = useState('');
    const [header, setHeader] = useState([]);
    const [output, setOutput] = useState([]);
    // const [output, setOutput] = useState({header:[], data:[]});

    const {Header, Content, Sider} = Layout;
    const history = useHistory();
    useEffect(() =>
    {
        // const now = Date.now()
        const questionPromise = axios.get('/api/student/selectQuestionById', {
            params: {
                question_id: window.sessionStorage.record_question_id
            }
        })
        const recordPromise = axios.get('api/student/selectRecordById', {
            params: {
                record_id: window.sessionStorage.record_id
            }
        })

        Promise.all([questionPromise, recordPromise]).then((responses) =>
        {
            const questionResponse = responses[0], recordResponse = responses[1]
            if (recordResponse.data.record_status.toUpperCase() === 'RUNNING')
            {
                message.info('Executing...')
            } else if (recordResponse.data.record_status.toUpperCase() === 'RE')
            {
                message.error('Please retry! The execution of your commands caused runtime error.');
            } else
            {
                if (recordResponse.data.record_status.toUpperCase() === 'AC')
                {
                    message.success('Congrats! You completed this question');
                } else if (recordResponse.data.record_status.toUpperCase() === 'WA')
                {
                    message.error('Please retry! Your answer is ' + recordResponse.data.record_lack +
                        ' rows missing and ' + recordResponse.data.record_err + ' rows wrong');
                } else if (recordResponse.data.record_status.toUpperCase() === 'TLE')
                {
                    message.error('Please retry! The execution of your commands reached the time limit');
                }
                recordResponse.data.record_header = eval(recordResponse.data.record_header)
                recordResponse.data.record_output = eval(recordResponse.data.record_output)
                // console.log(response.data.record_header, response.data.record_output)
                let temp_header = []
                recordResponse.data.record_header.forEach((element) =>
                {
                    temp_header.push({title: element, dataIndex: element, key: element, align: "center"})
                })
                let temp_output = []
                recordResponse.data.record_output.forEach((list, i) =>
                {
                    let temp = {key: i}
                    list.forEach((element, j) =>
                    {
                        temp[temp_header[j].title] = element
                    })
                    temp_output.push(temp)
                })
                console.log(temp_header)
                console.log(temp_output)
                // setTimeout(()=>setOutput(temp_output), 1000)

                setQuestionName(questionResponse.data.question_name)
                setDescription(questionResponse.data.question_description)
                setOutput(questionResponse.data.question_output)
                setCode(recordResponse.data.record_code)
                setHeader(temp_header)
                setOutput(temp_output)
                // setOutput({header: temp_header, data: temp_output})
            }
        })
    }, [])
    return <Layout>
        <Header className="header">
            <img src={logo} style={{height: '45px'}} alt=""/>
        </Header>
        <Layout>
            <Sider width={200} className="site-layout-content"><GuideStudent item="records"/></Sider>
            <Layout style={{padding: '0 24px 24px'}}>
                <Content className="default_font" style={{margin: '24px 0'}}>
                    <QueueAnim
                        key="demo"
                        type={['right', 'left']}
                        duration="2000"
                        ease={['easeOutQuart', 'easeInOutQuart']}>
                        <div key="question_name">
                            <Card className="info-card" title="question name">
                                <div dangerouslySetInnerHTML={{__html: questionName}}/>
                            </Card>
                        </div>
                        <div key="question_description">
                            <Card className="info-card" title="question description">
                                <div dangerouslySetInnerHTML={{__html: description}}/>
                            </Card>
                        </div>
                        {/*<div key="output">*/}
                        {/*    <Card className="info-card" title="output">*/}
                        {/*        <div dangerouslySetInnerHTML={{__html: output}}/>*/}
                        {/*    </Card>*/}
                        {/*</div>*/}
                        <div key="code">
                            <Card title="submitted code">
                                <CodeMirror
                                    key='editor'
                                    value={code}
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
                        <div key="record_output">
                            <Card className="info-card" title="Output">
                                <Table pagination={{defaultPageSize: 5, showSizeChanger: true, showQuickJumper: true}} columns={header} dataSource={output}/>
                                {/*<Table pagination={{defaultPageSize: 5, showSizeChanger: true, showQuickJumper: true}} columns={output.header} dataSource={output.data}/>*/}
                            </Card>
                        </div>
                        <div key="return" className="button-container">
                            <Button type="primary"
                                    className='button' onClick={() =>
                            {
                                history.push('/records');
                            }}>return</Button>
                        </div>
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}
