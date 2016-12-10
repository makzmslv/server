package backend.server.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter
{

    public static final String INDEX_PATH = "/frontend/index.html";
    public static final String SWAGGER_PATH = "/swagger-ui.html";

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
    {
        // @formatter:off
        configurer
            .favorPathExtension(false)
            .favorParameter(true)
            .parameterName("mediaType")
            .ignoreAcceptHeader(true)
            .useJaf(false)
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_JSON);
        // @formatter:on
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
    {
        converters.add(converter());
    }

    @Bean
    MappingJackson2HttpMessageConverter converter()
    {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new JacksonObjectMapper());
        return converter;
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer()
    {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setPort(8080);
        factory.setSessionTimeout(10, TimeUnit.MINUTES);
        // factory.addErrorPages(new ErrorPage(HttpStatus.404, "/notfound.html"));
        return factory;
    }

    @Bean
    public Mapper mapper()
    {
        List<String> mappingFiles = new ArrayList<String>();
        mappingFiles.add("dozer-mappings/bill.xml");
        mappingFiles.add("dozer-mappings/menu.xml");
        mappingFiles.add("dozer-mappings/order.xml");
        mappingFiles.add("dozer-mappings/menuItem.xml");
        return new DozerBeanMapper(mappingFiles);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addRedirectViewController("/", INDEX_PATH);
        registry.addRedirectViewController("/frontend", INDEX_PATH);
        registry.addRedirectViewController("/frontend/", INDEX_PATH);
        registry.addRedirectViewController("/swagger", SWAGGER_PATH);
        registry.addRedirectViewController("/swagger/", SWAGGER_PATH);
    }
}