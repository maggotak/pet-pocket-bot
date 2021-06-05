package maggotak.petbot.pets;

import java.time.LocalDate;
import maggotak.petbot.data.DataReader;
import maggotak.petbot.states.PetState;

public class Pet {
  private static final String TO_STRING_PATTERN = DataReader.getValueOf("bot.statistic_pattern");
  private final Pets pet;
  private final LocalDate birthday;
  private PetState petState;

  public Pet(Pets pet, LocalDate birthday, PetState petState) {
    this.pet = pet;
    this.birthday = birthday;
    this.petState = petState;
  }

  public void setPetState(PetState petState) {
    this.petState = petState;
  }

  public PetState getPetState() {
    return petState;
  }

  public Pets getPet() {
    return pet;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  @Override
  public String toString() {
    return String.format(TO_STRING_PATTERN, pet.getName(), birthday.toString());
  }
}
