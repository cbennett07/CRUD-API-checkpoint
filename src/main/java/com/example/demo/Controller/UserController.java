package com.example.demo.Controller;

import com.example.demo.Model.Authenticated;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //CREATE
    @PostMapping("/users")
    @JsonView(Views.IdEmail.class)
    public User addUser(@RequestBody User myUser){
        return this.userRepository.save(myUser);
    }
    //READ
    @GetMapping("/users/{id}")
    @JsonView(Views.IdEmail.class)
    public User getUserById(@PathVariable Long id){
        User myUser = userRepository.findById(id).get();
        return myUser;
    }
    //UPDATE
    @PatchMapping("/users/{id}")
    @JsonView(Views.IdEmail.class)
    public User updateUserById(@PathVariable Long id, @RequestBody Map<String, Object> newUser){
        User oldUser = this.userRepository.findById(id).get();
        newUser.forEach((key, value) -> {
            switch (key){
                case "name":
                    oldUser.setName((String) value);
                    break;
                case "email":
                    oldUser.setEmail((String) value);
                    break;
                case "password":
                    oldUser.setPassword((String) value);
                    break;
            }

        });
        return this.userRepository.save(oldUser);
    }
    //DELETE
    @DeleteMapping("/users/{id}")
    public String deleteUserById(@PathVariable Long id){
        StringBuilder sb = new StringBuilder();
        try{
        User myUser = this.userRepository.findById(id).get();

        String userName = myUser.getName();

            this.userRepository.deleteById(id);
            sb.append("{\n\"count\": " + this.userRepository.count() + "\n}");
        }catch(Exception e)
        {
            sb.append("No user with id:" + id + " found.\n");
            sb.append(e);
        }
        return sb.toString();
    }
    //LIST
    @GetMapping("/users")
    @JsonView(Views.IdEmail.class)
    public Iterable<User> getUsers(){
        return this.userRepository.findAll();
    }
    //AUTHENTICATE - ENDPOINT #6
    @PostMapping("/users/authenticate")
    @JsonView(Views.IdEmail.class)
    public Authenticated authenticateUser(@RequestBody Map<String, String> userCreds){
        String userEmail = userCreds.get("email");
        String userPassword = userCreds.get("password");
        Authenticated auth = new Authenticated();
        auth.setUser(this.userRepository.findUserByEmailAndPassword(userEmail, userPassword));
        auth.setAuthenticated(true);
        if(auth.getUser()==null){
            auth.setAuthenticated(false);
        }

        return auth;
    }

}
