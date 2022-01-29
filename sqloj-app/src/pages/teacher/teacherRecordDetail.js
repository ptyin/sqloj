import React, {useEffect, useState} from "react";
import {useHistory} from "react-router-dom";
import GuideTeacher from "../../components/guideTeacher";
import logo from '../../common/images/logo.png';
import '../../common/css/layout.css';
import '../../common/css/question.css'
import {Button, Card, Layout} from "antd";
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
    // const [data, setData] = useState([])
    const [username, setUsername] = useState('');
    const [code, setCode] = useState('');
    // const [output, setOutput] = useState('');
    const {Header, Content, Sider} = Layout;
    const id = window.sessionStorage.record_id;
    const history = useHistory();

    useEffect(() =>
    {
        axios.get('/api/teacher/RecordOutput', {
            params: {
                record_id: id
            }
        }).then((response) =>
        {
            console.log(response.data)
            setUsername(response.data.username);
            setCode(response.data.record_code)
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
                        {/*<div id="detail">*/}

                        <div key="username">
                            <Card className="info-card" title="Username">
                                {/*{username}*/}
                                <div dangerouslySetInnerHTML={{__html: username}}/>
                            </Card>
                        </div>
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
                        ,
                        {/*<div key="output">*/}
                        {/*    <Card className="info-card" title="Output">*/}
                        {/*        <div dangerouslySetInnerHTML={{__html: output}}/>*/}
                        {/*    </Card>*/}
                        {/*</div>*/}
                        {/*<div style={{padding:'3px'}}/>*/}
                        <div key="return" className="button-container">
                            <Button style={{width: "90px"}} type="primary" onClick={() =>
                            {
                                history.push('/questionDetail')
                            }}>return</Button>
                        </div>
                        {/*</div>*/}
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}
