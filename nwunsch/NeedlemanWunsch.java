package nwunsch;

/**
 *
 * @author mateus
 */

public class NeedlemanWunsch {

    private final int MATCH; // caracteres iguais
    private final int MISMATCH; // caracteres diferentes
    private final int GAP; // penalidade por lacuna
    private String sequencia1;
    private String sequencia2;        
    private boolean allowMismatch;
    private Integer[][] solution;
    private int score;
    private String[] stringAlinhada;

    public NeedlemanWunsch(int MATCH, int MISMATCH, int GAP, String sequencia1, String sequencia2, boolean allowMismatch, Integer[][] solution, int score, String[] stringAlinhada) {
        this.MATCH = MATCH;
        this.MISMATCH = MISMATCH;
        this.GAP = GAP;
        this.sequencia1 = sequencia1;
        this.sequencia2 = sequencia2;
        this.allowMismatch = allowMismatch;
        this.solution = solution;
        this.score = score;
        this.stringAlinhada = stringAlinhada;
    }

    
    public NeedlemanWunsch(String sequencia1, String sequencia2) {
        this(sequencia1, sequencia2, 1, -1, -1, true);
    }

    
    public NeedlemanWunsch(String sequencia1, String sequencia2, int match, int mismatch, int gap, boolean allowMismatch) {
        this.sequencia1 = sequencia1;
        this.sequencia2 = sequencia2;
        this.MATCH = match;
        this.MISMATCH = mismatch;
        this.GAP = gap;
        this.allowMismatch = allowMismatch;        
        this.solution = findSolution();       
        this.score = solution[solution.length-1][solution[0].length-1];        
        this.stringAlinhada = recursiveFindPath(solution.length-1, solution[0].length-1);
        
    }

    public Integer[][] findSolution() {
        
        Integer[][] solution = new Integer[sequencia1.length()+1][sequencia2.length()+1];
        solution[0][0] = 0;

        for (int i = 1; i < sequencia2.length()+1; i++) {
            solution[0][i] = solution[0][i-1] + GAP;
        }
        
        for (int i = 1; i < sequencia1.length()+1; i++) {
            solution[i][0] = solution[i-1][0] + GAP;
        }
        for (int i = 1; i < sequencia1.length()+1; i++) {
            for (int j = 1; j < sequencia2.length()+1; j++) {
                int matchValue;
                if (sequencia1.charAt(i-1) == sequencia2.charAt(j-1)) {
                    matchValue = MATCH;
                }
                else{
                    matchValue = MISMATCH;
                }

                solution[i][j] = max(solution[i][j-1] + GAP, solution[i-1][j] + GAP, solution[i-1][j-1] + matchValue);
            }
        }
        
        return solution;
    }

    private int max(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }

    public String[] findPath() {

        String seq1 = "";
        String seq2 = "";
        
        int i = solution.length - 1;
        int j = solution[0].length - 1;

        boolean matchAllowed;
        int matchValue;

        while (i != 0  && j != 0) {            
            matchAllowed = true;            
            if (sequencia1.charAt(i-1) != sequencia2.charAt(j-1) && !allowMismatch){
                matchAllowed = false;
            }            
            if (sequencia1.charAt(i-1) == sequencia2.charAt(j-1)){
                matchValue = MATCH;
            }
            else{
                matchValue = MISMATCH;
            }
            if (solution[i-1][j-1] == solution[i][j] - matchValue && matchAllowed) {
                seq1 = sequencia1.charAt(i-1) + seq1;
                seq2 = sequencia2.charAt(j-1) + seq2;                
                i -= 1;
                j -= 1;            
            } else if (solution[i][j-1] == solution[i][j] - GAP) {
                seq1 = "-" + seq1;
                seq2 = sequencia2.charAt(j-1) + seq2;                
                j -= 1;            
            } else {
                seq1 = sequencia1.charAt(i-1) + seq1;
                seq2 = "-" + seq2;                
                i -= 1;            
            }
        }
        
        if (i == 0) {
            for (int k = 0; k < j; k++) {
                seq1 = "-" + seq1;
                seq2 = sequencia2.charAt(j-k) + seq2;
            }        
        } else {
            for (int k = 0; k < i; k++) {
                seq1 = sequencia1.charAt(i-k) + seq1;
                seq2 = "-" + seq2;
            }
        }

        return new String[] {seq1, seq2};
    }
    
    public String[] recursiveFindPath(int i, int j) {
        String seq1 = "";
        String seq2 = "";
        
        if (i == 0) {
            for (int k = 0; k < j; k++) {
                seq1 = "-" + seq1;
                seq2 = sequencia2.charAt(j-k) + seq2;
            }
            return new String[] {seq1, seq2};

        } else if (j == 0) {
            for (int k = 0; k < i; k++) {
                seq1 = sequencia1.charAt(i-k) + seq1;
                seq2 = "-" + seq2;
            }

            return new String[] {seq1, seq2};
        }

        boolean matchAllowed = true;
        int matchValue;

        if (sequencia1.charAt(i-1) != sequencia2.charAt(j-1) && !allowMismatch){
            matchAllowed = false;
        }        
        if (sequencia1.charAt(i-1) == sequencia2.charAt(j-1)){
            matchValue = MATCH;
        }
        else{
            matchValue = MISMATCH;
        }

        if (solution[i-1][j-1] == solution[i][j] - matchValue && matchAllowed) {
            seq1 = "" + sequencia1.charAt(i-1);
            seq2 = "" + sequencia2.charAt(j-1);            
            i -= 1;
            j -= 1;        
        } else if (solution[i][j-1] == solution[i][j] - GAP) {
            seq1 = "-";
            seq2 = "" + sequencia2.charAt(j-1);            
            j -= 1;        
        } else {
            seq1 = "" + sequencia1.charAt(i-1);
            seq2 = "-";            
            i -= 1;
        }
        
        String[] stringAlinhada = recursiveFindPath(i, j);

        seq1 = stringAlinhada[0] + seq1;
        seq2 = stringAlinhada[1] + seq2;

        return new String[] {seq1, seq2};
    }

    public void printStrandInfo() {
//        System.out.println(stringAlinhada[0]);
//        System.out.println(stringAlinhada[1]);
        System.out.println("A pontuação para este alinhamento é: " + score);
    }
    
    public void printMatrizScore() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < getSolution().length; i++) {
            s.append('[');
            for (int j = 0; j < getSolution()[i].length; j++) {
                s.append(getSolution()[i][j]);
                if (j != getSolution()[i].length - 1) // não é o último elemento da linha
                {
                    s.append(',');
                    s.append(' ');
                } else {
                    s.append(']');
                }
            }
            if (i != getSolution().length - 1) // não é a última linha
            {
                s.append('\n');
            }
        }

        System.out.println(" \n" + s.toString());
    }

    public Integer[][] getSolution() {
        return solution;
    }

    public int getScore() {
        return score;
    }

    public String[] getStringAlinhada() {
        return stringAlinhada;
    }
}
