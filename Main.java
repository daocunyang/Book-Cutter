import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {   
	   System.out.println("Start converting...");
       displayDir(new File("D:\\txt").listFiles());
       System.out.println("Done");
	}

	private static void displayDir(File[] files) throws IOException{
		String s;
		for (File file: files)	
			if(file.isDirectory()){
				s = file.getName();
				new File("d:/out/" + s).mkdir();
				parseOneBook(s);
			}
	}
	
	private static void parseOneBook(String dirName) throws IOException{
		String catalogPath = "d:/txt/" + dirName + "/content.txt";
        List<String> catalogList = createCatalog(catalogPath);
        if (catalogList.size()==0)
        	throw new IOException("ERROR - file not found: " + "d:/txt/" + dirName + "/content.txt");

        String blackPath = "d:/txt/" + dirName + "/blacklist.txt";
        List<String> blackList = createCatalog(blackPath);

        int name = 1;
        RandomAccessFile ff = new RandomAccessFile("d:/out/" + dirName + "/0001.txt","rw");
        boolean isFirstChapter = true;

        File f = new File("d:/txt/" + dirName + "/main.txt");
        InputStreamReader read = new InputStreamReader(new FileInputStream(f),"UTF-8");
        BufferedReader readBuffer = new BufferedReader(read);
        String tempString = null;
        while ((tempString = readBuffer.readLine()) != null) {
            String out = new String(tempString.getBytes("UTF-8"), "ISO-8859-1");
            out += "\r\n\r\n";
            if (blackList.contains(tempString.trim())) {
                continue;
            }
            else if (catalogList.contains(tempString.trim())) {
                if (isFirstChapter){
                    ff.writeBytes(out);
                    isFirstChapter = false;
                    continue;
                }
                else {
                    ff.close();
                    name++;
                }
                String lpad = lpad(4,name);
                ff = new RandomAccessFile("d:/out/" + dirName + "/" + String.valueOf(lpad) + ".txt","rw");
                ff.writeBytes(out);
            }
            //else if (tempString !=null && !"".equals(tempString)){
            else {
                ff.writeBytes(out);
            }
        }
        readBuffer.close();
        read.close();
	}
	
    private static List<String> createCatalog(String path) {
        List<String> catalog = new ArrayList<String>();
        File f = new File(path);
        InputStreamReader read = null;
        BufferedReader readBuffer = null;
        try {
            read = new InputStreamReader(new FileInputStream(f),"UTF-8");
            readBuffer = new BufferedReader(read);

            String tempString = null;
            while ((tempString = readBuffer.readLine()) != null) {
                if (tempString !=null && !"".equals(tempString)) {
                    catalog.add(tempString.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (readBuffer != null) {
                try {
                    readBuffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return catalog;
    }

    private static String lpad(int length, int number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }
}
