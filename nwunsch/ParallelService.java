package nwunsch;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParallelService {

	private final Integer numThreads;

	private final Data data;

	private List<NeedlemanWunschThread> pool = new ArrayList();

	public ParallelService(Integer numThreads, Data data) {

		this.numThreads = numThreads;
		this.data = data;
	}

	public int runParallel() {
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

//		for (int i = 1; i <= numThreads; i++) {
//			pool.add(new NeedlemanWunschThread(String.valueOf(i), data));
//		}
//		List<List<Integer>> listOfIndexes = new ArrayList<>();
//		for (int i = 0; i<numThreads; i++){
//			List<Integer> indexes = new ArrayList<>();
//			for (int j=i+1; j< data.solution.length; j+=numThreads){
//				indexes.add(j);
//			}
//			listOfIndexes.add(indexes);
//		}
//		for (int i = 0; i < pool.size(); i++) {
//			pool.get(i).setI(listOfIndexes.get(i));
//		}
		long currentTimeStart = System.nanoTime();
//		for (NeedlemanWunschThread td : pool) {
//			td.start();
//		}

//		Cell[][] cells = Arrays.stream(data.solution).map(Integer[]::clone).toArray(Cell[][]::new);
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
		Integer[][] teste ;

		synchronized (data.solution[data.solution.length - 1][data.solution[0].length - 1]) {

			if (data.solution[data.solution.length - 1][data.solution[0].length - 1] == null) {
				System.out.println("processando...");
				try {
					data.solution[data.solution.length - 1][data.solution[0].length - 1].wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		long currentTimeEnd = System.nanoTime();
		BigDecimal finalTime = BigDecimal.valueOf((currentTimeEnd - currentTimeStart) / 1000000);
		System.out.println("tempo de execução (ms): " + finalTime);
		return data.solution[data.solution.length - 1][data.solution[0].length - 1];
	}

}