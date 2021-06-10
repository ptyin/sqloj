import React, {useState, useEffect} from "react";
import {useHistory} from "react-router-dom";
import GuideTeacher from "../components/guideTeacher";
import logo from '../common/images/logo.png';
import '../common/layout.css';
import {Layout, Card, Input, Button, DatePicker, Checkbox, Badge, message} from "antd";
import axios from "axios";
import QueueAnim from "rc-queue-anim";
import 'github-markdown-css'
import moment from 'moment';
import strftime from 'strftime'


export default function UpdateAssignment()
{
    // const [timer, setTimer] = useState(0)
    // const [show] = useState(true)
    const [id, setID] = useState('')
    const [name, setName] = useState('')
    const [start, setStart] = useState('');
    const [end, setEnd] = useState('');
    // const [isVisible, setIsvisible] = useState(0);
    // const [description, setDescription] = useState('');
    const {Header, Content, Sider} = Layout;
    const {RangePicker} = DatePicker;
    // const {TextArea} = Input;
    const history = useHistory();

    useEffect(() =>
    {
        // const id = window.sessionStorage.assignment_id
        setID(window.sessionStorage.assignment_id)
        setName(window.sessionStorage.assignment_name)
        setStart(window.sessionStorage.assignment_start_time)
        setEnd(window.sessionStorage.assignment_end_time)
        // window.sessionStorage.removeItem('assignment_id')
        // window.sessionStorage.removeItem('assignment_name')
        // window.sessionStorage.removeItem('assignment_start_time')
        // window.sessionStorage.removeItem('assignment_end_time')
        // setDescription(props.assignment_description)
    }, [])

    return <Layout>
        <Header className="header">
            <img src={logo} style={{height: '45px'}} alt=""/>
        </Header>
        <Layout>
            <Sider width={200}
                   className="site-layout-content">
                <GuideTeacher item={"assignments"}/>
            </Sider>
            <Layout style={{padding: '0 24px 24px'}}>
                <Content className="default_font" style={{margin: '24px 0'}}>
                    <QueueAnim
                        key="demo"
                        type={['right', 'left']}
                        duration="2000"
                        ease={['easeOutQuart', 'easeInOutQuart']}>
                        {
                            <Card key="demo1" title="Update Assignment">
                                <div><Badge status="processing" text="Assignment Name"/></div>
                                <Input style={{width: "200px"}} placeholder={name} onChange={value =>
                                {
                                    setName(value.target.value)
                                }}/>
                                {/*<div style={{height: "20px"}}/>*/}
                                <div><Badge status="processing" text="Assignment Duration"/></div>
                                <RangePicker
                                    ranges={{
                                        Today: [moment(), moment()],
                                        'This Month': [moment().startOf('month'), moment().endOf('month')],
                                    }}
                                    showTime
                                    defaultValue={[moment(start), moment(end)]}
                                    format="YYYY/MM/DD HH:mm:ss"
                                    onChange={(dateStrings) =>
                                    {
                                        if (dateStrings.length > 0)
                                        {
                                            setStart(strftime('%B %d, %Y %H:%M:%S', new Date(dateStrings[0])));
                                            setEnd(strftime('%B %d, %Y %H:%M:%S', new Date(dateStrings[1])))
                                        }
                                    }}
                                />
                                <div style={{height: "20px"}}/>
                                <Button style={{width: "90px", margin: "0 10px"}} type="primary" onClick={() =>
                                {
                                    axios.patch('api/teacher/AssignmentDetail', {
                                        assignment_id: id,
                                        assignment_name: name,
                                        assignment_start_time: start,
                                        assignment_end_time: end
                                    }).then((response) =>
                                    {
                                        if (response.data.success)
                                        {
                                            message.success('Update successfully.');
                                            history.push('/teacherAssignments')
                                        }
                                        else
                                        {
                                            message.error('Fail to update, please retry.');
                                        }
                                    })
                                }}>submit</Button>
                            </Card>
                        }
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}
