# 국내 여행 일정 생성 서비스 ‘캐리온’

![최종프로젝트_2팀_발표.jpg](https://github.com/user-attachments/assets/987b5c63-7bdb-4628-b790-b9c8f9610706)



## **프로젝트 소개**

---

<aside>
📌

<캐리온>은 **여행 일정**을 효율적으로 **계획**하고,친구와 **공유**하며,**여행 후기**를 남길 수 있는 **종합 여행 플랫폼**입니다.

</aside>

![carryon_logo_final.png](https://github.com/user-attachments/assets/db9e886b-5896-4aaf-92b4-2367dec69321)

- **프로젝트명** : 국내 여행 계획 어플리케이션 캐리온 (CarryOn)
- **프로젝트 기간** : 2025.02.07 ~ 03.05
- **기획의도** : 여행 일정을 계획하며 친구와 공유, 편집할 수 있고 사용자에게 정확한 국내 여행지 정보를 제공하고자 캐리온을 개발하게되었습니다.

<aside>

## 목표

**✔️ 여행 일정 관리의 간편화**

**:** 사용자가 손쉽게 **여행 일정을 생성**하고, 날짜별/장소별 **일정 관리**가 가능하도록 제공

**✔️ 일정 공유 및 공동 수정 기능 제공**

: 공유 코드를 이용하여 친구와 **여행 일정을 공유 및 편집** 가능

**✔️ 여행 후기 및 자유로운 정보 공유 공간 제공**

: 캐리톡(CarryTalk) **커뮤니티**를 통해 **여행 후기**를 사진과 함께 **공유**할 수 있도록 지원

**✔️ 국내 여행 특화 및 향후 글로벌 확장**

: 정확한 **국내 여행지 정보 제공**

</aside>

### **프로젝트 아키텍처**

![image](https://github.com/user-attachments/assets/2eaaf09b-1e03-476d-9989-2a910a10f3fe)


### 개발 환경

- **Architecture Pattern** : MVVM (Model-View-ViewModel))
- **FrameWork**
    - Android Jetpack (Compose, ViewModel, LiveData, Room, Navigation)
    - Firebase (Firestore, Authentication, Cloud Storage)
- **Third Party Library**
    - 네트워크 통신: Retrofit, OkHttp3
    - 의존성 주입: Hilt
    - 이미지 로딩: Coil
    - JSON 파싱: Gson
    - 지도 서비스: Google Maps API
    - UI 애니메이션: Lottie
    - 상태 관리: StateFlow, LiveData
- **DataBase**
    - Firestore (Cloud Database)
- **Language**
    - Kotlin
- **VersionControl**
    - 브랜치 전략: `main`, `develop`, `feature/기능`
    - 협업 툴: GitHub Issues, Pull Request

## 레퍼런스

---

<aside>
📊

**DB 설계서**

- https://docs.google.com/spreadsheets/d/11xW7QGEtBlWqxMNuQ7tYBlN-ymfP_E1sA-evmzJtfC0/edit?gid=1433313731#gid=1433313731
- ERD : https://dbdiagram.io/d/CarryOn-67a9b2e4263d6cf9a0974ab3
</aside>

<aside>
🧑‍🎨

**디자인**

- https://www.figma.com/design/2btJIpCIFaiRhE1GYcKMZB/%ED%8C%8C%EC%9D%B4%EB%84%90%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8_2%ED%8C%80?node-id=350-16420&t=VhlLZcwhsYpTr8cY-1
</aside>

<aside>
🙋

**요구사항 명세서**

- https://docs.google.com/spreadsheets/d/1tD7FVl9ZmXt9sas6y4ChpS8wdmZQxfB7/edit?gid=1388090367#gid=1388090367
</aside>

## 핵심 기능

---

![image 1](https://github.com/user-attachments/assets/24ad5cc7-98c7-4e50-8d87-6560069cf06f)


![image 2](https://github.com/user-attachments/assets/5156bfa5-cfd3-426c-9997-6b3d706e4093)


![image 3](https://github.com/user-attachments/assets/f5ebe8cf-3dcc-4b7f-8d5c-eef6f54e4f3c)


![image 4](https://github.com/user-attachments/assets/93db3f4a-dd5c-43c4-9495-1acc90482a69)


![image 5](https://github.com/user-attachments/assets/0d4b47f0-e6e9-46d7-8248-3d94ff0469b0)


![image 6](https://github.com/user-attachments/assets/e17c3c25-fceb-441d-bb92-74ff41b4caf2)


![image 7](https://github.com/user-attachments/assets/36259faa-fca0-42e6-8a81-8a553c39d0ff)


![image 8](https://github.com/user-attachments/assets/094f27b8-91c8-4c51-847c-8e252b7ee47b)


![image 9](https://github.com/user-attachments/assets/df425c29-70d1-4212-8278-e44f422052c4)


## 시연영상

---

https://youtu.be/QOMqkSoCrjo

## 추후 개선사항

---

![image 10](https://github.com/user-attachments/assets/3e17cb0c-7af5-40f9-90d4-d7854b367318)


## **GITHUB**

---

- https://github.com/AOS3/FinalProject-CarryOn-Anywhere.git

## 팀원 소개 및 역할

---

| **황서영 (PL)** | **장은혁 (부팀장)** | **김현서 (팀원)** | **김성진 (팀원)** |
| --- | --- | --- | --- |
| [seoyoung31](https://github.com/seoyoung31) | [Jang Eunhyeok](https://github.com/jeh200223) | [14857](https://github.com/AOS3/FinalProject-CarryOn-Anywhere/commits?author=14857) | [sungjin-Kim11](https://github.com/AOS3/FinalProject-CarryOn-Anywhere/commits?author=sungjin-Kim11)  |
| - 프로젝트 계획 및 관리 | - 프로젝트 일정 조율 및 팀원 간 커뮤니케이션 | - 마이페이지 기능 구현
| - 팀 리딩 및 커뮤니케이션 | - 코드 리뷰 진행 | - 사용자 마이페이지 및 찜 화면 디자인
| - 캐리온 여행 계획 기능 디자인 | - 캐리온 메인, 로그인 기능 디자인
| - 로그인 / 회원가입 기능 구현(카카오, authentication) | - 여행 계획 작성 & 공유, 장소 요청하기 기능 구현
| - 메인 화면 구현 | - Google Maps API 사용하여 지도 구현 
| - 한국 관광공사 API 사용하여 장소 검색 기능 및 찜 기능 구현 |- 한국 관광공사 API 사용하여 장소 검색 기능 구현
|  | - 다음 주소 검색 API 사용하여 주소 검색 기능 구현 | 






