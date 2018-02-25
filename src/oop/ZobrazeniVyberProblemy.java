package oop;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

/**
 * Třída prezentační logiky.
 * Zobrazí modální okno aplikace se informacemi o vybraném studentovi a
 * JCheckBoxy s výběrem možných chyb.
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */
public class ZobrazeniVyberProblemy extends JDialog {
  private JLabel jmenoLB;        // jméno studenta
  private JLabel prijmeniLB;     // příjmení studenta
  private JLabel celkCasNavstev; // celkový čas návštěv studenta
  private JLabel pocetNavstev;   // celkový počet návštěv studenta

  private KontejnerProblemu problemy;
  private Vyucujici vyucujici;

  /**
   * Zobrazí modální okno, k čemuž potřebuje data objetku vyucujici a zapamatuje
   * si vlastníka, který jej zavolal.
   */
  public ZobrazeniVyberProblemy(JFrame vlastnik, Vyucujici vyucujici) {
    super(vlastnik, true);

    Student student;  // aktuální student u vyučujícího
    ArrayList ar; // ArrayList řetězců: četnost + text problému
    int polozekInf=5; // počet info položek o studentovi
    ZpracujRetezec zpracuj = new ZpracujRetezec(); // využiji některé metody
    student=vyucujici.getStudent();

    this.vyucujici=vyucujici;
    this.problemy = vyucujici.getKontejnerProblemu();

    this.setTitle("Výběr chyb");
    this.getContentPane().setLayout(new BorderLayout(5, 5));
    this.getContentPane().setBackground(Color.orange);
    this.setBounds(420, 0, 600, 730);

    // blok informací o studentovi
    TitledBorder titulStudent = BorderFactory.createTitledBorder(
        " Informace o studentovi ");
    titulStudent.setTitleJustification(TitledBorder.CENTER);
    titulStudent.setTitleFont(new Font("SansSerif", Font.BOLD, 16));

    TitledBorder titulChyby = BorderFactory.createTitledBorder(
        " Výběr nalezených chyb ");
    titulChyby.setTitleJustification(TitledBorder.CENTER);
    titulChyby.setTitleFont(new Font("SansSerif", Font.BOLD, 16));

    // panel s dvěma podpanely - infoPN, hodnotyPN s informacemi o studentovi
    JPanel infoStudentJP = new JPanel(new BorderLayout(5,5));
    infoStudentJP.setBorder(titulStudent);
    infoStudentJP.setBackground(Color.lightGray);

    ar = vyucujici.vratProblemySCetnostiArray(student.vratProblemySCetnosti());
    JPanel infoPN = new JPanel(new GridLayout(polozekInf+ar.size(), 1, 5, 5));
    infoPN.setBackground(Color.lightGray);
    JPanel hodnotyPN = new JPanel(new GridLayout(polozekInf+ar.size(), 1, 5,5));
    hodnotyPN.setBackground(Color.lightGray);
    JScrollPane hodnotyJSP = new JScrollPane(hodnotyPN);

    addLabel("Jméno", SwingConstants.LEFT, Color.lightGray, infoPN);
    addLabel("Příjmení", SwingConstants.LEFT, Color.lightGray, infoPN);
    addLabel("Celkový čas návštěv", SwingConstants.LEFT, Color.lightGray,
             infoPN);
    addLabel("Počet návštěv", SwingConstants.LEFT, Color.lightGray, infoPN);
    addLabel("Problémy v minulosti", SwingConstants.LEFT, Color.lightGray, infoPN);

    addTextField(student.getJmeno(), 20, false, Color.lightGray, hodnotyPN);
    addTextField(student.getPrijmeni(), 20, false, Color.lightGray, hodnotyPN);
    addTextField("" + zpracuj.vratCasFormat(student.getCelkovyCasNavstev(), true), 20,
                 false, Color.lightGray, hodnotyPN);
    addTextField("" + student.getPocetNavstev(), 20, false, Color.lightGray,
                 hodnotyPN);
    if (ar.size() == 0) { // student dosud žádné problémy
      addLabel("", SwingConstants.RIGHT, Color.lightGray, hodnotyPN);
        // pomocný prázdný Label
    }
    else { // student absolvoval alepoň jednu problémovou návštěvu
      for(int i=0; i<ar.size(); i++) {
      addLabel("", SwingConstants.RIGHT, Color.lightGray, infoPN);
      /* trik, aby se levý panel neroztáhl na max šířku panelu problémů
       * --> vlevo přidám pro každý problém prázdný "" label
       */
      addTextField((String) ar.get(i), ((String) ar.get(i)).length(), false, Color.lightGray, hodnotyPN);
      }
    }

    infoStudentJP.add(infoPN,BorderLayout.LINE_START);
    infoStudentJP.add(hodnotyPN, BorderLayout.CENTER);

    // panel s JCheckBoxy chyb
    JPanel chybyJP = new JPanel(new GridBagLayout());
    chybyJP=vratKontejnerChyb();
    chybyJP.setBorder(titulChyby);
    chybyJP.setBackground(Color.white);
    JScrollPane chybyJSP = new JScrollPane(chybyJP);
    // nastavit, aby se vertikální bar pohyboval rychleji

    // panel tlačítek odevzdáno, problémové
    JPanel odevzdanoJP = new JPanel(new GridLayout(1, 1, 1, 1));
    JButton odevzdanoJB = new JButton("Odevzdáno");
    JButton problemoveJB = new JButton("Problémové");
    odevzdanoJP.add(odevzdanoJB);
    odevzdanoJP.add(problemoveJB);

    // registrace posluchačů
    odevzdanoJB.addActionListener(new OdevzdanoAL());
    problemoveJB.addActionListener(new ProblemoveAL());

    this.getContentPane().add(infoStudentJP, BorderLayout.PAGE_START);
    this.getContentPane().add(chybyJSP, BorderLayout.CENTER);
    this.getContentPane().add(odevzdanoJP, BorderLayout.PAGE_END);

    this.setVisible(true);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Exception in thread "AWT-EventQueue-0" java.lang.IllegalArgumentException: defaultCloseOperation must be one of: DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE, or DISPOSE_ON_CLOSE
  }

