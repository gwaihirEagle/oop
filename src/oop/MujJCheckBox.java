package oop;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author <a href="mailto: marek.styblo@post.cz">Marek Stýblo</a>
 * @version 1.0 (2004-May-9)
 */

 public class MujJCheckBox extends JCheckBox implements ItemListener {
   MujJCheckBox(String text) {
      super(text);
   }


   public void itemStateChanged(ItemEvent e) {
      if (e.getStateChange() == ItemEvent.SELECTED)
        this.setSelected(true);
      else
        this.setSelected(false);
   }

  }

