package replacetabforpipe;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GC014121
 */
public class ReplaceTabForPipe {

    private static void changeTokenInFile(File file, String tokenToChange, String tokenToReplace) {
        BufferedReader reader = null;
        FileWriter writer = null;
        try {

            //Lectura de archivo y reemplazo de token.
            reader = new BufferedReader(new FileReader(file));
            System.out.println(reader.lines().count() - 1);
            StringBuilder builder = new StringBuilder();
            String[] wordsArray;
            String lineJustFetched;
            while (true) {
                lineJustFetched = reader.readLine();
                if (lineJustFetched == null) {
                    break;
                } else {
                    wordsArray = lineJustFetched.split(tokenToChange);
                    if (wordsArray != null) {
                        for (String each : wordsArray) {
                            if (!"".equals(each)) {
                                builder.append(each);
                                builder.append(tokenToReplace);
                            }
                        }
                        builder.replace(builder.lastIndexOf(tokenToReplace), builder.length(), "\n");
                    }
                }
            }

            //Escritura en archivo.
            writer = new FileWriter(file);
            writer.write(builder.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(ReplaceTabForPipe.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(ReplaceTabForPipe.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static InputStream changeTokenInFile(InputStream file, String tokenToChange, String tokenToReplace) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();

        try {
            String[] wordsArray;
            String lineJustFetched;
            //Lectura de archivo y reemplazo de token.
            try ( BufferedReader br = new BufferedReader(
                    new InputStreamReader(file, StandardCharsets.UTF_8))) {
                // read line by line
                while ((lineJustFetched = br.readLine()) != null) {
                    wordsArray = lineJustFetched.split(tokenToChange);
                    if (wordsArray != null) {
                        for (String each : wordsArray) {
                                builder.append(each);
                                builder.append(tokenToReplace);
                        }
                        builder.replace(builder.lastIndexOf(tokenToReplace), builder.length(), "\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(ReplaceTabForPipe.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return new ByteArrayInputStream(builder.toString().getBytes());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File initialFile = new File("filePath");
        InputStream inputStream = changeTokenInFile(new FileInputStream(initialFile), "\t", "|");
        OutputStream outStream = new FileOutputStream(initialFile);
        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
    }
}
