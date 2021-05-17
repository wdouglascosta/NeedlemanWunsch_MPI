package nwunsch;

public class PData {

	public Integer[][] solution;
	private Integer[] buffer;
	String firstSeq;
	String secondSeq;

	public Integer[][] getData() {

		return solution;
	}

	public void setData(Integer[][] data) {

		this.solution = data;
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
