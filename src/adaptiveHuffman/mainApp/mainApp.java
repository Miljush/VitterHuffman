package adaptiveHuffman.mainApp;

import adaptiveHuffman.basicForm;

import javax.swing.*;

public class mainApp {
    public static void main(String[] args){
        basicForm form = new basicForm();
        JFrame frame = new JFrame("Kompresija podataka primenom adaptivnog Huffman-ovog kodiranja: Vitter-ov metod");
        frame.setContentPane(form.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
