package com.Pahana_Edu_Backend.config;

// import org.apache.catalina.connector.Connector;
// import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
// import org.springframework.boot.web.server.WebServerFactoryCustomizer;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class TomcatUploadConfig {

//     @Bean
//     public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
//         return factory -> factory.addConnectorCustomizers((Connector connector) -> {
//             connector.setProperty("maxParameterCount", "10000");  // ✅ Increase form field/part count
//             connector.setProperty("maxPostSize", String.valueOf(50 * 1024 * 1024)); // 50MB
//             connector.setProperty("fileCountMax", "1000"); // ✅ Increase file+field part count limit
//         });
//     }
// }


import org.apache.catalina.Context;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

   @Configuration
   public class TomcatUploadConfig {

       @Bean
       public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
           return factory -> {
               factory.addConnectorCustomizers(connector -> {
                   connector.setProperty("maxParameterCount", "10000"); // allow 10,000 form fields
                   connector.setProperty("maxPostSize", String.valueOf(50 * 1024 * 1024)); // 50 MB max POST size
                   connector.setProperty("maxFileCount", "100"); // Set the maximum number of files
               });

               factory.addContextCustomizers(context -> {
                   context.setAllowCasualMultipartParsing(true);
               });
           };
       }
   }
   