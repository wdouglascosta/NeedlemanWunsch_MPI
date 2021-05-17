package nwunsch;

import java.io.IOException;
import java.util.List;

public class main {

    public static void main(String[] args) throws IOException {
        NeedlemanWunschMain needlemanWunschMain = new NeedlemanWunschMain();
        List<PData> initialize = needlemanWunschMain.initialize(args);
        PData pData = initialize.get(0);
        System.out.println("first Seq: " + pData.getFirstSeq());
        System.out.println("second Seq: " + pData.getSecondSeq());

        print(pData.solution);
        needlemanWunschMain.run(pData, comm);
        System.out.println("after");
        print(pData.solution);


    }

    private static void print(Integer[][] array) {

        System.out.println("tamanho do array ->" +array.length);
        for (int i = 0; i < array.length; i++){
            System.out.println();
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + ", ");
            }
        }


    }
}
