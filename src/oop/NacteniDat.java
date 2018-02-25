package oop;
import java.io.*;
import java.util.*;

/**
 * Načte vstupní soubory chyby, studenti, navstevy.
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */
public class NacteniDat {
  private String fProblemy;
  private String fStudenti;
  private String fNavstevy;
  private KontejnerStudentu studenti;
  private KontejnerProblemu problemy;
  private ZpracujRetezec praceSRetezcem;
  /**
   * Vytvoří objekt třídy NacteniDat. fProblemy, fStudenti a fNavstevy jsou cesty
   * k textovým souborům, studenti a problemy jsou kontejnery dat
   */
  public NacteniDat(String fProblemy, String fStudenti, String fNavstevy,
                    KontejnerStudentu studenti, KontejnerProblemu problemy) {
  this.fProblemy=fProblemy;
  this.fStudenti=fStudenti;
  this.fNavstevy=fNavstevy;
  this.studenti=studenti;
  this.problemy=problemy;
  this.praceSRetezcem=new ZpracujRetezec();

  }
  /**
   * Načte soubor "studenti.txt" do kontejneru studenti a prohodí
   * načtené pořadí jméno a příjmení, upravený řetězec uloží do atributu studenta
   * prijmeniAJmeno
   */
  public void nactiStudenty() {
    BufferedReader souborStudentu;
    String radka;

    try {
      souborStudentu = new BufferedReader(new FileReader(fStudenti));
      while ( (((radka = souborStudentu.readLine()).trim()) != null) && (radka.compareTo("") !=0) ) {
        // test (radka.compareTo("") kvuli možnosti prázdné řádky na konci v čteném souboru
        studenti.add(new Student(praceSRetezcem.vratPrijmeniJmeno(radka)));
      }
      souborStudentu.close();
    }
    catch(IOException e) {
      System.err.println("oop: Chyba cteni souboru: " + fStudenti+" ,soubor musi byt v aktualnim adresari");

    }
  }
  /**
   * Načte soubor "problemy.txt" a zpracuje položky problému na řádce:
   * číslo chyby, klávesová zkratka, text problému, čísla podproblémů.
   * Výsledky uloží do kontejneru problemy.
   */
  public void nactiSeznamProblemu() {
    BufferedReader souborProblemu;
    String radka;
    String nazevProblemu;
    String oddelZkratka="[]";  // oddělovač klávesové zkratky v souboru
    char oddelPodProblemy='#'; // znak uvozující čísla podproblémů
    int klavesZkratka;         // číslo klávesové zkratky
    int offsetZacatek=2;       // přeskočení "nějakého" znaku + mezery --> začátek názvu problému
    int offsetProblemy=1;      // preskočení mezery za textem názvu problému
    int zacatekNazvuProb;      // index začátku názvu problému v řádce
    int konecNazvuProb;        // index konce názvu problému v řádce
    boolean jeZakladniProblem; // true .. řádka neobsahuje znak oddelPodProblemy

    Problem problem;
    ArrayList podProblemy = new ArrayList(); // čísla podproblémů daného problému
    int pocetProblemu=0;       // počítadlo počtu problému a zároveň ID problému

    try {
      souborProblemu = new BufferedReader(new FileReader(fProblemy));
      while ( (radka = souborProblemu.readLine()) != null) {
        pocetProblemu++;
        radka.trim();
        klavesZkratka = praceSRetezcem.vratKlavesZkratku(radka, oddelZkratka);
        podProblemy = praceSRetezcem.vratSeznamPodProblemu(radka, oddelPodProblemy);

        //nalezení indexu ZAČÁTKU názvu problému
        if (klavesZkratka != -1) // nalezena klávesová zkratka
          zacatekNazvuProb=radka.indexOf(oddelZkratka.charAt(1))+offsetZacatek;
        else  // nenalezena kláv. zkratka, název problému je za tečkou+mezerou
          zacatekNazvuProb=radka.indexOf('.')+offsetZacatek;

        //nalezení indexu KONCE názvu problému
        if (podProblemy != null) // nalezen znak oddelPodProblemy --> problém ma podprob
          konecNazvuProb=radka.indexOf(""+oddelPodProblemy)-offsetProblemy;
        else
          konecNazvuProb=radka.length();

        nazevProblemu=radka.substring(zacatekNazvuProb, konecNazvuProb);

        if (podProblemy != null) {
          // přidávaný problém je NADproblémem pro jiné problémy
          problem=new Problem("Všechny problémy",nazevProblemu, pocetProblemu);
          jeZakladniProblem=false;
          problem.setZakladni(jeZakladniProblem);
          for (int i = 0; i < podProblemy.size(); i++) {
            // nalezení podproblému a nastavení nadproblému
            problemy.get( ( (Integer) podProblemy.get(i)).intValue()).setNadProblem(nazevProblemu);
          }

        }
        else // problém je základní
          problem=new Problem(nazevProblemu, pocetProblemu);

        if (klavesZkratka != -1) { // problém má přiřazenu klávesovou zkratku
          problem.setKlavesZkratka(klavesZkratka);
          problem.setMnemonic(problem.getKlavesZkratka());

        }
        problemy.add(problem); // přidání nového problému do kontejneru problemy

      } // while
    }

    catch(IOException e) {
      System.err.println("oop: Chyba cteni souboru: " + fProblemy+" ,soubor musi byt v aktualnim adresari");

    }
  }

