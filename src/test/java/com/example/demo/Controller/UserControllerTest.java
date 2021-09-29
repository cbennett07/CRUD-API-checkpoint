package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.Media;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository repository;

    @Test
    @Transactional
    @Rollback
    void addUserWithPost() throws Exception{
        MockHttpServletRequestBuilder request = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Chris\",\"email\":\"chris@bennett.net\"}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("chris@bennett.net") ));

    }
    @Test
    @Transactional
    @Rollback
    void getAllUsers() throws Exception{
        User myUser = new User();
        myUser.setName("Patrick");
        myUser.setEmail("Pat@mahomes.net");
        myUser.setPassword("chiefs");
        repository.save(myUser);

        MockHttpServletRequestBuilder request = get("/users")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", greaterThan(0)));
    }
    @Test
    @Transactional
    @Rollback
    void updateUserById() throws Exception {
        //make user and add to db
        User myUser = new User();
        myUser.setName("Patrick");
        myUser.setEmail("Pat@mahomes.net");
        myUser.setPassword("chiefs");
        repository.save(myUser);

        //get the assigned id for my new user in db and pass it in query string to update
        MockHttpServletRequestBuilder request = patch("/users/" + myUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Alex\",\"email\":\"alex@smith.net\",\"password\":\"CHIEFS\"}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("alex@smith.net")));

    }
    @Test
    @Transactional
    @Rollback
    void deleteUserById() throws Exception {
        //make user and add to db
        User myUser = new User();
        myUser.setName("Patrick");
        myUser.setEmail("Pat@mahomes.net");
        myUser.setPassword("chiefs");
        repository.save(myUser);

        //get the assigned id for my new user in db and pass it in query string to delete
        MockHttpServletRequestBuilder request = delete("/users/" + myUser.getId())
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().
                        string("{\n\"count\": " + this.repository.count() + "\n}"));

    }
    @Test
    @Transactional
    @Rollback
    void getUserById() throws Exception{
        //make user and add to db
        User myUser = new User();
        myUser.setName("Patrick");
        myUser.setEmail("Pat@mahomes.net");
        myUser.setPassword("chiefs");
        repository.save(myUser);

        MockHttpServletRequestBuilder request = get("/users/" + myUser.getId())
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.name", is("Patrick")))
                .andExpect(jsonPath("$.email", is("Pat@mahomes.net")));
                //.andExpect(jsonPath("$.password", is("chiefs")));
    }
    @Test
    @Transactional
    @Rollback
    void authenticateUserCorrectCreds() throws Exception {
        //make user and add to db
        User myUser = new User();
        myUser.setName("Patrick");
        myUser.setEmail("Pat@mahomes.net");
        myUser.setPassword("chiefs");
        repository.save(myUser);

        MockHttpServletRequestBuilder request = post("/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"Pat@mahomes.net\",\"password\":\"chiefs\"}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated", is(true) ))
                .andExpect(jsonPath("$.user.id", is(myUser.getId().intValue()) ))
                .andExpect(jsonPath("$.user.email", is(myUser.getEmail()) ));
    }
    @Test
    @Transactional
    @Rollback
    void authenticateUserIncorrectCreds() throws Exception {
        //make user and add to db
        User myUser = new User();
        myUser.setName("Patrick");
        myUser.setEmail("Pat@mahomes.net");
        myUser.setPassword("chiefs");
        repository.save(myUser);

        MockHttpServletRequestBuilder request = post("/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"Pat@mahomes.net\",\"password\":\"SBLIV\"}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated", is(false) ));
//                .andExpect(jsonPath("$.user.id", is("") ));
//                .andExpect(jsonPath("$.user.email", is(myUser.getEmail()) ));

    }
}

