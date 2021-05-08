package nwunsch;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import nwunsch.utils.FileLoader;

/**
 * @author mateus
 */
public class NeedlemanWunschMain {

    private final static int MATCH = 4; // caracteres iguais

    private final static int MISMATCH = -2; // caracteres diferentes

    private final static int GAP = -1; // penalidade por lacuna

    public static CyclicBarrier barrier;

    public static void main(String[] args) throws IOException {
        FileLoader fileLoader = new FileLoader();

//        String fileName1 = "seq1_SARS-COV.data";
//        String fileName2 = "seq2_MERS.data";
        String firstSeq;
        String secondSeq;
        Integer limit = 1000;
        int numThreads = 4;

        if (args.length > 0) {
            System.out.println("Input limit size: " + args[0]);
            try {
                limit = Integer.parseInt(args[0]);
                numThreads = Integer.parseInt(args[1]);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Erro de argumentos, o formato deve ser: [tamanhoDaEntrada] [numThreads]");
            }

        }
        validateInput(limit, numThreads);

        if (limit != null && limit > 0) {
//            firstSeq = fileLoader.getSequence("Sequence A").substring(0, limit);
//            secondSeq = fileLoader.getSequence("Sequence B").substring(0, limit);
        } else {
            firstSeq = fileLoader.getSequence("Sequence A");
            secondSeq = fileLoader.getSequence("Sequence B");
        }

//        //sequencial
//        System.out.println("------------------------------------------------");
//        System.out.println("Execução Sequencial:");
//        System.out.println("processando...");
//        long currentTimeStart = System.nanoTime();
//        nwunsch.NeedlemanWunsch alinhamento = new nwunsch.NeedlemanWunsch(firstSeq, secondSeq, MATCH, MISMATCH, GAP, true);
//        long currentTimeEnd = System.nanoTime();
//
//        BigDecimal finalTime = BigDecimal.valueOf((currentTimeEnd - currentTimeStart) / 1000000.);
//        System.out.println("tempo de execução (ms): " + finalTime);
//        alinhamento.printStrandInfo();
//        System.out.println("Fim da execução sequencial:");
        firstSeq =  "ATTAAAGGTTTATACCTTCC";
        secondSeq = "ATCTCACTTCCCCTCGTTCT";
        //parallel
        System.out.println("------------------------------------------------");
        System.out.println("Execução Paralela:");
        System.out.println("numThreads " + numThreads);
        Data data = new Data(firstSeq, secondSeq, MATCH, MISMATCH, GAP);
        nwunsch.ParallelService parallelService = new nwunsch.ParallelService(numThreads, data);
        int score = parallelService.runParallel();

        System.out.println("A pontuação para este alinhamento (paralelo) é: " + score);

    }

    private static void validateInput(Integer limit, int numThreads) {

        int resto = limit % numThreads;
        int sugestao = numThreads * (limit / numThreads);
        if (resto != 0) {
            throw new IllegalArgumentException(
                    "Entrada inválida, o tamanho da string de entrada deve ser um múltiplo do numero de threads, sugestão: "
                            + sugestao);
        }
    }

    public static void reachBarrier() {

        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

}
