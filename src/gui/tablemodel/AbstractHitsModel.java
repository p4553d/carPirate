/**
 * 
 */
package gui.tablemodel;

import gui.message.HitGUI;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import core.config.Config;

/**
 * @author sucker
 * 
 */
public abstract class AbstractHitsModel extends AbstractTableModel {

	/**
	 * standard serialization uid
	 */
	private static final long serialVersionUID = 1L;

	protected String[] tableHead;
	protected String[] tableCaption;
	protected int[] columnWidth;
	protected Vector<HitGUI> rowData;

	@SuppressWarnings("unchecked")
	public AbstractHitsModel(String extractFile) {
		rowData = new Vector<HitGUI>();

		SAXBuilder builder = new SAXBuilder(false);
		Document extractXML;
		try {
			extractXML = builder.build(extractFile);

			XPath keywords = XPath.newInstance("gui.out/table/column");

			List<Element> keywordsList = keywords.selectNodes(extractXML);
			tableHead = new String[keywordsList.size()];
			tableCaption = new String[keywordsList.size()];
			columnWidth = new int[keywordsList.size()];

			Locale currLocale = new Locale(Config.getConfig().getLanguage());
			ResourceBundle captions = ResourceBundle.getBundle(
					"gui/bundle/gui", currLocale);

			for (int i = 0; i < keywordsList.size(); i++) {
				tableHead[i] = keywordsList.get(i).getAttributeValue("name");
				tableCaption[i] = captions.getString("table." + tableHead[i]);
				columnWidth[i] = new Integer(keywordsList.get(i)
						.getAttributeValue("width"));
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (tableHead[columnIndex].equals("price")) {
			return Integer.class;
		}
		return Object.class;
	}

	@Override
	public int getColumnCount() {
		return this.tableHead.length;
	}

	@Override
	public int getRowCount() {
		return rowData.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (tableHead[columnIndex].equals("price")) {
			return stringDot2Int((rowData.get(rowIndex))
					.getVal(tableHead[columnIndex]));
		}
		return (rowData.get(rowIndex)).getVal(tableHead[columnIndex]);
	}

	protected int stringDot2Int(String s) {
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
	public String getColumnName(int col) {
		return tableCaption[col];
	}

	public String getColumnKey(int col) {
		return tableHead[col];
	}

	public int getColumnWidth(int col) {
		return columnWidth[col];
	}

	public void addRow(HitGUI h) {
		rowData.add(h);
		fireTableRowsInserted(rowData.size() - 1, rowData.size() - 1);
	}

	public String getURL(int rowIndex) {
		return rowData.get(rowIndex).getVal("url");
	}

	public String getImageURL(int rowIndex) {
		return rowData.get(rowIndex).getVal("image");
	}

	public HitGUI getHit(int rowIndex) {
		return (this.rowData.get(rowIndex));
	}

	public void remRow(int rowIndex) {
		this.rowData.remove(rowIndex);
		fireTableRowsDeleted(rowIndex, rowIndex);
	}
}