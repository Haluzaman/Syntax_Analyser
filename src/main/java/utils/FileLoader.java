package utils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileLoader {

    public static File loadSourceCodeFile(String name) throws FileNotFoundException {
        return loadFile("files/sourceCodes/" + name);
    }

    public static File loadParsingTableAsCSV(String name) throws FileNotFoundException {
        return loadFile("files/definitions/parseTables/" + name);
    }

    public static File loadLangDefinitionFile() throws FileNotFoundException {
        return loadFile("files/definitions/SMALL_definitions.txt");
    }

    private static File loadFile(String name) throws FileNotFoundException {
        URL fileURL = FileLoader.class.getClassLoader().getResource(name);

        if(fileURL == null) throw new FileNotFoundException();

        return new File(fileURL.getFile());
    }

    public static List<String> readFileLines(File file) {
        InputStream in = null;
        List<String> result = null;

        try {
            in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            result = new ArrayList<>();

            String line;
            while((line = br.readLine()) != null) {
                result.add(line.strip());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(in != null) {
                try{
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

}
