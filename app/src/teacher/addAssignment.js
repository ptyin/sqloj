import React, {useState} from "react";
import {useHistory} from "react-router-dom";
import GuideTeacher from "../components/guideTeacher";
import logo from '../common/images/logo.png';
import '../common/layout.css';
import {Layout, Card, Input, Button, DatePicker, Checkbox, Badge, message} from "antd";
import axios from "axios";
import QueueAnim from "rc-queue-anim";
import 'github-markdown-css'
import moment from 'moment';
import strftime from "strftime";


export default function Submit()
{
    // const [show] = useState(true)
    const [name, setName] = useState('')
    const [start, setStart] = useState('');
    const [end, setEnd] = useState('');
    // const [isVisible,setIsvisible] = useState(0);
    // const [description,setDescription] = useState('');
    const {Header, Content, Sider} = Layout;
    const {RangePicker} = DatePicker;
    // const {TextArea} = Input;
    const history = useHistory();
    return <Layout>
        <Header className="header">
            <img src={logo} style={{height: '45px'}} alt=""/>
        </Header>
        <Layout>
            <Sider width={200}
                   className="site-layout-content">
                <GuideTeacher item="assignments"/>
            </Sider>
            <Layout style={{padding: '0 24px 24px'}}>
                <Content className="default_font" style={{margin: '24px 0'}}>
                    <QueueAnim
                        key="demo"
                        type={['right', 'left']}
                        duration="2000"
                        ease={['easeOutQuart', 'easeInOutQuart']}>
                        {[
                            <Card key="demo1" title="Add Assignment">
                                <div><Badge status="processing" text="Assignment Name"/></div>
                                <Input style={{width: "200px"}} placeholder="Assignment Name" onChange={value =>
                                {
                                    setName(value.target.value)
                                }}/>
                                <div style={{height: "20px"}}/>
                                <div><Badge status="processing" text="Assignment Duration"/></div>
                                <RangePicker
                                    ranges={{
                                        Today: [moment(), moment()],
                                        'This Month': [moment().startOf('month'), moment().endOf('month')],
                                    }}
                                    showTime
                                    format="YYYY/MM/DD HH:mm:ss"
                                    onChange={(dateStrings) =>
                                    {
                                        if (dateStrings != null)
                                        {
                                            if (dateStrings.length > 0)
                                            {

                                                setStart(strftime('%B %d, %Y %H:%M:%S', new Date(dateStrings[0])));
                                                setEnd(strftime('%B %d, %Y %H:%M:%S', new Date(dateStrings[1])))
                                                // setStart(1000 * dateStrings[0].unix());
                                                // setEnd(1000 * dateStrings[1].unix())
                                            }
                                        }
                                    }}
                                />
                                {/*<div style={{height: "20px"}}/>*/}
                                {/*<TextArea className="submit_text" key="demo4" rows={5}*/}
                                {/*          placeholder="Input topic description" onChange={value =>*/}
                                {/*{*/}
                                {/*    setDescription(value.target.value)*/}
                                {/*}}/>*/}
                                {/*<div style={{height: "20px"}}/>*/}
                                {/*<Checkbox onChange={e =>*/}
                                {/*{*/}
                                {/*    if (e.target.checked)*/}
                                {/*    {*/}
                                {/*        setIsvisible(1)*/}
                                {/*    } else*/}
                                {/*        setIsvisible(0)*/}
                                {/*}}>is visible</Checkbox>*/}
                                {/*<div style={{height: "20px"}}/>*/}
                                <Button style={{width: "90px", margin: "0 10px"}} type="primary" onClick={() =>
                                {
                                    axios.post('api/teacher/AssignmentDetail', {
                                        assignment_name: name,
                                        assignment_start_time: start,
                                        assignment_end_time: end,
                                    }).then((response) =>
                                    {
                                        if (response.data.success)
                                        {
                                            message.success('Add successfully.');
                                            history.push('/teacherAssignments')
                                        }
                                        else
                                        {
                                            message.error('Fail to add, please retry.');
                                        }
                                    })
                                }}>submit</Button>
                            </Card>
                        ]}
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}
