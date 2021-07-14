package com.example.ui.controller;

import com.example.shared.dto.UserDto;
import com.example.ui.model.request.UserDetailsRequestModel;
import com.example.ui.model.response.UserRest;
import com.example.ws.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//controller responsible for all operations that have to do with users


@RestController
@RequestMapping("users") //http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService; //property

    @GetMapping
    public String getUser(){
        return "get user was called";
    }




    @PostMapping  // mit requestBody Annotation wird UserDetailsRequestModel mit JSON info injected
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails){

        //return of T UserRest
        UserRest returnValue = new UserRest();


        //populate userDTO with information that i have recieved from
        //the Requestbody
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);


        //UserService
        //UserDto is shared across different layers
        //in service layer we'll add some additional values
        //in datalayer we make entity object of UserDto
        //once createUser.method has done its work we'll return
        //some information to the user
        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser,returnValue);



        return returnValue;
    }

    @PutMapping
    public String updateUser(){
        return "update user was called";
    }

    @DeleteMapping //binds this method to DeleteHTTP-Request an die bestimmte URL
    public  String deleteUser(){
        return "delete user was called";
    }


}