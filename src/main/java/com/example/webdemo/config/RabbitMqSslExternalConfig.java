package com.example.webdemo.config;

import com.rabbitmq.client.DefaultSaslConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Collections;

@Slf4j
@Configuration
public class RabbitMqSslExternalConfig {

    private static final String TRUST_STORE_PATH = "/certs/truststore.p12";
    private static final String KEY_STORE_PATH = "/certs/client_keystore.p12";

    private RabbitProperties rabbitProperties;

    /**
     * 自定义 RabbitMQ 连接工厂，支持双向认证和 EXTERNAL SASL 登录
     */
    @Bean
    @Primary
    public CachingConnectionFactory rabbitConnectionFactory() throws Exception {
        // 原生客户端工厂
        com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory = new com.rabbitmq.client.ConnectionFactory();

        // 设置服务器地址和端口
        rabbitConnectionFactory.setHost(rabbitProperties.getHost());
        rabbitConnectionFactory.setPort(rabbitProperties.getPort());

        // 设置 SSL 上下文
        SSLContext sslContext = buildSslContext();
        rabbitConnectionFactory.useSslProtocol(sslContext);

        // 设置 SASL 登录方式为 EXTERNAL（基于客户端证书）
        rabbitConnectionFactory.setSaslConfig(DefaultSaslConfig.EXTERNAL);

        // 不设置用户名密码，因为EXTERNAL认证基于证书
//        rabbitConnectionFactory.setUsername("");
//        rabbitConnectionFactory.setPassword("");

        // 你可以根据需求设置其他属性，比如心跳、超时等
        rabbitConnectionFactory.setRequestedHeartbeat(60);
        rabbitConnectionFactory.setConnectionTimeout(30000);

        // 使用 Spring AMQP 的缓存工厂包装
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(rabbitConnectionFactory);
        cachingConnectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
        cachingConnectionFactory.setPublisherReturns(true);

        log.info("RabbitMQ SSL EXTERNAL ConnectionFactory created: {}:{}",
                rabbitProperties.getHost(), rabbitProperties.getPort()
        );
        return cachingConnectionFactory;
    }

    /**
     * 构建双向 TLS 的 SSLContext
     */
    private SSLContext buildSslContext() throws Exception {
        // 加载信任库，验证服务端证书
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        try (InputStream trustStream = getClass().getResourceAsStream(TRUST_STORE_PATH)) {
            if (trustStream == null) {
                throw new IllegalStateException("TrustStore file not found: " + TRUST_STORE_PATH);
            }
            trustStore.load(trustStream, rabbitProperties.getSsl().getTrustStorePassword().toCharArray());
        }
        log.info("Loaded TrustStore with aliases: {}", Collections.list(trustStore.aliases()));

        // 加载密钥库，提供客户端证书和私钥
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream keyStream = getClass().getResourceAsStream(KEY_STORE_PATH)) {
            if (keyStream == null) throw new IllegalStateException("KeyStore file not found: " + KEY_STORE_PATH);
            keyStore.load(keyStream, rabbitProperties.getSsl().getKeyStorePassword().toCharArray());
        }
        log.info("Loaded KeyStore with aliases: {}", Collections.list(keyStore.aliases()));

        // 初始化 TrustManager，验证服务端证书
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        // 初始化 KeyManager，提供客户端证书
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, rabbitProperties.getSsl().getKeyStorePassword().toCharArray());

        // 初始化 SSLContext，指定 TLS 版本
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return sslContext;
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Autowired
    public void setRabbitProperties(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }
}
