
//package com.soso.domain.RAG;
//
//import java.util.Map;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.beans.factory.annotation.Value;
//
//@RestController
//@RequestMapping("/ai")
//public class RagController {
//	
//	@Value("${rag.api.url}")
//	private String ragApiUrl;
//	
//	// 외부(파이썬 AI 서버)와 통신하기 위한 스프링 전용 도구
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    @GetMapping("/chat")
//    public ResponseEntity<Map<String, Object>> chat(
//            @RequestParam String message,
//            @RequestParam int storeSeq,
//            @RequestParam int userSeq,
//            @RequestParam String userType) {
//        
//        System.out.println("========== [React -> Spring] 요청 들어옴 ==========");
//        System.out.println("유저 질문: " + message);
//        System.out.println("매장 번호: " + storeSeq);
//
//        // 1. 파이썬 AI 서버 주소 설정
////        String pythonUrl = ragApiUrl + "/ai/query";
//        String pythonUrl = ragApiUrl + "/rag/ask";
//
//        // 2. 파이썬이 요구하는 JSON 규격 데이터 바구니(DTO) 만들기
//        Map<String, Object> requestBody = Map.of(
//            "question", message,
//            "store_seq", storeSeq,
//            "user_seq", userSeq,
//            "user_type", userType
//        );
//
//        try {
//            // 3. 파이썬 AI 서버로 "질문이랑 매장번호 들고 결과 받아와!" 하고 슥 던지기
//            System.out.println(">>>> 파이썬 AI 서버로 요청 전달 중...");
//            Map<String, Object> response = restTemplate.postForObject(pythonUrl, requestBody, Map.class);
//            
//            // 4. 파이썬이 돌려준 결과를 React(화면)에 그대로 리턴
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(Map.of(
//                "status", "FAIL",
//                "error_message", "AI 서버와의 통신 중 오류가 발생했습니다: " + e.getMessage()
//            ));
//        }
//    }
//}

package com.soso.domain.RAG;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/ai")
public class RagController {
	
	@Value("${rag.api.url}")
	private String ragApiUrl;
	
	// 외부(파이썬 AI 서버)와 통신하기 위한 스프링 전용 도구
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/chat")
    public ResponseEntity<Map<String, Object>> chat(
            @RequestParam String message,
            @RequestParam int storeSeq,
            @RequestParam int userSeq,
            @RequestParam String userType) {
        

        // 1. 파이썬 AI 서버 주소 설정
//        String pythonUrl = ragApiUrl + "/ai/query";
        String pythonUrl = ragApiUrl + "/rag/ask";

        // 2. 파이썬이 요구하는 JSON 규격 데이터 바구니(DTO) 만들기
        Map<String, Object> requestBody = Map.of(
            "question", message,
            "store_seq", storeSeq,
            "user_seq", userSeq,
            "user_type", userType
        );

        try {
            // 3. 파이썬 AI 서버로 "질문이랑 매장번호 들고 결과 받아와!" 하고 슥 던지기
            Map<String, Object> response = restTemplate.postForObject(pythonUrl, requestBody, Map.class);
            
            // 4. 파이썬이 돌려준 결과를 React(화면)에 그대로 리턴
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "status", "FAIL",
                "error_message", "AI 서버와의 통신 중 오류가 발생했습니다: " + e.getMessage()
            ));
        }
    }
}
