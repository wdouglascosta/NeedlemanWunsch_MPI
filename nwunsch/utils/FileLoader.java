package nwunsch.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileLoader {

    public String getSequence(String sequenceName) throws IOException {

        String absolutePath;
        JFileChooser path = new JFileChooser();
        path.setDialogTitle("Select " + sequenceName);
        path.setFileFilter(new FileNameExtensionFilter(sequenceName + ".data", "data"));

        if (path.showDialog(null, "Open") == JFileChooser.APPROVE_OPTION) {
            absolutePath = path.getSelectedFile().toString().trim();
            System.out.println(absolutePath);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid input!", "Fail", JOptionPane.ERROR_MESSAGE);
            throw new IOException("Invalid path");
        }

        BufferedReader br = new BufferedReader(new FileReader(absolutePath));
        String data = br.readLine();
        return data;

    }

}
