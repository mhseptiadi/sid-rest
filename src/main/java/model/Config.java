package model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("file:/src/main/resources")
public class Config {
	
	@Value("${test}")
	private String couchdb;

	public String getCouchdb() {
		return couchdb;
	}

	public void setCouchdb(String couchdb) {
		this.couchdb = couchdb;
	}
	
    @Autowired
    private Environment env;

    public String readProperty() {
        return env.getProperty("test");
    }
}
