# API Documentation

## Description

前端react+axios，后端可以使用node/python实现逻辑，用nginx做负载均衡然后用nginx反向代理node，这样不必写路由只需实现本文档定义的接口即可。

示例nginx反向代理配置：

    upstream nodejs{
            server 127.0.0.1:3000;
            keepalive 64;
        }
    
        server {
            listen       80;
            server_name  localhost;
            location / {
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header Host  $http_host;
                proxy_set_header X-Nginx-Proxy true;
                proxy_set_header Connection "";
                proxy_pass      http://nodejs;
            }

## User Management

-------------

### 1. 登录

#### 1.1 URL
[http://localhost:3000/api/login]()

#### 1.2 Request
##### 1.2.1 Method
post
##### 1.2.2 Header
```json
{"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"}
```
##### 1.2.2 Content

| 参数名   | 必选 | 类型   | 说明 |
| -------- | ---- | ------ | ---- |
| username | True | string |      |
| password | True | string |      |

##### 1.2.3 Example

```json
{"username": "peter", "password": "123456"}
```

#### 1.3 Response

#### 1.3 Response

##### 1.3.1 Header

```json
{"Content-Type": "application/json"}
```

##### 1.3.2 Content

返回一个json对象

| 参数名  | 必选 | 类型   | 说明                                        |
| ------- | ---- | ------ | ------------------------------------------- |
| success | true | bool   | 登录是否成功                                |
| data    | true | string | 表示身份，可选字符串("teacher",  "student") |

同时后端可以使用cookie+session保存凭据

##### 1.3.3 Example

```json
{"success": true, "data": "student"}
```

-------------

## Student

---------

### 1. 请求作业列表

#### 1.1 URL

[http://localhost:3000/api/student/queryAssignmentList]()

#### 1.2 Request

轮询请求

##### 1.2.1 Method

get

##### 1.2.2 Parameters

No parameters

#### 1.3 Response

##### 1.3.1 Header

```json
{"Content-Type": "application/json"}
```

##### 1.3.2 Content

返回一个json array，其中每个元素是一个json对象字段如下：

| 参数名                | 必选 | 类型   | 说明                               |
| --------------------- | ---- | ------ | ---------------------------------- |
| assignment_id         | true | string | 作业唯一标识                       |
| assignment_name       | true | string | 作业名称                           |
| assignment_start_time | true | string | 作业起始时间，表示成一个datestring |
| assignment_end_time   | true | string | 作业起始时间，表示成一个datestring |

##### 1.3.3 Example

```json
[
    {
        "assignment_id": "a-066ab87a062b",
        "assignment_name": "第一次作业",
     	"assignment_start_time": "June 13, 2021 11:13:00",
     	"assignment_end_time": "June 20, 2021 23:59:59"
    },
    {
        "assignment_id": "a-204urhiugr9r",
        "assignment_name": "第二次作业",
     	"assignment_start_time": "June 20, 2021 12:47:00",
     	"assignment_end_time": "June 27, 2021 23:59:59"
    },

]
```

-----------

### 2. 请求问题列表

#### 2.1 URL

[http://localhost:3000/api/student/selectQuestionsByAssignment]()

#### 2.2 Request

轮询请求

##### 2.2.1 Method

get

##### 2.2.2 Parameters

| 参数名        | 必选 | 类型   | 说明 |
| ------------- | ---- | ------ | ---- |
| assignmetn_id | true | string |      |

#### 2.3 Response

##### 2.3.1 Header

```json
{"Content-Type": "application/json"}
```

##### 2.3.2 Content

返回一个json array，其中每个元素是一个json对象字段如下：

| 参数名        | 必选 | 类型   | 说明             |
| ------------- | ---- | ------ | ---------------- |
| question_id   | true | string | 问题唯一标识     |
| question_name | true | string | 问题名称         |
| is_finished   | true | bool   | 问题是否已经完成 |

##### 2.3.3 Example

```json
[
    {
        "question_id": "q-21ru2933hui4",
        "question_name": "7-1 select查询",
     	"is_finished": true,
    },
    {
        "question_id": "q-r9imvrvnq40s",
        "question_name": "7-3 delete删除",
     	"is_finished": false
    },

]
```

-----------

### 3. 获取问题详细信息

#### 3.1 URL

[http://localhost:3000/api/student/selectQuestionsById]()

#### 3.2 Request

##### 3.2.1 Method

get

##### 3.2.2 Parameters

| 参数名      | 必选 | 类型   | 说明 |
| ----------- | ---- | ------ | ---- |
| question_id | true | string |      |

#### 3.3 Response

##### 3.3.1 Header

```json
{"Content-Type": "application/json"}
```

##### 3.3.2 Content

返回一个json对象

| 参数名               | 必选 | 类型   | 说明         |
| -------------------- | ---- | ------ | ------------ |
| question_name        | true | string | 问题名称     |
| question_description | true | string | 问题描述     |
| question_output      | true | bool   | 问题输出格式 |

--------

### 4. 提交答案

#### 4.1 URL

[http://localhost:3000/api/student/submit]()

#### 3.2 Request

##### 3.2.1 Method

post

##### 3.2.2 Header

```json
{"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"}
```

##### 3.2.3 Content

发送一个json对象

| 参数名      | 必选 | 类型   | 说明          |
| ----------- | ---- | ------ | ------------- |
| question_id | true | string |               |
| code        | true | string | 提交的sql语句 |

#### 3.3 Response

##### 3.3.1 Header

```json
{"Content-Type": "application/json"}
```

##### 3.3.2 Content

返回一个json对象

| 参数名  | 必选 | 类型 | 说明         |
| ------- | ---- | ---- | ------------ |
| success | true | bool | 是否上传成功 |

在这里只返回是否上传成功即可，具体判题情况下一个接口描述

--------

