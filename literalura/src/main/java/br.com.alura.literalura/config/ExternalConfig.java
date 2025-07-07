package br.com.alura.literalura.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
//classe de configuração do Spring. Nela pode conter definições de beans e outras configurações para a aplicação.
@PropertySource("classpath:config.properties")
public class ExternalConfig {
    // Esta classe garante que o Spring Boot carregue as propriedades do config.properties

}