  /**
   * Přidá JLabel do container.
   * @param text - nápis na JLabelu.
   * @param pozice - horizontální pozice v kontejneru.
   * @param color - barva pozadí.
   * @param container - kontejner, do kterého vkládám JLabel.
   */
  private void addLabel(String text, int pozice, Color color, Container container) {
       JLabel button = new JLabel(text);
       button.setBackground(color);
       button.setHorizontalAlignment(pozice);
       container.add(button);
  }
  /**
  * Přidá JTextField do container.
  * @param sObsah - přednastavený obsah JTextFieldu.
  * @param width - šířka JTextFieldu.
  * @param editable - nastavení, zda bude editovatelný.
  * @param color - barva pozadí
  * @container - kontejner, do kterého vkládám.
  */
  private void addTextField(String sObsah, int width, boolean editable, Color color, Container container) {
       JTextField textField = new JTextField(sObsah, width);
       textField.setEditable(editable);
       textField.setBackground(color);
       container.add(textField);
  }

  /**
   * Vrátí JPanel naplněný seskupením JCheckBoxů týkajících se podobného tématu.
   * @param pocPoziceProb - počáteční index skupiny v kontejneru problemy
   */
  private JPanel vytvorSkupinuProblemu(int pocPoziceProb) {
     int endPoziceProb=1; // koncový index skupiny v kontejneru problemy

     TitledBorder titulSkupinyTB;
     JPanel skupinaProblemuJP;

     titulSkupinyTB = BorderFactory.createTitledBorder(problemy.get(pocPoziceProb).getNadProblem());
     titulSkupinyTB.setTitleJustification(TitledBorder.CENTER);
     titulSkupinyTB.setTitleFont(new Font("SansSerif", Font.BOLD, 16));
     skupinaProblemuJP = new JPanel(new GridBagLayout());
     skupinaProblemuJP.setBorder(titulSkupinyTB);
     skupinaProblemuJP.setBackground(Color.white);

     GridBagConstraints c = new GridBagConstraints();
     c.fill = GridBagConstraints.HORIZONTAL;
     c.weightx=0.5;
     c.gridx=0;
     c.gridy=0;
     // vložení nadproblému
     endPoziceProb=problemy.get(problemy.get(pocPoziceProb).getNadProblem());
     problemy.get(endPoziceProb).setBackground(Color.white);
     skupinaProblemuJP.add(problemy.get(endPoziceProb), c);

     // přidání JCheckBoxu do panelu + registrace posluchačů pro problemy.get(endPoziceProb)
     for(int j=pocPoziceProb; j<endPoziceProb; j++) {
       problemy.get(j).setBackground(Color.white);
       c.insets = new Insets(0,10,0,0);  //top padding
       c.gridx=0;
       c.gridy=j-pocPoziceProb+1;
       skupinaProblemuJP.add(problemy.get(j), c);
       problemy.get(endPoziceProb).addItemListener(problemy.get(j));
     }
   return(skupinaProblemuJP);
   }

