	package com.soso.domain.RAG;

	import java.util.HashMap;
	import java.util.Map;

	import org.springframework.stereotype.Service;
	import org.springframework.web.client.RestTemplate;

	@Service
	public class RagClient {

	    private final RestTemplate restTemplate = new RestTemplate();

	    private final String pythonUpsertUrl = "http://localhost:8000/rag/upsert";

	    public void upsert(String type, Object refId, String text, Map<String, Object> metadata) {
	        try {
	            Map<String, Object> requestBody = new HashMap<>();
	            requestBody.put("type", type);
	            requestBody.put("refId", refId);
	            requestBody.put("text", text);
	            requestBody.put("metadata", metadata);

	            restTemplate.postForObject(
	                    pythonUpsertUrl,
	                    requestBody,
	                    Map.class
	            );

	            System.out.println("RAG upsert 성공: " + type + ":" + refId);

	        } catch (Exception e) {
	            System.out.println("RAG upsert 실패: " + type + ":" + refId);
	            System.out.println(e.getMessage());

	            // 중요:
	            // RAG 실패해도 DB 등록/수정은 실패시키면 안 되니까 throw 안 함
	        }
	    }
	}
