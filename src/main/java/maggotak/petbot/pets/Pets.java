package maggotak.petbot.pets;

public enum Pets {
  SQUIRREL("\uD83D\uDC3F"), TIGER("\uD83D\uDC2F"), FOX("\uD83E\uDD8A"),
  BAT("\uD83E\uDD87"), UNICORN("\uD83E\uDD84"), LION("\uD83E\uDD81");

  private final String name;

  Pets(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static boolean isPet(String petName) {
    for (Pets p : Pets.values()) {
      if (p.getName().equals(petName)) {
        return true;
      }
    }
    return false;
  }

  public static Pets fromString(String text) {
    for (Pets b : Pets.values()) {
      if (b.name.equals(text)) {
        return b;
      }
    }
    return null;
  }
}
