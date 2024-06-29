package adaptiveHuffman;

import adaptiveHuffman.decoder.Decoder;
import adaptiveHuffman.encoder.Encoder;
import adaptiveHuffman.tree.Tree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static adaptiveHuffman.encoder.Encoder.getFileExtension;

public class basicForm {
    private JPanel panel;
    private JButton encodeButton;
    private JButton decodeButton;
    private JTextPane textPane2;
    private JTextPane textPane1;
    private JTextPane encodeInfoPane;
    private JTextPane decodeInfoPane;

    public basicForm() {
        // Initialize panel and set preferred size
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(650, 250)); // Adjust width and height as needed
        panel.setLayout(new GridBagLayout());

        encodeButton = new JButton("Encode");
        decodeButton = new JButton("Decode");

        encodeButton.setPreferredSize(new Dimension(150, 30));
        decodeButton.setPreferredSize(new Dimension(150, 30));

        encodeInfoPane = new JTextPane();
        decodeInfoPane = new JTextPane();

        encodeInfoPane.setPreferredSize(new Dimension(300, 150)); // Adjust size as needed
        decodeInfoPane.setPreferredSize(new Dimension(300, 150)); // Adjust size as needed

        encodeInfoPane.setEditable(false);
        decodeInfoPane.setEditable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Add encode button
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(encodeButton, gbc);

        // Add decode button
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(decodeButton, gbc);

        // Add encode info pane
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(encodeInfoPane), gbc);

        // Add decode info pane
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(decodeInfoPane), gbc);

        // Add encodeButton action listener
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String absolutePath = selectedFile.getAbsolutePath();
                    String outputFilePath = "output.txt"; // Adjust output file path as needed
                    File file = new File(absolutePath);
                    String extension = getFileExtension(file);
                    String newExtension = "." + extension;
                    String filePath = "ekstenzija.txt";

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                        writer.write(newExtension);
                        encodeInfoPane.setText("Successfully written extension to file: " + filePath + "\n");
                    } catch (IOException ex) {
                        encodeInfoPane.setText("Error writing to file " + filePath + ": " + ex.getMessage() + "\n");
                    }

                    Encoder encoder = new Encoder(absolutePath, outputFilePath);
                    Tree tree = new Tree();
                    File in = new File(absolutePath);
                    long startTime = System.nanoTime();
                    encoder.encode(tree);
                    long endTime = System.nanoTime();
                    File out = new File(outputFilePath);

                    // Example output
                    StringBuilder sb = new StringBuilder();
                    sb.append("Finished compression of: ").append(in.getName()).append(" in ").append((float) (endTime - startTime) / 1000000).append(" ms\n");
                    sb.append("Original size: ").append(in.length()).append(" bytes\n");
                    sb.append("Compressed size: ").append(out.length()).append(" bytes\n");
                    sb.append("Compression ratio: ").append((float) in.length() / (float) out.length()).append("\n");
                    encodeInfoPane.setText(sb.toString());
                } else {
                    encodeInfoPane.setText("No file selected\n");
                }
            }
        });


        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Read all bytes from the file and convert to string
                    String ekstenzija = new String(Files.readAllBytes(Paths.get("ekstenzija.txt")));
                    Decoder dec = new Decoder("output.txt","outputDecode.txt");
                    Tree tree = new Tree();
                    File in = new File("output.txt");
                    dec.decode(tree);
                    File out = new File("outputDecode.txt");
                    long duzina=out.length();

                    String currentFileName = out.getName();
                    String currentExtension = currentFileName.substring(currentFileName.lastIndexOf('.'));
                    String desiredExtension = ekstenzija;

                    StringBuilder sb = new StringBuilder();
                    if (!currentExtension.equals(desiredExtension)) {
                        String fileNameWithNewExtension = currentFileName.replaceFirst("\\.\\w+$", "") + desiredExtension;
                        File renamedFile = new File(out.getParent(), fileNameWithNewExtension);
                        if (renamedFile.exists()) {
                            if (renamedFile.delete()) {
                                System.out.println("Deleted existing file with the same name.\n");
                            } else {
                                System.out.println("Failed to delete existing file.\n");
                            }
                        }
                        if (out.renameTo(renamedFile)) {
                            System.out.println("File extension changed successfully.\n");
                            String absolutePathFile = renamedFile.getAbsolutePath();
                            sb.append("Finished decompression of: ").append(in.getName()).append("\n");
                            sb.append("Original size: ").append(in.length()).append(" bytes\n");
                            sb.append("Uncompressed size: ").append(duzina).append(" bytes\n");
                            sb.append("Compression ratio: ").append((float)duzina/(float)in.length()).append("\n");
                            sb.append("Apsolutna putanja do dekompresovanog fajla:").append(absolutePathFile);
                        } else {
                            System.out.println("Failed to change file extension.\n");
                        }
                    } else {
                        System.out.println("File extension is already " + desiredExtension);
                        String absolutePathFile = out.getAbsolutePath();
                        sb.append("Finished decompression of: ").append(in.getName()).append("\n");
                        sb.append("Original size: ").append(in.length()).append(" bytes\n");
                        sb.append("Uncompressed size: ").append(duzina).append(" bytes\n");
                        sb.append("Compression ratio: ").append((float)duzina/(float)in.length()).append("\n");
                        sb.append("Apsolutna putanja do dekompresovanog fajla:").append(absolutePathFile);
                    }


                    decodeInfoPane.setText(sb.toString());
                } catch (IOException exec) {
                    decodeInfoPane.setText("Error during decompression: " + exec.getMessage() + "\n");
                }
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }
}
