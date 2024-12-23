# 📚 **스프링부트 - 날씨 일기 프로젝트**



## 🌟 **소개**

외부 API를 활용해 날씨 데이터를 가져오고 이를 기반으로 일기를 작성하는 프로젝트입니다.



## 💻 **기술 스택**

- **언어 및 프레임워크**:
    - Java 8
    - Spring Boot 2.7.14
- **데이터베이스**:
    - MySQL
- **ORM 및 데이터 관리**:
    - Spring Boot Data JPA
    - Spring Boot JDBC
- **문서화 및 로깅**:
    - Swagger
    - Logback
- **버전 관리**:
    - Git



## 💡 **주요 기능**

1. **일기 CRUD**
    - 사용자가 날씨 데이터를 기반으로 일기를 작성, 수정, 조회, 삭제할 수 있습니다.

2. **외부 API 연동**
    - 외부 날씨 API를 통해 실시간 날씨 데이터를 가져옵니다.

3. **스케줄러 활용**
    - 매일 새벽 1시에 날씨 데이터를 자동으로 DB에 저장합니다.

4. **로그 관리**
    - Logback을 활용하여 시스템 동작 로그를 기록합니다.

5. **API 문서화**
    - Swagger를 통해 API 명세서를 제공합니다.



## 🖼 **ERD**
![image](https://github.com/user-attachments/assets/774b94f1-c35e-4e1a-b2fa-cad835264d62)




## 🗂 **API 명세서**

### **1. 일기 작성**

- **Description**:  
  입력된 날짜와 내용을 기반으로 새로운 일기를 작성합니다.



### **2. 일기 조회**

#### **단일 일기 조회**
- **Description**:  
  특정 날짜에 작성된 일기를 조회합니다.

#### **기간 내 일기 조회**
- **Description**:  
  지정된 시작일과 종료일 사이에 작성된 모든 일기를 조회합니다.



### **3. 일기 수정**

- **Description**:  
  특정 날짜에 작성된 일기의 내용을 수정합니다.



### **4. 일기 삭제**

- **Description**:  
  특정 날짜에 작성된 일기를 삭제합니다.
