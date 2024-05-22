package com.csc340.restapidemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class RestApiController {

    /**
     * Hello World API endpoint.
     *
     * @return response string.
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    /**
     * Greeting API endpoint.
     *
     * @param name the request parameter
     * @return the response string.
     */
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "Dora") String name) {
        return "Hola, soy " + name;
    }

    private static final Logger logger = Logger.getLogger(RestApiController.class.getName());

    /**
     * List all students.
     *
     * @return the list of students.
     */
    @GetMapping("students/all")
    public Object getAllStudents() {
        return StudentFileHandler.readStudentsFromFile().values();
    }

    @GetMapping("students/{id}")
    public Student getStudentById(@PathVariable int id) {
        return StudentFileHandler.readStudentsFromFile().get(id);
    }

    @PostMapping("students/create")
    public Object createStudent(@RequestBody Student student) {
        Map<Integer, Student> students = StudentFileHandler.readStudentsFromFile();
        students.put(student.getId(), student);
        StudentFileHandler.writeStudentsToFile(students);
        return students.values();
    }

    @PutMapping("students/update/{id}")
    public Object updateStudent(@PathVariable int id, @RequestBody Student student) {
        Map<Integer, Student> students = StudentFileHandler.readStudentsFromFile();
        if (students.containsKey(id)) {
            Student existingStudent = students.get(id);
            existingStudent.setName(student.getName());
            existingStudent.setMajor(student.getMajor());
            existingStudent.setGpa(student.getGpa());
            StudentFileHandler.writeStudentsToFile(students);
        }
        return students.values();
    }

    @DeleteMapping("students/delete/{id}")
    public Object deleteStudent(@PathVariable int id) {
        Map<Integer, Student> students = StudentFileHandler.readStudentsFromFile();
        students.remove(id);
        StudentFileHandler.writeStudentsToFile(students);
        return students.values();
    }

    /**
     * joke api
     * @return
     */
    @GetMapping("/joke")
    public Object getJoke() {
        try {
            String url = "https://official-joke-api.appspot.com/random_joke";
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String jsonJoke = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jsonJoke);

            String setup = root.get("setup").asText();
            String punchline = root.get("punchline").asText();
            System.out.println("Setup: " + setup);
            System.out.println("Punchline: " + punchline);

            return root;

        } catch (JsonProcessingException ex) {
            logger.log(Level.SEVERE, "Error processing JSON from /joke", ex);
            return "error in /joke";
        }
    }

    /**
     * Get a list of universities from hipolabs and make them available at our own API
     * endpoint.
     *
     * @return json array.
     */
    @GetMapping("/univ")
    public Object getUniversities() {
        try {
            String url = "http://universities.hipolabs.com/search?name=sports";
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String jsonListResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jsonListResponse);
            //The response from the above API is a JSON Array, which we loop through.
            for (JsonNode rt : root) {
                //Extract relevant info from the response and use it for what you want, in this case just print to the console.
                String name = rt.get("name").asText();
                String country = rt.get("country").asText();
                System.out.println(name + ": " + country);
            }
            return root;
        } catch (JsonProcessingException ex) {
            logger.log(Level.SEVERE, "Error processing JSON from /univ", ex);
            return "error in /univ";
        }
    }

    /**
     * Get a quote from quotable and make it available our own API endpoint
     *
     * @return The quote json response
     */
    @GetMapping("/quote")
    public Object getQuote() {
        try {
            String url = "https://api.quotable.io/random";
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            //We are expecting a String object as a response from the above API.
            String jSonQuote = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jSonQuote);

            //Parse out the most important info from the response and use it for whatever you want. In this case, just print.
            String quoteAuthor = root.get("author").asText();
            String quoteContent = root.get("content").asText();
            System.out.println("Author: " + quoteAuthor);
            System.out.println("Quote: " + quoteContent);

            return root;

        } catch (JsonProcessingException ex) {
            Logger.getLogger(RestApiController.class.getName()).log(Level.SEVERE,
                    null, ex);
            return "error in /quote";
        }
    }
}

