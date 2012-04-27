package at.netcrawler.ui.crawler;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;

import at.andiwand.library.component.JFrameUtil;
import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.io.json.JsonHelper;
import at.netcrawler.network.connection.ConnectionGateway;
import at.netcrawler.network.connection.ssh.LocalSSHGateway;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;
import at.netcrawler.network.crawler.SimpleNetworkCrawler;
import at.netcrawler.network.manager.DeviceManagerFactory;
import at.netcrawler.network.manager.cli.CommandLineDeviceManagerFactory;
import at.netcrawler.network.topology.HashTopology;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.ui.assistant.ConfigurationManager;
import at.netcrawler.ui.component.CrapGraphLayout;
import at.netcrawler.ui.component.TopologyViewer;
import at.netcrawler.ui.device.DeviceView;
import at.netcrawler.util.Settings;

@SuppressWarnings("serial")
public class GUI extends JFrame {

    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");

    private JScrollPane scrollPane;
    private TopologyViewer viewer;
    private DeviceTable table;
    private JLabel statusLabel;
    private JMenuItem saveItem;
    private JFileChooser fileChooser;
    private Topology topology;
    private boolean dontClose;
    private boolean tableVisible;

    public GUI(Topology topology) {
        this.topology = topology;

        setTitle("netCrawler");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                Settings.setLastWindowSize(getSize());
            }
        });

        fileChooser = new JFileChooser(Settings.getLastCrawl());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return "*.crawl - Saved netCrawler Crawls";
            }

            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".crawl");
            }
        });

        JMenuBar menu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        final JMenu helpMenu = new JMenu("Help");
        JMenuItem crawlItem = new JMenuItem("Crawl...");
        JMenuItem loadItem = new JMenuItem("Load");
        saveItem = new JMenuItem("Save");
        JMenuItem closeItem = new JMenuItem("Exit");
        JMenuItem toggleViewItem = new JMenuItem("Toggle view");

        saveItem.setEnabled(false);

        helpMenu.addMenuListener(new MenuListener() {

            @Override
            public void menuSelected(MenuEvent arg0) {
                JOptionPane
                        .showMessageDialog(
                                GUI.this,
                                "Help is available online at http://netcrawler.tomtasche.at/",
                                "Help", JOptionPane.INFORMATION_MESSAGE);

                helpMenu.setSelected(false);
            }

            @Override
            public void menuDeselected(MenuEvent arg0) {
            }

            @Override
            public void menuCanceled(MenuEvent arg0) {
            }
        });
        loadItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(GUI.this) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    FileReader reader = null;
                    BufferedReader bufferedReader = null;
                    try {
                        reader = new FileReader(file);
                        bufferedReader = new BufferedReader(reader);

                        String json = "";
                        for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                                .readLine()) {
                            json += s + LINE_SEPARATOR;
                        }

                        viewer = JsonHelper.getGson().fromJson(json,
                                TopologyViewer.class);

                        // TODO: ?
                        GUI.this.topology = (Topology) viewer.getModel();

                        table.setTopology(GUI.this.topology);

                        viewer.addVertexMouseListener(new MouseAdapter() {

                            @Override
                            public void mouseClicked(MouseEvent e) {
                                handleMouse(e, (TopologyDevice) e.getSource());
                            }
                        });
                        
                        scrollPane.setViewportView(viewer);
                    } catch (IOException ex) {
                        // TODO Auto-generated catch block
                        ex.printStackTrace();
                    } finally {
                        try {
                            if (bufferedReader != null) bufferedReader.close();
                        } catch (IOException exc) {
                        }
                        try {
                            if (reader != null) reader.close();
                        } catch (IOException exc) {
                        }
                    }
                }
            }
        });
        saveItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showSaveDialog(GUI.this) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    if (file.getName().endsWith(".crawl")) {
                        file = new File(file.getAbsoluteFile() + ".crawl");
                    }

                    FileWriter writer = null;
                    try {
                        writer = new FileWriter(file);

                        String json = JsonHelper.getGson().toJson(
                                GUI.this.viewer);

                        writer.write(json);
                        writer.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        try {
                            if (writer != null) writer.close();
                        } catch (IOException exc) {
                        }
                    }
                }
            }
        });
        closeItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        crawlItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                crawl();
            }
        });
        toggleViewItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                toggleView();
            }
        });

        fileMenu.add(crawlItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(closeItem);
        viewMenu.add(toggleViewItem);

        menu.add(fileMenu);
        menu.add(viewMenu);
        menu.add(helpMenu);

        setJMenuBar(menu);

        table = new DeviceTable(this);
        table.setTopology(topology);

        viewer = new TopologyViewer();
        // TODO: use another GraphLayout
        viewer.setGraphLayout(new CrapGraphLayout(viewer));
        viewer.setModel(topology);
        viewer.addRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        viewer.addRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        viewer.addVertexMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouse(e, (TopologyDevice) e.getSource());
            }
        });

        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(400, 400));
        if (Settings.getLastView() == 1) {
            scrollPane.setViewportView(table);

            tableVisible = true;
        } else {
            scrollPane.setViewportView(viewer);
        }

        statusLabel = new JLabel();
        statusLabel
                .setText("Start a new crawl or load an old one using the menu above...");

        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        Dimension lastSize = Settings.getLastWindowSize();
        if (lastSize == null) {
            pack();
            setMinimumSize(getSize());
        } else {
            setSize(lastSize);
        }

        JFrameUtil.centerFrame(this);

        setVisible(true);
    }

    protected void handleMouse(final MouseEvent event,
            final TopologyDevice device) {
        // TODO: OH. MY. GOD! :|
        new Thread() {

            @Override
            public void run() {
                JFrame frame = null;
                if (event.getButton() == MouseEvent.BUTTON1) {
                    try {
                        frame = new DeviceView(device);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (event.getButton() == MouseEvent.BUTTON3) {
                    frame = new ConfigurationManager(device);
                }

                if (frame != null) {
                    JFrameUtil.centerFrame(frame);
                    frame.setVisible(true);
                }
            }
        }.start();
    }

    private void close() {
        if (dontClose
                && JOptionPane.showOptionDialog(GUI.this,
                        "Do you really want to close netCrawler?", "",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, null, null) == JOptionPane.OK_OPTION) {
            dispose();
        } else if (!dontClose) {
            dispose();
        }

        Settings.write();
    }

    private void crawl() {
        dontClose = true;

        statusLabel.setText("Crawling your net...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        final CrawlSettings crawlSettings = CrawlDialog.showCrawlDialog(this);
        if (crawlSettings != null) {
            // TODO: hotfix
            new Thread() {
                public void run() {
                    ConnectionGateway gateway = new LocalSSHGateway();

                    SSHSettings settings = new SSHSettings();
                    settings.setVersion(SSHVersion.VERSION2);
                    settings.setUsername(crawlSettings.getDefaultUsername());
                    settings.setPassword(crawlSettings.getDefaultPassword());

                    DeviceManagerFactory managerFactory = new CommandLineDeviceManagerFactory();

                    IPv4Address start = crawlSettings.getAddress();

                    SimpleNetworkCrawler crawler = new SimpleNetworkCrawler(
                            gateway, settings, managerFactory, start);

                    try {
                        topology = new HashTopology();
                        viewer.setModel(topology);
                        table.setTopology(topology);
                        crawler.crawl(topology);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    finishCrawl();
                }
            }.start();
        } else {
            finishCrawl();
        }
    }

    private void finishCrawl() {
        saveItem.setEnabled(true);

        setCursor(Cursor.getDefaultCursor());
        statusLabel.setText("Crawl completed.");

        dontClose = false;
    }

    private void toggleView() {
        int newView = 0;
        if (tableVisible) {
            scrollPane.setViewportView(viewer);
        } else {
            scrollPane.setViewportView(table);

            newView = 1;
        }

        Settings.setLastView(newView);

        tableVisible = !tableVisible;
    }

}
