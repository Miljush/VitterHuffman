package adaptiveHuffman.encoder;

import java.io.*;
import java.util.ArrayList;
import javax.swing.JFileChooser;

import adaptiveHuffman.BitByteOutputStream;
import adaptiveHuffman.tree.*;

public class Encoder {
	
	public FileInputStream in = null;
	public BitByteOutputStream out = null;
	public static String getFileExtension(File file) {
		String fileName = file.getName();
		int lastIndexOf = fileName.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return ""; // No extension found
		}
		return fileName.substring(lastIndexOf + 1);
	}
    public Encoder(String in, String out) {
		System.out.println(in);
    	try {
			this.in = new FileInputStream(in);
			this.out = new BitByteOutputStream(new FileOutputStream(out));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    }

	public void encode(Tree tree) {
		
		try {
			
			int c = 0;
			
			while((c = in.read()) != -1) {
				ArrayList<Boolean> buffer = new ArrayList<Boolean>();
				if (tree.contains(c)) {
					
					int len = tree.getCode(c,true,buffer);
					for(len=len-1 ;len>=0;len--){
						out.writeBit(buffer.get(len));
					}
					tree.insertInto((int)c);
				}
				else {
					int len = tree.getCode(c, false,buffer);
					for(len=len-1 ;len>=0;len--){
						out.writeBit(buffer.get(len));
					}
					out.writeByte(c);
					tree.insertInto(c);
				}
			}
			out.flush();
		}
		catch (IOException e) {
			System.err.println("Error reading from input");
			e.printStackTrace();
		}
		finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null) {
				out.close();
			}
		}
	}
	
	
	

}
