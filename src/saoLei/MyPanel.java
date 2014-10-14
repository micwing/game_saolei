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
	private int row;// ��
	private int col;// ��

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
	Thread tt = null;// �߳�
	boolean threadRunFlag = false;//�߳����б��

	/**
	 * ����
	 * @param xx
	 * @param yy
	 * @param mineNum
	 * @param face ���ݿ��Ƶ�Ц��JButton
	 * @param mineLabel ���ݿɿ��Ƶ�����JLabel
	 * @param timeLabel ���ݿɿ��Ƶ�ʱ��JLabel
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
		this.add(new JLabel("  "), BorderLayout.WEST);// �������ÿ�
		this.add(new JLabel("  "), BorderLayout.EAST);// �������ÿ�
		this.add(panel, BorderLayout.CENTER);

		init();
		initData(mineNum);
	}

	/**
	 * ��ʼ�����Ľ���
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
						if (e.getModifiersEx() == (e.BUTTON3_DOWN_MASK + e.BUTTON1_DOWN_MASK)) {// ����ͬʱ��
							if (!mb.isClicked()) {
								return;
							}
							openEightButtonAround(mb);
							isSuccess();// �ж�ʤ��
							return;
						}
						if (e.getButton() == e.BUTTON1) {// ���
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
							isSuccess();// �ж�ʤ��
							return;
						}
						if (e.getButton() == e.BUTTON3) {// �һ�
							if (mb.isClicked()) {
								return;
							}
							setFlagThisMine(mb);
							isSuccess();// �ж�ʤ��
							return;
						}
					}
				});
			}
		}
	}

	/**
	 * ��ʼ����������
	 * 
	 * @param xx
	 * @param yy
	 * @param mineNum
	 */
	public void initData(int mineNum) {
		// �������
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
		// Ϊÿ��λ�ò���
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				buttons[i][j].setMineNum(countMineForButton(buttons[i][j]));
			}
		}
		faceButton.setIcon(iiFaceReady);

		class TimeThread extends Thread {// ʱ���߳�
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
	 * �������ͬ��ʱ���� �ڿ�ĳһ��button����Χ����button
	 * 
	 * @param mb
	 */
	public void openEightButtonAround(MyButton mb) {
		int rcount = 0;
		List<int[]> list = getButtonAroundUtil(mb);
		// ����ȫ���ð�ť��Χ��8����ť
		for (int[] array : list) {
			// �������Ϸ�
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
				// �������Ϸ�
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
	 * �ڿ�һ���հ�button��ָ��Χȫ��button��û���׵�button����Χ���е�button �ݹ������ʵ�������ڿ�
	 * 
	 * @param mb
	 */
	public void openZeroButton(MyButton mb) {
		if (mb.getIcon() == iiflag) { // ����Ѿ������Ϊ����
			return;
		}
		mb.setClicked(true);
		mb.setBackground(Color.WHITE);// �ڿ�

		List<int[]> list = getButtonAroundUtil(mb);// ��ø�button��Χȫ������
		for (int[] array : list) {
			if (array[0] > -1 && array[0] < row && array[1] > -1
					&& array[1] < col) {// �������Ϸ�
				if (buttons[array[0]][array[1]].getIcon() == iiflag) {// ����ô��Ѿ��������
					continue;
				}
				if (buttons[array[0]][array[1]].getMineNum() == 0) {// �����λ��button��0��button
					if (!buttons[array[0]][array[1]].isClicked()) {// ����û�б���Ǵ򿪹�
						openZeroButton(buttons[array[0]][array[1]]);
					}
				} else {
					openNotZeroButton(buttons[array[0]][array[1]]);// �Զ��ڿ��ô�
				}
			}
		}
	}

	/**
	 * �ڿ�һ������button
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
	 * �ڿ�һ������button
	 * 
	 * @param mb
	 */
	public void openNotZeroButton(MyButton mb) {
		mb.setClicked(true);
		mb.setBackground(Color.WHITE);
		mb.setText(mb.getMineNum() + "");
	}

	/**
	 * Ϊһ��δ�򿪵�button��ӻ����Ƴ�С��
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
	 * �ж�ʤ��
	 */
	public void isSuccess() {
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				if (buttons[i][j].isMine() && buttons[i][j].getIcon() != iiflag) {// ��������׶������
					return;
				}
				if (!buttons[i][j].isMine() && !buttons[i][j].isClicked()) {// ������а�ť�����ڿ�
					return;
				}
			}
		}
		success();
	}
	
	/**
	 * ʤ��
	 */
	public void success() {
		this.setEnabled(false);// ����panel
		threadRunFlag = false;// ʱ��ͣ
		faceButton.setIcon(iiFaceSucc);// Ц��
		
		String name = null;
		if ((row == 9 && col == 9) || (row == 16 && col == 16) || (row == 16 && col == 30)) {
			List<String> list = getFileStringUtil();
			if (row == 9 && col == 9 && Integer.parseInt(timeLabel.getText()) <= Integer.parseInt(list.get(1).toString())) {
				name = JOptionPane.showInputDialog(this, "��ϲ������ɨ��Ӣ�۰��������մ�����","ʤ��", JOptionPane.QUESTION_MESSAGE);
				if (name == null || name.length() == 0) {
					name = "����";
				}
				list.remove(0);
				list.add(0, name);
				list.remove(1);
				list.add(1, timeLabel.getText());
				
				writeFileUtil(list);
				showHero();
			}
			if (row == 16 && col == 16 && Integer.parseInt(timeLabel.getText()) <= Integer.parseInt(list.get(3).toString())) {
				name = JOptionPane.showInputDialog(this, "��ϲ������ɨ��Ӣ�۰��������մ�����","ʤ��", JOptionPane.QUESTION_MESSAGE);
				if (name == null || name.length() == 0) {
					name = "����";
				}
				list.remove(2);
				list.add(2, name);
				list.remove(3);
				list.add(3, timeLabel.getText());
				
				writeFileUtil(list);
				showHero();
			}
			if (row == 16 && col == 30 && Integer.parseInt(timeLabel.getText()) <= Integer.parseInt(list.get(5).toString())) {
				name = JOptionPane.showInputDialog(this, "��ϲ������ɨ��Ӣ�۰��������մ�����","ʤ��", JOptionPane.QUESTION_MESSAGE);
				if (name == null || name.length() == 0) {
					name = "����";
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
	 * ��ʾӢ�۰�
	 */
	public void showHero() {
		List<String> lst = getFileStringUtil();
		StringBuffer sb = new StringBuffer();
		sb.append("������  " + lst.get(1) + "��          " + lst.get(0) + "\n");
		sb.append("�м���  " + lst.get(3) + "��          " + lst.get(2) + "\n");
		sb.append("�߼���  " +lst.get(5) + "��          " + lst.get(4) + "\n");
		JOptionPane.showMessageDialog(this, sb.toString(), "ɨ��Ӣ�۰�", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * ʧ�ܣ��������жϣ�
	 */
	public void fail(List<MyButton> mbs) {
		this.setEnabled(false);// ����panel
		threadRunFlag = false;// ʱ��ͣ
		faceButton.setIcon(iiFaceFail);// ����
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				buttons[i][j].setClicked(true);
				for (MyButton mb : mbs) {// ��ʾ���вȴ����
					if (buttons[i][j] == mb) {
						buttons[i][j].setIcon(iiMineBreak);
						buttons[i][j].setBackground(Color.RED);
					}
				}
				if (buttons[i][j].getIcon() == iiMineBreak) {// ����Ѿ���ʾΪʧ�ܵ���
					continue;
				}
				if (!buttons[i][j].isMine()
						&& buttons[i][j].getIcon() == iiflag) {// ������Ѿ������ȷ����
					buttons[i][j].setIcon(iiMineError);
					continue;
				}
				if (buttons[i][j].isMine()) {// �����û�б�ǵ���ͨ��
					buttons[i][j].setIcon(iiMine);
				}
			}
		}
	}

	/**
	 * ����һ������ ���ø÷������Ի��ĳ��ť��Χȫ������
	 * 
	 * @param mb
	 * @return ���꼯��
	 */
	private List<int[]> getButtonAroundUtil(MyButton mb) {
		int xx = mb.getXx();
		int yy = mb.getYy();
		// ���尴ť��Χ��8����ť����
		int[] array1 = { xx - 1, yy - 1 };
		int[] array2 = { xx - 1, yy };
		int[] array3 = { xx - 1, yy + 1 };
		int[] array4 = { xx, yy - 1 };
		int[] array5 = { xx, yy + 1 };
		int[] array6 = { xx + 1, yy - 1 };
		int[] array7 = { xx + 1, yy };
		int[] array8 = { xx + 1, yy + 1 };
		// ��8�������װ��List
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
	 * ����һ������ ����ĳ��button��Χ�ж����� ��ʼ��ʱ����
	 * 
	 * @param button
	 * @return
	 */
	public int countMineForButton(MyButton button) {
		List<int[]> list = getButtonAroundUtil(button);

		int rcount = 0;
		// ����ȫ������
		for (int[] array : list) {
			// ����ǺϷ�����
			if (array[0] > -1 && array[0] < row && array[1] > -1
					&& array[1] < col) {
				// �������
				if (buttons[array[0]][array[1]].isMine()) {
					rcount++;
				}
			}
		}
		return rcount;
	}
	
	/**
	 * ͨ��һ��File�ļ��õ��ļ����ݵĹ���
	 * 
	 * @param file
	 *            Ҫ��ȡ���ļ�
	 * @return һ��String��List��ÿ��List�е�Ԫ�ض��������ļ���ÿһ�е�����
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
	 * �Ѹ�����List�е�Stringд��file�У�ÿ��һ��
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
