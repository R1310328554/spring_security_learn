
Keycloak实现了业内常见的认证授权协议和通用的安全技术，主要有：
    
    浏览器应用程序的单点登录（SSO）。
    OIDC认证授权。
    OAuth 2.0。
    SAML。
    多租户支持。
    身份代理 - 使用外部 OpenID Connect 或 SAML 身份提供商进行身份验证。
    第三方登录。
    用户联盟 - 从 LDAP 和 Active Directory 服务器同步用户。
    Kerberos 网桥 - 自动验证登录到 Kerberos 服务器的用户。
    用于集中管理用户、角色、角色映射、客户端和配置的管理控制台。
    用户账户集中管理的管理控制台。
    自定义主题。
    两段身份认证。
    完整登录流程 - 可选的用户自注册、恢复密码、验证电子邮件、要求密码更新等。
    会话管理 - 管理员和用户自己可以查看和管理用户会话。
    令牌映射 - 将用户属性、角色等映射到令牌和语句中。
    安全策略恢复功能。
    CORS 支持 - 客户端适配器具有对 CORS 的内置支持。
    自定义SPI接口扩展。
    JavaScript 应用程序、WildFly、JBoss EAP、Fuse、Tomcat、Jetty、Spring 等客户端适配器。
    支持任何具有 OpenID Connect Relying Party 库或 SAML 2.0 Service Provider 库的平台/语言。

# Realm
如果你接触过知名安全框架Shiro相信对这个概念不会陌生。realm是管理用户和对应应用的空间，有点租户的味道，可以让不同realm之间保持逻辑隔离的能力。

默认情况下，Keycloack提供了一个叫Master的realm，这个Master不承担具体应用和用户的管理，它只用来管理其它realm的生命周期。


# 用户
访问 http://112.74.110.95:8080/auth/realms/demo/account/ ， 登录即可；


# composite roles
复合角色，听起来很玄乎，其实就是角色的从属关系或者说继承关系。B角色从属于A角色，那么你拥有了A角色就一定拥有B角色的权限。

# groups
用户组，你可以将一系列的角色赋予定义好的用户组，一旦某用户属于该用户组，那么该用户将获得对应组的所有角色权限。

# clients
客户端。通常指一些需要向Keycloack请求以认证一个用户的应用或者服务，甚至可以说寻求Keycloack保护并在Keycloack上注册的请求实体都是客户端。

相信不少同学用过微信开放平台、蚂蚁开放平台。首先我们需要在这些开放平台上注册一个客户端以获取一套类似用户名和密码的凭证。有的叫appid和secret；有的叫clientid和secret，都是一个意思。其实keycloak也差不多，也需要在对应的realm中注册一个客户端

## 重定向URI
为了测试，这里我只填写了设置选项卡中唯一的必填项有效的重定向URI，
这个选项（http://192.168.1.103:8080/*）的意思就是客户端springboot-client的所有API都会受到权限管控。

# client adapters
Keycloack为了支持多语言和跨平台而设计的适配器，比如适配Java的、适配Python的。有些是内置的实现，有些需要我们按照Keycloack的抽象定义来实现。后续我们主要和Spring Boot Adapter打交道。

# identity provider
用来认证用户的服务，简称IDP。Keycloack本身就是一个IDP。这个类似Spring Security中的AuthenticationProvider接口。

# 

### ff 
然后启动Spring Boot应用并在浏览器中调用http://localhost:8080/foo/bar，你会发现浏览器会跳转到下面这个地址：
http://localhost:8011/auth/realms/felord.cn/protocol/openid-connect/auth?response_type=code&client_id=springboot-client&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Ffoo%2Fbar&state=20e0958d-a7a9-422a-881f-cbd8f25d7842&login=true&scope=openid

