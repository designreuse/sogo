# maas 

## 系统运行环境配置

### 运行前所需

* Redis 3.2 (必须)

[Redis-x64-3.2.100.msi--Github](https://github.com/MicrosoftArchive/redis/releases/download/win-3.2.100/Redis-x64-3.2.100.msi)   
Windows版的msi运行之后会自动注册成服务，配置文件在安装文件夹下，基本使用已非常方便，此外，详细的使用说明，就在该Github下。 


* Nginx （可选）(模拟单点登录可选项，单点登录的简单演示不需要nginx, 模拟真实环境需要nginx，nginx配置文件在主目录)   

#### 可选工具 

[Redis Desktop Manager](https://redisdesktop.com/download)

[lombok 插件](https://www.projectlombok.org/)


### 运行须知

1. 首先运行 discovery-service -> config-service -> data-service -> auth-service -> platform-web 

2. 等待服务启动完毕，再启动下一个服务

3. 启动 platform-web 需要启动redise服务

## 开发注意事项

### 前端开发注意事项  

* 前端代理一般情况下，需要打开的服务，gateway-service, platform-web, node-web

* 静态资源文件引入链接前加上 ${Session.basePath} 解决localhost:8000/platform-web/ 访问静态资源路径需要由/assets变为/platform-web/assets

```freemaker
<link href="${Session.basePath}/assets/css/test.css" rel="stylesheet">
```

  
  
  
***  



