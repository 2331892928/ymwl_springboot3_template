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
## 开发方法
### 1.数据库设计
1. java 实体必须拥有，表字段必须拥有参考 java 实体：
```java
package 包名;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
public class 表名{
    //自增
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //主键
    @TableField(fill = FieldFill.INSERT)
    private String uuid;
    //乐观锁
    @Version
    private Integer revision;
    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 指定 JSON 序列化格式和时区
    private Date createdTime;
    //更新人
    @TableField(fill = FieldFill.UPDATE)
    private String updatedBy;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 指定 JSON 序列化格式和时区
    private Date updatedTime;
    //删除时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 指定 JSON 序列化格式和时区
    @TableLogic
    private Date deleteTime;
}
```
2. 判断是否登录及登录后存放数据（userinfovo自行更改，必须拥有 role，如果不需要请修改src/main/java/cn/ymypay/team/config/StpInterfaceImpl.java）：  
   使用satoken方法：
```java
StpUtil.login(用户的 uuid 或用户的 id);
UserInfoVo userInfoVo = new UserInfoVo();
// 放入用户数据后
userInfoVo.setLoginExpireTime(DateUtils.nowSeconds().getTime() + StpUtil.getTokenTimeout());
userInfoVo.setToken(StpUtil.getTokenValue());
UserUtils.setUserInfo(userInfoVo);
```
   后续拿取当前用户登录数据：
```java
UserInfoVo userInfoVo = UserUtils.getUserInfo();
```
   判断登录方法：参考satoken即可，一键拦截路由在：src/main/java/cn/ymypay/team/config/WebMvcConfig.java。如果是一般异步就是把注释的取消掉，把webmvc的注释掉。  
   判断userInfoVo是否为空也可以  
判断身份：  
   参考satoken或直接读取userinfovo
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