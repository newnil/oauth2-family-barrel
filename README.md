# oauth2-family-barrel
OAuth2全家桶项目。本项目演示了如何使用spring-boot、spring-security以及spring-security-oauth快速构建OAuth2服务框架体系。

本项目包含以下3个子项目：
1. oauth2-server-boot
2. oauth2-client-boot
3. ResourceServer

分别扮演以下角色：
1. OAuth2鉴权及授权服务中心
2. 使用OAuth2访问受保护资源的客户端
3. 提供受保护资源的服务端

## 使用方法
1. 使用`mvn spring-boot:run`分别启动Server、Client和ResourceServer。  
2. 浏览器访问 [http://localhost:8080/][1] 进行登录。
3. 仔细观察，登录页面会跳转到 `http://localhost:8082/uaa/login.html` 页面上去。
4. 登录后，会跳转到授权页面让用户进行授权，此时URL地址是 `http://localhost:8082/uaa/oauth/confirm_access` 。
5. 授权后会跳转会原来的页面，并显示已登录用户用户名信息。`Login`按钮变成`Logout`。
6. 点击`Get Data`按钮，会跳转到`resource.html`页面，此页面会从远程的ResourceServer上读取一段字符串`This message comes from res server`显示在页面上。
7. 如果用户未登录且点击`Get Data`按钮，页面会跳转至3中的登录页面进行登录和授权。登录并授权完成后会跳转至`resource.html`页面。
8. 浏览器访问 [http://localhost:8082/uaa/oauth/tokens][2] 查看用户登录后发行的所有令牌，以及撤销令牌等操作。

## 要点简述

### oauth2-server-boot

- 在`SecurityConfiguration`中配置Server的标准安全配置，包括url访问权限，用户名密码等。
- OAuth2相关的配置在`OAuth2ServerConfig`中。需要在此类中配置客户端的client-id、secret、authorizedGrantTypes以及scopes等。需要启用`@EnableAuthorizationServer`。
- `AccessConfirmationController`以及相关的view是授权页面的实现，可以修改来实现自定义授权页面样式等。
- `AdminController`提供了令牌列表页面。
- `MainController`提供了首页、用户登录页面。
- `application.yml`中配置了端口和`contextPath`，**需要特别注意** 的是，同一域名下，授权服务器的`contextPath`不可以和客户端以及资源服务器的相同(^注：[dsyer's comment on issues-322][3])，就算是不同端口也不行。

### oauth2-client-boot

- 与Server相同，在`config.SecurityConfiguration`中配置标准安全配置。不需要在此配置用户名及密码是因为这些都由oauth2-server-boot提供了（SSO单点登录模式）。
- 不需要另外单独配置OAuth2Client的参数，但需要启用`@EnableOAuth2Sso`
- `MainController`中提供了首页、访问资源页面。
- client相关的client-id、secret等配置在`application.yml`中。

### ResourceServer

- 需要在`@Configuration`类上启用`@EnableResourceServer`
- client相关的client-id、secret等配置在`application.yml`中。


[1]: http://localhost:8080/
[2]: http://localhost:8082/uaa/oauth/tokens
[3]: https://github.com/spring-projects/spring-security-oauth/issues/322#issuecomment-64951927
