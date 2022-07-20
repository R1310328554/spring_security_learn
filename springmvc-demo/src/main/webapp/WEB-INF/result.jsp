<%@ page contentType="text/html;charset=utf-8" language="java" %>
<html>
<head>
    <title>$</title>
</head>
<body>

————————————————
https://www.cnblogs.com/FlyAway2013/p/7968619.html

But ！这样做， 我还是不能访问到 jsp， public，static， resources各个目录都放了相同文件，但都访问不到。， 真是郁闷了。后面我发现这是boot 的一个坑， 貌似只有把 jsp 页面 放到META-INF.resources 目录下， 才能访问。 也就是说，boot 只会 去META-INF.resources 目录下查找jsp。 不知道为什么， 有谁知道了， 请告诉我。 除了META-INF.resources目录， 传统的 src/main/webapp/WEB-INF目录 也是可以的。但不知道为什么一定要在WEB-INF 目录下。



<h3>${msg}</h3>
</body>
</html>
