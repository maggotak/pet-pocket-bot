package maggotak.petbot.users;

import maggotak.petbot.pets.Pet;
import maggotak.petbot.states.State;

public class User {

  private long id;
  private String name;
  private Pet pet;
  private State state;

  public User(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Pet getPet() {
    return pet;
  }

  public void setPet(Pet pet) {
    this.pet = pet;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public boolean hasPet() {
    return pet != null;
  }
}
