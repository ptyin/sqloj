# SQLOJ

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

### 1. Login

#### 1.1 URL
[http://localhost:3000/api/login](http://localhost:3000/api/login)

#### 1.2 Request
##### 1.2.1 Method
post
##### 1.2.2 Header
```json
{"Content-Type": "application/json"}
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

### 1.3 Response

##### 后端可以使用cookie保存凭据

| 参数名  | 必选 | 类型   | 说明                                        |
| ------- | ---- | ------ | ------------------------------------------- |
| success | True | bool   | 登录是否成功                                |
| data    | True | string | 表示身份，可选字符串("teacher",  "student") |