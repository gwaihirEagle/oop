package oop;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Nese informace o sledovaných atributech problémů:
 * název problému, klávesová zkratka problému, číslo problému, text nadproblému,
 * četnost výskytu problému, zda je základní.
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */
public class Problem extends JCheckBox implements ItemListener, Comparable{
  String nazevProblemu;
  int klavesZkratka=0;
  int cisloProblemu=0;
  String nadProblem="";
  long cetnostProblemu=0;
  boolean zakladni=true;
  /**
   * Vytvoří objekt třídy Problem s nastavenými atributy nazevProblemu a cisloProblemu.
   */
  public Problem(String nazevProblemu, int cisloProblemu) {
    super(nazevProblemu);
    if (nazevProblemu == null)
      throw new NullPointerException();
    if (cisloProblemu < 1)
      throw new ArithmeticException();

    this.nazevProblemu=nazevProblemu;
    this.cisloProblemu=cisloProblemu;

  }

  /**
   * Vytvoří objekt třídy Problem s nastavenými atributy predejSuper, který je
   * pouze předán předkovi a dalšími parametry nazevProblemu a cisloProblemu.
   */

  public Problem(String predejSuper, String nazevProblemu, int cisloProblemu) {
    super(predejSuper);
    if (nazevProblemu == null)
      throw new NullPointerException();
    if (cisloProblemu < 1)
      throw new ArithmeticException();

    this.nazevProblemu=nazevProblemu;
    this.cisloProblemu=cisloProblemu;

  }


  public String toString() {
    return("NAZEV: "+nazevProblemu+"  KLAZKR: "+klavesZkratka+"  NADPROB: "+nadProblem+"  CETNOST: "+cetnostProblemu);
  }

  public boolean equals(Object o) {
    if (o==this)
      return(true);
    if ((o instanceof Problem) == false)
      return(false);
    Problem p = (Problem) o;
    boolean stejneZkratky = (klavesZkratka == p.klavesZkratka);
    boolean stejnaCislaProb = (cisloProblemu == p.cisloProblemu);
    boolean stejneNadProb = nadProblem.equals(p.nadProblem);
    boolean stejneCetnosti = (cetnostProblemu == p.cetnostProblemu);
    boolean stejneZakladni = (zakladni == p.zakladni);
    return(stejneZkratky && stejnaCislaProb && stejneNadProb && stejneCetnosti &&
           stejneZakladni);
  }

  public int hashCode() {
    int vysledek=17;
    int pom;
    pom=this.zakladni ? 0:1;
    vysledek=37*vysledek+pom;
    pom=this.klavesZkratka;
    vysledek=37*vysledek+pom;
    pom=this.cisloProblemu;
    vysledek=37*vysledek+pom;
    pom=this.nadProblem.hashCode();
    vysledek=37*vysledek+pom;
    pom=(int)this.cetnostProblemu; // loss of precision mi nevadí
    vysledek=37*vysledek+pom;
    return(vysledek);
  }
  /**
   * Přirozené řazení dle čísla problému.
   */
  public int compareTo(Object o) {
    long p1= this.cisloProblemu;
    long p2= ((Problem) o).getCisloProblemu();
      if (p1>p2)
        return(1);
      else
        if (p1<p2)
          return(-1);
        else
          return(0);
  }
  /**
   * Vrátí text nadproblému k danému problému.
   */
  public String getNadProblem() {
    return(nadProblem);
  }

  /**
   * Nastaví text atributu nadProblem na řetězec s.
   */
  public void setNadProblem(String s) {
    nadProblem=new String(s);
  }
  /**
   * Nastaví číslo klávesové zkratky na hodnota.
   */
  public void setKlavesZkratka(int hodnota) {
    klavesZkratka=hodnota;
  }
  /**
   * Nastaví stav atributu zakladni ( zda je problém základním, nebo má podproblémy )
   * na stav b.
   */
  public void setZakladni(boolean b) {
    zakladni=b;
  }
  /**
   * Zvýší četnost výskytu problému mezi studenty o 1.
   */
  public void setCetnostProblemu() {
    cetnostProblemu++;
  }
  /**
   * Vrátí text nadproblému k danému problému.
   */
  public String getNazevProblemu() {
    return(nazevProblemu);
  }
  /**
   * Vrátí četnost výskytu problému mezi studenty.
   */
  public long getCetnostProblemu() {
    return(cetnostProblemu);
  }
  /**
   * Vrátí číslo problému.
   */
  public int getCisloProblemu() {
    return(cisloProblemu);
  }
  /**
   * Vrátí hodnotu klávesové zkratky pro daný problém.
   */
  public int getKlavesZkratka() {
    return(klavesZkratka);
  }
  /**
   * Vrátí stav, zda je problém základní, nebo má porpdoblémy.
   */
  public boolean getZakladni() {
    return(zakladni);
  }
  /**
   * Proměnná komparátoru pro absolutní řazení dle četnosti výskytu problému.
   */

  /**
  * Reakce na kliknutí na JCheckBoxu.
  */
 public void itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED)
      this.setSelected(true);
    else
      this.setSelected(false);
 }
 /**
  * Proměnná typu Comparator pro absolutní řazení dle četnosti výskytu problému.
  */
  public static final Comparator PODLE_CETNOSTI = new Comparator() {
    /**
     * Absolutní řazení dle četnosti výskytu problému.
     */
    public int compare(Object o1, Object o2) {
    long p1= ((Problem) o1).getCetnostProblemu();
    long p2= ((Problem) o2).getCetnostProblemu();
    if (p1>p2)
      return(1);
    else
      if (p1<p2)
        return(-1);
      else
        return(0);
    }
  };

  public static void main(String[] args) {
  }

}