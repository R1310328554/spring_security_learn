package com.lk.learn.springmvc.demo.domain;

import lombok.Data;

public interface ReturnCode {
    int code();
    String message();
}
@Data
  class ReturnCode2 {
    private int code;
    private String msg;
}
