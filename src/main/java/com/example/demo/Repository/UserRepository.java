package com.example.demo.Repository;

import com.example.demo.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{

    User findUserByName(String name);
    User findUserByEmail(String email);
    User findUserByEmailAndPassword(String email, String password);
}