   /**
    * Vrátí panel osazený JCheckBoxy, které jsou seskupené do skupin podobných
    * problémů.
    */
   private JPanel vratKontejnerChyb() {
     JCheckBox chybaCHB;   // JCheckBox problému
     Problem problem;
     int i=1;              // počítadlo řádek pro GridBagLayout
                           // taktéž index do kontejneru problémů
     int endPoziceProb=1;  // index konce skupiny problémů

     JPanel chybyJP = new JPanel(new GridBagLayout());
     GridBagConstraints c = new GridBagConstraints();

     c.fill = GridBagConstraints.HORIZONTAL;
     c.weightx=0.5;
     while (i<=problemy.size()) {
       problem = problemy.get(i);
       if ( problem.getNadProblem().compareTo("") != 0 ) { // problém má nadproblém
         JPanel panelak = new JPanel(new GridLayout(1,1,1,1)); // panel skupiny problémů
         panelak.add(vytvorSkupinuProblemu(i));

         // nastavení velikosti panelu skupiny dle počtu zařazených problémů
         endPoziceProb=problemy.get(problemy.get(i).getNadProblem());
         c.ipady  = endPoziceProb-i;
         c.gridx = 0;
         c.gridy = i;

         chybyJP.add(panelak, c);
         // posuv aktuální řádky v hlavním panelu o velikost panelu skupiny problémů
         i=endPoziceProb;
         i++;
       }
       else { // problém nemá nadproblém --> nepotřebuji seskupovat problémy
         problem.setSelected(false);
         problem.setBackground(Color.white);
         if (problem.getKlavesZkratka() != 0) { // je přiřazena kláves zkratka
           // !! tady zkusit, aby to zabíralo JEN na F-ka, NE přes alt + F-ko !
           problem.setMnemonic(problem.getKlavesZkratka());
         }
         c.gridx = 0;
         c.gridy = i;
         c.ipady=0;
         chybyJP.add(problem, c);
         i++; // posuv v y-radce o JCheckBox
       }
     } // while
     return(chybyJP);
   }

   /**
    * Při stisku jakéhokoliv tlačítka se zneviditelní modální okno.
    */
   private void actionQuit() {
     this.setVisible(false);
   }

   /**
    * Vnitřní třída pro relizaci stisku tlačítka Problémové.
    */
   class ProblemoveAL implements ActionListener {
     /**
      * Reakce na stisk tlačítka "Problémové"
      */
    public void actionPerformed(ActionEvent e)  {
      vyucujici.kalkulaceNavstevy(false); // false .. neodevzdáno
      actionQuit();
    }
  }

  /**
   * Vnitřní třída pro realizaci stisku tlačítka Odevzdáno.
   */
  class OdevzdanoAL implements ActionListener {
    /**
      * Reakce na stisk tlačítka "Odevzdáno"
      */
    public void actionPerformed(ActionEvent e)  {
      actionQuit();
      vyucujici.kalkulaceNavstevy(true); // true .. odevzdáno

    }
  }

  public static void main(String[] args) {
   }

}
