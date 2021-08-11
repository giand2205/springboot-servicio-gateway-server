package com.springboot.app.gateway.filters.factory;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class CustomGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomGatewayFilterFactory.Configuration> {

    private final Logger logger = LoggerFactory.getLogger(CustomGatewayFilterFactory.class);

    public CustomGatewayFilterFactory() {
        super(Configuration.class);
    }

    @Override
    public GatewayFilter apply(Configuration config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            logger.info("ejecutando pre gateway filter factory: " + config.mensaje);
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Optional.ofNullable(config.cookieValor).ifPresent(cookie -> {
                    exchange.getResponse().addCookie(ResponseCookie.from(config.cookieNombre, cookie).build());
                });
                logger.info("ejecutando post gateway filter factory: " + config.mensaje);
            }));
        }, 2);
    }

    @Getter
    @Setter
    public static class Configuration {
        private String mensaje;
        private String cookieValor;
        private String cookieNombre;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("mensaje", "cookieNombre", "cookieValor");
    }

    @Override
    public String name() {
        return "CustomCookie";
    }
}
