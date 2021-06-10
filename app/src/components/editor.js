import 'braft-editor/dist/index.css'
import React from 'react'
import BraftEditor from 'braft-editor'

export default class BasicDemo extends React.Component {

    state = {
        editorState: BraftEditor.createEditorState(this.props.default), // 设置编辑器初始内容
        outputHTML: '<p></p>'
    }

    componentDidMount () {
        this.isLivinig = true
        // 3秒后更改编辑器内容
        setTimeout(this.setEditorContentAsync, 3000)
    }

    componentWillUnmount () {
        this.isLivinig = false
    }

    handleChange = (editorState) => {
        this.setState({
            editorState: editorState,
            outputHTML: editorState.toHTML()
        })
        if (this.props.usage==='submitCode'){
            window.localStorage.submitCode = editorState.toHTML()
        }else if (this.props.usage==='description'){
            window.localStorage.description = editorState.toHTML()
        }else if(this.props.usage==='output'){
            window.localStorage.output = editorState.toHTML()
        }
    }

    setEditorContentAsync = () => {
        this.isLivinig && this.setState({
            editorState: BraftEditor.createEditorState(this.props.default)
        })
    }

    render () {

        // const { editorState, outputHTML } = this.state
        const { editorState} = this.state
        const editorProps = {
            height: 500,
            contentFormat: 'html',
            initialContent: '<p>Hello World!</p>',
            onChange: this.handleChange
        }
        return (
                <div className="editor-wrapper">
                    <BraftEditor
                        {...editorProps}
                    />
                </div>
        )
    }
}