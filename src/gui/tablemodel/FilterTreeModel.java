/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.tablemodel;

import gui.MainFormOutline;
import gui.message.HitGUI;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import message.MessageHandler;
import message.MessageType;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.netbeans.swing.outline.RenderDataProvider;

import collector.data.Filter;
import collector.data.IFilter;
import core.config.Config;

/**
 * 
 * @author sucker
 */
public class FilterTreeModel implements TreeModel, RenderDataProvider {

	private IFilter root;
	private final HashMap<Filter, Vector<HitGUI>> treeData;
	private final HashMap<Filter, Integer> notSeen;
	private final Vector<TreeModelListener> listenerList;

	private final Icon carIcon;
	private final Icon favIcon;
	private final Icon searchIcon;
	private final Icon searchNewIcon;
	private final Icon carNewIcon;
	private final ResourceBundle bundle;

	public FilterTreeModel() {
		this.root = new Filter();
		notSeen = new HashMap<Filter, Integer>();
		this.treeData = new HashMap<Filter, Vector<HitGUI>>();
		this.listenerList = new Vector<TreeModelListener>();

		String language = Config.getConfig().getLanguage();

		Locale currLocale = new Locale(language);
		bundle = ResourceBundle.getBundle("gui/bundle/gui", currLocale);

		this.carIcon = new ImageIcon(
				MainFormOutline.class.getResource("images/car.png"));
		this.carNewIcon = new ImageIcon(
				MainFormOutline.class.getResource("images/car_new.png"));
		this.favIcon = new ImageIcon(
				MainFormOutline.class.getResource("images/heart.png"));
		this.searchIcon = new ImageIcon(
				MainFormOutline.class.getResource("images/magnifier.png"));
		this.searchNewIcon = new ImageIcon(
				MainFormOutline.class.getResource("images/magnifier_new.png"));
	}

	public FilterTreeModel(IFilter rf) {
		this();
		this.root = rf;
	}

	public Object getChild(Object parent, int index) {
		if (parent == root) {
			Set<Filter> fs = treeData.keySet();
			Object[] fa = (fs.toArray());
			if (fa.length > index) {
				return fa[index];
			} else {
				return null;
			}
		} else {
			return treeData.get(parent).get(index);
		}
	}

	public int getChildCount(Object parent) {
		if (parent == root) {
			return treeData.size();
		} else {
			if (treeData.containsKey(parent)) {
				return treeData.get(parent).size();
			} else {
				return 0;
			}
		}
	}

	public int getIndexOfChild(Object parent, Object child) {
		if (parent == root) {
			Set<Filter> fa = treeData.keySet();
			int i = 0;
			for (Filter f : fa) {
				if (f.equals(child)) {
					return i;
				}
				i++;
			}
			// not found
			return -1;
		} else {
			return treeData.get(parent).indexOf(child);
		}
	}

	public Object getRoot() {
		return root;
	}

	public boolean isLeaf(Object node) {
		return (node instanceof HitGUI);
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		// do nothing
	}

	public void addFilter(Filter f) {
		if (!f.isPoisoned()) {
			if (!treeData.containsKey(f)) {
				Vector<HitGUI> hv = new Vector<HitGUI>();
				treeData.put(f, hv);
				notSeen.put(f, 0);
				TreeModelEvent tme = new TreeModelEvent(this,
						new Object[] { root }, new int[] { this.getIndexOfChild(root, f) },
						new Object[] { f });
				fireInserted(tme);
			}
			saveFilters();
		}
	}

	public void addHit(HitGUI h) {

		Filter f = (Filter) h.getParent();
		if (!f.isPoisoned()) {
			Vector<HitGUI> hv;
			if (!treeData.containsKey(f)) {
				System.out.println("FilterTreeModel.addHit() ARGH");
				hv = new Vector<HitGUI>();
				treeData.put(f, hv);
				notSeen.put(f, 0);
				TreeModelEvent tme = new TreeModelEvent(this,
						new Object[] { root }, new int[] { treeData.size() },
						new Object[] { f });
				fireInserted(tme);
			} else {
				hv = treeData.get(f);
			}

			hv.add(0, h);
			notSeen.put(f, notSeen.get(f) + 1);
			TreeModelEvent tme = new TreeModelEvent(this, new Object[] { root,
					f }, new int[] { hv.size() }, new Object[] { h });
			fireInserted(tme);
		}
	}

