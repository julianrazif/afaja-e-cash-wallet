package com.julianraziffigaro.afajaecashwallet.web;

import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.http.config.CharCodingConfig;
import org.apache.hc.core5.http.config.Http1Config;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.reactive.function.client.WebClient;

import javax.net.ssl.SSLContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@SpringBootApplication(
  exclude = {
    JmxAutoConfiguration.class
  }
)
public class Application {

  @Bean
  public WebClient webClient() {
    SSLContext sslContext;

    try {
      sslContext = SSLContexts.custom()
        .loadTrustMaterial(null, (X509Certificate[] chain, String authType) -> true)
        .build();
    } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
      throw new RuntimeException(e);
    }

    TlsStrategy tlsStrategy = ClientTlsStrategyBuilder
      .create()
      .setSslContext(sslContext)
      .setTlsVersions(TLS.V_1_0.id, TLS.V_1_1.id, TLS.V_1_2.id)
      .setHostnameVerifier(new NoopHostnameVerifier())
      .build();

    SystemDefaultDnsResolver dnsResolver = new SystemDefaultDnsResolver() {
      @Override
      public InetAddress[] resolve(final String host) throws UnknownHostException {
        if ("localhost".equalsIgnoreCase(host)) {
          return new InetAddress[]{
            InetAddress.getByAddress(new byte[]{127, 0, 0, 1})
          };
        } else {
          return super.resolve(host);
        }
      }
    };

    PoolingAsyncClientConnectionManager connectionManager = PoolingAsyncClientConnectionManagerBuilder
      .create()
      .setTlsStrategy(tlsStrategy)
      .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
      .setConnPoolPolicy(PoolReusePolicy.LIFO)
      .setConnectionTimeToLive(TimeValue.ofSeconds(5L))
      .setDnsResolver(dnsResolver)
      .setMaxConnTotal(100)
      .setMaxConnPerRoute(10)
      .setValidateAfterInactivity(TimeValue.ofSeconds(10L))
      .build();

    CharCodingConfig charCodingConfig = CharCodingConfig.custom()
      .setMalformedInputAction(CodingErrorAction.IGNORE)
      .setUnmappableInputAction(CodingErrorAction.IGNORE)
      .setCharset(StandardCharsets.UTF_8)
      .build();

    IOReactorConfig ioReactorConfig = IOReactorConfig
      .custom()
      .setSoTimeout(Timeout.ofSeconds(10L))
      .setIoThreadCount(Runtime.getRuntime().availableProcessors() * 2)
      .build();

    CustomizableThreadFactory workerThreadFactory = new CustomizableThreadFactory("RCPA-worker-");
    workerThreadFactory.setThreadPriority(Thread.MAX_PRIORITY);
    workerThreadFactory.setDaemon(true);

    Http1Config http1Config = Http1Config
      .custom()
      .setMaxHeaderCount(100)
      .build();

    RequestConfig requestConfig = RequestConfig
      .custom()
      .setConnectTimeout(Timeout.ofSeconds(10L))
      .setConnectionRequestTimeout(Timeout.ofSeconds(10L))
      .setCookieSpec(StandardCookieSpec.STRICT)
      .setExpectContinueEnabled(true)
      .build();

    HttpAsyncClientBuilder clientBuilder = HttpAsyncClients
      .custom()
      .setThreadFactory(workerThreadFactory)
      .setIOReactorConfig(ioReactorConfig)
      .setCharCodingConfig(charCodingConfig)
      .setHttp1Config(http1Config)
      .setConnectionManager(connectionManager)
      .setDefaultCookieStore(new BasicCookieStore())
      .setDefaultRequestConfig(requestConfig);

    CloseableHttpAsyncClient client = clientBuilder.build();
    ClientHttpConnector connector = new HttpComponentsClientHttpConnector(client);

    return WebClient
      .builder()
      .clientConnector(connector)
      .baseUrl("http://localhost:9091").build();
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }
}
