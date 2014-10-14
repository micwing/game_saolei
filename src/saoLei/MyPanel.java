package saoLei;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MyPanel extends JPanel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -561046014072882662L;
	private int row;// 行
	private int col;// 列

	MyButton[][] buttons = null;

	ImageIcon iiMine = null;
	ImageIcon iiMineError = null;
	ImageIcon iiMineBreak = null;
	ImageIcon iiflag = null;
	ImageIcon iiFaceReady = null;
	ImageIcon iiFaceFail = null;
	ImageIcon iiFaceSucc = null;

	JPanel panel = null;
	JButton faceButton = null;
	JLabel mineLabel = null;
	JLabel timeLabel = null;
	Thread tt = null;// 线程
	boolean threadRunFlag = false;//线程运行标记

	/**
	 * 构造
	 * @param xx
	 * @param yy
	 * @param mineNum
	 * @param face 传递控制的笑脸JButton
	 * @param mineLabel 传递可控制的雷数JLabel
	 * @param timeLabel 传递可控制的时间JLabel
	 */
	public MyPanel(int xx, int yy, int mineNum, JButton face, JLabel mineLabel,
			JLabel timeLabel) {
		this.row = xx;
		this.col = yy;
		this.faceButton = face;
		this.mineLabel = mineLabel;
		this.timeLabel = timeLabel;

		buttons = new MyButton[row][col];
		panel = new JPanel();
		
		iiMine = new ImageIcon("img/mine.png");
		iiMineError = new ImageIcon("img/mine_fail.png");
		iiMineBreak = new ImageIcon("img/mine_break.png");
		iiflag = new ImageIcon("img/flag.png");
		iiFaceReady = new ImageIcon("img/face_ready.png");
		iiFaceFail = new ImageIcon("img/face_fail.png");
		iiFaceSucc = new ImageIcon("img/face_succ.png");

		panel.setLayout(new GridLayout(row, col));
		this.setLayout(new BorderLayout());
		this.add(new JLabel("  "), BorderLayout.WEST);// 挤挤更好看
		this.add(new JLabel("  "), BorderLayout.EAST);// 挤挤更好看
		this.add(panel, BorderLayout.CENTER);

		init();
		initData(mineNum);
	}

	/**
	 * 初始化核心界面
	 */
	public void init() {
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				buttons[i][j] = new MyButton(i, j);
				buttons[i][j].setBackground(Color.LIGHT_GRAY);
				buttons[i][j].setMargin(new Insets(0, 0, 0, 0));
				panel.add(buttons[i][j]);

				buttons[i][j].addMouseListener(new MouseAdapter() {
					@SuppressWarnings("static-access")
					public void mousePressed(MouseEvent e) {
						MyButton mb = (MyButton) e.getSource();
						if (e.getModifiersEx() == (e.BUTTON3_DOWN_MASK + e.BUTTON1_DOWN_MASK)) {// 左右同时按
							if (!mb.isClicked()) {
								return;
							}
							openEightButtonAround(mb);
							isSuccess();// 判断胜利
							return;
						}
						if (e.getButton() == e.BUTTON1) {// 左击
							if (mb.isClicked() || mb.getIcon() == iiflag) {
								return;
							}
							if (mb.isMine()) {
								List<MyButton> mbs = new ArrayList<MyButton>();
								mbs.add(mb);
								fail(mbs);
							} else {
								openNotMineButton(mb);
							}
							isSuccess();// 判断胜利
							return;
						}
						if (e.getButton() == e.BUTTON3) {// 右击
							if (mb.isClicked()) {
								return;
							}
							setFlagThisMine(mb);
							isSuccess();// 判断胜利
							return;
						}
					}
				});
			}
		}
	}

	/**
	 * 初始化地雷数据
	 * 
	 * @param xx
	 * @param yy
	 * @param mineNum
	 */
	public void initData(int mineNum) {
		// 随机布雷
		Random r = new Random();
		int rcount = 1;
		while (rcount <= mineNum) {
			int x = r.nextInt(row);
			int y = r.nextInt(col);
			if (!buttons[x][y].isMine()) {
				buttons[x][y].setMine(true);
				rcount++;
			}
		}
		// 为每个位置查雷
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				buttons[i][j].setMineNum(countMineForButton(buttons[i][j]));
			}
		}
		faceButton.setIcon(iiFaceReady);

		class TimeThread extends Thread {// 时间线程
			@Override
			public void run() {
				try {
					while (true) {
						sleep(1000);
						if (threadRunFlag) {
							timeLabel.setText((Integer.parseInt(timeLabel
									.getText()) + 1)
									+ "");
						} else {
							break;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		threadRunFlag = true;
		tt = new TimeThread();
		tt.start();
	}

	/**
	 * 鼠标左右同击时调用 挖开某一个button的周围所有button
	 * 
	 * @param mb
	 */
	public void openEightButtonAround(MyButton mb) {
		int rcount = 0;
		List<int[]> list = getButtonAroundUtil(mb);
		// 遍历全部该按钮周围的8个按钮
		for (int[] array : list) {
			// 如果坐标合法
			if (array[0] > -1 && array[0] < row && array[1] > -1
					&& array[1] < col) {
				if (buttons[array[0]][array[1]].getIcon() == iiflag) {
					rcount++;
				}
			}
		}
		if (mb.getMineNum() == rcount) {
			List<MyButton> mbs = new ArrayList<MyButton>();
			for (int[] array : list) {
				// 如果坐标合法
				if (array[0] > -1 && array[0] < row && array[1] > -1
						&& array[1] < col) {
					if (buttons[array[0]][array[1]].isClicked()) {
						continue;
					}
					if (buttons[array[0]][array[1]].getIcon() == iiflag) {
						continue;
					}
					if (buttons[array[0]][array[1]].isMine()) {
						mbs.add(buttons[array[0]][array[1]]);
						continue;
					}
					if (!buttons[array[0]][array[1]].isMine()) {
						openNotMineButton(buttons[array[0]][array[1]]);
					}
				}
			}
			if (mbs.size() > 0) {
				fail(mbs);
			}
		}
	}

	/**
	 * 挖开一个空白button（指周围全部button中没有雷的button）周围所有的button 递归调用以实现连续挖开
	 * 
	 * @param mb
	 */
	public void openZeroButton(MyButton mb) {
		if (mb.getIcon() == iiflag) { // 如果已经被标记为有雷
			return;
		}
		mb.setClicked(true);
		mb.setBackground(Color.WHITE);// 挖开

		List<int[]> list = getButtonAroundUtil(mb);// 获得该button周围全部坐标
		for (int[] array : list) {
			if (array[0] > -1 && array[0] < row && array[1] > -1
					&& array[1] < col) {// 如果坐标合法
				if (buttons[array[0]][array[1]].getIcon() == iiflag) {// 如果该处已经标记有雷
					continue;
				}
				if (buttons[array[0]][array[1]].getMineNum() == 0) {// 如果该位置button是0雷button
					if (!buttons[array[0]][array[1]].isClicked()) {// 并且没有被标记打开过
						openZeroButton(buttons[array[0]][array[1]]);
					}
				} else {
					openNotZeroButton(buttons[array[0]][array[1]]);// 自动挖开该处
				}
			}
		}
	}

	/**
	 * 挖开一个非雷button
	 * 
	 * @param mb
	 */
	public void openNotMineButton(MyButton mb) {
		if (mb.getMineNum() == 0) {
			openZeroButton(mb);
		} else {
			openNotZeroButton(mb);
		}
	}

	/**
	 * 挖开一个非零button
	 * 
	 * @param mb
	 */
	public void openNotZeroButton(MyButton mb) {
		mb.setClicked(true);
		mb.setBackground(Color.WHITE);
		mb.setText(mb.getMineNum() + "");
	}

	/**
	 * 为一个未打开的button添加或者移除小旗
	 * 
	 * @param mb
	 */
	public void setFlagThisMine(MyButton mb) {
		if (mb.getIcon() == iiflag) {
			mb.setIcon(null);
			mineLabel.setText((Integer.parseInt(mineLabel.getText()) + 1) + "");
		} else {
			mb.setIcon(iiflag);
			mineLabel.setText((Integer.parseInt(mineLabel.getText()) - 1) + "");
		}
	}

	/**
	 * 判断胜利
	 */
	public void isSuccess() {
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				if (buttons[i][j].isMine() && buttons[i][j].getIcon() != iiflag) {// 如果所有雷都被标记
					return;
				}
				if (!buttons[i][j].isMine() && !buttons[i][j].isClicked()) {// 如果所有按钮都被挖开
					return;
				}
			}
		}
		success();
	}
	
	/**
	 * 胜利
	 */
	public void success() {
		this.setEnabled(false);// 禁用panel
		threadRunFlag = false;// 时间停
		faceButton.setIcon(iiFaceSucc);// 笑脸
		
		String name = null;
		if ((row == 9 && col == 9) || (row == 16 && col == 16) || (row == 16 && col == 30)) {
			List<String> list = getFileStringUtil();
			if (row == 9 && col == 9 && Integer.parseInt(timeLabel.getText()) <= Integer.parseInt(list.get(1).toString())) {
				name = JOptionPane.showInputDialog(this, "恭喜您登上扫雷英雄榜！请问尊姓大名：","胜利", JOptionPane.QUESTION_MESSAGE);
				if (name == null || name.length() == 0) {
					name = "匿名";
				}
				list.remove(0);
				list.add(0, name);
				list.remove(1);
				list.add(1, timeLabel.getText());
				
				writeFileUtil(list);
				showHero();
			}
			if (row == 16 && col == 16 && Integer.parseInt(timeLabel.getText()) <= Integer.parseInt(list.get(3).toString())) {
				name = JOptionPane.showInputDialog(this, "恭喜您登上扫雷英雄榜！请问尊姓大名：","胜利", JOptionPane.QUESTION_MESSAGE);
				if (name == null || name.length() == 0) {
					name = "匿名";
				}
				list.remove(2);
				list.add(2, name);
				list.remove(3);
				list.add(3, timeLabel.getText());
				
				writeFileUtil(list);
				showHero();
			}
			if (row == 16 && col == 30 && Integer.parseInt(timeLabel.getText()) <= Integer.parseInt(list.get(5).toString())) {
				name = JOptionPane.showInputDialog(this, "恭喜您登上扫雷英雄榜！请问尊姓大名：","胜利", JOptionPane.QUESTION_MESSAGE);
				if (name == null || name.length() == 0) {
					name = "匿名";
				}
				list.remove(4);
				list.add(4, name);
				list.remove(5);
				list.add(5, timeLabel.getText());
				
				writeFileUtil(list);
				showHero();
			}
		}
	}
	
	/**
	 * 显示英雄榜
	 */
	public void showHero() {
		List<String> lst = getFileStringUtil();
		StringBuffer sb = new StringBuffer();
		sb.append("初级：  " + lst.get(1) + "秒          " + lst.get(0) + "\n");
		sb.append("中级：  " + lst.get(3) + "秒          " + lst.get(2) + "\n");
		sb.append("高级：  " +lst.get(5) + "秒          " + lst.get(4) + "\n");
		JOptionPane.showMessageDialog(this, sb.toString(), "扫雷英雄榜", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * 失败（不包含判断）
	 */
	public void fail(List<MyButton> mbs) {
		this.setEnabled(false);// 禁用panel
		threadRunFlag = false;// 时间停
		faceButton.setIcon(iiFaceFail);// 哭脸
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				buttons[i][j].setClicked(true);
				for (MyButton mb : mbs) {// 显示所有踩错的雷
					if (buttons[i][j] == mb) {
						buttons[i][j].setIcon(iiMineBreak);
						buttons[i][j].setBackground(Color.RED);
					}
				}
				if (buttons[i][j].getIcon() == iiMineBreak) {// 如果已经显示为失败的雷
					continue;
				}
				if (!buttons[i][j].isMine()
						&& buttons[i][j].getIcon() == iiflag) {// 如果是已经标记正确的雷
					buttons[i][j].setIcon(iiMineError);
					continue;
				}
				if (buttons[i][j].isMine()) {// 如果是没有标记的普通雷
					buttons[i][j].setIcon(iiMine);
				}
			}
		}
	}

	/**
	 * 这是一个工具 调用该方法可以获得某按钮周围全部坐标
	 * 
	 * @param mb
	 * @return 坐标集合
	 */
	private List<int[]> getButtonAroundUtil(MyButton mb) {
		int xx = mb.getXx();
		int yy = mb.getYy();
		// 定义按钮周围的8个按钮坐标
		int[] array1 = { xx - 1, yy - 1 };
		int[] array2 = { xx - 1, yy };
		int[] array3 = { xx - 1, yy + 1 };
		int[] array4 = { xx, yy - 1 };
		int[] array5 = { xx, yy + 1 };
		int[] array6 = { xx + 1, yy - 1 };
		int[] array7 = { xx + 1, yy };
		int[] array8 = { xx + 1, yy + 1 };
		// 把8个坐标封装进List
		List<int[]> list = new ArrayList<int[]>();
		list.add(array1);
		list.add(array2);
		list.add(array3);
		list.add(array4);
		list.add(array5);
		list.add(array6);
		list.add(array7);
		list.add(array8);
		return list;
	}

	/**
	 * 这是一个工具 计算某个button周围有多少雷 初始化时调用
	 * 
	 * @param button
	 * @return
	 */
	public int countMineForButton(MyButton button) {
		List<int[]> list = getButtonAroundUtil(button);

		int rcount = 0;
		// 遍历全部坐标
		for (int[] array : list) {
			// 如果是合法坐标
			if (array[0] > -1 && array[0] < row && array[1] > -1
					&& array[1] < col) {
				// 如果是雷
				if (buttons[array[0]][array[1]].isMine()) {
					rcount++;
				}
			}
		}
		return rcount;
	}
	
	/**
	 * 通过一个File文件得到文件内容的工具
	 * 
	 * @param file
	 *            要读取的文件
	 * @return 一个String的List，每个List中的元素都保存了文件的每一行的内容
	 */
	public synchronized List<String> getFileStringUtil() {
		File file = new File("data/mine.data");
		List<String> list = new ArrayList<String>();
		BufferedReader bf = null;
		try {
			String s = null;
			bf = new BufferedReader(new FileReader(file));
			while ((s = bf.readLine()) != null) {
				list.add(s);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(bf!=null)try{bf.close();}catch(IOException e){}
		}
		return list;
	}
	
	/**
	 * 把给定的List中的String写到file中，每个一行
	 * @param file
	 * @param list
	 * @return
	 */
	public synchronized void writeFileUtil(List<String> list) {
		File file = new File("data/mine.data");
		FileOutputStream fos=null;
		OutputStreamWriter osw=null;
		try {
			fos=new FileOutputStream(file);
			osw=new OutputStreamWriter(fos);
			for(int i=0;i<list.size();i++){
				osw.write(list.get(i).toString() + "\n");
			}
			osw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(osw!=null)try{osw.close();}catch(IOException e){}
			if(fos!=null)try{fos.close();}catch(IOException e){}
		}
	}
}
