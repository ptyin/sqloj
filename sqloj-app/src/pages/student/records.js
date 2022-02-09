import React, {useEffect, useState} from "react";
import {useHistory} from "react-router-dom";
import GuideStudent from "../../components/guideStudent";
import axios from 'axios';
import {Button, Layout, Space, Table, Tag} from 'antd';
import logo from '../../common/images/logo.png';
import QueueAnim from 'rc-queue-anim';
import '../../common/css/layout.css';
// import fireworks from 'react-fireworks';

export default function Records()
{
    // const [show] = useState(true)
    const {Header, Content, Sider} = Layout;
    const [data, setData] = useState([])
    const history = useHistory();
    const columns = [
        {
            title: 'Record',
            dataIndex: 'record_id',
            key: 'record_id',
        },
        {
            title: 'Time',
            dataIndex: 'record_time',
            key: 'time',
        },
        {
            title: 'Assignment',
            dataIndex: 'assignment_name',
            key: 'assignment_name'
        },
        {
            title: 'Question',
            dataIndex: 'question_name',
            key: 'question_name',
        },
        {
            title: 'Status',
            dataIndex: 'record_status',
            key: 'status',
            render: tag =>
            {
                tag = tag.toUpperCase()
                let color;
                // let color = 'geekblue';
                if (tag === 'RUNNING')
                    color = 'geekblue'
                else if (tag === 'AC')
                    color = 'green';
                else if (tag === 'WA')
                    color = 'volcano';
                else if (tag === 'TTL')
                    color = 'yellow';

                return (
                    <Tag color={color} key={tag}>
                        {tag}
                    </Tag>
                );
            }
        },
        {
            title: 'Running',
            dataIndex: 'running_time',
            key: 'running_time',
            // render: tag =>
            // {
            //     // if (tag<0) {
            //     //     tag = tag + 400;
            //     // }
            //     return (
            //         <Tag color={'geekblue'} key={tag}>
            //             {tag}
            //         </Tag>
            //     );
            // }
        },
        {
            title: 'Action',
            key: 'detail',
            render: (record) => (
                <Space size="middle">
                    <Button className='button' onClick={() =>
                    {
                        window.sessionStorage.record_id = record.record_id
                        window.sessionStorage.record_question_id = record.question_id;
                        history.push('recordDetail');
                    }}>detail</Button>
                </Space>
            ),
        }
    ];
    useEffect(() =>
    {
        // fireworks.init(document,{})
        // fireworks.start()
        function query_record_list()
        {
            axios.defaults.withCredentials = true;
            axios.get('/api/student/queryRecordList').then((response) =>
            {
                const temp = response.data
                for (let i = 0; i < temp.length; i++)
                {
                    if (temp[i].running_time === undefined || temp[i].running_time === null)
                    {
                        temp[i].running_time = 'not available'
                    }
                }
                for (let k = 0; k < temp.length; k++)
                {
                    for (let j = k; j < temp.length; j++)
                    {
                        if (new Date(temp[k].record_time) < new Date(temp[j].record_time))
                        {
                            const op = temp[k];
                            temp[k] = temp[j];
                            temp[j] = op;
                        }
                    }
                }
                // for (let i = 0; i < temp.length; i++)
                // {
                //     const newTime = temp[i].record_time;
                //     temp[i].record_time = newTime.getFullYear() + '/' + newTime.getMonth() + '/' + newTime.getDate() + ' ' + newTime.getHours() + ':' + newTime.getMinutes()
                // }
                setData(temp)
            })
        }

        query_record_list()
        const timer = setInterval(query_record_list, 500)
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
            <Sider width={200} className="site-layout-content">
                <GuideStudent item="records"/>
            </Sider>
            <Layout style={{padding: '0 24px 24px'}}>
                <Content className="default_font" style={{margin: '24px 0'}}>
                    <QueueAnim
                        key="demo"
                        type={['top', 'bottom']}
                        duration="1400"
                        ease={['easeOutQuart', 'easeInOutQuart']}>
                        <Table columns={columns} key="demo1" dataSource={data}/>
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}