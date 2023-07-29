package com.syed.code.config;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
public class FreeMarkerConf {

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        freemarker.template.Configuration configuration = new freemarker.template.Configuration();
        TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), "/templates/");
        configuration.setTemplateLoader(templateLoader);
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setConfiguration(configuration);
        return freeMarkerConfigurer;
    }
}
