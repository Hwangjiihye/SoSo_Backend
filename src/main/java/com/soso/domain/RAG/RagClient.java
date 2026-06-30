	package com.soso.domain.RAG;

	import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

	@Service
	public class RagClient {
		
		@Value("${rag.api.url}")
		private String ragApiUrl;

	    private final RestTemplate restTemplate = new RestTemplate();

	    private final String pythonUpsertUrl = "/rag/upsert";

	    public void upsert(String type, Object refId, String text, Map<String, Object> metadata) {
	        try {
	            Map<String, Object> requestBody = new HashMap<>();
	            requestBody.put("type", type);
	            requestBody.put("refId", refId);
	            requestBody.put("text", text);
	            requestBody.put("metadata", metadata);

	            restTemplate.postForObject(
	                    ragApiUrl + pythonUpsertUrl,
	                    requestBody,
	                    Map.class
	            );


	        } catch (Exception e) {

	            // 중요:
	            // RAG 실패해도 DB 등록/수정은 실패시키면 안 되니까 throw 안 함
	        }
	    }
	}
