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

        int[] nextId = {0, 1, 2, 3};
        Intracomm comm = MPI.COMM_WORLD.Create((MPI.COMM_WORLD.Group()).Incl(nextId));

        needlemanWunschMain.run(data, comm, me);

        MPJDev.finish();
    }

}
//  javac -cp .:$MPJ_HOME/lib/mpj.jar Main.java
//  mpjrun.sh -np 2 Main