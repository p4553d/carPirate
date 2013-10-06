/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainFormOutline.java
 *
 * Created on 09.05.2010, 22:44:44
 */
package gui;

import gui.message.HitGUI;
import gui.message.MessageListenerGUI;
import gui.tablemodel.AbstractHitsModel;
import gui.tablemodel.FavoritTableModel;
import gui.tablemodel.FilterRowModel;
import gui.tablemodel.FilterTreeModel;
import gui.tablemodel.CBItemUI;
import gui.tablemodel.TriTableSorter;
import gui.tablerender.TooltipTableRenderer;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.tree.TreePath;

import message.Message;
import message.MessageHandler;
import message.MessageListener;
import message.MessageType;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.OutlineModel;

import collector.data.Filter;
import collector.data.Hit;
import collector.data.IFilter;
import collector.message.MessageCollector;
import core.config.Config;

/**
 * 
 * @author sucker
 */
public class MainFormOutline extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2940386880643548843L;

	private FilterTreeModel treeMdl;
	private FilterRowModel frMdl;
	private final FavoritTableModel fvtm;

	private final MainFrameListener mListener;

	private String skin;
	private String language;
	private final ResourceBundle mainFormBundle;

	private TrayIcon trayIcon;
	private Image imageIcon;
	private Image imageIconAlert;

	private JMenuItem miOpen;
	private JMenuItem miFlush;
	private JMenuItem miRefresh;
	private IFilter activeFilter;

	private JMenuItem miSeen;
	private boolean bSound;
	private boolean bTrayMessage;

	/** Creates new form MainFormOutline */
	public MainFormOutline() {
		this.setTitle("CarPirate");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				(MainFormOutline.this).setVisible(false);
				treeMdl.markSeen();
			}
		});

		this.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				MainFormOutline.this.setIconImage(imageIcon);
				trayIcon.setImage(imageIcon);
				MainFormOutline.this.requestFocus();

			}
		});

		skin = Config.getConfig().getLaf();
		language = Config.getConfig().getLanguage();
		bSound = Config.getConfig().getSound();
		bTrayMessage = Config.getConfig().getTrayMessage();

		Locale currLocale = new Locale(language);
		mainFormBundle = ResourceBundle.getBundle("gui/bundle/gui", currLocale);

		fvtm = new FavoritTableModel("file:/"
				+ Config.getConfig().getHitsTableConf());

		initComponents();

		this.mListener = new MainFrameListener();

		SAXBuilder builder = new SAXBuilder(false);
		try {
			Document guiConfigXML = builder.build("file:/"
					+ Config.getConfig().getHitsTableConf());
			Element size = (Element) XPath.selectSingleNode(guiConfigXML,
					"/gui.out/size");
			int width = new Integer(size.getAttributeValue("width"));
			int height = new Integer(size.getAttributeValue("height"));
			this.setSize(width, height);

		} catch (JDOMException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		activeFilter = null;
	}

	private void saveLook() {
		Element guiout = new Element("gui.out");
		guiout.setAttribute("version",
				Integer.toString(Config.getConfig().getVersion()));

		// LAF
		Element laf = new Element("lookandfeel");
		laf.addContent(this.skin);
		guiout.addContent(laf);

		// Language
		Element lang = new Element("language");
		lang.addContent(this.language);
		guiout.addContent(lang);

		// Sound
		Element sound = new Element("sound");
		sound.addContent(new Boolean(this.bSound).toString());
		guiout.addContent(sound);

		// Tray message
		Element trayMessage = new Element("trayMessage");
		trayMessage.addContent(new Boolean(this.bTrayMessage).toString());
		guiout.addContent(trayMessage);

		// Size
		Element size = new Element("size");
		Dimension dim = this.getSize();
		size.setAttribute("width", String.valueOf((int) dim.getWidth()));
		size.setAttribute("height", String.valueOf((int) dim.getHeight()));
		guiout.addContent(size);

		// Table columns

		Element xmlTable = new Element("table");
		Enumeration<TableColumn> itc = outlineSearch.getColumnModel()
				.getColumns();
		while (itc.hasMoreElements()) {
			TableColumn tmpTC = itc.nextElement();
			Element xmlColumn = new Element("column");
			int idx = tmpTC.getModelIndex();
			xmlColumn.setAttribute("name", fvtm.getColumnKey(idx));
			xmlColumn.setAttribute("width", String.valueOf(tmpTC.getWidth()));

			xmlTable.addContent(xmlColumn);
		}

		guiout.addContent(xmlTable);

		Document doc = new Document(guiout);
		XMLOutputter out = new XMLOutputter();
		try {
			// out.setFormat(Format.getPrettyFormat());
			out.output(doc, new FileOutputStream(Config.getConfig()
					.getHitsTableConf()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {

		jTabbedPane1 = new javax.swing.JTabbedPane();
		jPanelSearch = new javax.swing.JPanel();
		jToolBarSearch = new javax.swing.JToolBar();
		jButtonCollapse = new javax.swing.JButton();
		jButtonAddFav = new javax.swing.JButton();
		jSeparator1 = new javax.swing.JToolBar.Separator();
		jButtonAddSearch = new javax.swing.JButton();
		jButtonRemSearch = new javax.swing.JButton();
		jSeparator2 = new javax.swing.JToolBar.Separator();
		jButtonRemHit = new javax.swing.JButton();
		jButtonRemAllHits = new javax.swing.JButton();
		jScrollPaneSearch = new javax.swing.JScrollPane();
		outlineSearch = new org.netbeans.swing.outline.Outline();
		jPanelFav = new javax.swing.JPanel();
		jToolBarFav = new javax.swing.JToolBar();
		jButtonRemFav = new javax.swing.JButton();
		new javax.swing.JToolBar.Separator();
		new javax.swing.JButton();
		new javax.swing.JButton();
		new javax.swing.JToolBar.Separator();
		new javax.swing.JButton();
		new javax.swing.JButton();
		jScrollPanefav = new javax.swing.JScrollPane();
		jMenuBarMain = new javax.swing.JMenuBar();
		jMenuFile = new javax.swing.JMenu();
		jMenuView = new javax.swing.JMenu();
		jMenuLanguage = new javax.swing.JMenu();
		jMenuItemExit = new javax.swing.JMenuItem();
		jMenuSettings = new javax.swing.JMenu();
		jMenuHelp = new javax.swing.JMenu();
		jMenuItem2 = new javax.swing.JMenuItem();
		jMenuItem3 = new javax.swing.JMenuItem();
		jPopMenu = new javax.swing.JPopupMenu();

		imageIcon = Toolkit.getDefaultToolkit().getImage(
				MainFormOutline.class.getResource("images/clock.png"));
		imageIconAlert = Toolkit.getDefaultToolkit().getImage(
				MainFormOutline.class.getResource("images/clock_red.png"));
		this.setIconImage(imageIcon);

		jToolBarSearch.setRollover(true);
		jToolBarSearch.setFloatable(false);

		jButtonCollapse.setFocusable(false);
		jButtonCollapse
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButtonCollapse
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jButtonCollapse.setText(mainFormBundle.getString("button.collapse"));
		jButtonCollapse.setToolTipText(mainFormBundle.getString("button.tooltip.collapse"));
		jButtonCollapse.setIcon(new ImageIcon(MainFormOutline.class
				.getResource("images/collapse.png")));
		jButtonCollapse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				markAllSeen(e);
			}
		});
		jToolBarSearch.add(jButtonCollapse);

		jToolBarSearch.add(new javax.swing.JToolBar.Separator());

		jButtonAddFav.setFocusable(false);
		jButtonAddFav
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButtonAddFav
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jButtonAddFav.setText(mainFormBundle.getString("button.mark"));
		jButtonAddFav.setToolTipText(mainFormBundle.getString("button.tooltip.mark"));
		jButtonAddFav.setIcon(new ImageIcon(MainFormOutline.class
				.getResource("images/accept.png")));
		jButtonAddFav.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				favoritAddAction(e);
			}
		});
		jToolBarSearch.add(jButtonAddFav);

		jToolBarSearch.add(jSeparator1);

		jButtonAddSearch.setFocusable(false);
		jButtonAddSearch
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButtonAddSearch.setName("jButton2"); // NOI18N
		jButtonAddSearch
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jButtonAddSearch.setText(mainFormBundle.getString("button.add"));
		jButtonAddSearch.setToolTipText(mainFormBundle.getString("button.tooltip.add"));
		jButtonAddSearch.setIcon(new ImageIcon(MainFormOutline.class
				.getResource("images/search_add.png")));
		jButtonAddSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filterAddAction(e);
			}
		});
		jToolBarSearch.add(jButtonAddSearch);

		jButtonRemSearch.setFocusable(false);
		jButtonRemSearch
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButtonRemSearch.setName("jButton3"); // NOI18N
		jButtonRemSearch
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jButtonRemSearch.setText(mainFormBundle
				.getString("button.remove.filter"));
		jButtonRemSearch.setToolTipText(mainFormBundle
				.getString("button.tooltip.remove.filter"));
		jButtonRemSearch.setIcon(new ImageIcon(MainFormOutline.class
				.getResource("images/search_delete.png")));
		jButtonRemSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filterDeleteAction(e);
			}
		});
		jToolBarSearch.add(jButtonRemSearch);

		jToolBarSearch.add(jSeparator2);

		jButtonRemHit.setFocusable(false);
		jButtonRemHit
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButtonRemHit.setName("jButton4"); // NOI18N
		jButtonRemHit
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jButtonRemHit.setText(mainFormBundle.getString("button.remove.hit"));
		jButtonRemHit.setToolTipText(mainFormBundle
				.getString("button.tooltip.remove.hit"));
		jButtonRemHit.setIcon(new ImageIcon(MainFormOutline.class
				.getResource("images/delete.png")));
		jButtonRemHit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hitsRemoveAction(e);
			}
		});
		jToolBarSearch.add(jButtonRemHit);

		jButtonRemAllHits.setFocusable(false);
		jButtonRemAllHits
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButtonRemAllHits.setName("jButton5"); // NOI18N
		jButtonRemAllHits
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jButtonRemAllHits.setText(mainFormBundle
				.getString("button.remove.allhits"));
		jButtonRemAllHits.setIcon(new ImageIcon(MainFormOutline.class
				.getResource("images/bin.png")));
		jButtonRemAllHits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hitsFlushAllAction(e);
			}
		});
		jButtonRemAllHits.setToolTipText(mainFormBundle
				.getString("button.tooltip.remove.allhits"));
		jToolBarSearch.add(jButtonRemAllHits);

		jScrollPaneSearch.setName("jScrollPaneSearch"); // NOI18N

		outlineSearch.setName("outlineSearch"); // NOI18N
		// Init outline
		Filter rf = new Filter();
		treeMdl = new FilterTreeModel(rf);
		frMdl = new FilterRowModel("file:/"
				+ Config.getConfig().getHitsTableConf());
		OutlineModel mdl = DefaultOutlineModel.createOutlineModel(treeMdl,
				frMdl, true, mainFormBundle.getString("table.title"));

		outlineSearch.setRootVisible(false);
		outlineSearch.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		outlineSearch.setModel(mdl);
		outlineSearch.setRenderDataProvider(treeMdl);

		for (int i = 0; i < outlineSearch.getColumnCount(); i++) {
			TableColumn col = outlineSearch.getColumnModel().getColumn(i);
			col.setPreferredWidth((fvtm).getColumnWidth(i));
		}

		outlineSearch.setRowHeight(20);
		outlineSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				outlineRowClickedPopup(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				outlineRowClickedPopup(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				outlineRowClicked(e);
			}
		});

		jScrollPaneSearch.setViewportView(outlineSearch);

		javax.swing.GroupLayout jPanelSearchLayout = new javax.swing.GroupLayout(
				jPanelSearch);
		jPanelSearch.setLayout(jPanelSearchLayout);
		jPanelSearchLayout.setHorizontalGroup(jPanelSearchLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jToolBarSearch,
						javax.swing.GroupLayout.DEFAULT_SIZE, 614,
						Short.MAX_VALUE)
				.addComponent(jScrollPaneSearch,
						javax.swing.GroupLayout.DEFAULT_SIZE, 614,
						Short.MAX_VALUE));
		jPanelSearchLayout
				.setVerticalGroup(jPanelSearchLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanelSearchLayout
										.createSequentialGroup()
										.addComponent(
												jToolBarSearch,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												50,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jScrollPaneSearch,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												370, Short.MAX_VALUE)));

		jTabbedPane1.addTab(
				mainFormBundle.getString("tab.search"),
				new ImageIcon(MainFormOutline.class
						.getResource("images/magnifier.png")), jPanelSearch);

		jPanelFav.setName("jPanelFav"); // NOI18N

		jToolBarFav.setRollover(true);
		jToolBarFav.setFloatable(false);
		jToolBarFav.setName("jToolBarFav"); // NOI18N

		jButtonRemFav.setFocusable(false);
		jButtonRemFav
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jButtonRemFav
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jButtonRemFav.setText(mainFormBundle.getString("button.remove.fav"));
		jButtonRemFav.setIcon(new ImageIcon(MainFormOutline.class
				.getResource("images/heart_delete.png")));
		jButtonRemFav.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				favRemoveAction(e);
			}
		});
		jButtonRemFav.setToolTipText(mainFormBundle
				.getString("button.tooltip.remove.fav"));
		jToolBarFav.add(jButtonRemFav);

		jScrollPanefav.setName("jScrollPanefav"); // NOI18N

		// Favorit Table
		fvtm.readFavorits();
		jTableFav = new JTable(fvtm);
		for (int i = 0; i < fvtm.getColumnCount(); i++) {
			TableColumn col = jTableFav.getColumnModel().getColumn(i);
			col.setPreferredWidth(((AbstractHitsModel) fvtm).getColumnWidth(i));
		}
		jTableFav.setRowHeight(20);
		jTableFav.setDefaultRenderer(Object.class, new TooltipTableRenderer());
		jTableFav.setRowSorter(new TriTableSorter<TableModel>(fvtm));
		jTableFav.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				favRowClicked(e);
			}
		});
		jScrollPanefav.setViewportView(jTableFav);

		javax.swing.GroupLayout jPanelFavLayout = new javax.swing.GroupLayout(
				jPanelFav);
		jPanelFav.setLayout(jPanelFavLayout);
		jPanelFavLayout.setHorizontalGroup(jPanelFavLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jToolBarFav,
						javax.swing.GroupLayout.DEFAULT_SIZE, 614,
						Short.MAX_VALUE)
				.addComponent(jScrollPanefav,
						javax.swing.GroupLayout.DEFAULT_SIZE, 614,
						Short.MAX_VALUE));
		jPanelFavLayout
				.setVerticalGroup(jPanelFavLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanelFavLayout
										.createSequentialGroup()
										.addComponent(
												jToolBarFav,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												50,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jScrollPanefav,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												370, Short.MAX_VALUE)));

		jTabbedPane1.addTab(
				mainFormBundle.getString("tab.favorits"),
				new ImageIcon(MainFormOutline.class
						.getResource("images/star.png")), jPanelFav);

		jMenuBarMain.setName("jMenuBarMain"); // NOI18N

		jMenuFile.setText(mainFormBundle.getString("menu.file"));
		jMenuFile.setName("jMenuFile"); // NOI18N

		jMenuItemExit.setText(mainFormBundle.getString("menu.exit"));
		jMenuItemExit.setIcon(new ImageIcon(MainFormOutline.class
				.getResource("images/exit.png")));
		jMenuItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				teardown();
			}
		});
		jMenuFile.add(jMenuItemExit);
		jMenuBarMain.add(jMenuFile);

		jMenuView.setText(mainFormBundle.getString("menu.view"));
		javax.swing.ButtonGroup bgView = new javax.swing.ButtonGroup();
		javax.swing.JRadioButtonMenuItem menuItemSkin1 = new javax.swing.JRadioButtonMenuItem(
				"Blue Steel");
		menuItemSkin1
				.setSelected(skin
						.equals("org.jvnet.substance.skin.SubstanceBusinessBlueSteelLookAndFeel"));
		menuItemSkin1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectSkin("org.jvnet.substance.skin.SubstanceBusinessBlueSteelLookAndFeel");
			}
		});

		bgView.add(menuItemSkin1);
		jMenuView.add(menuItemSkin1);
		JRadioButtonMenuItem menuItemSkin2 = new JRadioButtonMenuItem(
				"Black Steel");
		menuItemSkin2
				.setSelected(skin
						.equals("org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel"));
		menuItemSkin2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectSkin("org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel");
			}
		});

		bgView.add(menuItemSkin2);
		jMenuView.add(menuItemSkin2);
		JRadioButtonMenuItem menuItemSkin3 = new JRadioButtonMenuItem("Nebula");
		menuItemSkin3.setSelected(skin
				.equals("org.jvnet.substance.skin.SubstanceNebulaLookAndFeel"));
		menuItemSkin3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectSkin("org.jvnet.substance.skin.SubstanceNebulaLookAndFeel");
			}
		});

		bgView.add(menuItemSkin3);
		jMenuView.add(menuItemSkin3);
		jMenuView.addSeparator();
		JRadioButtonMenuItem menuItemSkin4 = new JRadioButtonMenuItem("Dust");
		menuItemSkin4.setSelected(skin
				.equals("org.jvnet.substance.skin.SubstanceDustLookAndFeel"));
		menuItemSkin4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectSkin("org.jvnet.substance.skin.SubstanceDustLookAndFeel");
			}
		});

		bgView.add(menuItemSkin4);
		jMenuView.add(menuItemSkin4);
		JRadioButtonMenuItem menuItemSkin5 = new JRadioButtonMenuItem("Raven");
		menuItemSkin5.setSelected(skin
				.equals("org.jvnet.substance.skin.SubstanceRavenLookAndFeel"));
		menuItemSkin5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectSkin("org.jvnet.substance.skin.SubstanceRavenLookAndFeel");
			}
		});

		bgView.add(menuItemSkin5);
		jMenuView.add(menuItemSkin5);
		JRadioButtonMenuItem menuItemSkin6 = new JRadioButtonMenuItem(
				"Raven Graphite Glass");
		menuItemSkin6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectSkin("org.jvnet.substance.skin.SubstanceRavenGraphiteGlassLookAndFeel");
			}
		});

		bgView.add(menuItemSkin6);
		jMenuView.add(menuItemSkin6);

		// ==== menu5 ====

		jMenuLanguage.setText(mainFormBundle.getString("menu.language"));
		ButtonGroup bgLang = new ButtonGroup();
		JRadioButtonMenuItem menuItemDE = new JRadioButtonMenuItem(
				mainFormBundle.getString("menu.deutsch"), new ImageIcon(
						MainFormOutline.class.getResource("images/de.png")));
		menuItemDE.setSelected(this.language.equals("de"));
		menuItemDE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				language = "de";
			}
		});
		bgLang.add(menuItemDE);
		jMenuLanguage.add(menuItemDE);

		JRadioButtonMenuItem menuItemEN = new JRadioButtonMenuItem(
				mainFormBundle.getString("menu.english"), new ImageIcon(
						MainFormOutline.class.getResource("images/gb.png")));
		menuItemEN.setSelected(this.language.equals("en"));
		menuItemEN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				language = "en";
			}
		});
		bgLang.add(menuItemEN);
		jMenuLanguage.add(menuItemEN);

		JRadioButtonMenuItem menuItemRU = new JRadioButtonMenuItem(
				mainFormBundle.getString("menu.russian"), new ImageIcon(
						MainFormOutline.class.getResource("images/ru.png")));
		menuItemRU.setSelected(this.language.equals("ru"));
		menuItemRU.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				language = "ru";
			}
		});
		bgLang.add(menuItemRU);
		jMenuLanguage.add(menuItemRU);

		jMenuSettings.setText(mainFormBundle.getString("menu.settings"));
		jMenuSettings.add(jMenuView);
		jMenuSettings.add(jMenuLanguage);

		// some local settings
		jMenuSettings.addSeparator();

		final JCheckBoxMenuItem menuItemTrayMessage = new JCheckBoxMenuItem(mainFormBundle.getString("menu.trayMessage"));
		menuItemTrayMessage.setUI(new CBItemUI());
		menuItemTrayMessage.setSelected(bTrayMessage);
		menuItemTrayMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bTrayMessage = !bTrayMessage;
				menuItemTrayMessage.setSelected(bTrayMessage);
			}
		});
		jMenuSettings.add(menuItemTrayMessage);

		final JCheckBoxMenuItem menuItemSound = new JCheckBoxMenuItem(mainFormBundle.getString("menu.sound"));
		menuItemSound.setUI(new CBItemUI());
		menuItemSound.setSelected(bSound);
		menuItemSound.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bSound = !bSound;
				menuItemSound.setSelected(bSound);
			}
		});
		jMenuSettings.add(menuItemSound);

		jMenuBarMain.add(jMenuSettings);

		jMenuHelp.setText(mainFormBundle.getString("menu.help"));

		JMenuItem menuItem71 = new JMenuItem(
				mainFormBundle.getString("menu.onlinehelp"));
		menuItem71.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MessageHandler.getHandler().broadcast(
						"http://www.carpirate.de", MessageType.VISITURL);
			}
		});
		JMenuItem menuItem72 = new JMenuItem(
				mainFormBundle.getString("menu.about"));
		menuItem72.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane optionPane = new JOptionPane();
				optionPane.setMessage(mainFormBundle
						.getString("menu.abouttext"));
				optionPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
				JDialog dialog = optionPane.createDialog(MainFormOutline.this,
						mainFormBundle.getString("menu.about"));
				dialog.setVisible(true);
			}
		});

		jMenuHelp.add(menuItem71);

		jMenuHelp.add(menuItem72);

		jMenuBarMain.add(jMenuHelp);

		setJMenuBar(jMenuBarMain);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 619,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 426,
				Short.MAX_VALUE));

		pack();
		setLocationRelativeTo(getOwner());

		// Trayicon / -menu definition
		PopupMenu popup = new PopupMenu();
		MenuItem pop_show = new MenuItem(mainFormBundle.getString("menu.show"));
		pop_show.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				(MainFormOutline.this).setVisible(true);
			}
		});
		MenuItem pop_exit = new MenuItem(mainFormBundle.getString("menu.exit"));
		pop_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				teardown();
			}
		});
		popup.add(pop_show);
		popup.addSeparator();
		popup.add(pop_exit);

		trayIcon = new TrayIcon(imageIcon, "CarCrawler", popup);
		trayIcon.setImageAutoSize(true);
		trayIcon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				(MainFormOutline.this).setVisible(!(MainFormOutline.this)
						.isVisible());
			}
		});

		SystemTray tray = SystemTray.getSystemTray();
		try {
			tray.add(trayIcon);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}

		// Pop-up menu definitions
		miOpen = new JMenuItem(mainFormBundle.getString("menu.openBrowser"));
		miOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (activeFilter instanceof HitGUI) {
					MessageHandler.getHandler().broadcast(
							activeFilter.getVal("url"), MessageType.VISITURL);
				} else {
					MessageHandler.getHandler().receiveMessage(
							new MessageCollector(activeFilter,
									MessageType.VISITURL));
				}
			}
		});
		miFlush = new JMenuItem(mainFormBundle.getString("menu.flush"));
		miFlush.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (activeFilter instanceof Filter) {
					treeMdl.flushHits((Filter) activeFilter);
				}
			}
		});
		miSeen = new JMenuItem(mainFormBundle.getString("menu.markSeen"));
		miSeen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (activeFilter instanceof Filter) {
					treeMdl.markSeen((Filter) activeFilter);
				}
			}
		});
		miRefresh = new JMenuItem(mainFormBundle.getString("menu.restart"));
		miRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (activeFilter instanceof Filter) {
					treeMdl.flushHits((Filter) activeFilter);
					MessageHandler.getHandler().receiveMessage(
							new MessageCollector(activeFilter,
									MessageType.REFRESH));
				}
			}
		});

		jPopMenu.add(miOpen);
		jPopMenu.add(miSeen);
		jPopMenu.add(miFlush);
		jPopMenu.add(miRefresh);
	}

	/**
	 * @param e
	 */
	protected void outlineRowClickedPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			activeFilter = (IFilter) (outlineSearch.getClosestPathForLocation(
					e.getX(), e.getY()).getLastPathComponent());
			if (activeFilter instanceof HitGUI) {
				miFlush.setEnabled(false);
				miRefresh.setEnabled(false);
			} else {
				miFlush.setEnabled(true);
				miRefresh.setEnabled(true);
			}
			jPopMenu.show(e.getComponent(), e.getX(), e.getY());
		}
		e.consume();
	}

	/**
	 * @param e
	 */
	protected void favRemoveAction(ActionEvent e) {
		int[] selRows = jTableFav.getSelectedRows();
		FavoritTableModel ftm = (FavoritTableModel) jTableFav.getModel();

		while (selRows.length != 0) {
			int tmpRow = jTableFav.convertRowIndexToModel(selRows[0]);
			ftm.getHit(tmpRow).setFav(false);
			ftm.remRow(tmpRow);
			selRows = jTableFav.getSelectedRows();
		}
		// ftm.saveFavorits();
	}

	/**
	 * @param e
	 */
	protected void markAllSeen(ActionEvent e) {
			this.collapseAll();
			treeMdl.markSeen();
	}
	
	protected void collapseAll() {
		int cnt = treeMdl.getChildCount(treeMdl.getRoot());
		for (int i = 0; i < cnt; i++) {
			outlineSearch
					.collapsePath(new TreePath(new Object[] {
							treeMdl.getRoot(),
							treeMdl.getChild(treeMdl.getRoot(), i) }));
		}
	}

	/**
	 * @param e
	 */
	protected void hitsFlushAllAction(ActionEvent e) {
		treeMdl.flushHits();
	}

	/**
	 * @param e
	 */
	protected void hitsRemoveAction(ActionEvent e) {
		int rows = outlineSearch.getSelectedRow();
		IFilter af = (IFilter) outlineSearch.getValueAt(rows, 0);
		if (af != null && af instanceof Filter) {
			treeMdl.flushHits((Filter) af);
		}
		if (af != null && af instanceof HitGUI) {
			treeMdl.removeHit((HitGUI) af);
		}
	}

	/**
	 * @param e
	 */
	protected void filterDeleteAction(ActionEvent e) {
		int rows = outlineSearch.getSelectedRow();
		IFilter af = (IFilter) outlineSearch.getValueAt(rows, 0);
		if (af != null && af instanceof Filter) {
			MessageHandler.getHandler().receiveMessage(
					new MessageCollector(af, MessageType.REMOVE));
			treeMdl.removeFilter((Filter) af); // remove from tree
		}
	}

	/**
	 * @param e
	 */
	protected void favoritAddAction(ActionEvent e) {
		int selRows = outlineSearch.getSelectedRow();

		FavoritTableModel ftm = (FavoritTableModel) jTableFav.getModel();

		Object o = outlineSearch.getValueAt(selRows, 0);
		if (o instanceof HitGUI) {
			((HitGUI) o).setFav(true);
			ftm.addRow((HitGUI) o);
		}
		ftm.saveFavorits();
	}

	/**
	 * @param e
	 */
	protected void filterAddAction(ActionEvent e) {
		FilterForm ff;
		ff = new FilterForm(this);

		ff.setVisible(true);

	}

	/**
	 * @param e
	 */
	protected void favRowClicked(final MouseEvent e) {
		if (e.getClickCount() == 2) {
			JTable target = (JTable) e.getSource();
			int rowIndex = target.convertRowIndexToModel(target
					.getSelectedRow());
			AbstractHitsModel htm = (AbstractHitsModel) target.getModel();
			String url = htm.getURL(rowIndex);
			MessageHandler.getHandler().broadcast(url, MessageType.VISITURL);
		}
	}

	/**
	 * @param e
	 */
	protected void outlineRowClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && !e.isPopupTrigger()) {
			Object sp = outlineSearch.getClosestPathForLocation(e.getX(),
					e.getY()).getLastPathComponent();
			if (sp instanceof HitGUI) {
				MessageHandler.getHandler().broadcast(
						((HitGUI) sp).getVal("url"), MessageType.VISITURL);
			}
		}

		if (e.isPopupTrigger()) {
			activeFilter = (IFilter) (outlineSearch.getClosestPathForLocation(
					e.getX(), e.getY()).getLastPathComponent());
			boolean on = (activeFilter instanceof Filter);
			miFlush.setEnabled(on);
			miRefresh.setEnabled(on);
			miSeen.setEnabled(on);
			jPopMenu.show(e.getComponent(), e.getX(), e.getY());
		}

		e.consume();
	}

	public MessageListener getListener() {
		return this.mListener;
	}

	public void addFilter(Filter f) {
//		this.collapseAll();
		treeMdl.addFilter(f);
	}

	public void addHit(Hit h) {
		HitGUI hg = new HitGUI(h);
		treeMdl.addHit(hg);

		if (!this.hasFocus()) {
			this.setIconImage(imageIconAlert);
			this.trayIcon.setImage(imageIconAlert);
			if (bTrayMessage) {
				StringBuffer descr = new StringBuffer(h.getVal("title")
						+ "\n\n");
				if (h.getVal("price") != null) {
					descr.append(h.getVal("price") + " EUR\n");
				}
				if (h.getVal("mileage") != null) {
					descr.append(h.getVal("mileage") + " km");
				}
				this.trayIcon.displayMessage("New Hits for "
						+ h.getParent().getVal("make") + " "
						+ h.getParent().getVal("model"), descr.toString(),
						TrayIcon.MessageType.INFO);
			}
			if (bSound) {
				Toolkit.getDefaultToolkit().beep();
			}
		}
	}

	private void teardown() {
		// Save data from tables
		saveData();

		// Save look from tableHit
		saveLook();

		// Send a message to all
		MessageHandler.getHandler().receiveMessage(
				new Message("User closes application", MessageType.CLOSE));
		this.dispose();
		SystemTray.getSystemTray().remove(trayIcon);
	}

	/**
	 * 
	 */
	private void saveData() {
		treeMdl.saveFilters();
		fvtm.saveFavorits();
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButtonCollapse;
	private javax.swing.JButton jButtonAddFav;
	private javax.swing.JButton jButtonAddSearch;
	private javax.swing.JButton jButtonRemSearch;
	private javax.swing.JButton jButtonRemHit;
	private javax.swing.JButton jButtonRemAllHits;
	private javax.swing.JButton jButtonRemFav;
	private javax.swing.JMenuBar jMenuBarMain;
	private javax.swing.JMenu jMenuFile;
	private javax.swing.JMenu jMenuHelp;
	private javax.swing.JMenuItem jMenuItemExit;
	private javax.swing.JMenuItem jMenuItem2;
	private javax.swing.JMenuItem jMenuItem3;
	private javax.swing.JMenu jMenuView;
	private javax.swing.JMenu jMenuLanguage;
	private javax.swing.JMenu jMenuSettings;
	private javax.swing.JPanel jPanelFav;
	private javax.swing.JPanel jPanelSearch;
	private javax.swing.JScrollPane jScrollPaneSearch;
	private javax.swing.JScrollPane jScrollPanefav;
	private javax.swing.JToolBar.Separator jSeparator1;
	private javax.swing.JToolBar.Separator jSeparator2;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JTable jTableFav;
	private javax.swing.JToolBar jToolBarFav;
	private javax.swing.JToolBar jToolBarSearch;
	private org.netbeans.swing.outline.Outline outlineSearch;
	private javax.swing.JPopupMenu jPopMenu;

	// End of variables declaration//GEN-END:variables

	private class MainFrameListener extends MessageListenerGUI {

		/*
		 * (non-Javadoc)
		 * 
		 * @see gui.message.MessageListenerGUI#addHit(collector.data.Hit)
		 */
		@Override
		public void addHit(Hit h) {
			MainFormOutline.this.addHit(h);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see message.Listener.MessageListener#getListener()
		 */
		@Override
		public MessageListener getListener() {
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see gui.message.MessageListenerGUI#addFilter(collector.data.Filter)
		 */
		@Override
		public void addFilter(Filter f) {
			MainFormOutline.this.addFilter(f);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * gui.message.MessageListenerGUI#removeFilter(collector.data.Filter)
		 */
		@Override
		public void removeFilter(IFilter f) {
			// do nothing
		}
	}

	private void selectSkin(String skin) {

		this.skin = skin;

		try {
			UIManager.setLookAndFeel(skin);
			this.repaint();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (javax.swing.UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

}