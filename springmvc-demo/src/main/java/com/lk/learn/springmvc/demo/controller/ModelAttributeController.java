package com.lk.learn.springmvc.demo.controller;

import com.lk.learn.springmvc.demo.domain.UserForm;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class ModelAttributeController {

    // 方法无返回值
    @ModelAttribute // 是对当前 controller起作用， 还是全局？
    // @ModelAttribute 并不要求在有@ControllerAdvice注解的类下面 !!
    public void myModel(@RequestParam(required = false) String name, Model model) {
        System.out.println("ModelAttributeController.方法无返回值");
        model.addAttribute("name", name);
    }

    // 方法有返回值
    @ModelAttribute
    // 虽然@RequestParam参数name和myModel 重名，但丝毫不受影响，仍然绑定成功！
    // public String myModel2(@RequestParam(required = false) String name) {
    public String myModel2(@RequestParam(required = false) String name) {// 不用改名为 name2， 冲突会覆盖即可
        System.out.println("ModelAttributeController.方法有返回值");
        // 对于以上情况，返回值对象 name 会被默认放到隐含的 Model 中，
        // 在 Model 中 key 为返回值首字母小写，value 为返回的值。等同于 model.addAttribute("string", name);。
        return name + " " + System.nanoTime();// key 竟然是 string
    }

    // 方法有返回值
    @ModelAttribute("name3")
    //正常情况下，程序中尽量不要出现 key 为 string、int、float 等这样数据类型的返回值，使用 @ModelAttribute 注解 value 属性可以自定义 key
    public String myModel3(@RequestParam(required = false) String name3, Model model) {
        System.out.println("ModelAttributeController.111  方法有返回值");
        // 对于以上情况，返回值对象 name 会被默认放到隐含的 Model 中，
        return name3 + model;
    }

    // 方法有返回值
    @ModelAttribute
    public String myModel4(@RequestParam(required = false) String name3, Model model) {
        System.out.println("ModelAttributeController.22  方法有返回值");
        // 对于以上情况，返回值对象 name 会被默认放到隐含的 Model 中，
        return name3 + " 方法有返回值  " + System.nanoTime() + model;
    }

    @RequestMapping("/cc")
    // 会读取前面绑定的name2到 参数name3！
    public String cc(@ModelAttribute("name2") String name3, Model model) {
        System.out.println("ModelAttributeController.cc");
        return name3 + " 方法有返回值 cc     " + System.nanoTime() + model;
    }
    @RequestMapping("/cc2")
    // 会读取前面绑定的name3到 参数name3！ 默认就使用 参数作为属性名！
    public String cc2(@ModelAttribute String name3, Model model) {
        System.out.println("ModelAttributeController.cc 2");
        return name3 + " 方法有返回值 cc2     " + System.nanoTime() + model;
    }

    /*
        http://192.168.1.103:8080/cc?name=zhangsan&name2=22&name3=33&password=1234256
     */
    @RequestMapping("/register")
    public String register(@ModelAttribute("uasdsSdqwer") UserForm user, Model model) {
        // 会读取前面绑定的name到user实例里面，但因为之前password没有赋值，所以 password 不会绑定
        // @ModelAttribute("uasdsSdqwer") 在的uasdsSdqwer 随意的，
        // why？ 其实会把 uasdsSdqwer作为key 创建一个user实例添加到model！ 默认是 userForm！
        if ("zhangsan".equals(user.getName()) && "123456".equals(user.getPassword())) {
            return "login " + model;
        } else {
            return "register " + model;
        }
    }

    // @ModelAttribute和@RequestMapping同时放在方法上
    /*
    @ModelAttribute 和 @RequestMapping 注解同时应用在方法上时，有以下作用：
        方法的返回值会存入到 Model 对象中，key 为 ModelAttribute 的 value 属性值。
        方法的返回值不再是方法的访问路径，访问路径会变为 @RequestMapping 的 value 值，例如：@RequestMapping(value = "/index") 跳转的页面是 index.jsp 页面。

        总而言之，@ModelAttribute 注解的使用方法有很多种，非常灵活，可以根据业务需求选择使用。

       如果是 @RequestMapping(value = "/index") 则陷入循环
        Circular view path [index]: would dispatch back to the current handler URL [/index] again. C
     */
    @RequestMapping(value = "/asd/index")
    @ModelAttribute("name4")
    @ResponseBody // 不管是否有ResponseBody，RestController， 返回的一律是 视图
    public String model(@RequestParam(required = false) String name4, Model model) {
        System.out.println("ModelAttributeController. @ModelAttribute 和 @RequestMapping 注解同时应用在方法上时  " + model);
        return name4;
    }

    @RequestMapping(value = "/model")
    public String model(Model model) {
        return "index =>  " + model;
    }

    /*
        @ModelAttribute注解方法的返回值上，添加方法返回值到模型对象中，用于视图页面展示时使用

        不管是否有ResponseBody，RestController， 返回的一律是 视图

        此时 @RequestMapping(value = "/helloWorld/aa")   返回到相同的路径的 view

        返回值有什么用？ 同样 添加到 model ！
     */
    @RequestMapping(value = "/index")
    public @ModelAttribute("user") UserForm helloWorld(UserForm user, Model model) {
        System.out.println("ModelAttributeController.helloWorld user = [" + user + "], model = [" + model + "]");
        UserForm userForm = new UserForm();
        userForm.setName("nnnnnaaameee ++++");
        return userForm;
    }

    /*
        我访问 http://192.168.1.103:8080/aa.html 、 http://192.168.1.103:8080/aa.dasfqwe
        为何会到这里来？ 难道是ContentNegotiation？ 任意后缀 都如此！
     */
    @RequestMapping(value = "/aa")
    public String aa(Model model) {
        System.out.println("ModelAttributeController.aa");
        // return "aa =>  " + model;
        return "aa";
    }

    @RequestMapping(value = "/bb")
    public String bb(Model model) {
        System.out.println("ModelAttributeController.bb");
        return "bb =>  " + model;
    }

}
