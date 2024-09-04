# 안드로이드앱을 이용한 전열교환기 모니터링 및 무선 제어 (졸업작품)

<br>

## [소개]
전열교환기와 센서, 서버, 앱을 연결하여 켜두기만 하면 평균 실내 대기질 수치를 아두이노 센서로 확인하여 각각 일정 수치가 넘어가면 자동으로 전열교환기가 세기를 조절하여 대기질 수치를 낮추도록 하였습니다.
앱으로 전열교환기 수동 제어가 가능하며, 앱에서 현재 센서값들을 실시간으로 확인 가능한 그래프도 볼수 있도록 제작하습니다.

<br>


개발기간 : 2022.03 ~ 2022.11

<br>

## [팀 소개 및 역할]

#### 김동찬 [전자공학과] (서버, 클라이언트)
- AndroidStudio를 이용하여 클라이언트단을 만들고, 서버(Node.js)와 클라이언트(AndroidStudio) 데이터 송수신 파트를 담당하였습니다. 


#### 이석원 [전자공학과] (서버, 데이터베이스) 
- 서버(Node.js), 데이터베이스(MySql), 아두이노 IDE 데이터 송수신 파트를 담당하였습니다.


#### 박상현 [전자공학과] (전열교환기, 아두이노)
- 서버(Node.js), 전열교환기, 아두이노 IDE 통신 연결 파트를 담당하였습니다. 

<br>


## [목표]

- 전력낭비 개선을 위해 전열교환기에 네개의 아두이노 센서들을 장착하여 공기질 파악 후 자동으로 세기를 조절하도록 합니다.

- 사용자의 편의성을 위해 휴대폰으로 무선으로 제어가 가능하며, 실시간 공기질의 미세먼지, 온도, 습도값을 그래프로도 확인 가능한 리모콘 앱을 제작합니다.

<br> 

## [흐름도]

![image](https://github.com/user-attachments/assets/4228f0c4-93dc-4aad-b8a6-cf37e55dd707)


<br>

# [화면단]
