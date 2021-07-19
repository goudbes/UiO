public class LinkedList {
  public static void main(String[] args) {
    LenkeListe<Integer> list = new LenkeListe<Integer>();
    LenkeListe<String> names = new LenkeListe<String>();
    list.leggTil(15);
    list.leggTil(10);
    list.leggTil(3);
    list.leggTil(56);

    while(!list.tom()) {
      System.out.println(list.fjernMinste());
    }
    System.out.println("");

    names.leggTil("Jonas");
    names.leggTil("Franz Ferdinand");
    names.leggTil("Zorg");
    names.leggTil("Mr. Potato");

    while (!names.tom()) {
      System.out.println(names.fjernMinste());
    }

    names.leggTil("Jonas");
    names.leggTil("Franz Ferdinand");
    names.leggTil("Zorg");
    names.leggTil("Mr. Potato");

    System.out.println("");

    if (names.inneholder("Zorg")==true) {
      System.out.println("There is such element");
    } else {
      System.out.println("There is no such element");
    }
  }
}
