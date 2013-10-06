/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.tablemodel;

import gui.message.HitGUI;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.netbeans.swing.outline.RowModel;

import collector.data.Filter;
import core.config.Config;

/**
 * 
 * @author sucker
 */
public class FilterRowModel extends DefaultTableModel implements RowModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7734451785328558007L;
	protected String[] tableHead;
	protected String[] tableCaption;
	protected int[] columnWidth;
	protected Vector<HitGUI> rowData;
	protected ResourceBundle captions;

	@SuppressWarnings("unchecked")
	public FilterRowModel(String extractFile) {
		rowData = new Vector<HitGUI>();

		SAXBuilder builder = new SAXBuilder(false);
		Document extractXML;
		try {
			extractXML = builder.build(extractFile);

			XPath keywords = XPath.newInstance("gui.out/table/column");

			List<Element> keywordsList = keywords.selectNodes(extractXML);
			tableHead = new String[keywordsList.size() - 1];
			tableCaption = new String[keywordsList.size() - 1];
			columnWidth = new int[keywordsList.size() - 1];

			Locale currLocale = new Locale(Config.getConfig().getLanguage());
			captions = ResourceBundle.getBundle("gui/bundle/gui", currLocale);
			int j = 0;
			for (int i = 0; i < keywordsList.size(); i++) {
				if (!keywordsList.get(i).getAttributeValue("name").equals(
						"title")) {
					tableHead[j] = keywordsList.get(i)
							.getAttributeValue("name");
					tableCaption[j] = captions.getString("table."
							+ tableHead[j]);
					columnWidth[j] = new Integer(keywordsList.get(i)
							.getAttributeValue("width"));
					j++;
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO expand for different (configurable) classes
		if (tableHead[columnIndex].equals("price")) {
			return Integer.class;
		}
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return this.tableHead.length;
	}

	@Override
	public String getColumnName(int col) {
		return tableCaption[col];
	}

	@Override
	public Object getValueFor(Object node, int column) {
		if (node instanceof HitGUI) {
			HitGUI f = (HitGUI) node;
			if (tableHead[column].equals("price")) {
				return stringDot2Int(f.getVal(tableHead[column]));
			}
			return f.getVal(tableHead[column]);
		}

		if (node instanceof Filter) {
			Filter f = (Filter) node;
			if (f.getVal(tableHead[column]) == null) {
				String from = f.getVal(tableHead[column] + "From");
				String to = f.getVal(tableHead[column] + "To");
				if (from != null && to != null) {
					return from + " - " + to;
				} else {
					if (from != null) {
						return captions.getString("filter.von") + " " + from;
					}
					if (to != null) {
						return captions.getString("filter.bis") + " " + to;
					}
				}
			}
		}

		return null;
	}

	private int stringDot2Int(String s) {
		if (s != null) {
			String tmp = "";
			for (int i = 0; i < s.length(); i++) {
				if (s.charAt(i) != '.') {
					tmp += s.charAt(i);
				}
			}
			return Integer.valueOf(tmp);
		}
		return 0;
	}

	@Override
	public boolean isCellEditable(Object node, int column) {
		return false;
	}

	@Override
	public void setValueFor(Object node, int column, Object value) {
		// do nothing for now
	}

	/**
	 * @param i
	 * @return
	 */
	public int getColumnWidth(int idx) {
		return columnWidth[idx];
	}

	/**
	 * @param idx
	 * @return
	 */
	public String getColumnKey(int idx) {
		return tableHead[idx];
	}
}
