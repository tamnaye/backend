
<div align=center>
  
## '더큰내일센터 온라인 회의실 예약 시스템' 프로젝트
</div>

<br/>

### 📌 목차 
1. [프로젝트 배경](https://github.com/m1naworld/Tamna-ConferenceRoom_reservation_system#1-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%B0%B0%EA%B2%BD)
2. [프로젝트 소개](https://github.com/m1naworld/Tamna-ConferenceRoom_reservation_system#2-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%86%8C%EA%B0%9C)
3. [기술스택](https://github.com/m1naworld/Tamna-ConferenceRoom_reservation_system#3-%EA%B8%B0%EC%88%A0%EC%8A%A4%ED%83%9D)
4. [DB구조](https://github.com/m1naworld/Tamna-ConferenceRoom_reservation_system#4-db-%EA%B5%AC%EC%A1%B0)
5. [페이지별 구현기능](https://github.com/m1naworld/Tamna-ConferenceRoom_reservation_system#5-%ED%8E%98%EC%9D%B4%EC%A7%80%EB%B3%84-%EA%B5%AC%ED%98%84%EA%B8%B0%EB%8A%A5)
6. [개발 및 기획](https://github.com/m1naworld/Tamna-ConferenceRoom_reservation_system#6-%EA%B0%9C%EB%B0%9C-%EB%B0%8F-%EA%B8%B0%ED%9A%8D)

<br/>

## 1. 프로젝트 배경
<div align=center>
  
### 현재 더큰내일센터(이하 센터) 내 회의실 예약 시스템 현황

<br/>
</div>

| 수기 작성 | LMS(센터 내 웹사이트) 장소대관 페이지|
|:------:|:------------:|
|<img width="425" alt="스크린샷 2022-09-12 오후 9 52 30" src="https://user-images.githubusercontent.com/85235063/189785872-15dd59a3-fbaf-4da6-adaa-2da3f15a8847.png">|<img width="567" alt="스크린샷 2022-09-12 오후 9 51 03" src="https://user-images.githubusercontent.com/85235063/189785946-8269da48-11f6-4f4e-9696-b3ea04f8e3d1.png">|
 <div align=right>
(*더큰내일센터: 제주의 미래를 위한 프로젝트를 창출할 수 있는 혁신적 인재를 육성하는 기관)
</div>

<br/>

* 수기작성: 직접 회의실들을 돌아다녀야 하는 번거로움과 언제 적었는지 모르는 모호성으로인해 종종 혼란 야기
* LMS: 센터 내 알고 있는 사람이 많지는 않으며 실 사용자 10명, 작성 횟수 15번일 정도로 활용도가 낮음. 또한 한눈에 현재 회의실 현황을 알 수 없어 불편함이 따름

#### 📌 프로젝트 목적 : 실제 센터 내에서 사용할 수 있는 더큰내일센터 회의실 온라인 예약 시스템을 개발한다. 센터 내 상황을 최대한 반영하여 사용하는데 가장 편리한 기능들을 구현한다.

<br/>

## 2. 프로젝트 소개 

- 프로젝트 기간: 2022/08/01 ~ 2022/09/16
- 센터 내 상황을 반영하여 직원(MANAGER)과 교육생(USER)일 때의 권한 차이를 두어 기능의 범위를 다르게 구현
- 편의를 위해 로그인을 LMS와 연동하여 간편로그인으로 구현 및 모바일 화면을 위 반응형 웹 구현 

<br/>


## 3. 기술스택

| React | Springboot | Mariadb | Mybatis | OracleColud | 
|:------:|:------:|:------:|:------:|:------:|
|![React-icon svg](https://user-images.githubusercontent.com/85235063/189798318-2c7fe7d2-b9ea-45a8-a373-8386474da228.png)|![spring](https://user-images.githubusercontent.com/85235063/189798456-35af17d6-c99e-4412-9057-e01c79cf6d9c.png)|![mariadb](https://user-images.githubusercontent.com/85235063/189801497-829ad6eb-1649-446b-968c-073416987b59.png) | ![mybatis](https://user-images.githubusercontent.com/85235063/189801070-15b28cf1-78e7-4e76-9bf1-82f86999a86f.jpeg) | <img width="114" alt="oracle" src="https://user-images.githubusercontent.com/85235063/189800843-0b890118-80b8-4ec6-80af-0ece725a8441.png"> | 

<br/>

## 4. DB 구조 
<img width="567" alt="스크린샷 2022-09-12 오후 9 23 06" src="https://user-images.githubusercontent.com/85235063/189786156-9f12c71c-a7db-4d2e-8577-db439c28b5ec.png">

<br/>

## 5. 페이지별 구현기능
> ### 1. 로그인 및 로그인 유지 기능
> LMS와 연동한 간편 로그인, Security를 활용한 JWT 구현

<img width="567" alt="스크린샷 2022-09-13 오후 3 18 53" src="https://user-images.githubusercontent.com/85235063/189826559-a79dd59d-f698-4af1-a51f-fdc16d55703a.png"> 

<br/>

> ### 2. 메인 페이지
> 교육생 기수마다 사용하는 층수가 달라 사용하는 층수의 회의실만을 보여주고 매니저는 회의실을 관리하므로 모든 층의 회의실을 보여줌
> 
> 회의실의 위치를 파악할 수 있도록 센터 내 구조를 반영, 예약은 당일예약만을 하므로 오전 8시반 ~ 오후 9시에만 활성화

| 최신 기수 | 나머지 기수 | 매니저(직원) |
|:------:|:------:|:------:|
|<img width="284" alt="스크린샷 2022-09-13 오후 3 45 25" src="https://user-images.githubusercontent.com/85235063/189829598-a8957004-7c21-4f16-a7e8-459a4ad85a6f.png">  | <img width="284" alt="스크린샷 2022-09-13 오후 3 36 56" src="https://user-images.githubusercontent.com/85235063/189828022-75e13892-6f25-474b-bee7-6197f8da3b6d.png">| ![화면_기록_2022-09-13_오후_3_51_04_AdobeExpress](https://user-images.githubusercontent.com/85235063/189830918-bb2127b4-7e5f-48b2-862f-1c5f440beb83.gif)|

<br/>
<br/>

> ### 3. 예약 페이지 
> 회의실은 2인이상을 규칙으로함으로 함께 쓸 팀원 검색 및 버튼을 통해 선택 가능
> 
> 회의실 및 개인 자습실 각각의 시간 규칙에 맞게 예약 구현, 현재 시간보다 지난 시간 및 이미 예약된 시간 버튼 비활성화
> 
> 교육생 동시간대 예약불가 기능 구현, 공식일정으로 인해 매니저가 예약하는 경우 우선예약 구현  

| 회의실 | 개인 자습실 |
|:------:|:------:|
|<img width="510" alt="스크린샷 2022-09-13 오후 4 00 28" src="https://user-images.githubusercontent.com/85235063/189855406-07916773-cf6f-4e43-8340-0772fa4cf18f.png"> | <img width="510" alt="스크린샷 2022-09-13 오후 4 02 24" src="https://user-images.githubusercontent.com/85235063/189833070-1c90dd22-96e5-4aaa-9887-40a8a70522fd.png">|


<br/>
<br/>

> ### 4. 마이페이지
> 자신의 예약 현황을 보여줌(선택된 팀원일 경우에도 마이페이지에서 자신이 예약된 예약들을 모두 볼 수 있음)
> 
> 내 예약이 매니저의 공식일정으로 인한 예약과 겹쳤을 경우 사용불가, 시간이 지난 예약은 비활성화, 자신이 예약한 예약만 취소가능

| 교육생 마이페이지| 매니저 마이페이지 |
|:------:|:------:|
| <img width="510" alt="스크린샷 2022-09-13 오후 4 19 55" src="https://user-images.githubusercontent.com/85235063/189836387-a9f24f6c-8333-4c66-b797-338c8f861d18.png"> | <img width="510" alt="스크린샷 2022-09-13 오후 4 10 48" src="https://user-images.githubusercontent.com/85235063/189835817-c8c632c9-5493-494d-8029-056aaf193dbc.png"> |

<br/>
<br/>

> ### 5. 예약 현황페이지 
<img width="510" alt="스크린샷 2022-09-13 오후 4 58 12" src="https://user-images.githubusercontent.com/85235063/189845062-e328f7bf-0ea8-468c-8720-02849613d006.png"> 
<br/>

## 6. 개발 및 기획
- 송민아 [m1naworld](https://github.com/m1naworld)
- 안수빈 [AnSuebin](https://github.com/AnSuebin)
- 이현정 [cchloe0927](https://github.com/cchloe0927)
- 조무결 [mugyeol](https://github.com/mugyeol)

