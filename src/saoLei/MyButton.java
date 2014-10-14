package saoLei;

import javax.swing.JButton;

public class MyButton extends JButton {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	// x����
	private int xx;
	// y����
	private int yy;
	// ��Χ����
	private int mineNum;
	// �Ƿ�����
	private boolean isMine;
	// �Ƿ��ڿ�
	private boolean isClicked;
	// �Ƿ񱻼�����Ϊ�����ڿ���
	private boolean tempCheck;



	/**
	 * @return the xx
	 */
	public int getXx() {
		return xx;
	}



	/**
	 * @param xx the xx to set
	 */
	public void setXx(int xx) {
		this.xx = xx;
	}



	/**
	 * @return the yy
	 */
	public int getYy() {
		return yy;
	}



	/**
	 * @param yy the yy to set
	 */
	public void setYy(int yy) {
		this.yy = yy;
	}



	/**
	 * @return the mineNum
	 */
	public int getMineNum() {
		return mineNum;
	}



	/**
	 * @param mineNum the mineNum to set
	 */
	public void setMineNum(int mineNum) {
		this.mineNum = mineNum;
	}



	/**
	 * @return the isMine
	 */
	public boolean isMine() {
		return isMine;
	}



	/**
	 * @param isMine the isMine to set
	 */
	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}



	/**
	 * @return the isClicked
	 */
	public boolean isClicked() {
		return isClicked;
	}



	/**
	 * @param isClicked the isClicked to set
	 */
	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}



	/**
	 * @return the tempCheck
	 */
	public boolean isTempCheck() {
		return tempCheck;
	}



	/**
	 * @param tempCheck the tempCheck to set
	 */
	public void setTempCheck(boolean tempCheck) {
		this.tempCheck = tempCheck;
	}



	public MyButton(int xx, int yy) {
		super();
		this.xx = xx;
		this.yy = yy;
		mineNum = 0;
		isMine = false;
		isClicked = false;
		tempCheck = false;
	}



}
