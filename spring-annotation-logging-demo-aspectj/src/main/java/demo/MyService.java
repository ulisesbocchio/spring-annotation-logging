package demo;

import com.ulisesbocchio.springannotationlogging.Log;
import org.springframework.stereotype.Component;

/**
 * @author Ulises Bocchio
 */
@Component
public class MyService {
    @Log("'Return: ' + toString() + ' from: ' + #from")
    public String getMessage(String from) {
        return from + " says Hello World!";
    }
}