	public void removeFilter(Filter f) {
		// remove hits to the filter
		flushHits(f);
		// remove filter
		TreeModelEvent tme = new TreeModelEvent(this, new Object[] { root },
				new int[] { getIndexOfChild(root, f) }, new Object[] { f });
		treeData.remove(f);
		notSeen.remove(f);
		fireRemoved(tme);
	}

	public void removeHit(HitGUI h) {
		Filter f = (Filter) h.getParent();
		Vector<HitGUI> vh = treeData.get(f);

		int index = getIndexOfChild(f, h);

		TreeModelEvent tme = new TreeModelEvent(this, new Object[] { root, f },
				new int[] { index }, new Object[] { h });

		vh.remove(h);
		if (!h.isSeen()) {
			notSeen.put(f, notSeen.get(f) - 1);
		}

		// remove hit
		fireRemoved(tme);

	}

	public void flushHits(Filter f) {
		notSeen.put(f, 0);
		int size = getSize(f);
		if (size > 0) {
			int[] idx = new int[getSize(f)];
			for (int i = 0; i < getSize(f); i++) {
				idx[i] = i;
			}
			TreeModelEvent tme = new TreeModelEvent(this, new Object[] { root,
					f }, idx, treeData.get(f).toArray());
			treeData.get(f).clear();
			fireRemoved(tme);
		}
		saveFilters();
	}

	/**
	 * @param f
	 * @return
	 */
	private int getSize(Filter f) {
		return treeData.get(f).size();
	}

	public void flushHits() {
		for (IFilter f : treeData.keySet()) {
			flushHits((Filter) f);
		}
	}

	public void addTreeModelListener(TreeModelListener l) {
		listenerList.add(l);
	}

	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(l);
	}

	private void fireInserted(TreeModelEvent tme) {
		for (TreeModelListener tml : listenerList) {
			tml.treeNodesInserted(tme);
		}
	}

	private void fireRemoved(TreeModelEvent tme) {
		for (TreeModelListener tml : listenerList) {
			tml.treeNodesRemoved(tme);
		}
	}

	/**
	 * 
	 */
	public void markSeen() {
		for (Filter f : treeData.keySet()) {
			markSeen(f);
		}
	}

	public void markSeen(Filter f) {
		notSeen.put(f, 0);
		for (HitGUI hg : treeData.get(f)) {
			hg.wasSeen();
		}
	}

	/**
	 * 
	 */
	public void saveFilters() {
		Element depository = new Element("depository");
		for (Filter filter : treeData.keySet()) {
			if (!filter.isPoisoned()) {
				Element xmlFilter = new Element("filter");
				for (String key : filter.getKeys()) {
					Element xmlSet = new Element("set");
					xmlSet.setAttribute("key", key);
					xmlSet.setText(filter.getVal(key));

					xmlFilter.addContent(xmlSet);
				}
				depository.addContent(xmlFilter);
			}
		}

		Document doc = new Document(depository);
		XMLOutputter out = new XMLOutputter();
		try {
			// out.setFormat(Format.getPrettyFormat());
			out.output(doc, new FileOutputStream(Config.getConfig()
					.getFilterSource()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			MessageHandler.getHandler().broadcast("Can not save filters",
					MessageType.ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			MessageHandler.getHandler().broadcast("Can not save filters",
					MessageType.ERROR);
		}
	}

	// Render part
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
			boolean newHits = (notSeen.get(arg0) != 0);
			StringBuffer temp = new StringBuffer();

			if (newHits) {
				temp.append("<b> (");
				temp.append(notSeen.get(f));
				temp.append(" / ");
				temp.append(getSize(f));
				temp.append(") </b>");
			} else {
				temp.append(" (");
				temp.append(getSize(f));
				temp.append(") ");
			}
			temp.append(f.getVal("make"));
			temp.append(" ");
			temp.append(f.getVal("model"));
			return temp.toString();
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
			if (notSeen.get(arg0) == 0) {
				return searchIcon;
			} else {
				return searchNewIcon;
			}
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
		return true;
	}
}
