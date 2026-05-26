CREATE TABLE `user_sanctions` (
  `sanction_seq` INT NOT NULL PRIMARY KEY COMMENT '제재 기록 고유 번호',
  `user_seq` INT NOT NULL COMMENT '사용자 ID',
  `penalty_score` INT NULL DEFAULT 0 COMMENT '누적된 횟수',
  `reason` VARCHAR(255) NULL COMMENT '제재 사유',
  `created_at` TIMESTAMP NULL COMMENT '날짜'
);

CREATE TABLE `group_buys` (
  `group_buy_seq` INT NOT NULL PRIMARY KEY,
  `item_seq` INT NOT NULL,
  `target_quantity` INT NOT NULL,
  `current_quantity` INT NULL DEFAULT 0,
  `end_date` DATETIME NOT NULL,
  `status` ENUM('RECRUITING','CLOSED','SHIPPING','INSPECTING','DELIVERING','COMPLETED') NULL DEFAULT 'RECRUITING',
  `created_at` TIMESTAMP NULL
);

CREATE TABLE `finances` (
  `finance_seq` INT NOT NULL PRIMARY KEY COMMENT '장부 기록 고유 번호',
  `user_seq` INT NOT NULL COMMENT '장부 주인 seq',
  `type` ENUM('INCOME','EXPENSE') NOT NULL COMMENT '매출, 지출 구분',
  `amount` INT NOT NULL COMMENT '거래 금액',
  `category` VARCHAR(50) NULL COMMENT '비용 카테고리',
  `description` VARCHAR(255) NULL COMMENT '거래 상세 내용',
  `target_date` DATE NOT NULL COMMENT '매출/지출 날짜',
  `created_at` TIMESTAMP NULL COMMENT '데이터 입력 일시'
);

CREATE TABLE `files` (
  `files_seq` INT NOT NULL PRIMARY KEY COMMENT '파일 고유 번호',
  `user_seq` INT NULL COMMENT '파일을 올린 유저 ID',
  `board_seq` INT NULL COMMENT '게시판 연동용',
  `file_category` ENUM('STORE_IMAGE','BOARD_ATTACH','BUSINESS_LICENSE') NULL COMMENT '파일 카테고리',
  `oriname` VARCHAR(100) NULL COMMENT '사용자가 올린 실제 파일명',
  `sysname` VARCHAR(300) NULL COMMENT '서버 내부 고유 파일명',
  `file_size` BIGINT NULL COMMENT '파일 사이즈',
  `file_type` VARCHAR(50) NULL COMMENT '파일 확장자'
);

CREATE TABLE `chat_logs` (
  `chat_seq` INT NOT NULL PRIMARY KEY COMMENT '채팅 메시지 고유 번호 PK',
  `user_seq` INT NOT NULL COMMENT '대화에 참여 중인 사용자 ID',
  `room_seq` VARCHAR(50) NULL COMMENT '어떤 채팅방인지 구분하기 위한 룸 아이디',
  `message` TEXT NOT NULL COMMENT '주고받은 메시지 내용',
  `sender_type` ENUM('USER','BOT','SYSTEM') NOT NULL COMMENT '보낸 주체 (일반유저, 챗봇AI, 시스템알림)',
  `created_at` TIMESTAMP NULL COMMENT '메시지 전송 일시'
);

CREATE TABLE `boards` (
  `board_seq` INT NOT NULL PRIMARY KEY COMMENT '게시글 고유 번호 PK',
  `user_seq` INT NOT NULL COMMENT '작성자 user_id',
  `board_type` ENUM('HR','TIP','QNA','CS') NOT NULL COMMENT '게시판 종류 구분',
  `title` VARCHAR(200) NOT NULL COMMENT '글 제목',
  `content` TEXT NOT NULL COMMENT '글 내용',
  `views` INT NULL DEFAULT 0 COMMENT '조회수',
  `created_at` TIMESTAMP NULL COMMENT '작성일수'
);

