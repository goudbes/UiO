public class Oblig1 {
  public static void main(String[] args) {
    //Lag Bol<Mus> musebol og Bol<Rotte> rottebol
    Bol<Mus> musebol = new Bol<Mus>();
    Bol<Rotte> rottebol = new Bol<Rotte>();
    //Lag en katt
    Katt katten = new Katt("Tom");
    //La katten gaa paa jakt og sjekk at det skrives ut at katten ikke fant noen
    katten.jakt(musebol,rottebol);
    //opprett ei rotte
    Rotte rotte = new Rotte("Ronny");
    //Sett rotta du lagde i forrige punkt inn i rottebolet
    rottebol.settInn(rotte);
    //la kattem gaa paa jakt paa nytt. Sjekk at rotta faar state som skadet
    katten.jakt(musebol,rottebol);
    //lag en mus
    Mus mus = new Mus("Jerry");
    //sett musen inn i musebolet
    musebol.settInn(mus);
    //Lag en ny mus
    Mus mus_test = new Mus("Daphne");
    //Proev aa sette inn musen i musebolet..
    musebol.settInn(mus_test);
    //La katten gaa paa jakt igjen. Sjekk at katten angriper musen og musen endrer tilstand.
    katten.jakt(musebol,rottebol);
  }
}
