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

	private int row;// 行
	private int col;// 列
	private int LandmineNum;// 雷个数

	private JPanel jpNorth = null;
	private MyPanel jpCenter = null;

	private JLabel mineLabel = null;// 显示雷数的JLabel
	private JLabel timeLabel = null;

	private JButton jb1 = null;// 笑脸

	/**
	 * 构造
	 */
	public SaoLei() {
		initMenu();
		init();
		restart();
	}

	/**
	 * 生成菜单栏
	 */
	public void initMenu() {
		
		JMenuBar bar=new JMenuBar();
		JMenu game=new JMenu("游戏");
		JMenu help=new JMenu("帮助");
		JMenuItem item;
		game.add(item=new JMenuItem("开局"));item.addActionListener(this);
		game.addSeparator();
		ButtonGroup bg=new ButtonGroup();
		game.add(item=new JCheckBoxMenuItem("初级",true));bg.add(item);item.addActionListener(this);
		game.add(item=new JCheckBoxMenuItem("中级"));bg.add(item);item.addActionListener(this);
		game.add(item=new JCheckBoxMenuItem("高级"));bg.add(item);item.addActionListener(this);
		game.add(item=new JCheckBoxMenuItem("自定义..."));bg.add(item);item.addActionListener(this);
		game.addSeparator();
		game.add(item=new JMenuItem("扫雷英雄榜..."));item.addActionListener(this);
		game.add(item=new JMenuItem("重置英雄榜"));item.addActionListener(this);
		game.addSeparator();
		game.add(item=new JMenuItem("退出"));item.addActionListener(this);
	
		help.add(item=new JMenuItem("关于扫雷"));item.addActionListener(this);
		
		bar.add(game);
		bar.add(help);
		
		this.setJMenuBar(bar);
	}

	/**
	 * 初始化界面
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
		
		jpNorth1.add(new JLabel("剩"));
		jpNorth1.add(mineLabel);
		jpNorth1.add(new JLabel("个雷"));
		jpNorth2.add(jb1);
		jpNorth3.add(new JLabel("时间"));
		jpNorth3.add(timeLabel);

		jpNorth.setLayout(new GridLayout());
		jpNorth.add(jpNorth1);
		jpNorth.add(jpNorth2);
		jpNorth.add(jpNorth3);
		BorderLayout bl1 = new BorderLayout();
		this.setLayout(bl1);
		this.add(jpNorth, BorderLayout.NORTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("扫雷");
	}

	/**
	 * 新游戏/重新开始
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
	 * 初始化窗口位置
	 */
	public void initLocation() {
		int windowWidth = this.getWidth(); // 获得窗口宽
		int windowHeight = this.getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		this.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2
				- windowHeight / 2);// 设置窗口居中显示
	}

	public void actionPerformed(ActionEvent e) {
		if ("开局".equals(e.getActionCommand())) {
			restart();
			return;
		}
		if ("初级".equals(e.getActionCommand())) {
			row = 9;
			col = 9;
			LandmineNum = 10;
			restart();
			return;
		}
		if ("中级".equals(e.getActionCommand())) {
			row = 16;
			col = 16;
			LandmineNum = 40;
			restart();
			return;
		}
		if ("高级".equals(e.getActionCommand())) {
			row = 16;
			col = 30;
			LandmineNum = 99;
			restart();
			return;
		}
		if ("自定义...".equals(e.getActionCommand())) {

			String rowInput = JOptionPane.showInputDialog(this, "您要设定多少行？","自定义行", JOptionPane.QUESTION_MESSAGE);
			String colInput = JOptionPane.showInputDialog(this, "您要设定多少列？","自定义列", JOptionPane.QUESTION_MESSAGE);
			String mineInput = JOptionPane.showInputDialog(this, "您要设定多少雷？","自定义", JOptionPane.QUESTION_MESSAGE);
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
		if ("扫雷英雄榜...".equals(e.getActionCommand())) {
			jpCenter.showHero();
			return;
		}
		if ("重置英雄榜".equals(e.getActionCommand())) {
			int choice=JOptionPane.showConfirmDialog(this,"您确定要重置英雄榜吗？","重置英雄榜",
					JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.OK_OPTION) {
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < 3; i++){
					list.add("匿名");
					list.add("999");
				}
				jpCenter.writeFileUtil(list);
			}
			return;
		}
		if ("退出".equals(e.getActionCommand())) {
			System.exit(0);
			return;
		}
		if ("关于扫雷".equals(e.getActionCommand())) {
			JOptionPane.showMessageDialog(this, "名称：扫雷\n版本：1.1\n作者：Mic.Wing\n时间：2010.07.26", "关于扫雷", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
	}

	/**
	 * 设置界面大小
	 */
	private void setFrameSize() {
		this.setSize(col * 27, row * 30 + 10);
	}

	/**
	 * 程序入口
	 * @param args
	 */
	public static void main(String[] args) {
		new SaoLei();
	}
}
