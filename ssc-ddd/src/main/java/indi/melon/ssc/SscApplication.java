package indi.melon.ssc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author vvnn1
 * @since 2024/10/20 12:41
 */
@SpringBootApplication
@EnableJpaRepositories(
        basePackages = "indi.melon.ssc.*.south.repository.dao",
        repositoryImplementationPostfix = "Dao"
)
@EntityScan(basePackages = "indi.melon.ssc.*.domain.*")
public class SscApplication {
    public static void main(String[] args) {
        SpringApplication.run(SscApplication.class);
    }
}
