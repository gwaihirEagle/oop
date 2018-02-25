package oop;
import java.util.*;

/* Zatím tato třída celkem zbytečná. Zatím námět na přemýšlení :-) */
/**
 * Sdružuje rysy společné ostatním kontejnerům: KontejnerStudentu,
 * KontejnerProblemu, KontejnerNavstev, KontejnerIndProblemu.
 * Je tedy určen k tomu, aby od něj bylo odděděno.
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */
abstract public class Kontejner {
  protected ArrayList seznam;

  /**
   * Vrátí velikost (počet prvků ) ArrayListu.
   */
  public int size() {
    return seznam.size();
  }
  /**
   * Vytiskne prvky ArrayListu.
   */
  public void tisk() {
    if (seznam != null) {
      for (int i = 0; i < seznam.size(); i++) {
        System.out.println(seznam.get(i));
      }
    }
  }

  public static void main(String[] args) {
  }

}