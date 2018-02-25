package oop;
import java.util.*;
import javax.swing.DefaultListModel;

/**
 * Slouží jako tzv. type-conscious ArrayList.
 * Kontejner, který má informaci o typech objektů v něm uložených.
 * Primární funkcí je správa, vkládání, a výběru objektů třídy Student.
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */
public class KontejnerStudentu extends Kontejner {
  /**
   * Inicilizuje ArrayList studentů.
   */
  public KontejnerStudentu() {
    seznam=new ArrayList();
  }
  /**
   * Přidá studenta student do ArrayListu.
   */
  public void add(Student student) {
    seznam.add(student);
  }
  /**
   * Vrátí referenci na studenta danou indexem index v ArrayListu.
   */
  public Student get(int index) {
    return (Student)seznam.get(index);
  }

  /**
   * Nalezene studenta v ArrayListu dle jeho příjmení a jména, využívá binární
   * vyhledávání a přirozené řazení dle příjmení a jména.
   */
  public Student get(String hledanePrijmJm) {
    int pozice=0;
    Student hledany=new Student(hledanePrijmJm);

    // využívám přirozené řazení dle příjmení a jména
    pozice=Collections.binarySearch((List) seznam, hledany);
    if (pozice >= 0)
      return(this.get(pozice));
    else
      return(this.get(0)); // nenalezen hledaný --> vrácen první v seznamu
  }

  /**
   * Seřadí ArrayList studentů dle příjmení a jmen studentů.
   */
  public void seradPrijmJm() {
    // využívám přirozené řazení dle příjmení a jména
    Collections.sort((List) seznam);
  }
  /**
   * Seřadí ArrayList studentů dle jejich celkých dob strávených u učitele.
   */
  public void seradCelkDoba() {
    Collections.sort((List) seznam, Student.PODLE_CELK_DOBA);
    Collections.reverse((List) seznam);

  }

  /**
   * Vrátí počet studentů, kteří se již alespoň jednou pokusili odevzdat.
   * Slouží pro statistiku.
   */
  public int jednouOdevzdali() {
    Student student;
    int pocitadlo=0; // počítadlo studentů, kteří alespoň jednou odevzdali

    for(int i=0; i<seznam.size(); i++) {
      student=(Student) seznam.get(i);
      if (student.getPocetNavstev() >0)
       pocitadlo++;

    }
    return(pocitadlo);
  }

  /**
   * Vrátí DefaultListModel jmen studentů. Slouží pro zobrazení seznamu studentů v JListu.
   */
  public DefaultListModel vratSeznamJmenListModel() {
    DefaultListModel listModel = new DefaultListModel();

    for(int i=0; i<this.size(); i++) {
      if (this.get(i).getOdevzdano() == false) {
        listModel.addElement(this.get(i).getPrijmeniAJmeno());
      }
    }
    return(listModel);

  }

   public static void main(String[] args) {
  }

}