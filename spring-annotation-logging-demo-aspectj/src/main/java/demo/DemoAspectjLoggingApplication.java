package demo;

import com.ulisesbocchio.springannotationlogging.LoggingAspect;
import org.aspectj.lang.Aspects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

/**
 * @author Ulises Bocchio
 */
@SpringBootApplication
@EnableLoadTimeWeaving
@EnableSpringConfigured
@ComponentScan()
public class DemoAspectjLoggingApplication {

    private static final Logger LOG = LoggerFactory.getLogger(DemoAspectjLoggingApplication.class);

    public static void main(String[] args) {
        ApplicationContext appCtx = SpringApplication.run(DemoAspectjLoggingApplication.class, args);
        MyService service = appCtx.getBean(MyService.class);
        LOG.info("MyService's message: {}", service.getMessage("Uli"));
        LOG.info("Done!");
    }

    @Bean
    public LoggingAspect profilingAspect() {
        return Aspects.aspectOf(LoggingAspect.class);
    }
}
