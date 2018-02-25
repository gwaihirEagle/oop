package oop;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

/**
 * Třída prezentační logiky.
 * Zobrazí hlavní okno aplikace se seznamem studentů a možnostmi statistik.
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */
public class ZobrazeniVyber extends JFrame implements Observer{
  private Vyucujici vyucujici;
  private DefaultListModel seznamStudentu;      // ListModel jmen studentů pro výpis v JListu
  private int kolikZobrazitNejcProb=2;  // pro účely statistik

  private JTextField vyhledejTF; // inkrementální vyhledávání
  private JList studentiJL;      // seznam jmen studentů
  private JCheckBox statCHB;     // zobrazování nevýznamových nul ve statistikách
  private JComboBox statCB;      // výběr počtu zobrazených nejčastějších problémů

  /**
   * Zobrazí hlavní okno aplikace, k čemuž potřebuje data objektu vyucujici.
   */
  public ZobrazeniVyber(Vyucujici vyucujici) {
    super.setTitle("Statistika odevzdávání semestrálních prací");

    this.vyucujici=vyucujici;

    String [] seznamCiselProblemu=vyucujici.seznamCiselProblemu(); // pro zobrazení v JComboBoxu
    seznamStudentu=vyucujici.seznamJmenStudentu();

    this.getContentPane().setBackground(Color.orange);
    this.setBackground(Color.orange);
    this.getContentPane().setLayout(new BorderLayout(10,10));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setDefaultLookAndFeelDecorated(true);

    //inkrementální vyhledávání
    JPanel zadejPN=new JPanel(new GridLayout(0,2,50,10));
    zadejPN.setBackground(Color.orange);
    vyhledejTF = new JTextField(SwingConstants.NORTH);
    JLabel infoHledLB = new JLabel("Zadej jméno studenta");
    infoHledLB.setHorizontalAlignment(SwingConstants.RIGHT);
    zadejPN.add(infoHledLB);
    zadejPN.add(vyhledejTF);

    // JList seznamu studentů
    studentiJL = new JList(seznamStudentu);
    studentiJL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    studentiJL.setSelectedIndex(0);
    studentiJL.setVisibleRowCount(35);
    JScrollPane listScrollPane = new JScrollPane(studentiJL);
    listScrollPane.setPreferredSize(new Dimension(350, 550));

    // oblast nastavení statistik
    TitledBorder titulStatistik = BorderFactory.createTitledBorder(
        " Generování statistik ");
    titulStatistik.setTitleJustification(TitledBorder.CENTER);
    titulStatistik.setTitleFont(new Font("SansSerif", Font.BOLD, 16));

    JPanel statPN = new JPanel(new GridLayout(3,2,1,1));
    statPN.setBorder(titulStatistik);
    statPN.setBackground(Color.white);
    statCHB = new JCheckBox("Zobrazit nedosažené jednotky");
    statCHB.setSelected(true);
    statCHB.setBackground(Color.white);
    statCB = new JComboBox(seznamCiselProblemu);
    statCB.setSelectedIndex(1);
    JButton statJB = new JButton("Generuj Statistiky");


    statPN.add(statCHB);
    statPN.add(new JLabel("  ")); // pomocný pro vyplnění
    statPN.add(new JLabel("Vyber zobrazovaný počet nejčastějších problémů"));
    statPN.add(statCB);
    statPN.add(statJB);
    //statPN.add(statLB);

    this.getContentPane().add(zadejPN, BorderLayout.PAGE_START);
    this.getContentPane().add(listScrollPane, BorderLayout.CENTER);
    this.getContentPane().add(statPN, BorderLayout.PAGE_END);

    /// registrace posluchačů
    vyhledejTF.addKeyListener(new InkremVyhledKL(this));
    vyhledejTF.addActionListener(new InkremVyhledKL(this));
    //studentiJL.addListSelectionListener(new ZobrazModalSL(this));
    studentiJL.addMouseListener(new ZobrazModalML(this));
    studentiJL.addKeyListener(new ZobrazModalKL(this));
    statJB.addActionListener(new StatistikyAL());
    statCB.addActionListener(new ZobrazProblemuAL());
    this.addWindowListener(new UzavreniOknaWL());

    this.pack();
    this.setVisible(true);
    // zjisti prečo se používá !
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            }
        });

  } // konstruktor ZobrazeniVyber()

  /**
   * Implementace inkrementálního vyhledávání.
   * Pokusí se nalézt řetězec začínající hodnotou proměnné str v poli jmen
   * studentů seznamStudentu. Pokud řetězec nalezne - nastaví jej visible v
   * JListu, jinak nastaví 0.index pole jako visible.
   * @param - vstupní parametr, kterým má začínat řetězec v poli seznamStudentu
   */
 void nastavAktualniPohled(String str) {
   int index;
   index=hledejJmenoStudenta(str);
   if (index != -1) {
     studentiJL.ensureIndexIsVisible(index);
     studentiJL.setSelectedIndex(index);
   }
   else {
     studentiJL.ensureIndexIsVisible(0);
     studentiJL.setSelectedIndex(0);
   }
 }

 /**
   * Implementace inkrementálního vyhledávání.
   * Vrátí index do pole jmen studentů na položku, která začíná řetězcem str.
   * @param str - hledaný začátek řetězce
   */
 int hledejJmenoStudenta(String str) {
   String hledPodRet=str.toLowerCase();
   for(int i=0; i<seznamStudentu.size(); i++) {
     if (((String) seznamStudentu.get(i)).toLowerCase().startsWith(hledPodRet))
       return(i);
   }
   return(-1); // nenalezena žádná odpovívající položka
 }

 /**
  * Reakce na stisk tlačítka "odevzdáno" --> odevzdal --> vyjmutí studenta z JListu.
  */
 public void update(Observable o, Object arg) {
   seznamStudentu.remove(((Integer) arg).intValue());
 }


  /**
   * Vnitřní třída pro reakci na změnu písmene v JTextFieldu --> implementace
   * inkrementálního vyhledávání. + reakce na Enter v TextFieldu.
   */
  class InkremVyhledKL extends KeyAdapter implements ActionListener{
    JFrame rodic;

    public InkremVyhledKL(JFrame rodic) {
      this.rodic=rodic;
    }
    //public void keyTyped(KeyEvent e) {
    //public void keyPressed(KeyEvent e) {

    /**
     * Reakce na zadání znaku z klávesnice do JTextFieldu
     */
    public void keyReleased(KeyEvent e) {
      nastavAktualniPohled(vyhledejTF.getText());
    }
    /**
     * Reakce na stisk ENTERU v JTextFIeldu
     */
    public void actionPerformed(ActionEvent e) {

      vyucujici.setStudent((String) seznamStudentu.get(studentiJL.getSelectedIndex()));
      vyucujici.setPocatekNavstevy(vyucujici.aktualCas());
      vyucujici.setIDStudenta(studentiJL.getSelectedIndex());
      new ZobrazeniVyberProblemy(this.rodic, vyucujici);

    }
  }

   /**
    * Vnitřní třída pro reakci na click do JListu --> otevření modálního okna.
    */
   class ZobrazModalML extends MouseAdapter {
     JFrame rodic;
     public ZobrazModalML(JFrame rodic) {
        this.rodic=rodic;
     }
     /**
      * Reakce na kliknutí do JListu.
      */
     public void mouseClicked(MouseEvent e) {
       vyucujici.setStudent((String) seznamStudentu.get(studentiJL.getSelectedIndex()));
       vyucujici.setPocatekNavstevy(vyucujici.aktualCas());
       vyucujici.setIDStudenta(studentiJL.getSelectedIndex());
       new ZobrazeniVyberProblemy(this.rodic, vyucujici);
     }
   }

   /**
    * Vnitřní třída pro reakci na stisk Enteru v JListu --> otevření modálního okna.
    */
   class ZobrazModalKL extends KeyAdapter {
     JFrame rodic;

     public ZobrazModalKL(JFrame rodic) {
       this.rodic = rodic;
     }

     // public void keyTyped(KeyEvent e) {
     // public void keyPressed(KeyEvent e) {

     /**
      * Reakce na zadání znaku z klávesnice do JTextFieldu
      */
     public void keyReleased(KeyEvent e) {
       if (e.getKeyCode() == KeyEvent.VK_ENTER) {
         vyucujici.setStudent((String) seznamStudentu.get(studentiJL.getSelectedIndex()));
         vyucujici.setPocatekNavstevy(vyucujici.aktualCas());
         vyucujici.setIDStudenta(studentiJL.getSelectedIndex());
         new ZobrazeniVyberProblemy(this.rodic, vyucujici);
       }
     }
   }

   /**
    * Vnitřní třída pro reakci na výběr z JComboBoxu.
    */
  class ZobrazProblemuAL implements ActionListener {
    /**
     * Reakce na výběr z JComboBoxu.
     */
     public void actionPerformed(ActionEvent e) {
       kolikZobrazitNejcProb = (statCB.getSelectedIndex())+1;
        //+1 kvůli transformaci indexů v kontejneru problemy -> číslo problému
     }
  }


 /**
  * Vnitřní třída pro reakci na stisk tlačítka Generuj statistiky.
  */
 class StatistikyAL implements ActionListener {
   //!!!dodelat popup okno////
   /**
    * Reakce na kliknutí na tlačítko "Generuj statistiky"
    */
   public void actionPerformed(ActionEvent e) {
     boolean zobrazNulyCasu=true;

     if (statCHB.isSelected() == false)
       zobrazNulyCasu=false;

     vyucujici.generujStatistiky(zobrazNulyCasu, kolikZobrazitNejcProb);
   }
 }

 /**
  * Vnitřní třída pro reakci na uzavření hlavního okna --> konec programu.
  */
  class UzavreniOknaWL extends WindowAdapter {
    /**
     * Reakce na uzavření hlavního okna.
     */
    public void windowClosing(WindowEvent e) {
      vyucujici.ukonciCinnost();
    }
  }

  public static void main(String[] args) {
  }

}