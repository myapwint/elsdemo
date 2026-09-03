package com.es.elsdemo.config;

import com.es.elsdemo.document.Candidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;

@Configuration
public class ElasticsearchIndexInitializer {

	private static final Logger log = LoggerFactory.getLogger(ElasticsearchIndexInitializer.class);

	@Bean
	@ConditionalOnProperty(prefix = "app.elasticsearch", name = "create-index-on-startup", havingValue = "true", matchIfMissing = true)
	ApplicationRunner createCandidateIndex(ElasticsearchOperations elasticsearchOperations) {
		return args -> {
			IndexOperations indexOperations = elasticsearchOperations.indexOps(Candidate.class);

			if (indexOperations.exists()) {
				log.info("Elasticsearch index '{}' already exists", indexOperations.getIndexCoordinates().getIndexName());
				return;
			}

			indexOperations.createWithMapping();
			log.info("Created Elasticsearch index '{}'", indexOperations.getIndexCoordinates().getIndexName());
		};
	}
}
