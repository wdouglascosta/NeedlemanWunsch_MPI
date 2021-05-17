package nwunsch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mpi.Intracomm;
import mpi.MPI;
import nwunsch.utils.FileLoader;

/**
 * @author mateus
 */
public class NeedlemanWunschMain {

    private final static int MATCH = 4; // caracteres iguais

    private final static int MISMATCH = -2; // caracteres diferentes

    private final static int GAP = -1; // penalidade por lacuna

    public static int[] sendBuffer;
    public static int[] receiveBuffer;


    public List<PData> initialize(String[] args) throws IOException {

        FileLoader fileLoader = new FileLoader();

        String firstSeq;
        String secondSeq;
        Integer limit = 1000;
        int numThreads = 4; //USAR O MPI SIZE AQUI


        if (limit != null && limit > 0) {
//            firstSeq = fileLoader.getSequence("Sequence A").substring(0, limit);
//            secondSeq = fileLoader.getSequence("Sequence B").substring(0, limit);
        } else {
            firstSeq = fileLoader.getSequence("Sequence A");
            secondSeq = fileLoader.getSequence("Sequence B");
        }

        firstSeq =  "ATTAAAGGTTTATACCTTCC";
        secondSeq = "ATCTCACTTCCCCTCGTTCT";
        //parallel

//        System.out.println("numThreads " + numThreads);
        Data data = new Data(firstSeq, secondSeq, MATCH, MISMATCH, GAP);
        nwunsch.ParallelService parallelService = new nwunsch.ParallelService(numThreads, data);
        return buildDataSlices(numThreads, data);

    }

    public List<PData> buildDataSlices(int numThreads, Data data) {
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
            pData.solution = subData;
            String subSeq = data.getFullSeq1().substring( i*tam, (i+1) *tam);
            pData.setFirstSeq(subSeq);
            pData.setSecondSeq(data.getFullSeq2());
            pSolution.add(pData);

        }

        return pSolution;
    }

    public void run(PData pData, Intracomm comm){
        int[] processes = {0, 1, 2, 3};
//        Intracomm comm = MPI.COMM_WORLD.Create((MPI.COMM_WORLD.Group()).Incl(processes));

        String firstSeq = pData.getFirstSeq();
        String secondSeq = pData.getSecondSeq();
        int length = firstSeq.length();
        sendBuffer = new int[secondSeq.length() + 1];
        receiveBuffer = new int[length];
        for (int j = 1; j < secondSeq.length() + 1; j++) {
            for (int i = 1; i < length; i++) {
                int matchValue;
                if (firstSeq.charAt(i-1) == secondSeq.charAt(j-1)) {
                    matchValue = MATCH;
                }
                else{
                    matchValue = MISMATCH;
                }

                pData.solution[i][j] = max(pData.solution[i][j-1] + GAP, pData.solution[i-1][j] + GAP, pData.solution[i-1][j-1] + matchValue);
                if (i == length-1){
                    sendBuffer[j] = pData.solution[i][j];
//                    comm.Send(sendBuffer, j, 1, MPI.INT, 1, 10 );
                }
            }
        }
    }

    private int max(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }

    private void validateInput(Integer limit, int numThreads) {

        System.out.println("tamanho " + limit + " numThreads "+ numThreads);
        int resto = limit % numThreads;
        int sugestao = numThreads * (limit / numThreads);
        if (resto != 0) {
            throw new IllegalArgumentException(
                    "Entrada inválida, o tamanho da string de entrada deve ser um múltiplo do numero de threads, sugestão: "
                            + sugestao);
        }
    }

}
