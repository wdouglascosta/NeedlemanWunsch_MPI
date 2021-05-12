package nwunsch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParallelService {

	private final Integer numThreads;

	private final Data data;

	public ParallelService(Integer numThreads, Data data) {

		this.numThreads = numThreads;
		this.data = data;
	}

	public List<PData> buildDataSlices() {
		for (int i = 0; i < data.solution.length; i++) {
			for (int j = 0; j < data.solution.length; j++) {
				data.solution[i][j] = 0;
			}
		}

		data.solution[0][0] = 0;

		for (int i = 1; i < data.getFullSeq2().length() + 1; i++) {
			data.solution[0][i] = data.solution[0][i - 1] + data.getGAP();
		}

		for (int i = 1; i < data.getFullSeq1().length() + 1; i++) {
			data.solution[i][0] = data.solution[i - 1][0] + data.getGAP();
		}

		int tam = data.solution.length / numThreads;
		List<PData> pSolution = new ArrayList<>();

		for (int i = 0; i < numThreads; i ++){
			PData pData = new PData();
			Integer[][] subData = Arrays.copyOfRange(data.solution, i*tam, (i+1) *tam);
			pData.setData(subData);
			String subSeq = data.getFullSeq1().substring( i*tam, (i+1) *tam);
			pData.setFirstSeq(subSeq);
			pData.setSecondSeq(data.getFullSeq2());
			pSolution.add(pData);

		}

		return pSolution;
	}

}