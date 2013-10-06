/**
 * 
 */
package gui.tablerender;

import gui.MainFormOutline;
import gui.message.HitGUI;

import java.awt.Color;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.netbeans.swing.outline.RenderDataProvider;

import collector.data.Filter;
import core.config.Config;

/**
 * @author sucker
 * 
 */
public class TreeHitsRender implements RenderDataProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5855430566933247085L;
	private final Icon carIcon;
	private final Icon favIcon;
	private final Icon searchIcon;
	private final ImageIcon carNewIcon;

	public TreeHitsRender() {
		String language = Config.getConfig().getLanguage();

		Locale currLocale = new Locale(language);
		bundle = ResourceBundle.getBundle("gui/bundle/gui", currLocale);

		this.carIcon = new ImageIcon(MainFormOutline.class
				.getResource("images/car.png"));
		this.carNewIcon = new ImageIcon(MainFormOutline.class
				.getResource("images/car_new.png"));
		this.favIcon = new ImageIcon(MainFormOutline.class
				.getResource("images/heart.png"));
		this.searchIcon = new ImageIcon(MainFormOutline.class
				.getResource("images/magnifier.png"));

	}

	private final ResourceBundle bundle;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.netbeans.swing.outline.RenderDataProvider#getBackground(java.lang
	 * .Object)
	 */
	@Override
	public Color getBackground(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.netbeans.swing.outline.RenderDataProvider#getDisplayName(java.lang
	 * .Object)
	 */
	@Override
	public String getDisplayName(Object arg0) {
		if (arg0 instanceof HitGUI) {
			return ((HitGUI) arg0).getVal("title");
		}

		if (arg0 instanceof Filter) {
			Filter f = (Filter) arg0;
			return (f.getVal("make") + " " + f.getVal("model"));
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.netbeans.swing.outline.RenderDataProvider#getForeground(java.lang
	 * .Object)
	 */
	@Override
	public Color getForeground(Object arg0) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.netbeans.swing.outline.RenderDataProvider#getIcon(java.lang.Object)
	 */
	@Override
	public Icon getIcon(Object arg0) {
		if (arg0 instanceof HitGUI) {
			if (((HitGUI) arg0).isFav()) {
				return favIcon;
			} else {
				if (((HitGUI) arg0).isSeen()) {
					return carIcon;
				} else {
					return carNewIcon;
				}
			}
		}
		if (arg0 instanceof Filter) {
			return searchIcon;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.netbeans.swing.outline.RenderDataProvider#getTooltipText(java.lang
	 * .Object)
	 */
	@Override
	public String getTooltipText(Object arg0) {
		if (arg0 instanceof HitGUI) {
			HitGUI tmpH = (HitGUI) arg0;
			String image = tmpH.getVal("image");
			String adress = tmpH.getVal("address");
			String descr = tmpH.getVal("descr");
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
			return text;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.netbeans.swing.outline.RenderDataProvider#isHtmlDisplayName(java.
	 * lang.Object)
	 */
	@Override
	public boolean isHtmlDisplayName(Object arg0) {
		return false;
	}

}
