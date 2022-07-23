package com.lk.learn.springboot.demo.domain;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class NotifyEvent extends ApplicationEvent {
    private String address;
    private String text;

    public NotifyEvent(Object source, String address, String text) {
        super(source);
        this.address = address;
        this.text = text;
    }
    public NotifyEvent(Object source) {
        super(source);
    }
    //setter、getter
}
