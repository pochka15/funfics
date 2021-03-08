package com.pochka15.funfics.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
public class ElasticRestClientConfig extends AbstractElasticsearchConfiguration {

    // ...

    //    @Autowired
//    RestHighLevelClient highLevelClient;
//
//    RestClient lowLevelClient = highLevelClient.lowLevelClient();
//
//// ...
//
//    IndexRequest request = new IndexRequest("spring-data", "elasticsearch", randomID())
//            .source(singletonMap("feature", "high-level-rest-client"))
//            .setRefreshPolicy(IMMEDIATE);
//
//    IndexResponse response = highLevelClient.index(request);
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}

