package maggotak.petbot.handler;

import java.time.LocalDateTime;

import static maggotak.petbot.data.DataConstants.*;
import static maggotak.petbot.data.DataReader.*;

import java.util.Locale;
import maggotak.petbot.pets.Pet;
import maggotak.petbot.pets.Pets;
import maggotak.petbot.states.PetState;
import maggotak.petbot.users.User;
import maggotak.petbot.states.State;
import maggotak.petbot.states.StateConfig;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AnswerHandler {

  private static StateConfig config = new StateConfig();


  public String howToAnswerToChoosePet(Update update, User user) {
    if (Pets.isPet(update.getMessage().getText())) {
      config.changeState(user, State.MENU);
      user.setPet(new Pet(Pets.fromString(update.getMessage().getText()),
          LocalDateTime.now().toLocalDate(), PetState.NOT_SLEEP));
      return chosePet(update.getMessage().getText());
    }
    return getValueOf(ONLY_EMOJI);
  }

  public String howToAnswerSpeak(Update update, User user) {
    if (user.getPet() == null) {
      return getValueOf(NOT_HAVE_PET_YET);
    }
    if (update.getMessage().getText().equals(getValueOf(MAIN_MENU_PLAY))
        && user.getPet().getPetState() != PetState.SLEEP) {
      config.changeState(user, State.PLAY);
      return getValueOf(PLAY_WITH_PET);
    } else if (user.getPet().getPetState() == PetState.SLEEP) {
      return getValueOf(PET_IS_SLEEPING);
    } else if (update.getMessage().getText().equals(getValueOf(BACK_TO_MENU))) {
      config.changeState(user, State.MENU);
      return getValueOf(WHAT_TO_DO);
    } else {
      config.changeState(user, State.PLAY);
      return playPet(update);
    }
  }

  public String howToAnswerSleep(Update update, User user) {
    if (user.getPet() == null) {
      return getValueOf(NOT_HAVE_PET_YET);
    }
    if (update.getMessage().getText().equals(getValueOf(GO_TO_SLEEP))) {
      config.changeState(user, State.SLEEP);
      user.getPet().setPetState(PetState.SLEEP);
      return getValueOf(SLEEPING);
    } else if (update.getMessage().getText().equals(getValueOf(BACK_TO_MENU))) {
      config.changeState(user, State.MENU);
      return getValueOf(WHAT_TO_DO);
    } else {
      config.changeState(user, State.SLEEP);
      return sleepPet(update, user);
    }
  }

  public String howToAnswerFeed(Update update, User user) {
    if (user.getPet() == null) {
      return getValueOf(NOT_HAVE_PET_YET);
    }
    if (update.getMessage().getText().equals(getValueOf(MAIN_MENU_FEED))
        && user.getPet().getPetState() != PetState.SLEEP) {
      config.changeState(user, State.FEED);
      return getValueOf(WHAT_TO_FEED);
    } else if (user.getPet().getPetState() == PetState.SLEEP) {
      return getValueOf(PET_IS_SLEEPING);
    } else if (update.getMessage().getText().equals(getValueOf(BACK_TO_MENU))) {
      config.changeState(user, State.MENU);
      return getValueOf(WHAT_TO_DO);
    } else {
      config.changeState(user, State.FEED);
      return feedPet(update);
    }
  }

  public String howToAnswerMenu(Update update, User user) {
    String answer = getValueOf(UNCLEAR);
    if (update.hasMessage()) {
      String text = update.getMessage().getText();
      if (text.equals(getValueOf(MAIN_MENU_CHOOSE))) {
        answer = chooseInMenu(user);
      }
      if (text.equals(getValueOf(MAIN_MENU_FEED))) {
        answer = howToAnswerFeed(update, user);
      }
      if (text.equals(getValueOf(MAIN_MENU_PLAY))) {
        answer = howToAnswerSpeak(update, user);
      }
      if (text.equals(getValueOf(MAIN_MENU_SLEEP))) {
        answer = howToAnswerSleep(update, user);
      }
      if (text.equals(getValueOf(MAIN_MENU_DELETE))) {
        answer = delete(user);
      }
      if (text.equals(getValueOf(MAIN_MENU_STATISTIC))) {
        answer = statistic(user);
      }
    }
    return answer;
  }

  private String chosePet(String message) {
    Pets pet = Pets.fromString(message);
    String petName;
    switch (pet) {
      case SQUIRREL:
        petName = getValueOf(CHOSEN_SQUIRREL);
        break;
      case BAT:
        petName = getValueOf(CHOSEN_BAT);
        break;
      case LION:
        petName = getValueOf(CHOSEN_LION);
        break;
      case FOX:
        petName = getValueOf(CHOSEN_FOX);
        break;
      case TIGER:
        petName = getValueOf(CHOSEN_TIGER);
        break;
      case UNICORN:
        petName = getValueOf(CHOSEN_UNICORN);
        break;
      default:
        petName = getValueOf(CHOSEN_NOBODY);
    }
    return String.format(getValueOf(CHOSEN_PATTERN), petName);
  }

  private String delete(User user) {
    user.setPet(null);
    return getValueOf(PET_DELETED);
  }

  private String chooseInMenu(User user) {
    if (user.getPet() != null) {
      return getValueOf(ONLY_ONE);
    }
    config.changeState(user, State.CHOOSE);
    return getValueOf(CHOOSE_PET);
  }

  private String feedPet(Update update) {
    String text = update.getMessage().getText();
    if (!text.equals(getValueOf(FOOD_MEAT)) && !text.equals(getValueOf(FOOD_CHEESE))
        && !text.equals(getValueOf(FOOD_MILK)) && !text.equals(getValueOf(FOOD_APPLE))) {
      return getValueOf(NO_SUCH);
    }
    return String.format(getValueOf(SO_TASTY), text.toLowerCase());
  }

  private String playPet(Update update) {
    String text = update.getMessage().getText();
    if (text.equals(getValueOf(TO_KICK))) {
      return getValueOf(DO_NOT_KICK_PET);
    }
    if (!text.equals(getValueOf(TO_SCRATCH_PET)) && !text.equals(getValueOf(TO_PET))
        && !text.equals(getValueOf(TO_THROW_A_BALL))) {
      return getValueOf(PET_DO_NOT_UNDERSTAND);
    }
    return getValueOf(PET_HAPPY);
  }

  private String sleepPet(Update update, User user) {
    String text = update.getMessage().getText();
    if (getValueOf(WAKE_UP).equals(text)) {
      config.changeState(user, State.MENU);
      user.getPet().setPetState(PetState.NOT_SLEEP);
      return getValueOf(PET_WAKED_UP);
    }
    return getValueOf(PET_DO_NOT_WAKE_UP);
  }

  private String statistic(User user) {
    if (user.getPet() == null) {
      return getValueOf(NOT_HAVE_PET_YET);
    }
    return user.getPet().toString();
  }
}