CREATE TABLE `items` (
  `item_seq` INT NOT NULL PRIMARY KEY,
  `category_seq` INT NOT NULL,
  `item_code` VARCHAR(50) NOT NULL,
  `item_name` VARCHAR(100) NOT NULL,
  `spec` VARCHAR(100) NULL,
  `unit_price` INT NULL DEFAULT 0,
  `item_image` VARCHAR(255) NULL
);

CREATE TABLE `accounts` (
  `account_seq` INT NOT NULL PRIMARY KEY COMMENT '계좌 고유 번호 (PK)',
  `user_seq` INT NOT NULL COMMENT '계좌를 등록한 사용자 ID',
  `bank_name` VARCHAR(50) NOT NULL COMMENT '은행명 (예: 신한은행)',
  `account_number` VARCHAR(100) NOT NULL COMMENT '계좌 번호',
  `partner_seq` INT NULL,
  `billing_key` VARCHAR(100) NULL COMMENT 'PG사에서 발급해 준 자동결제용 인증 키'
);

CREATE TABLE `order_details` (
  `order_detail_seq` INT NOT NULL PRIMARY KEY COMMENT '발주 상세 고유 번호PK',
  `order_seq` INT NOT NULL COMMENT '발주 번호',
  `item_seq` INT NOT NULL COMMENT '주문한 품목 ID',
  `quantity` INT NOT NULL COMMENT '주문 수량',
  `price` INT NOT NULL COMMENT '주문 당시의 개당 단가'
);

CREATE TABLE `payment_history` (
  `history_id` INT NOT NULL PRIMARY KEY COMMENT '이력 고유 ID (PK)',
  `schedule_id` INT NULL COMMENT '자동결제 설정 ID(FK)',
  `pg_tid` INT NULL COMMENT 'PG사에서 발급한 해당 건별 결제 고유 거래번호',
  `requested_amout` INT NULL COMMENT '요청 금액',
  `status` ENUM('SUCCESS','FAIL','PENDING') NULL,
  `result_code` INT NULL COMMENT 'PG사 응답 코드',
  `error_message` VARCHAR(100) NULL COMMENT '실패 사유'
);

CREATE TABLE `orders` (
  `order_seq` INT NOT NULL PRIMARY KEY COMMENT '발주번호 PK',
  `buyer_seq` INT NOT NULL COMMENT '주문자의 user_id',
  `seller_seq` INT NOT NULL COMMENT '공급자의 user_id',
  `total_amount` INT NULL DEFAULT 0 COMMENT '발주 총 금액',
  `status` ENUM('RECEIPT','SHIPPING','COMPLETED') NULL DEFAULT 'RECEIPT' COMMENT '발주 상태',
  `created_at` TIMESTAMP NULL COMMENT '발주 일시'
);

CREATE TABLE `group_buy_participants` (
  `participant_seq` INT NOT NULL PRIMARY KEY,
  `group_buy_seq` INT NOT NULL,
  `user_seq` INT NOT NULL,
  `quantity` INT NOT NULL,
  `created_at` TIMESTAMP NULL
);

CREATE TABLE `reply` (
  `comment_seq` INT NOT NULL PRIMARY KEY COMMENT '댓글 고유 번호 PK',
  `board_seq` INT NOT NULL COMMENT '댓글이 달린 상위 게시글 ID',
  `user_seq` INT NOT NULL COMMENT '댓글 작성자 user_id',
  `comment` VARCHAR(100) NOT NULL COMMENT '댓글 내용',
  `created_at` TIMESTAMP NULL COMMENT '댓글 작성 일시',
  `re_reply` VARCHAR(100) NULL COMMENT '대댓글'
);

