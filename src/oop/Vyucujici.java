package oop;
import java.util.*;
import java.text.*;
import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Pracuje s kontejnery studenti, problemy.
 * Provádí ukládání nasbíraných hodnot. Vytváří objekty prezentační logiky. </p>
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */
public class Vyucujici extends Observable {
  private static final String HTML_STATISTIK= "statistiky.html";

  private KontejnerStudentu studenti;
  private KontejnerProblemu problemy;
  private Student student;            // aktuální návštěva
  private int IDStudenta=0;           // ID studenta v JListu
  private String pocatekNavstevy=new String("000000");
    // uložení při aktuálního času při příchodu studenta
  private String konecNavstevy=new String("000000");
    // uložení při aktuálního času při ukončení návštěvy studenta
  private final String fNavstevy;
  private static long celkDoba=0;     // celková doba všech studentů u učitele
  private static long celkPruchodu=0; // počet návštěv všech studentů u učitele
  /**
   * Vytvoří objekt třídy Vyucujici, který potřebuje informace z kontejnerů studenti,
   * problemy a cestu k pomocnému souboru fNavstevy, kam se ukládají záznamy o
   * návštěvách studentů.
   *
   */
  public Vyucujici(KontejnerStudentu studenti, KontejnerProblemu problemy, String fNavstevy) {
    this.studenti=studenti;
    this.problemy=problemy;
    this.fNavstevy=fNavstevy;
    // zajištění vyjímání studentů, kteří odevzdají z JListu
    this.addObserver(new ZobrazeniVyber(this));
  }
  /**
   * Zvýší celkovou dobu studentů u vyučujícího o přírůstek.
   */
  public static void setCelkDoba(long prirustek) {
    celkDoba=celkDoba+prirustek;
  }
  /**
   * Vrátí aktuální dobu všech studentů u vyučujícího.
   */
  public static long getCelkDoba() {
    return(celkDoba);
  }
  /**
   * Zvýší počítadlo návštěv studentů o 1.
   */
  public static void setCelkPruchodu() {
    celkPruchodu++;
  }
  /**
   * Vrátí aktuální počet návštěv všech studentů u vyučujícího.
   */
  public static long getCelkPruchodu() {
    return(celkPruchodu);
  }
  /**
   * Nastaví atribut pocatekNavstevy na čas příchodu studenta k vyučujícímu.
   */
  public void setPocatekNavstevy(String hodnota) {
    this.pocatekNavstevy=new String(hodnota);
  }

  /**
   * Vrátí počátek návštěvy studenta u vyučujícího.
   */
  public String getPocatekNavstevy() {
    return(this.pocatekNavstevy);
  }
  /**
   * Nastaví atribut konecNavstevy na čas odchodu studenta od vyučujícího.
   */
  public void setKonecNavstevy(String hodnota) {
    this.konecNavstevy=new String(hodnota);
  }
  /**
   * Vrátí konec návštěvy studenta u vyučujícího.
   */
  public String getKonecNavstevy() {
   return(this.konecNavstevy);
 }
 /**
  * Nastaví číslo studenta IDStudenta na jeho číslo id v listModelu JListu.
  */
  public void setIDStudenta(int id) {
    this.IDStudenta=id;
  }
  /**
   * Vrátí id v listModelu studenta.
   */
  public int getIDStudenta() {
    return(this.IDStudenta);
  }
  /**
   * Vrátí listModel příjmení a jmen studentů, kteří ještě neodevzdali.
   */
  public DefaultListModel seznamJmenStudentu() {
    return(studenti.vratSeznamJmenListModel());
  }
  /**
   * Vrátí seznam čísel všech možných problémů ( určeno pro JComboBox ).
   */
  public String[] seznamCiselProblemu() {
    return(problemy.vratSeznamCiselProblemu());
  }
  /**
   * Vrátí referenci na kontejner problémů.
   */
  public KontejnerProblemu getKontejnerProblemu() {
    // ! není bezpečné
    return(problemy);
  }
  /**
   * Vrátí referenci na kontejner studentů.
   */
  public KontejnerStudentu getKontejnerStudentu() {
    return(studenti);
  }
  /**
   * Nastaví referenci student na aktulního studenta u vyučujícího.
   */
  public void setStudent(String jmenoStudenta) {
    student=studenti.get(jmenoStudenta);
  }
  /**
   * Vrátí referenci na aktuálního studenta u učitele.
   */
  public Student getStudent() {
    return(student);
  }

