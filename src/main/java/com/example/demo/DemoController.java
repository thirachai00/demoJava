package com.example.demo;

import com.example.demo.model.UserRequest;
import com.example.demo.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemoController {

    @Autowired
    DemoService demoService;

    @GetMapping("/getUser")
    public ResponseEntity<UserResponse> getUser( @RequestBody UserRequest request ){
        return ResponseEntity.ok(this.demoService.getUser(request));
    }
}
