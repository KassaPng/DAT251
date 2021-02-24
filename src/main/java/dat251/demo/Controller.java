package dat251.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @RequestMapping("/hello")
    public String index() {
        return "Hello World!";
    }

}
