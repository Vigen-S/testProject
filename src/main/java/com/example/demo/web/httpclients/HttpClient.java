package com.example.demo.web.httpclients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpClient {
    private final RestTemplate restTemplate;

    private static final String COMMA_DELIMITER = ",";
    private static final String URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref.zip";

    private LocalDate lastUpdateDate = LocalDate.now();

    private HashMap<String, Double> savedData = new HashMap<>();

    public HashMap<String, Double> getCurrenciesFormInternet() {
        if (dateNotChanged() && savedData.size()>0) {
            return savedData;
        }

        //getting zipped file
        File fileZip = restTemplate.execute(URL, HttpMethod.GET, null, clientHttpResponse -> {
            File ret = File.createTempFile("downloaded", ".zip");
            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
            return ret;
        });

        var unzipped = unzip(fileZip);

        var currenciesMap = new HashMap<String, Double>();

        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(unzipped))) {
            String line;

            //reading first line with headers
            var firstLine = br.readLine();
            String[] keys = firstLine.split(COMMA_DELIMITER);

            //reading second line with values
            var secondLine = br.readLine();
            String[] values = secondLine.split(COMMA_DELIMITER);


            //we always expect valid data from file if it is not we reject whole file
            for (int i = 1; i < keys.length-1; i++) {
                double value = Double.valueOf(values[i]);
                currenciesMap.put(keys[i].trim(),value);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
            LocalDate lastUpdateDate = LocalDate.parse(values[0], formatter);
            savedData = currenciesMap;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currenciesMap;
    }

    private boolean dateNotChanged(){
        LocalDate today = LocalDate.now();
        return today.isEqual(lastUpdateDate);
    }

    private File unzip(File fileZip) {
        File newFile;
        try {
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
            ZipEntry zipEntry = zis.getNextEntry();

            //since i know there is only one entry
            newFile = newFile(fileZip.getParentFile(), zipEntry);
            while (zipEntry != null) {
                //File newFile = newFile(fileZip.getParentFile(), zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // write file content
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return newFile;
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

}
