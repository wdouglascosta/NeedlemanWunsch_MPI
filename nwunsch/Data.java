package nwunsch;

public class Data {

	private final String fullSeq1;

	private final String fullSeq2;

	private final int MATCH; // caracteres iguais

	private final int MISMATCH; // caracteres diferentes

	private final int GAP; // penalidade por lacuna

	public Integer[][] solution;

	public Data(String fullSeq1, String fullSeq2, int MATCH, int MISMATCH, int GAP) {

		this.fullSeq1 = fullSeq1;
		this.fullSeq2 = fullSeq2;
		this.MATCH = MATCH;
		this.MISMATCH = MISMATCH;
		this.GAP = GAP;
		this.solution = new Integer[fullSeq1.length() + 1][fullSeq2.length() + 1];

	}

	public String getFullSeq1() {

		return fullSeq1;
	}

	public String getFullSeq2() {

		return fullSeq2;
	}

	public int getMATCH() {

		return MATCH;
	}

	public int getMISMATCH() {

		return MISMATCH;
	}

	public int getGAP() {

		return GAP;
	}
}
