package nwunsch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mpi.Intracomm;
import mpi.MPI;

/**
 * @author mateus
 */
public class NeedlemanWunschMain {

    private final static int MATCH = 4; // caracteres iguais
    private final static int MISMATCH = -2; // caracteres diferentes
    private final static int GAP = -1; // penalidade por lacuna
    public static int[] sendBuffer;
    public static int[] receiveBuffer = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private static void print(Integer[][] array) {

        System.out.println("imprimindo array de tamanho: " + array.length + " x " + array[0].length
                + "-----------------------------");
        for (int i = 0; i < array.length; i++) {
            System.out.println();
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + ", ");
            }
        }
        System.out.println();
        System.out.println("----------------------------------------------------------------");
    }

    public List<PData> initialize(String[] args) throws IOException {

        String firstSeq;
        String secondSeq;
        int numThreads = 4; //USAR O MPI SIZE AQUI

        firstSeq = "ATTAAAGGTTTATACCTTCC";
        secondSeq = "ATCTCACTTCCCCTCGTTCT";

//        System.out.println("numThreads " + numThreads);
        Data data = new Data(firstSeq, secondSeq, MATCH, MISMATCH, GAP);
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

        for (int i = 0; i < numThreads; i++) {
            PData pData = new PData();
            Integer[][] subData = Arrays.copyOfRange(data.solution, i * tam, (i + 1) * tam);
            pData.solution = subData;
            String subSeq = data.getFullSeq1().substring(i * tam, (i + 1) * tam);
            pData.setFirstSeq(subSeq);
            pData.setSecondSeq(data.getFullSeq2());
            pSolution.add(pData);

        }

        return pSolution;
    }

    public void run(PData pData, Intracomm comm, int me) {

        if (me == 0) {

            runFirstProcess(pData, comm);

        } else {
//            print(pData.solution);
            runSecProcess(pData, comm, me);
        }

    }

    private void runSecProcess(PData pData, Intracomm comm, int me) {

        int predecessor = me - 1;
        int successor = me + 1;

        String firstSeq = pData.getFirstSeq();
        String secondSeq = pData.getSecondSeq();
        int length = firstSeq.length();
        sendBuffer = new int[secondSeq.length() + 1];
        for (int j = 1; j < secondSeq.length() + 1; j++) {
            comm.Recv(receiveBuffer, 0, 20, MPI.INT, predecessor, 10);
            for (int c = 1; c < receiveBuffer.length; c++) {
                pData.solution[0][c] = receiveBuffer[c];
            }
            for (int i = 1; i < length; i++) {
                int matchValue;
                if (firstSeq.charAt(i - 1) == secondSeq.charAt(j - 1)) {
                    matchValue = MATCH;
                } else {
                    matchValue = MISMATCH;
                }

                pData.solution[i][j] = max(pData.solution[i][j - 1] + GAP, pData.solution[i - 1][j] + GAP,
                        pData.solution[i - 1][j - 1] + matchValue);
                if ((i == length - 1) && (successor < 4)) {
                    sendBuffer[j] = pData.solution[i][j];
                    comm.Send(sendBuffer, 0, 20, MPI.INT, successor, 10);
                }
            }
        }
        System.out.println("sendBuffer from p: " + me + " | " + Arrays.toString(sendBuffer));
        if (me == 3) {
            print(pData.solution);
            System.out.println("Solução: " + pData.solution[4][20]);
        }
    }

    private void runFirstProcess(PData pData, Intracomm comm) {

        String firstSeq = pData.getFirstSeq();
        String secondSeq = pData.getSecondSeq();
        int length = firstSeq.length();
        sendBuffer = new int[secondSeq.length() + 1];
        receiveBuffer = new int[length];
        for (int j = 1; j < secondSeq.length() + 1; j++) {
            for (int i = 1; i < length; i++) {
                int matchValue;
                if (firstSeq.charAt(i - 1) == secondSeq.charAt(j - 1)) {
                    matchValue = MATCH;
                } else {
                    matchValue = MISMATCH;
                }

                pData.solution[i][j] = max(pData.solution[i][j - 1] + GAP, pData.solution[i - 1][j] + GAP,
                        pData.solution[i - 1][j - 1] + matchValue);
                if (i == length - 1) {
                    sendBuffer[j] = pData.solution[i][j];
                    comm.Send(sendBuffer, 0, 20, MPI.INT, 1, 10);
                }
            }
        }
//        print(pData.solution);
        System.out.println("sendBuffer from 0: " + " | " + Arrays.toString(sendBuffer));
    }

    private int max(int a, int b, int c) {

        return Math.max(Math.max(a, b), c);
    }

}
