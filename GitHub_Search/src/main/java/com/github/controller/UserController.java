package com.github.controller;


import com.github.LogInSession;
import com.github.repo.User;
import com.github.repo.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Controller class
 */
@Controller
public class UserController {

    /* we inject a property from the application.properties  file */
    @Value("${user}")
    private String user;
    @Value("${password}")
    private String pass;

    /* we need it so  inject the User repo bean */
    @Autowired
    private UserRepository repository;
    private ApplicationContext context;

    /* the application context can also be injected via ctor */
    @Autowired
    public UserController(ApplicationContext c) {
        this.context = c;
    }

    /* the resource parameter for session Bean */
    @Resource(name="logIn")
    private LogInSession loginSession;

    private UserRepository getRepo() {
        return (UserRepository)this.context.getBean(UserRepository.class);
    }

    /**
     *
     * @param user for all the details database
     * @param model for thymeleaf of the page
     * @return the html page
     */
    @GetMapping("/")
    public String main(User user, Model model) {

        if(loginSession.getLoginStatus() == 0){
            return "login";
        }
        else {
            System.out.println("bla bla");
            model.addAttribute("userGit", getRepo().findFirst10ByOrderByCountDesc());
            return "index";
        }
    }

    /**
     * login Get function
     * @param model for 10 users in GitHub
     * @return login page if the user didn't connect else go to GitHub searching
     */
    @GetMapping("/login")
    public String login(Model model){
        if(loginSession.getLoginStatus() == 1){ return "redirect:/"; }
        else { return "login"; }
    }

    /**
     * Login Function by POST request
      * @param model the result of 10 users in GitHub with the high count in history and for bad input
     * @param loginParam the username and password the stored iin Map
     * @throws JSONException exception for json that we send back to the client
     * @return if the username and password is true return json of OK else return NOT
     */
    @ResponseBody
    @PostMapping("/login")
    public String login(Model model, @RequestBody Map<String,Object> loginParam) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        if(user.equals(loginParam.get("user")) && pass.equals(loginParam.get("pass"))) {
            loginSession.setLoginStatus(1);
            model.addAttribute("userGit", getRepo().findFirst10ByOrderByCountDesc());
            jsonObject.put("statusLogin","ok");
            return jsonObject.toString();
        } else {
            jsonObject.put("statusLogin", "not");
        }
        return jsonObject.toString();
    }

    /**
     *Delete function
     * @param model the details in database
     * @return the page after delete all the history of github searching
     */
    @RequestMapping("/delete")
    public String deleteData(Model model){
        getRepo().deleteAll();
        return "index";
    }

    /**
     * Logout function
     * @param request for logout from GitHub searching and delete the session
     * @return go to first route
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }

    /**
     * readAll function: read all data in reader and make it to String object
     * @param rd Buffer reader , read all the stream json
     * @return return the json in String
     * @throws IOException for read all the information from json object
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp); }
        return sb.toString();
    }

    /**
     * getSearching function
     * @param user to store data in database like(username of GitHub and history and followers)
     * @param model the result of 10 users in GitHub with the high count in history
     * @return return to html page (login if the user is logout, else to index)
     */
    @GetMapping("/search")
    public String getSearching(@Valid User user, Model model){
        if (loginSession.getLoginStatus() == 0)
            return "login";
        model.addAttribute("userGit", getRepo().findFirst10ByOrderByCountDesc());
        return "index";
    }

    /**
     * searching function
     * @param user to store data in database like(username of GitHub and history and followers)
     * @param model to result of 10 users in github with the high count in history and for situations of inputs
     * @param username the username from github
     * @return the index.html of GitHub
     * @throws IOException for InputStream
     */
    @PostMapping("/search")
    public String searching(@Valid User user, Model model,@RequestParam(name = "login_name", required = true)
            String username ) throws IOException {
        if(username.equals("")) {
            notInput(model);
            return "index"; }
        try {
            /*the api of GitHub with a username*/
            String url = "https://api.github.com/users/"+username;
            InputStream is = new URL(url).openStream();
            saveOrUpdateUser(user, model,username, is);
            is.close();
        }catch (FileNotFoundException e) {
            //System.out.println("file not found");
            model.addAttribute("notFound","This user not found.");
            model.addAttribute("userGit",getRepo().findFirst10ByOrderByCountDesc());
        } catch (IOException e) { System.out.println("not work IOException"); }
        return "index";
    }

    /**
     * notInput function
     * @param model update the model about bad input
     */
    private void notInput(Model model) {
        model.addAttribute("inputNotFound","please enter username.");
        model.addAttribute("userGit",getRepo().findFirst10ByOrderByCountDesc());
    }

    /**
     * saveOrUpdateUser function
     * @param user for checking if username of GitHub is exists and for save a new user or update the counter search
     * @param model to results of 10 users in github with the high counter and for details of user in GitHub
     * @param username to store in database or to check if he exist
     * @param is InputStream for BufferReader
     */
    public void saveOrUpdateUser(User user, Model model, String username, InputStream is){
        try {
            /*Reads text from character-input stream*/
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            User u = getRepo().findByUsername(username);
            if (u == null) { DBOperations.saveData(user,json,username,getRepo()); }
            else { DBOperations.updateData(u,getRepo()); }
            updateView(model, json,username);

        } catch (JSONException e) {
            //System.out.println("json exception");
            e.printStackTrace(); }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }


    /**
     * updateView function
     * @param model for thymeleaf in index page
     * @param json to get a details on followers
     * @param username for model with thymeleaf
     * @throws JSONException for Get function in JsonObject
     */
    private void updateView(Model model, JSONObject json, String username) throws JSONException {
        model.addAttribute("userGit", getRepo().findFirst10ByOrderByCountDesc());
        model.addAttribute("name", username);
        if ((int) json.get("followers") > 0) {
            model.addAttribute("number", "Followers: " + json.get("followers").toString());
        } else
            model.addAttribute("number", "There are no followers.");
    }
}

