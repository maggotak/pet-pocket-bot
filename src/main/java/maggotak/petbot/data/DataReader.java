package maggotak.petbot.data;

import java.util.ResourceBundle;

public class DataReader {
  private static ResourceBundle resourceBundle = ResourceBundle.getBundle("bot");

  public static String getValueOf(String key){
    return resourceBundle.getString(key);
  }
}