  /**
   * Vrátí aktuální čas ve formátu HHmmss.
   */
  public String aktualCas() {
    Calendar cal=new GregorianCalendar();
    SimpleDateFormat ds=new SimpleDateFormat("HHmmss");
    return(ds.format(cal.getTime()));
  }

  /**
   * Vrátí aktuální datum ve formátu yyyyMMdd.
   */
  public int aktualDatum() {
    Date d = new Date();
    Locale lo = Locale.getDefault();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");

    return(Integer.parseInt(sdf.format(d)));
  }

  /**
   * Zapisuje sledované hodnoty atributů do souboru "statistiky.html".
   * @param zobrazNulyCasu - zápis nevýznamových nul času, viz 2 a 02
   * @param kolikZobrazitNejcProb - počet zobrazovaných nejčastěji se vyskytujících problémů
   */
  public void generujStatistiky(boolean zobrazNulyCasu, int kolikZobrazitNejcProb) {
    GeneratorStatistik gs = new GeneratorStatistik(studenti, problemy, HTML_STATISTIK,
                                                    zobrazNulyCasu, kolikZobrazitNejcProb);
    gs.zapisHodnoty();
  }
  /**
   * Načte stavy JCheckBoxů problémů, selected == true --> uloží číslo problému.
   * @param navsteva - návštěva studenta jíž analyzuji
   * @param odevzdano - true .. student odevzdal --> nemusím kontrolovat stavy
   */
  private void zpracujJCheckBoxy(Navsteva navsteva, boolean odevzdano) {
    int indexNadProb=1; // index nadproblému k aktuálně testovanému
    int i=1;            // index aktuálně kontrolovaného problému
    boolean celaSkupina=true; // indikátor označení celé skupiny

    if (odevzdano == false ) {
      while (i<problemy.size()) { // kontrola zaškrtnutých problémů
        // problém má nadproblém --> skočím zkontrolovat nejdříve ten
        if (problemy.get(i).getNadProblem().compareTo("") != 0) {
          indexNadProb = problemy.get(problemy.get(i).getNadProblem());
          if (problemy.get(indexNadProb).isSelected() == true) {
            problemy.get(indexNadProb).setCetnostProblemu();
            navsteva.problemyInd.add(new Integer(problemy.get(indexNadProb).
                                             getCisloProblemu()));
            i = indexNadProb;
            // přeskočím kontrolu podproblémů, protože je zaškrtnutý nadproblém
            //+1 abych se dostal ZA nadproblém
          }
          else {
            /* ještě zkontroluji, jestli nejsou označeny všechny problémy skupiny
             * vyjma sdružujícího problému "označ všechny" */
            celaSkupina = true;
            for (int j = i; j < indexNadProb; j++) {
              if (problemy.get(j).isSelected() == false)
                celaSkupina = false; // alespoň jeden prvek skupiny odznačen
            }
            if (celaSkupina == true) { // celá skupina vyjma "všechny problémy" označena
              problemy.get(indexNadProb).setCetnostProblemu();
              navsteva.problemyInd.add(new Integer(problemy.get(indexNadProb).
              getCisloProblemu()));
              i = indexNadProb;
            }
            else { // celá skupina není označena --> projdu skupinu a uložím zaškrtlé
              for (int j = i; j < indexNadProb; j++) {
                if (problemy.get(j).isSelected() == true) {
                  problemy.get(j).setCetnostProblemu();
                  navsteva.problemyInd.add(new Integer(problemy.get(j).
                      getCisloProblemu()));
                }
              }
              i=indexNadProb; // skupina vyřízena --> posuv na nadproblém

            }

          }
        }
        else { // problém nemá nadproblém
          if (problemy.get(i).isSelected() == true) {
            problemy.get(i).setCetnostProblemu();
            navsteva.problemyInd.add(new Integer(problemy.get(i).getCisloProblemu()));
          }
        }
        i++;
      } // while
    }
    else { /* nemá cenu kontrolovat problémy (Jcheckboxy)
            * odevzdáno --> pouze bezchybná práce */
      student.setOdevzdano(true);
    }

  }

