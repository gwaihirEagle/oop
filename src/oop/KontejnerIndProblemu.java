package oop;
import java.util.*;

/**
 * Slouží jako tzv. type-conscious ArrayList.
 * Kontejner, který má informaci o typech objektů v něm uložených.
 * Primární funkcí je správa, vkládání, a výběru objektů třídy Integer.
 * Objekty třídy Integer slouží jako indexy do kontejneru KontejnerProblemu.
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */
public class KontejnerIndProblemu extends Kontejner {
  /**
   * Inicilizuje ArrayList indexů problémů.
   */
  public KontejnerIndProblemu() {
    seznam=new ArrayList();
  }
  /**
   * Přidá Integer s hodnotou indexu do ArrayListu.
   */
  public void add(Integer indexDoKontejneruProblemu) {
    seznam.add(indexDoKontejneruProblemu);
  }
  /**
   * Vrátí číslo indexu (obsahu prvku ArrayListu) do kontejneru problémů.
   */
  public int get(int index) {
    return(((Integer) seznam.get(index)).intValue());
  }

  public static void main(String[] args) {
  }

}