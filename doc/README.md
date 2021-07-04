# SQLOJ

## Team Member

| 姓名   | 学号         | 成员分工                             |
| ------ | ------------ | ------------------------------------ |
| 尹祥琨 | 201805130154 | Prototype design & UI implementation |
| 刘清扬 | 201800301071 | API specifying & logic handling      |
| 魏子耀 | 201800800651 | Judgement & isolation                |
| 贺伟强 | 201800301270 | Logic aggregation & deployment       |

## Functionality



## Project Structure

![](D:\OneDrive - mail.sdu.edu.cn\山大\homework\数据库系统\项目\SQLOJ\doc\images\structure.png)

- User Interface
  - React构建，优点在于有较高的性能，代码逻辑简单
  - Ant Design定制界面主题，构造交互语言和视觉风格
  - Braft Editor，富文本编辑，用以丰富问题描述
  - 代码编辑：Code Mirror，支持SQL自动补全，SQL语法高亮，显示行号等等
- Load Balancing
  - 将前端代码使用webpack打包为静态资源，使用Nginx做负载均衡的反向代理，占有内存少，并发能力强
- Logic Handler
  - Flask框架，使用 Python 编写的轻量级 Web 应用框架。 WSGI 工具箱采用 Werkzeug ，模板引擎则使用 Jinja2 。Flask使用 BSD 授权。
  - MongoDB存放网站数据，其是一个介于关系数据库和非关系数据库之间的产品，它支持的数据结构非常松散，是类似json的bson格式，因此可以存储比较复杂的数据类型。
- Asynchronous Judger
  - Sqlite3存放题目相关数据库，和判题相关
  - python threading库来派发调度异步判题器

