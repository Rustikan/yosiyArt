package intech.config;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Created by Valeev-RN on 08.02.2019.
 */
@Configuration
@EnableWebMvc
@ComponentScan("intech")
public class WebAppConfig extends WebMvcConfigurerAdapter{

    @Bean
    public UrlBasedViewResolver setupViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        // указываем где будут лежать наши веб-страницы
        resolver.setPrefix("/webapp/");
        // формат View который мы будем использовать
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        resolver.setContentType("text/html; charset=utf-8");

        return resolver;
    }

    @Bean("rest_template")
    @Lazy
    public RestTemplate restTemplateSSLLongTimout() {

        int timeout = 100000;
        String keyStorePassword = "changeit";

        KeyStore keyStore = null;
        SSLConnectionSocketFactory socketFactory = null;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream resourceAsStream = getClass().getResourceAsStream("../../cacerts");
            keyStore.load(resourceAsStream, keyStorePassword.toCharArray());

            socketFactory = new SSLConnectionSocketFactory(
                    new SSLContextBuilder()
                            .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                            .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
                            .build(),
                    NoopHostnameVerifier.INSTANCE);
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | KeyManagementException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }

        HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(timeout);
        httpRequestFactory.setConnectTimeout(timeout);
        httpRequestFactory.setReadTimeout(timeout);
        httpRequestFactory.setHttpClient(httpClient);

        return new RestTemplate(httpRequestFactory);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }
}
