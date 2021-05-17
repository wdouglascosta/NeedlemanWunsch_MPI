package nwunsch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mpi.MPI;

public class ParallelService {

	private final Integer numThreads;

	private final Data data;

	public ParallelService(Integer numThreads, Data data) {

		this.numThreads = numThreads;
		this.data = data;
	}



}