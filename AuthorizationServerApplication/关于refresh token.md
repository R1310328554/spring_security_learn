
当我们成功获取access_token的时候,大家应该也发现了下面有一个refresh_token的东西,那么这个东西到底是什么呢?它的作用是什么呢?大家都知道,token是有过期性质的,如果用户访问的时候,恰巧令牌过期了,那是不是有些尴尬。针对于这种情况,客户端访问令牌过期,则需要使用刷新令牌申请一个新的访问令牌。也就是俗称备胎~~
————————————————
原文链接：https://blog.csdn.net/weixin_54889617/article/details/121862028

就是说， 比如access_token 是半小时过期；每次，客户端都会携带这个access_token访问服务器，access_token本身是固定不变的。
服务端每次都会解析access_token，然后验证，然后呢，很重要一点，服务器端并不会 更新或延长 access_token 或session的有效期，
这样，如果 用户刚好在 29分59秒 访问了一次后端，然后30分01秒，又操作了一下，于是又访问后端， 结果发现竟然提示 会话过期。
—— 这个显然是不友好！  所以， 延长 access_token非常有必要。 

如果后端发现access_token过期，怎么办呢？

1. 如果后端发现access_token过期，那么立马使用对应的 refresh_token去获取新的 access_token，后续操作都使用这个新的access_token
        但是这样有一个问题是，refresh_token 也不是一直有效的，如果refresh_token 也过期了呢？ 而且 refresh_token 有效期怎么设置比较好？
        问题没有根本的解决，只是 refresh_token 相当于就是整个会话的有效期。
    
2. 每次前后端交互都刷新access_token 中的最后交互时间，相当于是延长它。 其实就是跟Tomcat的session的机制一样了。 这个肯定是ok的，没大问题。
但是，每次都 解析access_token，然后刷新其中最后时间， 效率可想而知，不会很高。 于是，我们可以使用另外的 map来存储每一个 access_token 的最后交互时间
而不用每次修改access_token 本身！  
        

##
