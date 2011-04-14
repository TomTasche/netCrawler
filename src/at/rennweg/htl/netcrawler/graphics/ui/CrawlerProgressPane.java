package at.rennweg.htl.netcrawler.graphics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import at.andiwand.library.util.JFrameUtil;


public class CrawlerProgressPane extends JDialog {
	
	private static final long serialVersionUID = -5443764649232145677L;
	
	public static final String DEFAULT_TITLE = "Crawling...";
	public static final String DEFAULT_PROGRESS_STRING = "Crawling";
	
	public static final int MAX_DOT_COUNT = 3;
	public static final int DOT_TIME = 1000;
	
	
	private final String progressString;
	private final JLabel progressLabel = new JLabel();
	private boolean lastCancel;
	
	public CrawlerProgressPane() {
		this(DEFAULT_TITLE, DEFAULT_PROGRESS_STRING);
	}
	public CrawlerProgressPane(String title, String progressString) {
		setTitle(title);
		
		this.progressString = progressString;
		
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		setLayout(groupLayout);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lastCancel = true;
				CrawlerProgressPane.this.setVisible(false);
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				lastCancel = true;
				CrawlerProgressPane.this.setVisible(false);
			}
		});
		
		ParallelGroup horizontalGroup = groupLayout.createParallelGroup()
				.addComponent(progressLabel, Alignment.LEADING)
				.addComponent(cancel, Alignment.CENTER)
				.addGap(0, 0, Short.MAX_VALUE);
		
		SequentialGroup verticalGroup = groupLayout.createSequentialGroup()
				.addComponent(progressLabel)
				.addGap(10, 10, Short.MAX_VALUE)
				.addComponent(cancel);
		
		groupLayout.setHorizontalGroup(horizontalGroup);
		groupLayout.setVerticalGroup(verticalGroup);
		
		
		setModal(true);
		setSize(200, 120);
	}
	
	
	public boolean showProgress(JFrame parent) {
		if (parent == null) {
			JFrameUtil.centerFrame(this);
		} else {
			JFrameUtil.centerFrame(this, parent);
		}
		
		lastCancel = false;
		
		Thread dotThread = new Thread() {
			public void run() {
				int dots = 0;
				String text = progressString;
				
				try {
					while (true) {
						if ((dots % (MAX_DOT_COUNT + 1)) == 0) text = progressString;
						else text += ".";
						
						progressLabel.setText(text);
						
						dots++;
						Thread.sleep(DOT_TIME);
					}
				} catch (InterruptedException e) {}
			}
		};
		dotThread.start();
		
		setVisible(true);
		
		dotThread.interrupt();
		try {
			dotThread.join();
		} catch (InterruptedException e) {}
		
		return lastCancel;
	}
	
}