package oop;
import java.util.*;

/**
 * Slouží jako tzv. type-conscious ArrayList.
 * Kontejner, který má informaci o typech objektů v něm uložených.
 * Primární funkcí je správa, vkládání, a výběru objektů třídy Problem.
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */
public class KontejnerProblemu extends Kontejner{
  /**
   * Inicilizuje ArrayList problémů.
   */
  public KontejnerProblemu() {
    seznam=new ArrayList();
  }
  /**
   * Přidá problém do ArrayListu.
   */
  public void add(Problem p) {
    seznam.add(p);
  }
  /**
   * Vrátí referenci na problém dle jeho indexu index v ArrayListu.
   */
  public Problem get(int index) {
    return((Problem) seznam.get(index-1));
     /* -1 kvůli přepočtu mezí
      * ArrayList začíná od 0, ALE seznam problémů v souboru začíná od 1 */
  }
  /**
   * Vrátí index do kontejneru na základě textu problému.
   */
  public int get(String nazevProblemu) {
    for(int i=1; i<this.size(); i++) {
      if (this.get(i).getNazevProblemu().compareTo(nazevProblemu) == 0)
        return(i); // +1 transformace kvůli skutečnému číslování
    }
    return(-1); // problem s požadovaným názvem nenalezen

  }
  /**
   * Seřadí ArrayList problémů dle čísel problémů.
   */
  public void seradCisloProblemu() {
    // přirozené řazení problémů dle čísla problému
    Collections.sort((List) seznam);
  }
  /**
   * Seřadí ArrayList problémů dle četností problémů.
   */
  public void seradCetnostProblemu() {
    Collections.sort((List) seznam, Problem.PODLE_CETNOSTI);
  }
  /**
   * Provede nastavení všech problémů (děděno od JCheckBox) na isSelected() == false.
   */
  public void odznacJCheckBoxy() {
    for (int i = 1; i < this.size(); i++) {
      this.get(i).setSelected(false);
    }
  }
  /**
   * Vrátí řetězec čísel všech problémů v kontejneru.
   */
  public String[] vratSeznamCiselProblemu() {
    String[] poleCiselProb = new String[this.size()];
    for(int i=0; i<this.size(); i++) {
      poleCiselProb[i]=new String((new Integer(i+1)).toString());
        // (i+1) kvůli transformaci z indexů v arraylistu na číslování problémů
    }
    return(poleCiselProb);
  }

  /**
  * Vrátí ArrayList čísel a textů nejčastějších problémů uspořádaný sestupně.
  * @param kolikNejcastejsich - kolik vybrat nejčastějších problémů
  */
  public ArrayList vratNejcastejsiProblemy(int kolikNejcastejsich) {
    ArrayList seznamNejc=new ArrayList();

    this.seradCetnostProblemu(); // seřadím dle četnosti vzestupně
    /* vyberu getKolikZobrazitNejcProb posledních prvků kontejneru problemy,
     * ty mají největší četnost */
    for(int i=0; i<kolikNejcastejsich; i++) {
      seznamNejc.add(""+this.get(this.size()-i).getCisloProblemu()+" - "+
                   this.get(this.size()-i).getNazevProblemu());
    }
    this.seradCisloProblemu(); // na konci seřadím problemy opět dle čísel problémů
    return(seznamNejc);
  }


  public static void main(String[] args) {

  }



}