import React, {useState} from "react";
import {useHistory} from "react-router-dom";
import GuideTeacher from "../components/guideTeacher";
import logo from '../common/images/logo.png';
import '../common/layout.css';
import {Badge, Button, Card, Input, Layout, message, Upload} from "antd";
import axios from "axios";
import {UploadOutlined} from '@ant-design/icons';
import QueueAnim from "rc-queue-anim";
import 'github-markdown-css'
// import CustomUpload from '../components/upload'


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
        // action: 'https://www.mocky.io/v2/5cc8019d300000980a055e76',
        beforeUpload: (curFile, curFileList) =>
        {
            console.log(curFile);
            setFile(curFile);
            return false;
        },
        onChange: (info) =>
        {
            let fileList = [...info.fileList];

            // 1. Limit the number of uploaded files
            // Only to show two recent uploaded files, and old ones will be replaced by the new
            fileList = fileList.slice(-1);

            // 2. Read from response and show file link
            fileList = fileList.map(file =>
            {
                if (file.response)
                {
                    // Component will show file.url as link
                    file.url = file.response.url;
                }
                return file;
            });

            setFileList(fileList)
        },
        fileList: fileList
    };

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
                            {/*<form id="upload" encType="multipart/form-data" method="post">*/}
                            {/*    <input type="file" name="Select"/>*/}
                            {/*    <input type="button" value="提交" onClick={(event) =>*/}
                            {/*    {*/}
                            {/*        const upload = document.getElementById('upload')*/}
                            {/*        console.log(upload)*/}
                            {/*        const form = new FormData(upload)*/}
                            {/*        console.log(form.get('Select'))*/}
                            {/*    }}/>*/}
                            {/*</form>*/}
                            <Upload {...props}>
                                <Button icon={<UploadOutlined/>}>Select</Button>
                            </Upload>
                            <div style={{height: "20px"}}/>
                            <Button style={{width: "90px"}} type="primary" onClick={() =>
                            {
                                const form = new FormData()
                                form.append("db_name", databaseName)
                                form.append("db_description", description)
                                form.append("file", file)
                                axios.post('api/teacher/DatabaseDetail', form).then((response) =>
                                {
                                    if (response.data.success)
                                    {
                                        message.success('Add successfully.');
                                        history.push('/databases')
                                    } else
                                    {
                                        message.error('Fail to add, please retry.');
                                    }
                                })
                            }}>submit</Button>
                        </Card>
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}
