package com.Keyush.WeatherApiVertx;

import io.ebean.DB;
import java.util.List;

public class UserService {

  public void createUser(User user) {
    user.save();
  }

  public User getUser(int id) {
    return DB.find(User.class, id);
  }

  public List<User> getAllUsers() {
    return DB.find(User.class).findList();
  }

  public void updateUser(User user) {
    user.update();
  }

  public void deleteUser(int id) {
    User user = getUser(id);
    if (user != null) {
      user.delete();
    }
  }
}
