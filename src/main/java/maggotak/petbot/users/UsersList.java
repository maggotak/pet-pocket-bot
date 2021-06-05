package maggotak.petbot.users;

import java.util.ArrayList;
import java.util.List;

public class UsersList {
  private static List<User> usersList;

  private UsersList() {
    usersList = new ArrayList<>();
  }

  public static List<User> getUsersList() {
    if (usersList == null) {
      new UsersList();
    }
    return usersList;
  }
}
