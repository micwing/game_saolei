package saoLei;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class SaoLei extends JFrame implements ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1136874429954282415L;

	private int row;// ��
	private int col;// ��
	private int LandmineNum;// �׸���

	private JPanel jpNorth = null;
	private MyPanel jpCenter = null;

	private JLabel mineLabel = null;// ��ʾ������JLabel
	private JLabel timeLabel = null;

	private JButton jb1 = null;// Ц��

	/**
	 * ����
	 */
	public SaoLei() {
		initMenu();
		init();
		restart();
	}

	/**
	 * ���ɲ˵���
	 */
	public void initMenu() {
		
		JMenuBar bar=new JMenuBar();
		JMenu game=new JMenu("��Ϸ");
		JMenu help=new JMenu("����");
		JMenuItem item;
		game.add(item=new JMenuItem("����"));item.addActionListener(this);
		game.addSeparator();
		ButtonGroup bg=new ButtonGroup();
		game.add(item=new JCheckBoxMenuItem("����",true));bg.add(item);item.addActionListener(this);
		game.add(item=new JCheckBoxMenuItem("�м�"));bg.add(item);item.addActionListener(this);
		game.add(item=new JCheckBoxMenuItem("�߼�"));bg.add(item);item.addActionListener(this);
		game.add(item=new JCheckBoxMenuItem("�Զ���..."));bg.add(item);item.addActionListener(this);
		game.addSeparator();
		game.add(item=new JMenuItem("ɨ��Ӣ�۰�..."));item.addActionListener(this);
		game.add(item=new JMenuItem("����Ӣ�۰�"));item.addActionListener(this);
		game.addSeparator();
		game.add(item=new JMenuItem("�˳�"));item.addActionListener(this);
	
		help.add(item=new JMenuItem("����ɨ��"));item.addActionListener(this);
		
		bar.add(game);
		bar.add(help);
		
		this.setJMenuBar(bar);
	}

	/**
	 * ��ʼ������
	 */
	public void init() {
		row = 9;
		col = 9;
		LandmineNum = 10;

		jb1 = new JButton();
		jb1.setMargin(new Insets(0, 0, 0, 0));

		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restart();
			}
		});

		mineLabel = new JLabel();
		timeLabel = new JLabel();
		jpNorth = new JPanel();
		JPanel jpNorth1 = new JPanel();
		JPanel jpNorth2 = new JPanel();
		JPanel jpNorth3 = new JPanel();
		
		jpNorth1.add(new JLabel("ʣ"));
		jpNorth1.add(mineLabel);
		jpNorth1.add(new JLabel("����"));
		jpNorth2.add(jb1);
		jpNorth3.add(new JLabel("ʱ��"));
		jpNorth3.add(timeLabel);

		jpNorth.setLayout(new GridLayout());
		jpNorth.add(jpNorth1);
		jpNorth.add(jpNorth2);
		jpNorth.add(jpNorth3);
		BorderLayout bl1 = new BorderLayout();
		this.setLayout(bl1);
		this.add(jpNorth, BorderLayout.NORTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("ɨ��");
	}

	/**
	 * ����Ϸ/���¿�ʼ
	 */
	public void restart() {
		mineLabel.setText(LandmineNum + "");
		timeLabel.setText("0");
		if (jpCenter != null) {
			jpCenter.threadRunFlag = false;
			this.remove(jpCenter);
		}

		jpCenter = new MyPanel(row, col, LandmineNum, jb1, mineLabel, timeLabel);
		this.add(jpCenter, BorderLayout.CENTER);

		this.setVisible(true);
		setFrameSize();

		this.setResizable(false);
		initLocation();
	}

	/**
	 * ��ʼ������λ��
	 */
	public void initLocation() {
		int windowWidth = this.getWidth(); // ��ô��ڿ�
		int windowHeight = this.getHeight(); // ��ô��ڸ�
		Toolkit kit = Toolkit.getDefaultToolkit(); // ���幤�߰�
		Dimension screenSize = kit.getScreenSize(); // ��ȡ��Ļ�ĳߴ�
		int screenWidth = screenSize.width; // ��ȡ��Ļ�Ŀ�
		int screenHeight = screenSize.height; // ��ȡ��Ļ�ĸ�
		this.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2
				- windowHeight / 2);// ���ô��ھ�����ʾ
	}

	public void actionPerformed(ActionEvent e) {
		if ("����".equals(e.getActionCommand())) {
			restart();
			return;
		}
		if ("����".equals(e.getActionCommand())) {
			row = 9;
			col = 9;
			LandmineNum = 10;
			restart();
			return;
		}
		if ("�м�".equals(e.getActionCommand())) {
			row = 16;
			col = 16;
			LandmineNum = 40;
			restart();
			return;
		}
		if ("�߼�".equals(e.getActionCommand())) {
			row = 16;
			col = 30;
			LandmineNum = 99;
			restart();
			return;
		}
		if ("�Զ���...".equals(e.getActionCommand())) {

			String rowInput = JOptionPane.showInputDialog(this, "��Ҫ�趨�����У�","�Զ�����", JOptionPane.QUESTION_MESSAGE);
			String colInput = JOptionPane.showInputDialog(this, "��Ҫ�趨�����У�","�Զ�����", JOptionPane.QUESTION_MESSAGE);
			String mineInput = JOptionPane.showInputDialog(this, "��Ҫ�趨�����ף�","�Զ���", JOptionPane.QUESTION_MESSAGE);
			int rowNum = 0;
			int colNum = 0;
			int mineNum = 0;
			try {
				rowNum = Integer.parseInt(rowInput);
				colNum = Integer.parseInt(colInput);
				mineNum = Integer.parseInt(mineInput);
			} catch (NumberFormatException e1) {
				restart();
				return;
			}
			if (rowNum > 31 || colNum > 31 || rowNum < 5 || colNum < 5 || rowNum * colNum <= mineNum) {
				restart();
				return;
			}
			row = rowNum;
			col = colNum;
			LandmineNum = mineNum;
			restart();
			return;
		}
		if ("ɨ��Ӣ�۰�...".equals(e.getActionCommand())) {
			jpCenter.showHero();
			return;
		}
		if ("����Ӣ�۰�".equals(e.getActionCommand())) {
			int choice=JOptionPane.showConfirmDialog(this,"��ȷ��Ҫ����Ӣ�۰���","����Ӣ�۰�",
					JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.OK_OPTION) {
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < 3; i++){
					list.add("����");
					list.add("999");
				}
				jpCenter.writeFileUtil(list);
			}
			return;
		}
		if ("�˳�".equals(e.getActionCommand())) {
			System.exit(0);
			return;
		}
		if ("����ɨ��".equals(e.getActionCommand())) {
			JOptionPane.showMessageDialog(this, "���ƣ�ɨ��\n�汾��1.1\n���ߣ�Mic.Wing\nʱ�䣺2010.07.26", "����ɨ��", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
	}

	/**
	 * ���ý����С
	 */
	private void setFrameSize() {
		this.setSize(col * 27, row * 30 + 10);
	}

	/**
	 * �������
	 * @param args
	 */
	public static void main(String[] args) {
		new SaoLei();
	}
}
