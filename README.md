# kakaoRepository

1. 개발 프레임워크 : Spring boot 2.2.7
2. 테이블 설계
CREATE TABLE PAYMENT_INFO (
  MANAGE_ID VARCHAR2(20) NOT NULL,
  CARD_INFO VARCHAR2(200) NOT NULL,
  INSTALLMENT VARCHAR2(2) NOT NULL,
  PRICE VARCHAR2(10) NOT NULL,
  TAX VARCHAR2(10),
  STATUS VARCHAR2(2) NOT NULL,
  ORG_MANAGE_ID VARCHAR2(20),
  ACCUM_CANCEL_PRICE VARCHAR2(10),
  ACCUM_TAX_CANCEL_PRICE VARCHAR2(10),
  IF_DATA VARCHAR2(450) NOT NULL,
  IF_YN VARCHAR2(1) DEFAULT('N'),
  CREATE_ID VARCHAR2(20) NOT NULL,
  CREATE_DATE DATE NOT NULL,
  MODIFY_ID VARCHAR2(20) NOT NULL,
  MODIFY_DATE DATE NOT NULL,
  CONSTRAINT PAYMENT_INFO_PK PRIMARY KEY(MANAGE_ID)
);

#MANAGE_ID -> 관리번호(YYYYMMDD + SEQ)
#CARD_INFO -> 암호화카드정보
#INSTALLMENT -> 할부개월
#PRICE -> 금액
#TAX -> 부가가치세
#STATUS -> 상태값
#ORG_MANAGE_ID -> 취소시 원 관리번호
#ACCUM_CANCEL_PRICE -> 취소누적금액
#ACCUM_TAX_CANCEL_PRICE -> 취소누적부가가치세
#IF_DATA -> 카드사 Interface String 데이터
#IF_YN -> Interface 여부
#CREATE_ID -> 생성자ID
#CREATE_DATE -> 생성일
#MODIFY_ID -> 수정자ID
#MODIFY_DATE -> 수정일

CREATE TABLE PAYMENT_TRANSACTION (
  UNIQUE_ID VARCHAR2(50) NOT NULL,
  CREATE_ID VARCHAR2(20) NOT NULL,
  CREATE_DATE DATE NOT NULL,  
  CONSTRAINT PAYMENT_TRANSACTION_PK PRIMARY KEY(UNIQUE_ID)
);

#UNIQUE_ID -> 유일키값(결제 시 암호화 카드번호/ 취소 시 원 관리번호)
#CREATE_ID -> 생성자ID
#CREATE_DATE -> 생성일

CREATE SEQUENCE SEQ_MANAGE
  START WITH 0
  INCREMENT BY 1
  MINVALUE 0
  MAXVALUE 999999999999
  CYCLE
;

3. 문제해결 전략
-> String 데이터는 거래 건에 I/F 데이터로 명기하여 저장함
-> 카드정보 암/복호화는 AES통해 쌍키로 암호화 선택(실제로는 Public key/ Private key 구현해야 하나 거기까지 별도로 하진 않음)
-> Table 설계 시 원 관리번호 컬럼 추가하여 원 거래건과 연결되게 설계 및 원 거래 건 누적 취소금액, 
   누적 취소부가가치세 컬럼 Update하여 취소금액 확인
-> 제약조건을 만족시키기 위해 거래가 발생 시 UNIQUE_ID(결제 시 암호화 카드번호/ 취소 시 원 관리번호)를 두어 Process 흐름 발생 시
   데이터 Insert 후 프로세스 완료되면 삭제되는 방향으로 진행하여 동시에 두 거래 건이 발생하지 않도록 처리
   원래 HashMap에 담아서 memory 활용하는 생각까지 했으나 서버 다중화 및 Out of memory 문제 염려하여 취소함
   
   
4. Build 및 실행 방법
 (1)프로젝트 받은 후 STS 실행하여(혹은 Eclipse) General -> Projects from Folder or Archive 선택 후
    다운받은 소스의 디렉토리 선택.
 (2)Maven Build 후 src/test/java에서 에러 발생 시 Java 파일 연후 Junit4 추가
 (3)서버는 kakaoApplication.java에서 Run as -> Java application 또는 Spring boot app으로 실행
 (4)Test Case 실행은 Run as -> JUnit Test 실행하여 결과 확인
   
