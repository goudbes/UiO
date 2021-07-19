public class DoublyLinkedMain {
  public static void main(String[] args) {
    DoublyLinkedList<Integer> list = new DoublyLinkedList<Integer>();
    DoublyLinkedList<String> names = new DoublyLinkedList<String>();

    //Adding the numbers
    list.leggTil(2);
    list.leggTil(45);
    list.leggTil(237);
    list.leggTil(23);
    list.leggTil(1);
    list.leggTil(986);

    //Adding the names and then removing them again
    names.leggTil("Rollo");
    names.leggTil("David");
    names.leggTil("Shannon");
    names.leggTil("Fox");

    while(!list.tom())
    System.out.println(list.fjernMinste());

    System.out.println("");

    while(!names.tom())
    System.out.println(names.fjernMinste());

    //Adding names again to check if the list contains specific name
    names.leggTil("Rollo");
    names.leggTil("David");
    names.leggTil("Shannon");
    names.leggTil("Fox");

    if (names.inneholder("Rollo")==true) {
      System.out.println("The list contains this name");
    } else {
      System.out.println("The list does not contain this name");
    }
  }
}
