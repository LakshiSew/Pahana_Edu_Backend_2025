// package com.Pahana_Edu_Backend.config;

// // import org.apache.catalina.connector.Connector;
// // import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
// // import org.springframework.boot.web.server.WebServerFactoryCustomizer;
// // import org.springframework.context.annotation.Bean;
// // import org.springframework.context.annotation.Configuration;

// // @Configuration
// // public class TomcatUploadConfig {

// //     @Bean
// //     public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
// //         return factory -> factory.addConnectorCustomizers((Connector connector) -> {
// //             connector.setProperty("maxParameterCount", "10000");  // ✅ Increase form field/part count
// //             connector.setProperty("maxPostSize", String.valueOf(50 * 1024 * 1024)); // 50MB
// //             connector.setProperty("fileCountMax", "1000"); // ✅ Increase file+field part count limit
// //         });
// //     }
// // }






// import org.apache.catalina.Context;
// import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
// import org.springframework.boot.web.server.WebServerFactoryCustomizer;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class TomcatUploadConfig {

//     @Bean
//     public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {
//         return factory -> factory.addConnectorCustomizers(connector -> {
//             connector.setMaxPostSize(1048576000); // 1000MB
//             connector.setProperty("maxParameterCount", "10000");
//             connector.setProperty("fileCountMax", "1000"); // 🔥 THIS IS THE ONE causing your issue
//         });
//     }
// }


   