package oop;
import java.util.*;
import java.text.*;

/**
 * Nese informace o sledovaných atributech studenta:
 * příjmení a jméno, celkový čas návštěv u učitele, odevzdal/neodevzdal.
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */
public class Student implements Comparable {
  private String prijmeniAJmeno;
  private long celkovyCasNavstev=0;
  private boolean odevzdano=false;
  private KontejnerNavstev navstevy;

  /**
   * Vytvoří objekt studenta s příjmením a jménem uloženým v parametru prijmJm.
   */
  public Student(String prijmJm) {
    if (prijmJm == null)
      throw new NullPointerException();
    this.prijmeniAJmeno=prijmJm;
    navstevy=new KontejnerNavstev();
  }

  public String toString() {
    return("Jméno: "+prijmeniAJmeno+"/n"+"Celkový čas návštěv: "+celkovyCasNavstev+"/n");
  }

  public boolean equals(Object o) {
    if (o==this)
      return(true);
    if ((o instanceof Student) == false)
      return(false);
    /* následující je pravděpodobně zbytečné, protože já potřebuji stejně jen o==this
     * tzn. mám stejné reference, protože v reálu neexistují úplně stejní lidé
     */
    Student stud = (Student) o;
    boolean stejnePrijmeniAJmeno = prijmeniAJmeno.equals(stud.prijmeniAJmeno);
    boolean stejnyCelkovyCasNavstev = (celkovyCasNavstev==stud.celkovyCasNavstev);
    boolean stejneOdevzdano = (odevzdano == stud.odevzdano);
    boolean stejneNavstevy = navstevy.equals(stud.navstevy); // stejné reference
    return(stejnePrijmeniAJmeno && stejnyCelkovyCasNavstev && stejneOdevzdano
           && stejneNavstevy);
  }

  public int hashCode() {
    int vysledek = 17;
    int pom;
    pom=this.odevzdano ? 0:1;
    vysledek=37*vysledek+pom;
    pom=(int)this.celkovyCasNavstev; // možná loss of precision, ALE to mi zde tolik nevadí
    vysledek=37*vysledek+pom;
    pom=this.prijmeniAJmeno.hashCode();
    vysledek=37*vysledek+pom;
    pom=this.navstevy.hashCode();
    vysledek=37*vysledek+pom;
    return(vysledek);
  }
  /**
   * Přirozené řazení dle příjmení a jména.
   */
  public int compareTo(Object o) {
    Student s = (Student) o;
    Collator col = Collator.getInstance(new Locale("cs","CZ"));

    String s1= this.prijmeniAJmeno;
    String s2= ((Student) o).getPrijmeniAJmeno();
  return(col.compare(s1, s2));

  }
  /**
   * Vrátí počet návštěv studenta u vyučujícího.
   */
  public int getPocetNavstev() {
    return(navstevy.size());
  }
  /**
   * Přidá novou návštěvu studenta u vyučujícího do kontejneru návštěv studenta.
   */
  public void addNavsteva(Navsteva n) {
    navstevy.add(n);
  }
  /**
   * Vrátí řetězec návštěv tvaru: "datum:časOd-časDo:".
   */
  public String vratNavstevy() {
    String strNav="";
    if (navstevy.size()>0) {
      for(int i=0; i<navstevy.size(); i++) {
        strNav=new String(strNav+navstevy.get(i).pomVratNavstevu());
      }
    }
    return(strNav+"\n");
  }

  /**
   * Vrátí příjmení studenta z řetězce příjmení a jména.
   */
  public String getPrijmeni() {
    StringTokenizer st = new StringTokenizer(prijmeniAJmeno);
    return(st.nextToken());
  }
  /**
   * Vrátí jméno studenta z řetězce příjmení a jména.
   */
  public String getJmeno() {
    StringTokenizer st = new StringTokenizer(prijmeniAJmeno);
    st.nextToken();
    return(st.nextToken());
  }
  /**
   * Vrátí příjmení a jméno studenta v jednom řetězci.
   */
  public String getPrijmeniAJmeno() {
    return(prijmeniAJmeno);
  }
  /**
   * Vrátí informaci o stavu odevzdání práce studenta.
   */
  public boolean getOdevzdano() {
    return(odevzdano);
  }
  /**
   * Nastaví stav odevzdání práce studenta.
   */
  public void setOdevzdano(boolean b) {
    odevzdano=b;
  }
  /**
   * Vrátí celkový čas strávený u učitele během všech návštěv.
   */
  public long getCelkovyCasNavstev() {
    return(celkovyCasNavstev);
  }
  /**
   * Zvžší celkový čas strávený u učitele během všech návštěv o prirustek.
   */
  public void setCelkovyCasNavstev(long prirustek) {
    if (prirustek > 0)
      celkovyCasNavstev=celkovyCasNavstev+prirustek;
    else {
      System.out.println("oop: Chyba: zaporny cas");
      new ArithmeticException();
    }
  }

