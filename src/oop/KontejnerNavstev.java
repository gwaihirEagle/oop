package oop;
import java.util.*;

/**
 * Slouží jako tzv. type-conscious ArrayList.
 * Kontejner, který má informaci o typech objektů v něm uložených.
 * Primární funkcí je správa, vkládání, a výběru objektů třídy Navsteva.
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */
public class KontejnerNavstev extends Kontejner {
  /**
   * Inicilizuje ArrayList návštěv.
   */
  public KontejnerNavstev() {
    seznam=new ArrayList();
  }
  /**
   * Přidá návštěvu do ArrayListu.
   */
  public void add(Navsteva n) {
   seznam.add(n);
  }
  /**
   * Vrátí referenci na návštěvu dle indexu index v ArrayListu.
   */
  public Navsteva get(int index) {
    return((Navsteva) seznam.get(index));
  }

  public static void main(String[] args) {
  }

}