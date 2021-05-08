package nwunsch;

import java.util.List;

public class NeedlemanWunschThread extends Thread {

    private final String name;

    private final Data data;

    private List<Integer> indexes;

    public NeedlemanWunschThread(String name, Data data) {

        this.name = name;
        this.data = data;
    }

    public void setI(List<Integer> indexes) {

        this.indexes = indexes;
    }

    @Override
    public void run() {

        for (Integer i : indexes) {

//            System.out.println(Thread.currentThread().getName() + " - index: " + i);

            for (int j = 1; j < data.getFullSeq1().length() + 1; j++) {
                int matchValue;
                if (data.getFullSeq1().charAt(i - 1) == data.getFullSeq2().charAt(j - 1)) {
                    matchValue = data.getMATCH();
                } else {
                    matchValue = data.getMISMATCH();
                }
                if (data.solution[i - 1][j] == null) {
                    waitingRoom(i - 1, j);
                }
                int max = max(data.solution[i][j - 1] + data.getGAP(),
                        data.solution[i - 1][j] + data.getGAP(),
                        data.solution[i - 1][j - 1] + matchValue);

                data.solution[i][j] = max;
            }
        }
    }

    private void waitingRoom(int i, int j) {

//        System.out.println(Thread.currentThread().getName() + " ativou o semaforo - indices: i= " + i + " j= " + j);
//        data.solution[i][j].await();

    }

    private int max(int a, int b, int c) {

        return Math.max(Math.max(a, b), c);
    }
}
