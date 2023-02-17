package au.net.horizondigital.assessment.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "file:/opt/conf/app-configs.properties")
public class ApplicationConfigs {
}
