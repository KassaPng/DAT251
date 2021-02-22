import org.junit.jupiter.api.Test;

public class TestMain {




    @Test
    void testHelloWorld() {
        Main main = new Main();
        assert(main.helloWorld().equals("Hello Worl!"));
    }



}
