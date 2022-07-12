OAauth2.0包括以下角色：

客户端：本身不存储资源，需要通过资源拥有者的授权去请求资源服务器的资源，比如：Android客户端、Web客户端（浏 览器端）、微信客户端等。
资源拥有者： 通常为用户，也可以是应用程序，即该资源的拥有者。
授权服务器（也称认证服务器）：用于服务提供商对资源拥有的身份进行认证、对访问资源进行授权，认证成功后会给客户端发放令牌 （access_token），作为客户端访问资源服务器的凭据。
资源服务器：存储资源的服务器，本例子为微信存储的用户信息。
————————————————
版权声明：本文为CSDN博主「prefect_start」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/prefect_start/article/details/125356882


授权服务器/ 资源服务器 可以合并到一起部署！

资源服务器 即 ResourceServer 通过 EnableResourceServer 启用
它有哪些功能？

授权服务器 即 AuthorizationServer 通过 EnableAuthorizationServer 启用

授权服务器 验证资源所有者身份、然后客户端发放令牌，客户端拿令牌去访问资源服务器； 那资源服务器需要知道这个token 身份有效，身份来自 同源的 授权服务器吧！ 如何？ 


AuthorizationServer 授权服务器 也会配置用户名密码、它也提供了登录入口页面，所以也可以直接登录，但是这个登录功能主要是给 客户端使用的。 如果 直接的话， 怎么登出呢？


-------
本质上， security也好、oauth2也好，添加了一堆的过滤器、mvc，其主要工作 就是由过滤器触发的！ 过滤器拦截、然后检查授权等等。 




## EnableWebSecurity​



## public void configure(HttpSecurity http)  方法的用意
就是 框架已经配置好七七八八了，该配置的、改实例化的 都已经做了，剩下一些 可以客制化的东西，或者必须要客制化的、不能使用默认值的东西， 那么就暴露出来一个方法，
参数是已经配置好的， 你们再去完善一下吧！！



# HttpSessionSecurityContextRepository


## SecurityContextRepository 
