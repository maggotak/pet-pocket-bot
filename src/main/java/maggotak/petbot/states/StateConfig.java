package maggotak.petbot.states;

import maggotak.petbot.users.User;
import maggotak.petbot.users.UsersList;

public class StateConfig {

  public User checkUserExistence(long id) {
    User user;
    if (UsersList.getUsersList().stream().noneMatch(u -> u.getId() == id)) {
      user = new User(id);
      user.setState(State.MENU);
      UsersList.getUsersList().add(user);
    } else {
      user = UsersList.getUsersList().stream().filter(u -> u.getId() == id).findFirst().get();
    }
    return user;
  }

  public State getUsersState(User user) {
    return user.getState();
  }

  public void changeState(User user, State state) {
    user.setState(state);
  }
}
