package utils;

import models.CarDetailsModel;

import java.io.BufferedReader;
import java.io.File ;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {

    public List<String> readInputFiles(){

        String numberPlatePattern = "\\b[A-Z]{2}\\d{2}\\s?[A-Z]{3}\\b"; // Regex for UK Reg Plate
        Pattern pattern = Pattern.compile(numberPlatePattern);
        List numberPlateList = new ArrayList<String>();
        File[] listOfFiles = getAllFiles("input"); // Handling multiple files

        if (listOfFiles != null && listOfFiles.length > 0) {
            for (File currentFile : listOfFiles) { //Iterating through all the files in the folder
                if (currentFile.isFile()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            Matcher matcher = pattern.matcher(line);
                            while(matcher.find()) {
                                String str = matcher.group();
                                String regionAndAge = str.substring(0, 4);
                                String randomLetters = str.substring(4).trim();
                                numberPlateList.add(regionAndAge + " " + randomLetters);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return numberPlateList;
    }

    public List<CarDetailsModel> readOutputFiles(){

        List carDetailsModelList = new ArrayList<CarDetailsModel>();
        File[] listOfFiles = getAllFiles("output"); // Handling multiple files

        if (listOfFiles != null && listOfFiles.length > 0) {
            for (File currentFile : listOfFiles) { //Iterating through all the files in the folder
                if (currentFile.isFile()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {

                        reader.readLine(); // Skipping the first line of the provided file

                        String line;
                        while ((line = reader.readLine()) != null) {

                            String[] splits = line.split(",");
                            if (splits.length == 4) {
                                String variantReg = splits[0].trim();
                                String regionAndAge = variantReg.substring(0, 4);
                                String randomLetters = variantReg.substring(4).trim();
                                variantReg = regionAndAge + " " + randomLetters;

                                String make = splits[1].trim();
                                String model = splits[2].trim();
                                String year = splits[3].trim();

                                CarDetailsModel car = new CarDetailsModel(variantReg, make, model, year);
                                carDetailsModelList.add(car);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return carDetailsModelList;
    }

    private File[] getAllFiles(String folderName){
        //This method handles multiple file in a folder
        ClassLoader cl = getClass().getClassLoader();
        URL resource = cl.getResource(folderName);

        if (resource != null) {
            File directory = new File(resource.getFile());
            File[] listOfFiles = directory.listFiles();
            return  listOfFiles;
        }
        return null;
    }
}