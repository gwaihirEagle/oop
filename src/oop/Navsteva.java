package oop;

/**
 * Nese informace o sledovaných atributech návštěv:
 * datum návštěvy, počáteční čas, koncový čas, čísla nalezených problémů.
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */
public class Navsteva implements Comparable {
  int datumNavstevy=0;
  String casPocatek=new String("000000");
  String casKonec=new String("000000");
  KontejnerIndProblemu problemyInd;

  /**
   * Vytvoří objekt třídy Navsteva s parametry datumNavstevy, casPocatek, casKonec.
   */
  public Navsteva(int datumNavstevy, String casPocatek, String casKonec) {
    if ( (datumNavstevy<0))
      throw new ArithmeticException();
    if ((casPocatek == null) || (casKonec == null) )
      throw new NullPointerException();

    this.datumNavstevy=datumNavstevy;
    this.casPocatek=new String(casPocatek);
    this.casKonec=new String(casKonec);
    this.problemyInd = new KontejnerIndProblemu();
  }

  public String toString() {
    return("Datum: "+datumNavstevy+"/n"+"Zacatek: "+casPocatek+"/n"+"Konec: "+casKonec);
  }

  public boolean equals(Object o) {
    if (o == this)
      return(true);
    if ((o instanceof Navsteva) == false)
      return(false);
    Navsteva n = (Navsteva) o;
    boolean stejneDatum = (datumNavstevy == n.datumNavstevy);
    boolean stejnyCasPocatek;
    if ((casPocatek.compareTo(n.casPocatek)) == 0)
      stejnyCasPocatek=true;
    else
      stejnyCasPocatek=false;

    boolean stejnyCasKonec;
    if ((casKonec.compareTo(n.casKonec)) == 0)
      stejnyCasKonec=true;
    else
      stejnyCasKonec=false;
    return(stejneDatum && stejnyCasPocatek && stejnyCasKonec);
  }

  public int hashCode() {
    int vysledek = 17;
    int pom;
    pom=datumNavstevy;
    vysledek=37*vysledek+pom;
    pom=casPocatek.hashCode();
    vysledek=37*vysledek+pom;
    pom=casKonec.hashCode();
    vysledek=37*vysledek+pom;
    return(vysledek);
  }
  /**
   * Přirozené řazení dle datumu návštěvy.
   */
  public int compareTo(Object o) {
    Navsteva n = (Navsteva) o;

   int d1= this.datumNavstevy;
   int d2= ((Navsteva) o).datumNavstevy;
     if (d1>d2)
       return(1);
     else
       if (d1<d2)
         return(-1);
       else
         return(0);
 }
 /**
   * Vrátí řetězec návštěvy ve tvaru: "datum:časOd-časDo:".
   */
  public String pomVratNavstevu() {
    String polNav=""+datumNavstevy+":"+casPocatek+"-"+casKonec+":";
    return(polNav+pomVratCislaProb());
  }

  /**
   * Projde kontejner čísel problémů jedné návštěvy a vrátí je (čísla) ve Stringu.
   * Určeno pro ukádání parametrů návštěv do textového souboru
   */
  public String pomVratCislaProb() {
    String seznProb = "";
    int i=0;

    for (i = 0; i < problemyInd.size(); i++) {
      if (i>0) // pro rozlišení, zda zapsat oddělovač problémů
        seznProb = new String(seznProb + "," + problemyInd.get(i));
      else
        seznProb = new String(seznProb+problemyInd.get(i));

    } // for
    if (i==0) // návštěva byla bez problémů --> odevzdáno, což je indikováno "*"
      seznProb = new String(seznProb+"*;");
    else // návštěva byla problémová --> pouze oddělovač
      seznProb = new String(seznProb+";");
    return(seznProb); // oddělovač návštěv ";"
  }


  public static void main(String[] args) {
  }

}