CREATE TABLE `auto_payment_schedule` (
  `schedule_seq` INT NOT NULL PRIMARY KEY COMMENT '설정 고유 ID(PK)',
  `company_seq` INT NULL COMMENT '사업자 ID(FK)',
  `vendor_seq` INT NULL COMMENT '거래처 ID(PK)',
  `builling_key_seq` INT NULL COMMENT '사용할 빌링키 ID(FK)',
  `payment_day` INT NULL COMMENT '매달 결제일(1~31)',
  `amount` INT NULL COMMENT '매달 결제할 금액',
  `is_active` ENUM('Y','N') NULL COMMENT '자동결제 활성화 여부(Y/N)',
  `start_date` INT NULL COMMENT '자동결제 시작일'
);

CREATE TABLE `stocks` (
  `stock_seq` INT NOT NULL PRIMARY KEY COMMENT '재고 고유 번호',
  `user_seq` INT NOT NULL,
  `stocks` VARCHAR(100) NOT NULL COMMENT '대상 품목명',
  `lot_number` INT NULL COMMENT '로트 번호',
  `lot_expiration` DATE NULL COMMENT '로트 유통기한',
  `quantity` INT NULL DEFAULT 0,
  `expiration_date` DATE NULL,
  `safety_stock` INT NULL DEFAULT 10,
  `alert_on_off` TINYINT(1) NULL DEFAULT 1
);

CREATE TABLE `categories` (
  `category_seq` INT NOT NULL PRIMARY KEY,
  `category_name` VARCHAR(50) NOT NULL
);

CREATE TABLE `users` (
  `user_seq` INT NOT NULL PRIMARY KEY COMMENT '회원 고유 번호 (PK) 로그인 아이디',
  `user_id` VARCHAR(50) NOT NULL COMMENT '로그인 아이디',
  `password` VARCHAR(255) NOT NULL COMMENT '암호화된 비밀번호',
  `nickname` VARCHAR(50) NULL COMMENT '서비스 내에서 사용할 닉네임',
  `email` VARCHAR(100) NULL COMMENT '본인 확인 및 알림용 이메일',
  `biz_number` VARCHAR(20) NULL COMMENT '사업자 등록 번호',
  `company_name` VARCHAR(100) NULL COMMENT '상호명/업체명',
  `opening_date` DATE NULL COMMENT '개업일자',
  `store_image` VARCHAR(255) NULL COMMENT '가게 대표사진 파일 경로 URL',
  `zonecode` INT(10) NULL COMMENT '우편번호',
  `address1` VARCHAR(100) NULL COMMENT '세부주소1',
  `address2` VARCHAR(100) NULL COMMENT '세부주소2',
  `phone` VARCHAR(20) NULL COMMENT '휴대폰 번호',
  `user_type` ENUM('BUSINESS','PARTNER','ADMIN') NOT NULL COMMENT '회원 구분',
  `created_at` TIMESTAMP NULL COMMENT '계정 생성 일시'
);

CREATE TABLE `employees` (
  `employee_seq` INT NOT NULL PRIMARY KEY COMMENT '직원 고유 번호 (PK)',
  `business_seq` INT NOT NULL COMMENT '어느 가게 소속인지 구분용 (User연동)',
  `emp_name` VARCHAR(50) NOT NULL COMMENT '직원 이름',
  `phone` VARCHAR(20) NULL COMMENT '직원 연락처',
  `work_start_time` TIME NULL COMMENT '출근 시간',
  `work_end_time` TIME NULL COMMENT '퇴근 시간',
  `status` ENUM('WORK','LEAVE','REST','정상근무') NULL DEFAULT 'WORK' COMMENT '현재 근무 상태'
);

CREATE TABLE `partner_relations` (
  `relation_seq` INT NOT NULL PRIMARY KEY COMMENT '관계 고유 번호',
  `business_seq` INT NOT NULL COMMENT '소상공인 가게 ID',
  `partner_seq` INT NOT NULL COMMENT '등록된 거래처 ID',
  `memo` VARCHAR(255) NULL COMMENT '해당 거래처에 대한 메모',
  `created_at` TIMESTAMP NULL COMMENT '등록된 날짜'
);