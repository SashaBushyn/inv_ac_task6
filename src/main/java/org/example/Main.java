package org.example;

import org.example.dal.UserDal;
import org.example.model.User;

public class Main {
  public static void main(String[] args) {
    createUser();
    System.out.println(UserDal.getAll());
  }
  public static void createUser(){
    User user = User.builder().firstName("sasha").lastName("bushyn").email("sb@i.ua").build();
    System.out.println(UserDal.createUser(user));
  }
}