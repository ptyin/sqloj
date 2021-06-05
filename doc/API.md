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

## User Module

### 1. 登录

#### 1.1 URL
[http://localhost:3000/api/login](http://localhost:3000/api/login)

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

| 参数名  | 必选 | 类型   | 说明                                        |
| ------- | ---- | ------ | ------------------------------------------- |
| success | True | bool   | 登录是否成功                                |
| data    | True | string | 表示身份，可选字符串("teacher",  "student") |

同时后端可以使用cookie+session保存凭据

##### 1.3.3 Example

```json
{"success": True, "data": "student"}
```

## Student

### 1. 请求作业列表

#### 1.1 URL

[http://localhost:3000/api/student/queryAssignmentList](http://localhost:3000/api/student/queryAssignmentList)

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
| assignment_name       | True | string | 作业名称                           |
| assignment_start_time | True | string | 作业起始时间，表示成一个datestring |
| assignment_end_time   | True | string | 作业起始时间，表示成一个datestring |

##### 1.3.3 Example

```json
[
    {
        "assignment_name": "第一次作业",
     	"assignment_start_time": "June 13, 2021 11:13:00",
     	"assignment_end_time": "June 20, 2021 23:59:59"
    },
    {
        "assignment_name": "第二次作业",
     	"assignment_start_time": "June 20, 2021 12:47:00",
     	"assignment_end_time": "June 27, 2021 23:59:59"
    },
    ...
]
```

