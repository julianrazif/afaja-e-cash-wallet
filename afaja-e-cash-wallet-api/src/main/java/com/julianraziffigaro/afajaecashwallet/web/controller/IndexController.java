package com.julianraziffigaro.afajaecashwallet.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

  @GetMapping(value = {"/"})
  public String hello() {
    return "hello";
  }
}