  /**
   * Načte soubor "navstevy.txt" a zpracuje položky návštěvy na řádce:
   * jméno, příjmení, datum, časOd, časDo, čísla problémů
   * Výsledky uloží do kontejneru navstevy k příslušnému studentovi.
   */
  public void nactiSeznamNavstev() {
    BufferedReader souborNavstev;
    String radka;
    String cislaProblemu;        // čísla problémů jedné návštěvy
    String hledanePrijmJm;       // příjmení a jméno studenta, určeno k vyhled v kontejneru studenti

    int datNavst=0;              // datum návštěvy - rokMesDen
    String pocNavst="";          // čas počátku návštěvy
    String konNavst="";          // čas konce návštěvy

    Student student;             // student, jehož návštěvy právě analyzuji
    Navsteva navsteva;           // přidávaná návštěva studenta
    KontejnerIndProblemu seznamCiselProblemu;

    StringTokenizer st;          // pomocný pro "rozsekání" návštěv
    StringTokenizer st2;         // pomocné pro rozsekání řetězce čísel problémů návštěvy
    String s;                    // pomocná proměnná pro "rozsekávání" řetězce

    String retProblem;           // číslo problému při návštěvě
    Integer pridavanyProblem;    // číslo problému při návštěvě zabaleno do Integeru

    try {
      souborNavstev = new BufferedReader(new FileReader(fNavstevy));
      while ( (radka = souborNavstev.readLine()) != null) {
        hledanePrijmJm = praceSRetezcem.vratPrijmeniJmeno(radka);
        student = studenti.get(hledanePrijmJm);
        st = new StringTokenizer(radka, "#"); // oddělení jména a příjmení a návštěv
        st.nextToken(); // přeskočení jména a příjmení
        s = st.nextToken(); // s .. seznam všech návštěv
        st = new StringTokenizer(s, ";"); // st použiji pro rozsekání jednotlivých návštěv
        while (st.hasMoreTokens()) { // jsou další návštěvy daného studenta?
          s = st.nextToken(); // záznamy jedné návštěvy ve stringu
          datNavst = Integer.parseInt(s.substring(0, s.indexOf(":")).trim());
          pocNavst = new String((s.substring(s.indexOf(":") + 1, s.indexOf("-")).trim()));
          konNavst = new String((s.substring(s.indexOf("-") + 1, s.lastIndexOf(":")).trim()));
          cislaProblemu = s.substring(s.lastIndexOf(":") + 1, s.length()).trim();
          st2 = new StringTokenizer(cislaProblemu, ","); // průchod čísly problémů
          seznamCiselProblemu = new KontejnerIndProblemu();
          while (st2.hasMoreTokens()) { // dokud cisla problemu oddelena carkami
            retProblem=st2.nextToken(); // načtení jednoho čísla problému z řetězce
            if (retProblem.compareTo("*") != 0) { // "*" nebylo odevzdáno
              pridavanyProblem=new Integer(Integer.parseInt(retProblem));
              seznamCiselProblemu.add(pridavanyProblem);
              // zvýšení četnosti přidávaného čísla problému v kontejneru problemy
              problemy.get(pridavanyProblem.intValue()).setCetnostProblemu();
            }
            else { // indikace "*" --> student odevzdal
              student.setOdevzdano(true); // nalezena "*" v seznamu problemu
              //seznamCiselProblemu=null;
              // --> student odevzdal praci
            }
          } //while
          student.setCelkovyCasNavstev(praceSRetezcem.spoctiCas(pocNavst, konNavst));
          navsteva = new Navsteva(datNavst, pocNavst, konNavst);
          navsteva.problemyInd=seznamCiselProblemu;
          student.addNavsteva(navsteva);

          // nastavení statických atributů učitele
          Vyucujici.setCelkDoba(praceSRetezcem.spoctiCas(pocNavst, konNavst));
          Vyucujici.setCelkPruchodu();

        } //while navstevy
      } // while not konec soub
    }
    catch(IOException e) {
      System.err.println("oop: Chyba cteni souboru: " + fNavstevy+" ,soubor musi byt v aktualnim adresari");
      System.err.println("oop: Nepodarilo se nacist vstupni soubory, program bude ukoncen ..");
      System.exit(1);
    }
  }

  public static void main(String[] args) {

  }

}