package nyomio.spring;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@ComponentScan("nyomio")
public class AppConfig {

  @Bean
  public Executor threadPoolTaskExecutor() {
    return new ThreadPoolTaskExecutor();
  }

}
