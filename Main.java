import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import mpi.Intracomm;
import mpi.MPI;
import mpjdev.Comm;
import mpjdev.MPJDev;
import mpjbuf.*;
import nwunsch.NeedlemanWunschMain;
import nwunsch.PData;

public class Main {

    public static PData data;

    private static final int GAP = -1;

    public static void main(String[] args) throws IOException, BufferException {
        MPI.Init(args);

        String args2[] = {"4", "20"};
        NeedlemanWunschMain needlemanWunschMain = new NeedlemanWunschMain();
        List<PData> initializedData = needlemanWunschMain.initialize(args2);

        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        System.out.println("i'am process " + me + "/" + size);

        data = initializedData.get(me);
        int[] sendBuffer = {-1,9,8,7,6,5,4,3,2,1,0,1,2,3,4,5,6,7,8,9};
        int[] receiveBuffer = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int[] nextId = {0, 1, 2, 3};
        Intracomm comm = MPI.COMM_WORLD.Create((MPI.COMM_WORLD.Group()).Incl(nextId));

        if(me == 0){

            comm.Send(sendBuffer, 0, 20, MPI.INT, 1, 10 );

            print(data.solution);
            needlemanWunschMain.run(data, comm);
            print(data.solution);

        }

        else {
            System.out.println("------------------recebendo no processo: " + me);
//            System.out.println("antes: "+Arrays.toString(receiveBuffer));
            comm.Recv(receiveBuffer, 0, 20, MPI.INT, 0, 10);


            System.out.println("p: " + me + " recebido: "+Arrays.toString(receiveBuffer));
        }

        if(me == 1){
            System.out.println("p1 chegou aqui");
            int[] sendBuffer2 = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
            comm.Send(sendBuffer2, 0, 20, MPI.INT, 2, 10 );
        }
        MPJDev.finish();
    }

    private static void print(Integer[][] array) {

        System.out.println("imprimindo array de tamanho: " +array.length + " x "+ array[0].length + "-----------------------------");
        for (int i = 0; i < array.length; i++){
            System.out.println();
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + ", ");
            }
        }
        System.out.println();
        System.out.println("----------------------------------------------------------------");
    }


    public static void receive(int index, int value){

    }
}
//  javac -cp .:$MPJ_HOME/lib/mpj.jar Main.java
//  mpjrun.sh -np 2 Main