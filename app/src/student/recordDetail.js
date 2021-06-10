import React, {useState, useEffect} from "react";
import {useHistory} from "react-router-dom";
import Guide from "../components/guide";
import logo from '../common/images/logo.png';
import '../common/layout.css';
import {Layout, Card, Button, message} from "antd";
import axios from "axios";
import QueueAnim from "rc-queue-anim";
import 'github-markdown-css'
import '../common/otto.css'
import 'codemirror/lib/codemirror.css';
import 'codemirror/mode/sql/sql';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/addon/hint/show-hint.js';
import 'codemirror/addon/hint/sql-hint.js';
import 'codemirror/theme/solarized.css';

export default function Submit()
{
    // const [timer,setTimer] = useState(0)
    // const [show] = useState(true)
    const [questionName, setQuestionName] = useState('');
    const [description, setDescription] = useState('');
    const [output, setOutput] = useState([]);
    const [code, setCode] = useState('');
    const {Header, Content, Sider} = Layout;
    const history = useHistory();
    useEffect(() =>
    {
        // const now = Date.now()
        axios.get('/api/student/selectQuestionById', {
            params: {
                question_id: window.sessionStorage.record_question_id
            }
        }).then((response) =>
        {
            setQuestionName(response.data.question_name)
            setDescription(response.data.question_description)
            setOutput(response.data.question_output)
        })
        axios.get('api/student/selectRecordById', {
            params: {
                record_id: window.sessionStorage.record_id
            }
        }).then((response) =>
        {
            if (response.data.record_status.toUpperCase() === 'AC')
            {
                message.success('Congrats! You completed this question');
            }
            else if (response.data.record_status.toUpperCase() === 'WA')
            {
                message.error('Please retry! Your answer is ' + response.data.record_lack +
                    ' rows missing and ' + response.data.record_err + ' rows wrong');
            }
            else if (response.data.record_status.toUpperCase() === 'TLE')
            {
                message.error('Please retry! The execution of your commands reached the time limit');
            }
            else if (response.data.record_status.toUpperCase() === 'RUNNING')
            {
                message.info('Executing...')
            }
            setCode(response.data.record_code)
        })
    }, [])
    return <Layout>
        <Header className="header">
            <img src={logo} style={{height: '45px'}} alt=""/>
        </Header>
        <Layout>
            <Sider width={200} className="site-layout-content"><Guide item="records"/></Sider>
            <Layout style={{padding: '0 24px 24px'}}>
                <Content className="default_font" style={{margin: '24px 0'}}>
                    <QueueAnim
                        key="demo"
                        type={['right', 'left']}
                        duration="2000"
                        ease={['easeOutQuart', 'easeInOutQuart']}>
                        {[
                            <div key="question_name">
                                <Card className="info-card" title="question name">
                                    <div dangerouslySetInnerHTML={{__html: questionName}}/>
                                </Card>
                            </div>,
                            <div key="question_description">
                                <Card className="info-card" title="question description">
                                    <div dangerouslySetInnerHTML={{__html: description}}/>
                                </Card>
                            </div>,
                            // <div key="output">
                            //     <Card className="info-card" title="output">
                            //         <div dangerouslySetInnerHTML={{__html: output}}/>
                            //     </Card>
                            // </div>,
                            <div key="code">
                                <Card title="submitted code">
                                    {code}
                                </Card>
                            </div>,
                            <div key="return" className="button-container">
                                <Button type="primary"
                                        className='button' onClick={() =>
                                {
                                    history.push('/records');
                                }}>return</Button>
                            </div>
                        ]}
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}
