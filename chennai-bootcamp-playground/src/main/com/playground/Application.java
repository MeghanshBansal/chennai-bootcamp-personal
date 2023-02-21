package main.com.playground;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.com.playground.domain.OTP;
import main.com.playground.domain.ToDo;
import main.com.playground.domain.User;
import main.com.playground.service.*;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.com.playground.util.GSONHelper.deserialize;
import static spark.Spark.*;

public class Application {

    private static final Integer SPARK_DEFAULT_PORT = 4567;
    private static final Integer SPARK_DEFAULT_MAX_THREADS = 20;
    private static final Integer SPARK_DEFAULT_MIN_THREADS = 5;
    private static final Integer SPARK_DEFAULT_TIMEOUT = 1000;

    public static void main(String[] args) {
        configureSpark();
        configureRoutes();
    }

    private static void configureSpark() {
        Spark.threadPool(SPARK_DEFAULT_MAX_THREADS, SPARK_DEFAULT_MIN_THREADS, SPARK_DEFAULT_TIMEOUT);
        Spark.port(SPARK_DEFAULT_PORT);
    }

    private static void configureRoutes() {
        Spark.staticFiles.location("/public");
        get("/home", (request, response) -> {
            response.type("text/html");
            Map<String, String> homeModel = new HashMap<>();
            homeModel.put("emailValue", "email");
            homeModel.put("nameValue", "name");
            return new VelocityTemplateEngine().render(new ModelAndView(homeModel, "/public/home.html"));
        });

        get("/ping", (request, response) -> ("{\"response\":\"pong\"}"));

        get("/user", ((request, response) -> {
            JsonObject jsonResponse = new JsonObject();
            Map<String, String> cookies = request.cookies();
            UserService userService = new UserService();
            User user = userService.getLoggedInUser(cookies.get("token"));
            int id = userService.getUserId(user);
            if (!user.getEmail().equals("") && !user.getName().equals("")) {
                response.status(200);
                jsonResponse.addProperty("success", "true");
                jsonResponse.addProperty("name", user.getName());
                jsonResponse.addProperty("id", id);
                jsonResponse.addProperty("email", user.getEmail());
                jsonResponse.addProperty("error", "");
                return jsonResponse;
            }
            response.status(403);
            jsonResponse.addProperty("success", "false");
            jsonResponse.addProperty("error", "user is not logged in");
            return jsonResponse;
        }));

        post("/SignUp", ((request, response) -> {
            JsonObject jsonResponse = new JsonObject();
            User user = deserialize(request.body(), User.class);
            SignUpService signup = new SignUpService(user);
            signup.save();
            response.status(201);
            jsonResponse.addProperty("success", "true");
            jsonResponse.addProperty("error", "");
            return jsonResponse;
        }));

        post("/SendOTP", ((request, response) -> {
            JsonObject jsonResponse = new JsonObject();
            OTP otp = deserialize(request.body(), OTP.class);
            System.out.println(otp.getEmail());
            OTPService otpService = new OTPService(otp.getEmail());
            otpService.sendOTP(otp);
            response.status(200);
            jsonResponse.addProperty("success", "true");
            jsonResponse.addProperty("error", "");
            return jsonResponse;
        }));

        post("/ValidateOTP", ((request, response) -> {
            JsonObject jsonResponse = new JsonObject();
            ValidateOTPService req = deserialize(request.body(), ValidateOTPService.class);
            int id = req.validateOTP();
            System.out.println(id);
            if (id > 0) {
                String authToken = req.getAuthToken();
                req.saveAuthToken(authToken, id);
                response.status(200);
                jsonResponse.addProperty("success", "true");
                jsonResponse.addProperty("error", "");
                jsonResponse.addProperty("token", authToken);
                return jsonResponse;
            }
            response.status(403);
            jsonResponse.addProperty("success", "false");
            jsonResponse.addProperty("error", "user is not logged in");
            return jsonResponse;
        }));

        post("/CreateToDo", ((request, response) -> {
            JsonObject jsonResponse = new JsonObject();
            Map<String, String> cookies = request.cookies();
            UserService userService = new UserService();
            User user = userService.getLoggedInUser(cookies.get("token"));
            if (!user.getEmail().equals("") && !user.getName().equals("")) {
                String userId = request.queryParams("id");
                ToDo todo = new ToDo("", "");
                List<String> message = todo.getMessage(userId);

                System.out.println(message.size());
                ToDoService req = deserialize(request.body(), ToDoService.class);
                if(message.size() >= 5 ) {
                    response.status(422);
                    jsonResponse.addProperty("success", "false");
                    jsonResponse.addProperty("total", message.size());
                    jsonResponse.addProperty("error", "User can not add more than 5 todos");
                    return jsonResponse;
                }
                else {
                    req.saveToDo(Integer.parseInt(userId));
                    response.status(201);
                    jsonResponse.addProperty("success", "true");
                    jsonResponse.addProperty("total", message.size()+1);
                    jsonResponse.addProperty("error", "");
                    return jsonResponse;
                }
            }
            response.status(403);
            jsonResponse.addProperty("success", "false");
            jsonResponse.addProperty("error", "user is not logged in");
            return jsonResponse;

        }));

        get("/GetToDo", ((request, response) -> {
            JsonObject jsonResponse = new JsonObject();
            Map<String, String> cookies = request.cookies();
            UserService userService = new UserService();
            User user = userService.getLoggedInUser(cookies.get("token"));
            if (!user.getEmail().equals("") && !user.getName().equals("")) {
                String userId = request.queryParams("id");
                System.out.println(userId);
                ToDo todo = new ToDo("", "");
                List<String> message = todo.getMessage(userId);

                JsonArray messages = new JsonArray();
                for (int i = 0; i < message.size(); i++) {
                    messages.add(message.get(i));
                }
                response.status(200);

                jsonResponse.add("todos", messages);
                jsonResponse.addProperty("success", "true");
                jsonResponse.addProperty("total", messages.size());
                jsonResponse.addProperty("error", "");
                return jsonResponse;
            }
            response.status(403);
            jsonResponse.addProperty("success", "false");
            jsonResponse.addProperty("error", "user is not logged in");
            return jsonResponse;
        }));

        get("/search", ((request, response) -> {
            JsonObject jsonResponse = new JsonObject();
            Map<String, String> cookies = request.cookies();
            UserService userService = new UserService();
            User user = userService.getLoggedInUser(cookies.get("token"));
            if (!user.getEmail().equals("") && !user.getName().equals("")) {
                String userId = request.queryParams("id");
                String searchQuery = request.queryParams("q");
                System.out.println(userId);
                System.out.println(searchQuery);
                ToDo todo = new ToDo("", "");
                List<String> message = todo.search(userId, searchQuery);

                JsonArray messages = new JsonArray();
                for (int i = 0; i < message.size(); i++) {
                    messages.add(message.get(i));
                }
                response.status(200);

                jsonResponse.add("todos", messages);
                jsonResponse.addProperty("success", "true");
                jsonResponse.addProperty("error", "");
                return jsonResponse;
            }
            response.status(403);
            jsonResponse.addProperty("success", "false");
            jsonResponse.addProperty("error", "user is not logged in");
            return jsonResponse;
        }));

    }
}
