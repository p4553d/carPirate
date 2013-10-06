/**
 * 
 */
package gui.tablemodel;

import javax.swing.JComponent;
import javax.swing.MenuSelectionManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;

import org.jvnet.substance.SubstanceCheckBoxMenuItemUI;


/**
 * @author p4553d
 * 
 * as seen on stackoverflow
 * modified for substance
 */
public class CBItemUI extends SubstanceCheckBoxMenuItemUI {
	@Override
	   protected void doClick(MenuSelectionManager msm) {
	      menuItem.doClick(0);
	   }

	   public static ComponentUI createUI(JComponent c) {
	      return new CBItemUI();
	   }
}
