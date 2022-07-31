# OIDC认证授权登录

走的是基于OIDC(OAuth 2.0的增强版)的认证授权模式。只有你正确填写了用户名和密码才能得到/foo/bar的正确响应。


# OIDC的产生背景

OAuth 2.0协议只解决了授权的问题，客户端只要得到了资源所有者的授权就能访问资源。OAuth 2.0本身并没有提供用户认证的规范。

OAuth 2.0本身无法证明资源所有者就是正确的资源所有者。OAuth 2.0中涉及的用户认证都建立在其它认证的可靠性之上，OAuth 2.0只消费认证的结果并不参与认证的流程。

基于这个原因OpenID Connect诞生了。它和OAuth 2.0的关系是这样的：

interface OIDC extends OAuth2{
   boolean authentication ()
}

也就是说OIDC 在OAuth 2.0 的基础之上增加一个对资源所有者的认证流程，实现了真正意义上的认证授权。

# OIDC 
OIDC（OpenID Connect）建立在Auth 2.0的流程之上，提出了终端用户认证标识ID Token概念。符合OIDC流程的一定符合OAuth2.0。OAuth 2.0 是关于如何发布访问令牌（AccessToken）的规范；而OIDC是关于如何发布ID 令牌的规范。虽然这两种令牌都是以JWT的形式体现。

在RFC 6749中定义的一个OAuth2.0授权端点（authorization endpoint） 用以请求授权，该端点需要一个response_type的参数用来通知授权服务器所需的授权类型，通常包括了code和token两种。OIDC扩展了这一属性，增加了id_token和none。那么response_type的值现在可能有下列组合的情况：

    code
    token
    id_token
    code token
    id_token token
    code id_token
    code id_token token
    none
另外如果该请求是一个OIDC授权认证请求还必须包含一个值为openid 的scope参数，这是区分普通OAuth 2.0和OIDC的关键。

# OIDC的关键术语
OIDC规定了一些术语用来提高我们学习的门槛:
    
    EU：End User 终端用户
    RP：Relying Party  即客户端（client），授权和认证的最终消费方，我搞不懂为啥要玩多余的概念
    OP：OpenID Provider，对EU进行认证的服务提供者
    ID Token：JWT格式，EU的认证通过后生成凭证，供RP消费
    UserInfo Endpoint：通过凭据查询用户基本信息的接口，建议上HTTPS。
    
# OIDC的流程
OIDC复用了OAuth2.0的授权流程，在授权的过程中增加了一些“小动作”来进行用户认证。结合其术语，大致的流程是这样的：

    RP发送一个认证请求给OP；
    OP先对EU进行身份认证，确认无误后提供授权；
    OP把ID Token和Access Token（需要的话）返回给RP；
    RP使用Access Token发送一个请求UserInfo EndPoint；（可选）
    UserInfo EndPoint返回EU的Claims。（基于第4个步骤可选）
    
图片

# OIDC协议流程图
另外，OIDC归纳了三种复用OAuth 2.0的流程：

    Authorization Code Flow：使用OAuth2的Authorization Code模式来换取Id Token和Access Token。
    Implicit Flow：使用OAuth2的Implicit模式获取Id Token和Access Token。
    Hybrid Flow：以上两种的混合实现。
    
# 总结
协议这个东西学起来确实比较枯燥难懂，需要结合一些场景才能说清楚，说实话有些东西我也云里雾里，不过这个是无法跳过去的东西。先不要想太多为什么，后续会结合一些场景来搞明白上面的术语和流程。

