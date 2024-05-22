package com.csc340.restapidemo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class StudentFileHandler {
    private static final String FILE_PATH = "students.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(StudentFileHandler.class.getName());

    public static Map<Integer, Student> readStudentsFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return new HashMap<>();
            }
            return mapper.readValue(file, mapper.getTypeFactory().constructMapType(HashMap.class, Integer.class, Student.class));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading students from file", e);
            return new HashMap<>();
        }
    }

    public static void writeStudentsToFile(Map<Integer, Student> students) {
        try {
            mapper.writeValue(new File(FILE_PATH), students);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing students to file", e);
        }
    }
}



