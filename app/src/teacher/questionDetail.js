import React, {useState, useEffect} from "react";
import {useHistory} from "react-router-dom";
import GuideTeacher from "../components/guideTeacher";
import logo from '../common/images/logo.png';
import '../common/layout.css';
import '../common/question.css'
import {Layout, Card, Button} from "antd";
import axios from "axios";
import QueueAnim from "rc-queue-anim";
import 'github-markdown-css'


export default function ()
{
    // const [timer,setTimer] = useState(0)
    // const [show] = useState(true)
    const [questionName, setQuestionName] = useState('');
    const [description, setDescription] = useState('');
    const [output, setOutput] = useState('');
    const {Header, Content, Sider} = Layout;
    const id = window.sessionStorage.detail_question_id;
    const history = useHistory();
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
        })
    })
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
                        <div key="output">
                            <Card className="info-card" title="Output">
                                <div dangerouslySetInnerHTML={{__html: output}}/>
                            </Card>
                        </div>
                        {/*<div style={{padding:'3px'}}/>*/}
                        <div key="submit" className="button-container">
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
