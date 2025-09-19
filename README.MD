# 自用Springboot3项目模板
## 简介
一个自用的Springboot3项目模板，集成了常用的功能，如Mybatis-Plus、Sa-Token、Redis、Multipart、P6spy、AES加密、Controller签名等。
## 功能
- Mybatis-Plus：用于简化数据库操作。
- Sa-Token：用于实现用户认证和权限管理。
- Redis：用于缓存和会话管理。
- P6spy：用于记录SQL语句。
- AES加密：用于加密和解密数据。
- Controller签名：用于验证请求的合法性。
- 工具：经纬度距离获取、经纬度天气获取、AES加解密、易支付、易聚合登录等
- 防抖
- xss检测拦截
## 使用方法
1. 克隆项目到本地。
2. 在`application.properties`或`application-dev.properties`或`application-pord.properties`文件中配置数据库连接信息、Redis连接信息、AES加密密钥等，AES加密(打开即AES加密+数据签名校验)、防抖、xss拦截都有开关，可根据需求开启，不建议XSS拦截和AES加密同时开启。
3. 根据需要修改`application.properties`文件中的其他配置。
4. 启动项目。
## 其他
### 加解密问题
加密算法使用AES，加密模式为ECB，填充方式为AES/ECB/PKCS5Padding，密钥长度为128位，初始向量长度为16位。
#### 客户端如何请求加密数据到服务器？
仅限json数据，put|post请求方式  
使用上述加密方式，将数据+时间戳(13位)封装在字符串里,例如:
```json
{
    "username":"username",
    "password":"password",
    "t":1678126400000
}
```
加密后的json字符串为:
```json
{
    "data":"加密后的字符串"
}
```
签名为：md5(t+signKey)
最终请求到服务器的数据为:

```json
{
    "data":"加密后的字符串",
    "sign":"签名"
}
```
#### 服务器如何解密数据？
服务器自动解密数据，您就像平常书写即可，不需要任何多余操作，就像平时没加解密一样
#### 服务器如何验证签名？
服务器自动验证签名
### 防抖问题
#### 防抖有效吗？
实测有效
### XSS问题
#### XSS有效吗？
实测有效
### 后续会升级吗？
会，根据需求进行升级
### 模板依赖是否可升级
可以，根据您的需求即可，但需要注意升级后一些方法失效，根据对应api文档修改即可
#### 模板依赖存在依赖冲突吗？
不会