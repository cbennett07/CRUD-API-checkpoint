package com.example.demo.Model;

import com.example.demo.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class Authenticated {

    @JsonView(Views.IdEmail.class)
    private boolean authenticated;
    @JsonView(Views.IdEmail.class)
    private User user;

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
