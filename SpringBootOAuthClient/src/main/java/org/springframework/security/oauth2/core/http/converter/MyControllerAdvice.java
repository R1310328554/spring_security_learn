package org.springframework.security.oauth2.core.http.converter;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@ControllerAdvice
// @RestControllerAdvice
public class MyControllerAdvice {

    // @ModelAttribute
    @Value("${user.oauth.clientUrl:}")
    private String oauthClientUrl;

    @InitBinder
    private void init(@ModelAttribute String ddd) {
        System.out.println("MyControllerAdvice.init");
    }

    @ModelAttribute
    private void init(Model model) {
        System.out.println("MyControllerAdvice.model " + model);
        model.addAttribute("oauthClientUrl", oauthClientUrl);
    }

}
