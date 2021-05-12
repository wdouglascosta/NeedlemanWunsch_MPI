import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import mpi.Intracomm;
import mpi.MPI;
import nwunsch.NeedlemanWunschMain;
import nwunsch.PData;

public class Main {

    public static PData data;
    public static int[] sendBuffer;
    public static int[] receiveBuffer;

    public static void main(String[] args) throws IOException {
        String args2[] = {"4", "20"};
        List<PData> initializedData = NeedlemanWunschMain.initialize(args2);

        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        data = initializedData.get(me);
//        int length = data.getFirstSeq().length();
        int length = 10;
        sendBuffer = new int[length];
        receiveBuffer = new int[length];

//        Arrays.stream(sendBuffer).map(i -> i = 9);

        sendBuffer[0] = 9;
        int[] nextId = {0, 1};
        Intracomm comm = MPI.COMM_WORLD.Create((MPI.COMM_WORLD.Group()).Incl(nextId));
        int message = buildMessage(me);

        if (me == 0){
            System.out.println("enviando do processo 0");
            comm.Send(sendBuffer, 0, 10, MPI.INT, 1, 10 );
        }

        if (me == 1){
            System.out.println("recebendo no processo 1");
            comm.Recv(receiveBuffer, 0, 10, MPI.INT, 0, 10);
        }

        if (me == 0 || me == 1){

            System.out.println("processo " + me + " sendBuffer -> " + sendBuffer[0]);
            System.out.println("processo " + me + " receiveBuffer -> " + receiveBuffer[0]);
        }

        PData cell = new PData();

//        System.out.println("numero do processo -> <"+me+"> size: " + size);
        MPI.Finalize();
    }

    private static String print(int[] sendBuffer) {

        return null;
    }

    private static int buildMessage(int me) {

        switch (me){
            case 1:
                return 111;
            case 2:
                return 222;
            case 3:
                return 333;
            case 4:
                return 444;
            default:
                return 0;
        }
    }

    public static void receive(int index, int value){

    }
}
//  javac -cp .:$MPJ_HOME/lib/mpj.jar Main.java
//  mpjrun.sh -np 2 Main