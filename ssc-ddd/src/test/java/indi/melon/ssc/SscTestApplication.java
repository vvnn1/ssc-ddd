package indi.melon.ssc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author vvnn1
 * @since 2024/10/7 22:51
 */
@SpringBootApplication
@EnableJpaRepositories(
        basePackages = "indi.melon.ssc.directory.south.repository.dao",
        repositoryImplementationPostfix = "Dao"
)
@EntityScan(basePackages = "indi.melon.ssc.domain.*")
public class SscTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SscTestApplication.class);
    }
}
