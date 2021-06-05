package maggotak.petbot;

import java.util.ArrayList;
import java.util.List;

import static maggotak.petbot.data.DataConstants.*;

import static maggotak.petbot.data.DataReader.*;

import maggotak.petbot.handler.AnswerHandler;
import maggotak.petbot.pets.Pets;
import maggotak.petbot.states.PetState;
import maggotak.petbot.states.State;
import maggotak.petbot.states.StateConfig;
import maggotak.petbot.users.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PetBot extends TelegramLongPollingBot {

  private final static Logger logger = LogManager.getRootLogger();
  public static String BOT_TOKEN = System.getenv("token");
  public static String BOT_USERNAME = System.getenv("name");

  @Override
  public String getBotUsername() {
    return BOT_USERNAME;
  }

  @Override
  public String getBotToken() {
    return BOT_TOKEN;
  }

  public void onUpdateReceived(Update update) {
    String chatId = update.getMessage().getChatId().toString();
    long userId = update.getMessage().getFrom().getId();
    String answer = "";

    StateConfig config = new StateConfig();
    User user = config.checkUserExistence(userId);
    State usersState = config.getUsersState(user);
    logger.info("Users state:" + usersState);

    AnswerHandler answerHandler = new AnswerHandler();
    if (update.hasMessage() && BOT_START.equals(update.getMessage().getText())) {
      answer = getValueOf(BOT_GREETING);
      config.changeState(user, State.MENU);
    } else {
      switch (usersState) {
        case CHOOSE:
          answer = answerHandler.howToAnswerToChoosePet(update, user);
          break;
        case PLAY:
          answer = answerHandler.howToAnswerSpeak(update, user);
          break;
        case FEED:
          answer = answerHandler.howToAnswerFeed(update, user);
          break;
        case SLEEP:
          answer = answerHandler.howToAnswerSleep(update, user);
          break;
        case MENU:
          answer = answerHandler.howToAnswerMenu(update, user);
          break;
      }
    }

    sendAnswer(chatId, answer, user);


  }

  private void sendAnswer(String chatId, String text, User user) {
    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText(text);

    if (user.getState() == State.CHOOSE) {
      sendPetChooseKeyboard(message);
    }

    if (user.getState() == State.MENU) {
      sendMenuKeyboard(message);
    }

    if (user.getState() == State.FEED && user.getPet().getPetState() != PetState.SLEEP) {
      sendFeedKeyboard(message);
    }

    if (user.getState() == State.PLAY && user.getPet().getPetState() != PetState.SLEEP) {
      sendSpeakKeyboard(message);
    }

    if (user.getState() == State.SLEEP) {
      sendSleepKeyboard(message);
    }

    try {
      execute(message);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }


  private void sendPetChooseKeyboard(SendMessage message) {

    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboard = new ArrayList<>();

    KeyboardRow row = new KeyboardRow();
    row.add(Pets.SQUIRREL.getName());
    row.add(Pets.TIGER.getName());
    keyboard.add(row);

    row = new KeyboardRow();
    row.add(Pets.FOX.getName());
    row.add(Pets.BAT.getName());
    keyboard.add(row);

    row = new KeyboardRow();
    row.add(Pets.UNICORN.getName());
    row.add(Pets.LION.getName());
    keyboard.add(row);

    keyboardMarkup.setKeyboard(keyboard);
    message.setReplyMarkup(keyboardMarkup);

  }

  private void sendMenuKeyboard(SendMessage message) {

    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboard = new ArrayList<>();

    KeyboardRow row = new KeyboardRow();
    row.add(getValueOf(MAIN_MENU_CHOOSE));
    row.add(getValueOf(MAIN_MENU_STATISTIC));
    keyboard.add(row);

    row = new KeyboardRow();
    row.add(getValueOf(MAIN_MENU_FEED));
    row.add(getValueOf(MAIN_MENU_PLAY));
    keyboard.add(row);

    row = new KeyboardRow();
    row.add(getValueOf(MAIN_MENU_SLEEP));
    row.add(getValueOf(MAIN_MENU_DELETE));
    keyboard.add(row);

    keyboardMarkup.setKeyboard(keyboard);
    message.setReplyMarkup(keyboardMarkup);

  }

  private void sendFeedKeyboard(SendMessage message) {
    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboard = new ArrayList<>();

    KeyboardRow row = new KeyboardRow();
    row.add(getValueOf(FOOD_MEAT));
    row.add(getValueOf(FOOD_CHEESE));
    keyboard.add(row);

    row = new KeyboardRow();
    row.add(getValueOf(FOOD_MILK));
    row.add(getValueOf(FOOD_APPLE));
    keyboard.add(row);

    row = new KeyboardRow();
    row.add(getValueOf(BACK_TO_MENU));
    keyboard.add(row);

    keyboardMarkup.setKeyboard(keyboard);
    message.setReplyMarkup(keyboardMarkup);

  }

  private void sendSpeakKeyboard(SendMessage message) {
    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboard = new ArrayList<>();

    KeyboardRow row = new KeyboardRow();
    row.add(getValueOf(TO_SCRATCH_PET));
    row.add(getValueOf(TO_PET));
    keyboard.add(row);

    row = new KeyboardRow();
    row.add(getValueOf(TO_THROW_A_BALL));
    row.add(getValueOf(TO_KICK));
    keyboard.add(row);

    row = new KeyboardRow();
    row.add(getValueOf(BACK_TO_MENU));
    keyboard.add(row);

    keyboardMarkup.setKeyboard(keyboard);
    message.setReplyMarkup(keyboardMarkup);

  }

  private void sendSleepKeyboard(SendMessage message) {
    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboard = new ArrayList<>();

    KeyboardRow row = new KeyboardRow();
    row.add(getValueOf(WAKE_UP));
    keyboard.add(row);

    row = new KeyboardRow();
    row.add(getValueOf(BACK_TO_MENU));
    keyboard.add(row);

    keyboardMarkup.setKeyboard(keyboard);
    message.setReplyMarkup(keyboardMarkup);

  }
}
