package com.how2java.tmall.dao;

import com.how2java.tmall.pojo.Manager;
import com.how2java.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerDAO extends JpaRepository<Manager,Integer>{
    Manager getByNameAndPassword(String name,String password);
    Manager findByName(String name);
}
