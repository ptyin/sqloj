import React, {useState, useEffect, forwardRef} from "react";
import {useHistory} from "react-router-dom";
import Guide from "../components/guide";
import logo from '../common/images/logo.png';
import '../common/layout.css';
import {Layout, Card, Button, Tag, Table, message} from "antd";
import axios from "axios";
import QueueAnim from "rc-queue-anim";
// import Cookies from 'js-cookie'
import 'github-markdown-css'
import '../common/otto.css'
import './submit.css'
import CodeMirror from 'react-codemirror';
import 'codemirror/lib/codemirror.css';
import 'codemirror/mode/sql/sql';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/addon/hint/show-hint.js';
import 'codemirror/addon/hint/sql-hint.js';
import 'codemirror/theme/solarized.css';

const Submit = forwardRef((props, refSelf)=>{
    // const [leader,setLeader] = useState([])
    // const [timer,setTimer] = useState(0)
    // const [show] = useState(true)
    const [questionName,setQuestionName] = useState('');
    const [description,setDescription] = useState('');
    const [output,setOutput] = useState([]);
    const [code, setCode] = useState('# press Ctrl to autocomplete.');
    // const [mode,setMode] = useState('sqlite');
    const { Header,Content,Sider }= Layout;
    const id = window.sessionStorage.question_ID;
    // const { Option } = Select;
    const history = useHistory();

    useEffect(()=>{
        console.log(code)
        axios.get('/api/student/selectQuestionsById', {
            params:
            {
                question_id: id
            }
        }).then((response) =>
        {
            setQuestionName(response.data.question_name);
            setDescription(response.data.question_description)
            // setMode(response.data.question_sql_type)
            setOutput(response.data.question_output)
            // setTimer(now)
        })
        // axios.get('/api/user/getLeaderBoardByQid',{params:{
        //         question_id:id
        //     }}).then((response) => {
        //     setLeader(response.data.data)
        //     setTimer(now)
        // })
    }, [])
    return <Layout>
        <Header className="header">
            <img src={logo} style={{height:'45px'}} alt= "" />
        </Header>
        <Layout>
            <Sider width={200} className="site-layout-content"><Guide/></Sider>
            <Layout style={{padding:'0 24px 24px'}}>
                <Content className="default_font" style={{ height:'700px', margin: '24px 0' }}>

                    <QueueAnim
                        key="demo"
                        type={['right', 'left']}
                        duration = "2000"
                        ease={['easeOutQuart', 'easeInOutQuart']}>
                        {/*<div key="question_name">*/}
                        {/*    <Card className="info-card" title="question name">*/}
                        {/*        <div dangerouslySetInnerHTML={{ __html: questionName }} />*/}
                        {/*    </Card>*/}
                        {/*</div>*/}
                        {/*<div key="question_description">*/}
                        {/*    <Card className="info-card" title="question description">*/}
                        {/*        <div dangerouslySetInnerHTML={{ __html: description }} />*/}
                        {/*    </Card>*/}
                        {/*</div>*/}
                        {/*<div key="output">*/}
                        {/*    <Card className="info-card" title="output">*/}
                        {/*        <div dangerouslySetInnerHTML={{ __html: output }} />*/}
                        {/*    </Card>*/}
                        {/*</div>*/}
                        {/*<div key="editor">*/}
                        {/*    <CodeMirror*/}
                        {/*        ref={refEditor}*/}
                        {/*        value='# press Ctrl to autocomplete.'*/}
                        {/*        options={{*/}
                        {/*            lineNumbers: true,*/}
                        {/*            mode: {name: "text/x-mysql"},*/}
                        {/*            extraKeys: {"Ctrl": "autocomplete"},*/}
                        {/*            // autofocus: true,*/}
                        {/*            styleActiveLine: true,*/}
                        {/*            lineWrapping: true,*/}
                        {/*            foldGutter: true,*/}
                        {/*            theme: "solarized",*/}
                        {/*        }}*/}
                        {/*    />*/}
                        {/*</div>*/}
                        {[
                            // <Tag  color="geekblue">{mode}</Tag>,
                            <div key="question_name">
                                <Card className="info-card" title="question name">
                                    <div dangerouslySetInnerHTML={{ __html: questionName }} />
                                </Card>
                            </div>,
                            // <div style={{padding:'3px'}}/>,
                            <div key="question_description">
                                <Card className="info-card" title="question description">
                                    <div dangerouslySetInnerHTML={{ __html: description }} />
                                </Card>
                            </div>,
                            // <div style={{padding:'3px'}}/>,
                            <div key="output">
                                <Card className="info-card" title="output">
                                    <div dangerouslySetInnerHTML={{ __html: output }} />
                                </Card>
                            </div>,
                            // <div style={{height:'10px'}}/>,
                            // <div style={{padding:'3px'}}/>,
                            <CodeMirror
                                key='editor'
                                value='# press Ctrl to autocomplete.'
                                onChange={(value)=>setCode(value)}
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
                            />,
                            // <Select key="demo5" defaultValue="postgreSQL" style={{ width: 120,left:800 ,top:15}} onChange={(value)=>{
                            //     console.log(this.refs.editor.outputHTML)
                            //     setMode(value)
                            // }}>
                            //     <Option value="sqlite">sqlite</Option>
                            //     <Option value="postgreSQL">postgresql</Option>
                            // </Select>,
                            <div key="submit" className="button-container">
                                <Button type="primary" className='button' onClick={()=>
                                {
                                    axios.defaults.withCredentials = true;
                                    const params = new URLSearchParams();
                                    console.log(id)
                                    console.log(code)
                                    params.append('question_id', id)
                                    params.append('code', code)
                                    // console.log(Cookies.get('JSESSIONID'))
                                    axios.post('/api/student/submit',params).then((response) =>
                                    {
                                        if (response.data.success)
                                        {
                                            message.success('Submit successfully.');
                                            window.sessionStorage.current='record'
                                            console.log(window.localStorage.submitCode)
                                            history.push('/records');
                                        } else
                                        {
                                            message.error('Failed to submit.');
                                        }
                                    })
                                }}>submit</Button>
                            </div>,
                            // <div style={{height:'20px'}} />,
                            // <div>leader board</div>,
                            // <Table columns={lead_columns} key="demo2" dataSource={leader}/>,
                            // <div style={{height:'20px'}} />,
                        ]}
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
})

export default Submit;