  /*
   * Využívám triku, že do TreeSet se položky stejné hodnoty nevloží -->
   * i kdy6 se o to pokusím, stále mám v množině jen unikátní čísla problémů
   */
  /**
   * Vrací všechny unikátní čísla problémů ze všech návštěv studenta u učitele.
   */
  public String vratProblemyNavstev() {
   Navsteva navsteva;
   Set uspMnozinaProblemu = new TreeSet();
   String sProb;

   for(int i=0; i<navstevy.size(); i++) { // projdu všechny návštěvy
     navsteva = navstevy.get(i);
     for (int j = 0; j < navsteva.problemyInd.size(); j++) {
       // projdu všechna čísla problémů jedné návštěvy
       uspMnozinaProblemu.add(new Integer(navsteva.problemyInd.get(j)));

     }
   }

   sProb=uspMnozinaProblemu.toString();
   // vyhození "standardních" závorek [ ] uvozujících čísla množiny
   sProb = new String(sProb.substring(sProb.indexOf("[")+1, sProb.indexOf("]")));
   if (sProb.compareTo("") == 0 )
     sProb=new String("-");
   return(sProb);

 }

 /*
  * Využívat triku vložení dvojice se stejným klíčem a jinou hodnotou --> přemazání
  */
 /**
  * Vrací uspořádanou mapu, kde klíčem je číslo problému a hodnotou jeho četnost.
  */
 public TreeMap vratProblemySCetnosti() {
   Navsteva aktNavsteva;
   TreeMap hm = new TreeMap();
   Integer cisloProb = new Integer(1);   // inicializace čísla problému
   Integer cetnostProb = new Integer(1); // inicializace četnosti problému

   for(int i=0; i<navstevy.size(); i++) { // projdu kontejner návštěv pro příslušného studenta
     aktNavsteva=navstevy.get(i);
     for(int j=0; j<aktNavsteva.problemyInd.size(); j++) {
       cisloProb=new Integer(aktNavsteva.problemyInd.get(j));
       if (hm.containsKey(cisloProb)) { // student "absolvoval" dané číslo problému
         // uložení původní četnosti daného problému
         cetnostProb=new Integer(((Integer) (hm.get(cisloProb))).intValue());
         /* číslo problému již jednou student "absolvoval" při jiné návštěvě
          * --> zvýším četnost +1 */
         cetnostProb=new Integer(cetnostProb.intValue()+1);
         // uložím do mapy číslo(klíč) a četnost(hodnotu) problému
         hm.put(new Integer(cisloProb.intValue()), new Integer(cetnostProb.intValue()));
        }
       else /* student neabsolvoval dané číslo problému --> vložím znovu
             * dvojici cisloProblemu, cetnost --> "přemazání" původního záznamu */
       hm.put(cisloProb, new Integer(1));
     }
   }
 return(hm);
 }

 /**
  * Proměnná typu Comparator pro absolutní řazení dle celkové doby strávené u
  * učitele.
  */
 public static final Comparator PODLE_CELK_DOBA = new Comparator() {
   /**
    * Absolutní řazení dle celkové doby strávené u učitele.
    */
  public int compare(Object o1, Object o2) {
    long doba1 = ((Student) o1).getCelkovyCasNavstev();
    long doba2 = ((Student) o2).getCelkovyCasNavstev();

    if (doba1>doba2)
      return(1);
    else
      if (doba1<doba2)
        return(-1);
      else
        return(0);
  }
 };

  public static void main(String[] args) {
  }

}