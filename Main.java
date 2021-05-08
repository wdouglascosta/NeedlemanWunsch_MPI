import mpi.MPI;
import nwunsch.PData;

public class Main {

    public static void main(String[] args) {
        MPI.Init(args);
        PData cell = new PData();
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        System.out.println("e agora? <"+me+"> size: " + size);
        MPI.Finalize();
    }
}
//  javac -cp .:$MPJ_HOME/lib/mpj.jar Main.java
//  mpjrun.sh -np 2 Main