# 편하게 스크럼 미팅을 관리하는 서비스 - 쓱크럼

# 1️⃣ **프로젝트 개요**

### **개발 기간**

| 개발기간 | 2023.12.31 ~ 진행중 |
| -------- | ----------------------------- |

<br>

| **[박기현](https://github.com/qkrrlgus114)** | **[최규호](https://github.com/Eungae-D)** |
| :----------------------------------------: | :--------------------------------------: |
| ![박기현](https://avatars.githubusercontent.com/u/121294224?v=4&s=120) | ![최규호](https://avatars.githubusercontent.com/u/135101171?v=4&s=120) |
|                   Backend                  |                  Backend                 |


### 역할 분담
**Backend**

- 박기현 : DB 설계 및 구축, 인프라, 백엔드 API(유저, 스크럼)
- 최규호 : DB 설계, 피그마, 백엔드 API(팀), 스웨거

### **기획 배경**

```
시간을 정해도 늘어지게 되는 스크럼 미팅에 불편함을 느껴 직접 관리 서비스를 제작하였습니다.

- 팀 단위별로 관리 가능한 스크럼 미팅
- 정해진 시간에 끝마치는 스크럼 미팅
- 스크럼 미팅의 히스토리를 관리
```

### **목표**

```
팀 단위별로 스크럼 미팅을 관리하고, 제 시간에 맞춰 진행할 수 있게 만드는 것이 목표입니다.
```

<hr>

# 2️⃣ **서비스 소개**

## 모두의 스크럼 관리 서비스 - 쓱크럼

### 1. 팀 단위로 관리
- **팀 생성 및 유저 초대**: 팀을 생성하고 유저를 초대하여 팀원으로 등록합니다.

### 2. 스크럼 미팅 (현재 기능 추가 중)
- **스크럼 미팅 생성**: 스크럼 미팅을 생성하여 팀원들과 진행합니다. 카메라와 채팅 기능을 지원합니다.
- **시간 설정**: 스크럼 미팅 시간을 설정하여 효율적인 시간 관리를 지원합니다.
- **채팅 기록**: 스크럼 채팅 기록을 통해 미팅 내용을 관리합니다.
- **음성 인식(STT)**: STT(Speech to Text) 기능을 통해 목소리를 인식하여 기록으로 저장 및 관리합니다.


<hr>

# 3️⃣ **개발 환경**

## ⚙ Management Tool

- 형상 관리 : GitHub
- 커뮤니케이션 : Mattermost, Notion, Discord, Kakao
- 디자인 : Figma

<br>

## 💻 IDE

- Visual Studio Code
- Intellij CE 2023.1.3

<br>

## 📁 Backend

- Springboot `3.20`
- Lombok
- Spring Data JPA
- Spring Web
- Spring Security
- JWT
- org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0
- Spring-oauth2-client

## 💾 Database
- MySQL
- Redis

## 🌁 Infra

- Jenkins 2.401.3
- docker
- SSL
- CertBot
- Nginx

## 🎞 Storage
- Amazon S3

## 외부 API
- Kakao Developer API

<hr>

# 4️⃣**주요 기술**

- KAKAO API

  - 카카오 로그인(Security Oauth2를 사용하여 BackEnd에서 로그인 로직 구현 / 클라이언트 <-> 백엔드 <-> 카카오서버 )

- JWT
  - 토큰 인증 방식을 사용하여 서버의 부담을 줄이고 확장성을 챙김.
  - 토큰 만료 되었을 시, refreshToken을 사용하여 새롭게 인증 토큰을 받음.

<hr>
<br><br>

<br><br>
