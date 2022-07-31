# Keycloak适配器的常用属性
在Spring Security集成Keycloak 适配器时需要引入一些额外的配置属性。一般我们会把它配置到Spring Boot的配置文件中。

## realm
Keycloak领域名称，这是一个必须项。

## resource
应用的client_id，Keycloak服务器上注册的每个客户端都有一个独一无二的标识。这是一个必须项。

## realm-public-key
PEM格式的realm公钥，不建议客户端配置。每次Keycloak Adapter会自动拉取它。

## auth-server-url
Keycloak服务器的基本地址，格式通常是https://host:port/auth，这是一个必须项。

## ssl-required
Keycloak 服务器的通信使用HTTPS的范围，是可选的，有三个选项：

external，默认值，表示外部的请求都必须使用HTTPS。
all，顾名思义，所有的都使用HTTPS。
none, 禁用HTTPS。
confidential-port
Keycloak服务器的安全端口，默认 8443。

## use-resource-role-mappings
如果设置为true, Keycloak Adapter将检查令牌携带的用户角色是否跟资源一致；否则会去查询realm中用户的角色。默认false。

## public-client
设置为true则不需要为客户端配置密码，否则需要配置keycloak.credentials.secret。生成secret的方法是在Keycloak控制台上修改对应客户端设置选项的访问类型为confidential,然后在安装中查看对应配置项。当访问类型不是confidential时该值为false。

## enable-cors
开启跨域（cors）支持。可选项，默认false。如果设置为true就激活了cors-开头的配置项，这些配置项都不啰嗦了，都是常见的跨域配置项。

## bearer-only
对于服务，这应该设置为true。如果启用，适配器将不会尝试对用户进行身份验证，而只会验证不记名令牌。如果用户请求资源时没有携带Bearer Token将会401。这是可选的。默认值为false。

## autodetect-bearer-only
如果你的应用不仅仅是Web应用而且还提供API服务（现在通常是Restful Service），开启了这一配置后Keycloak服务器会通过请求标头相对“智能”地引导未认证的用户到登录页面还是返回401状态。比bearer-only更加智能一些。

## enable-basic-auth
为适配器开启Basic Authentication认证，如果开启就必须提供secret。默认false。

## expose-token
JavaScript CORS 请求通过根路径下/k_query_bearer_token用来从服务器获取令牌的，好像是nodejs相关的后端应用使用的东西，我折腾了半天没有调用成功。

## credentials
当客户端的访问类型(access type)为Confidential时，需要配置客户端令牌，目前支持secret和jwt类型。参考public-client中的描述。

# 总结
上面列举了大部分我们常用的属性，后面的属性和定制Keycloak服务器相关暂时不介绍了。Keycloak剩下的配置项可以到Keycloak Java适配器配置项[1]查看。下一篇我将对Keycloak Spring Security认证授权的流程作出一个分析。

参考资料  
[1] Keycloak Java适配器配置项: https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config
