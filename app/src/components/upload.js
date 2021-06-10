import React from "react";
import {UploadOutlined} from '@ant-design/icons';
import {Button, Upload} from "antd";

export default class MyUpload extends React.Component
{
    state = {
        // fileList: [
        //     {
        //         uid: '-1',
        //         name: 'xxx.png',
        //         status: 'done',
        //         url: 'http://www.baidu.com/xxx.png',
        //     },
        // ],
        fileList: [],
    };


    beforeUpload = (curFile, curFileList) =>
    {
        console.log(curFile);
        return false;
    }

    handleChange = info =>
    {
        console.log(info.file.originFileObj)
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

        this.setState({fileList});
    };

    render()
    {
        const props = {
            // action: 'https://www.mocky.io/v2/5cc8019d300000980a055e76',
            action: '',
            beforeUpload: this.beforeUpload,
            onChange: this.handleChange,
            multiple: true,
        };
        return (
            <Upload {...props} fileList={this.state.fileList}>
                <Button icon={<UploadOutlined/>}>Upload</Button>
            </Upload>
        );
    }
}