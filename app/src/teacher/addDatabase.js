import React, {useState} from "react";
import {useHistory} from "react-router-dom";
import GuideTeacher from "../components/guideTeacher";
import logo from '../common/images/logo.png';
import '../common/layout.css';
import {Layout, Card, Input, Button, Upload, Badge} from "antd";
import axios from "axios";
import {UploadOutlined} from '@ant-design/icons';
import QueueAnim from "rc-queue-anim";
import 'github-markdown-css'


export default function AddDatabase()
{
    const {TextArea} = Input;
    // const [show] = useState(true)
    const [databaseName, setDatabaseName] = useState('')
    const [description, setDescription] = useState('')
    const [file, setFile] = useState('')
    const [fileList, setFileList] = useState([])
    const {Header, Content, Sider} = Layout;
    const history = useHistory();

    const props = {
        action: '',
        // fileList: fileList,
        beforeUpload()
        {
            return false;
        },
        onChange(info)
        {
            console.log(info)
            info.fileList = info.fileList.slice(-1)
            // setFileList(list)
            // console.log(file, list, fileList);
            // if (file.status !== 'uploading')
            // {
            //     console.log(file, fileList);
            //     setFile(file.response.data);
            // }
        },
        defaultFileList: [],
    }

    return <Layout>
        <Header className="header">
            <img src={logo} style={{height: '45px'}} alt=""/>
        </Header>
        <Layout>
            <Sider width={200} className="site-layout-content"><GuideTeacher item="databases"/></Sider>
            <Layout style={{padding: '0 24px 24px'}}>
                <Content className="default_font" style={{margin: '24px 0'}}>
                    <QueueAnim
                        key="demo"
                        type={['right', 'left']}
                        duration="2000"
                        ease={['easeOutQuart', 'easeInOutQuart']}>
                        <Card key="demo1" title="Add Database">
                            <div>
                                <Badge status="processing" text="Database Name"/>
                            </div>
                            <Input style={{width: "200px"}} placeholder="Database Name" onChange={value =>
                            {
                                setDatabaseName(value.target.value)
                            }}/>
                            <div style={{height: "20px"}}/>
                            <TextArea className="submit_text" key="demo4" rows={5}
                                      placeholder="Input topic description." onChange={value =>
                            {
                                setDescription(value.target.value)
                            }}/>
                            <div style={{height: "20px"}}/>
                            <div><Badge status="processing" text="Select the SQLite database file."/></div>
                            <Upload {...props}>
                                <Button icon={<UploadOutlined/>}>Select</Button>
                            </Upload>
                            <div style={{height: "20px"}}/>
                            <Button style={{width: "90px"}} type="primary" onClick={() =>
                            {
                                const params = new URLSearchParams();
                                params.append('database_name', databaseName)
                                params.append('database_description', description)
                                params.append('file_id', file)
                                axios.post('api/admin/files/createDatabase',
                                    {
                                        db_name: databaseName,
                                        db_description: description,

                                    })
                                history.push('/Database')
                            }}>submit</Button>
                        </Card>
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}
