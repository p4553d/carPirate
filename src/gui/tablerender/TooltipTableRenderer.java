/**
 * 
 */
package gui.tablerender;

import gui.tablemodel.AbstractHitsModel;

import java.awt.Component;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JTable;

import org.jvnet.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import core.config.Config;

/**
 * @author sucker
 * 
 */
public class TooltipTableRenderer extends SubstanceDefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7322503998789765316L;
	private final ResourceBundle bundle;

	public TooltipTableRenderer() {
		String language = Config.getConfig().getLanguage();

		Locale currLocale = new Locale(language);
		bundle = ResourceBundle.getBundle("gui/bundle/gui", currLocale);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int vrow, int col) {

		Component comp = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, vrow, col);
		// Build tooltip for one search hit
		int row = table.convertRowIndexToModel(vrow);
		String image = ((AbstractHitsModel) table.getModel()).getImageURL(row);
		String adress = ((AbstractHitsModel) table.getModel()).getHit(row)
				.getVal("address");
		String descr = ((AbstractHitsModel) table.getModel()).getHit(row)
				.getVal("descr");
		String text = "<html><table width=\"400\"><tr><td colspan=\"2\" align=\"center\">";
		if (image != null && !image.equals("")) {
			text += "<img src=\"" + image + "\"/>";
		} else {
			text += bundle.getString("tooltip.nophoto");
			;
		}
		text += "</td></tr><tr valign=\"top\"><td width=\"50%\">";

		if (descr != null && !descr.equals("")) {
			text += descr;
		} else {
			text += bundle.getString("tooltip.nodescr");
			;
		}
		text += "</td><td width=\"50%\">";

		if (adress != null && !adress.equals("")) {
			text += adress;
		} else {
			text += bundle.getString("tooltip.noaddr");
			;
		}
		text += "</td><tr>";

		text += "</table></html>";
		setToolTipText(text);

		return comp;
	}
}
