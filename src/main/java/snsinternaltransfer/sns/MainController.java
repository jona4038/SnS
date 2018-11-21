
package snsinternaltransfer.sns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import snsinternaltransfer.sns.models.User;
import snsinternaltransfer.sns.service.LoginService;

import java.util.ArrayList;
import java.util.logging.Logger;

@org.springframework.stereotype.Controller
public class MainController {

    @Autowired
    LoginService loginService;
    ArrayList<User> loggedInUsers;



    private final Logger log = Logger.getLogger(MainController.class.getName());

    @GetMapping("/")
    public String index(){

        log.info("index called");
        return "index";

    }

    @GetMapping("/userCreate")
    public String userCreate(){

        log.info("userCreate call");
        return "userCreate";
    }

    @GetMapping("/adminMenu")
    public String adminMenu(){

        log.info("adminMenu call");
        return "adminMenu";
    }

    @GetMapping("/itemList")
    public String itemList(){

        log.info("itemList call");
        return "itemList";
    }
}


