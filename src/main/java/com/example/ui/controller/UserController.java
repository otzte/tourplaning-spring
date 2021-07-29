package com.example.ui.controller;

import com.example.exceptions.UserServiceException;
import com.example.shared.dto.UserDto;
import com.example.ui.model.request.UserDetailsRequestModel;
import com.example.ui.model.response.ErrorMessages;
import com.example.ui.model.response.UserRest;
import com.example.ws.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

//controller responsible for all operations that have to do with users


@RestController
@RequestMapping("users") //http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService; //looks for bean of type UserSevice and autowires it

    @GetMapping(path="/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}) //send back Json or xml-type by default, order: first xml then Json
    public UserRest getUser(@PathVariable String id){ //@PathVariable injects Path in id
        //you only get here with successful Authentication

        UserRest returnValue = new UserRest();
        //populate UserRest with information

        UserDto userDto = userService.getUserById(id);
        BeanUtils.copyProperties(userDto,returnValue);

        return returnValue;
    }




    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails)throws Exception{ // mit requestBody Annotation wird UserDetailsRequestModel durch JSON info injected

        //return of T UserRest
        UserRest returnValue = new UserRest();

        if (userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());


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

    @PutMapping(
            path = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
            )
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails){
        UserRest returnValue = new UserRest();
        if (userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);
        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser,returnValue);
        return returnValue;

    }

    @DeleteMapping //binds this method to DeleteHTTP-Request an die bestimmte URL
    public  String deleteUser(){
        return "delete user was called";
    }


}