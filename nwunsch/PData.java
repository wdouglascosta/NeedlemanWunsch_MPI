package nwunsch;

import java.util.concurrent.Semaphore;


public class PData {

	private Integer[][] data;
	private Integer[] buffer;
	String firstSeq;
	String secondSeq;

	public Integer[][] getData() {

		return data;
	}

	public void setData(Integer[][] data) {

		this.data = data;
	}

	public Integer[] getBuffer() {

		return buffer;
	}

	public void setBuffer(Integer[] buffer) {

		this.buffer = buffer;
	}

	public String getFirstSeq() {

		return firstSeq;
	}

	public void setFirstSeq(String firstSeq) {

		this.firstSeq = firstSeq;
	}

	public String getSecondSeq() {

		return secondSeq;
	}

	public void setSecondSeq(String secondSeq) {

		this.secondSeq = secondSeq;
	}
}