  /**
   * Ukládá hodnoty atributů proběhlé návštěvy.
   * Přidává novou návštěvu do seznamu návštěv studenta.
   * @param odevzdano - určuje, zda student odevzdal práci, tzn. nebyli již nalezeny chyby
   */
  public void kalkulaceNavstevy( boolean odevzdano ) {
     Student student;
     Navsteva navsteva;
     ZpracujRetezec zpracuj = new ZpracujRetezec();
     student=this.getStudent();

     String pocNavst=this.getPocatekNavstevy();

     this.setKonecNavstevy(this.aktualCas());
     String konNavst=this.getKonecNavstevy();

     this.setCelkDoba(zpracuj.spoctiCas(pocNavst, konNavst));
     this.setCelkPruchodu();
     navsteva = new Navsteva(this.aktualDatum(), this.getPocatekNavstevy(),
                             this.getKonecNavstevy());


     zpracujJCheckBoxy(navsteva, odevzdano);
     problemy.odznacJCheckBoxy(); // odznačení(deselection) všech JCheckBoxů

     student.addNavsteva(navsteva);
     student.setCelkovyCasNavstev(zpracuj.spoctiCas(pocNavst, konNavst));
     if (odevzdano == true) {
       /* studenta odevzdal --> pošlu zprávu objektu třídy ZobrazeniVyber, aby
        * vyjmula studenta z JListu neodevzdaných studentů */
       setChanged();
       notifyObservers(new Integer(this.getIDStudenta()));
     }

   }

   /**
    * Uloží do souboru "navstevy.txt" sledované parametru studentů z kontejneru studenti
    */
   public void ukonciCinnost() {
     Student student;
     String JmPrijm;

     try {
       BufferedWriter souborNavstev = new BufferedWriter(new FileWriter(fNavstevy));
       PrintWriter fp = new PrintWriter(souborNavstev);
       for (int i = 0; i < studenti.size(); i++) {
         student = studenti.get(i);

         if (student.getPocetNavstev() > 0) {
          // přehození jména a příjmeni studenta
          JmPrijm = new ZpracujRetezec().vratPrijmeniJmeno(student.getPrijmeniAJmeno());
          if (student.getPocetNavstev() > 0)
            fp.print(JmPrijm + " # " + student.vratNavstevy());
         }

       }
       fp.close();
     }
     catch(IOException e) {
       System.err.println("oop: Chyba čtení souboru: " + fNavstevy);
     }
  }

   /**
    * Vrátí ArrayList řetězců, kde jedna položka obsahuje: četnost problému a
    * jeho text.
    * @param probCetnost - TreeMapa, kde klíčem je číslo problému a hodnotou
    * jeho četnost
    */
   public ArrayList vratProblemySCetnostiArray(TreeMap probCetnost) {
    int cisloProb=0;
    int cetnostProb=0;
    String textProblemu = new String("");
    ArrayList cetnostiCisla = new ArrayList(); // pomocný, pro seřazení dle četností
    ArrayList cislaSTextyProblemu = new ArrayList();

    for(Iterator it = probCetnost.entrySet().iterator(); it.hasNext();) {
      Map.Entry e = (Map.Entry) it.next();
      cisloProb = ((Integer) e.getKey()).intValue();
      cetnostProb = ((Integer) e.getValue()).intValue();
      cetnostiCisla.add(new PrvekMapy(cetnostProb, cisloProb));
    }
    Collections.sort(cetnostiCisla, new PrvekMapy(1,1).podleCetnosti);
    Collections.reverse(cetnostiCisla);
    for(int i=0; i<cetnostiCisla.size(); i++) {
      cetnostProb=((PrvekMapy) cetnostiCisla.get(i)).cetnost;
      cisloProb=((PrvekMapy) cetnostiCisla.get(i)).cisloProblemu;
      textProblemu = new String(this.problemy.get(cisloProb).getNazevProblemu());
      cislaSTextyProblemu.add(new String(""+cetnostProb+"X - "+textProblemu));
    }

    return(cislaSTextyProblemu);
  }

  /**
   * Pomocná vnitřní třída, pro seřazení četností výskytu problémů.
   */
  class PrvekMapy {
    private int cetnost;
    private int cisloProblemu;
    public Comparator podleCetnosti;
    /**
     * Vytvoří objekt naplněním hodnot atributů cetnost, cisloProblemu.
     */
    PrvekMapy(int cetnost, int cisloProblemu) {
      this.cetnost=cetnost;
      this.cisloProblemu=cisloProblemu;
      podleCetnosti = new Comparator() {
        /**
         * Řazení prvků dle četností.
         */
        public int compare(Object o1, Object o2) {
          long p1= ((PrvekMapy) o1).cetnost;
          long p2= ((PrvekMapy) o2).cetnost;
          if (p1>p2)
            return(1);
          else
          if (p1<p2)
            return(-1);
          else
            return(0);
        }
       };
    }
  }


  public static void main(String[] args) {
  }

}