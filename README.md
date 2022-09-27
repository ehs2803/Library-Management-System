# Library-Management-System

배포주소 : [http://54.180.182.109](http://54.180.182.109/)


작품 주제 : 스프링부트를 이용한 도서관 관리 시스템

개발기간 : 2022.06.20 ~ 2022.09.25

# **작품 구성도**

1. 시스템 구성도

![image](docs/images/struc.jpg)

2. CI/CD 구성도

![Untitled](docs/images/cicd.jpg)

# 데이터베이스

![image](docs/images/database.jpg)

- member : 사용자 정보 저장
- book : 책 정보
- book_imt : 책 이미지 정보
- book_interest : 관심도서 등록 정보
- book_reservation : 도서예약 정보
- book_hope : 희망도서 신청 정보
- loan : 도서 대출정보
- loan_wait_list : 대출 대기 도서 저장
- studyroom : 스터디룸 정보
- study_room_reservation : 스터디룸 예약 정보
- sanction : 도서 대출, 스터디룸 예약 제재 저장
- notice : 공지사항 정보
- hit_per_day : 일별 방문횟수 저장

# 기능


## 도서관 이용고객 

### - 계정기능
회원가입과 로그인을 할 수 있습니다. 마이페이지에서 회원정보를 수정할 수 있습니다.
### - 자료검색
도서관에서 소장중인 도서를 책이름, ISBN, 저자, 출판사, 출판년도를 입력해 검색할 수 있습니다.
### - 도서 예약
대출 중인 도서에 대해 예약신청을 할 수 있습니다.
### - 관심도서 추가
관심도서를 추가해 마이페이지에서 확인할 수 있습니다.
### - 네이버 검색 API 도서 검색 
네이버 검색 API를 통해서 도서 이름으로 도서를 검색하고 정보를 얻을 수 있습니다.
### - 희망도서 신청
회원가입과 로그인을 할 수 있습니다.
### - 스터디룸 예약
스터디룸을 예약할 수 있습니다.
### - 마이페이지
대출 중 도서(반납연장 기능), 연체 도서, 예약 도서, 관심도서(나의서재), 희망도서 신청, 대출 이력, 스터디룸 예약 현황, 제재 내역을 확인할 수 있습니다.

### - 공지사항 확인 
공지사항을 확인할 수 있습니다.


## 도서관 직원

### - 계정기능
회원가입과 로그인을 할 수 있습니다.
### - 새로운 책 입고
도서를 등록할 수 있습니다.
### - 희망도서 신청 관리
사용자가 신청한 희망도서 신청을 처리할 수 있습니다.
### - 대출, 반납
일반유저에게 도서 대출, 반납, 분실 처리를 할 수 있습니다.
### - 스터디룸 예약 관리
스터디룸 등록, 수정, 삭제. 스터디룸 예약 처리를 할 수 있습니다.
### - 공지사항 등록
공지사항을 등록, 수정, 삭제할 수 있습니다.

<br>
<br>


# 동작화면

## **메인화면**

![image](https://user-images.githubusercontent.com/65898555/192407233-a1ed8553-a29d-4dcd-aa57-b9179102a041.png)

<br>

## **기능 - 계정기능**


![image](https://user-images.githubusercontent.com/65898555/192407268-11066d3a-9587-48c6-9fb3-dc9ef4956833.png)
![image](https://user-images.githubusercontent.com/65898555/192407288-e684751c-bfa5-419c-bb5c-99705693ea44.png)

회원가입 시 일반사용자, 도서관 직원 중 하나를 선택해 회원가입을 할 수 있습니다.

주소는 필수 입력, 전화번호는 선택 입력값입니다.

<br>

## 자료검색(일반 사용자)

![image](https://user-images.githubusercontent.com/65898555/192407678-97c49b58-9eef-43fd-8324-482d4fd9de97.png)

소장중인 도서를 책이름, ISBN, 저자, 출판사, 출판년도를 입력해 검색할 수 있습니다.

<br>

## 도서 예약, 관심도서 추가(일반 사용자)

![image](https://user-images.githubusercontent.com/65898555/192410528-d9a5bec7-5d68-496e-9339-630ff94975bd.png)

자료검색 후 해당 도서의 이름을 클릭하면 상세보기 페이지로 이동합니다. 상세보기 페이지에서는 해당 도서의 정보를 확인할 수 있습니다.

해당 도서가 대출 중이면 도서 예약이 가능합니다.

관심도서담기 버튼을 클릭하면 마이페이지에서 확인이 가능합니다.

<br>

## 네이버 검색 API 도서 검색 (일반 사용자)

![image](https://user-images.githubusercontent.com/65898555/192407773-75cf6a65-7513-47ed-80b9-5b9f235cb135.png)

네이버 검색 API를 통해 도서를 검색할 수 있습니다.

<br>

## 희망도서 신청(일반 사용자)

![image](https://user-images.githubusercontent.com/65898555/192407824-1c5308ba-0984-47c9-86de-deb76692ca6d.png)
![image](https://user-images.githubusercontent.com/65898555/192407847-8e3b773c-ca95-40a2-96f7-e60fabefcbe1.png)
![image](https://user-images.githubusercontent.com/65898555/192407870-a38a8b75-9279-4585-ae1f-2cd464129ab9.png)

희망도서를 신청할 수 있습니다.

사용자가 직접 정보를 입력해 신청할 수 있고, 네이버 검색 API를 통해 '희망도서 신청하기' 버튼을 클릭하면 책이름, ISBN, 저자, 출판사, 출판년도가 자동으로 입력되 사용자가 직접 입력하지 않고 희망도서 신청이 가능합니다.

<br>

## 스터디룸 예약(일반 사용자)

![image](https://user-images.githubusercontent.com/65898555/192408034-49eca67f-c9c9-4d57-8889-7e79fc7eee55.png)
![image](https://user-images.githubusercontent.com/65898555/192407906-91f17e44-1421-43d0-80c8-e376792f1ec6.png)

스터디룸을 예약할 수 있습니다. 예약 내역은 마이페이지에서 확인 가능합니다.

<br>

## 마이페이지(일반 사용자)

![image](https://user-images.githubusercontent.com/65898555/192407649-67751922-5ec9-4fad-96ed-fdeff952d6f5.png)
![image](https://user-images.githubusercontent.com/65898555/192409230-1e39a9d0-e898-431c-a059-950413242aa4.png)

일반 사용자 마이페이지에서는 사용자 정보를 수정할 수 있습니다.

[대출중인 도서] : 대출 중인 도서 조회. 반납 기한 연장 기능.

[연체중인 도서] : 연체 중인 도서 조회.

[예약중인 도서] : 예약한 도서 조회. 예약 취소 기능.

[나의서재] : 관심도서 조회. 관심도서 삭제.

[희망도서 신청] : 신청한 희망도서 신청 내역 조회. 진행상황 상세 조회.

[대출 이력] : 대출 이력 조회.

[스터리룸 예약 현황] : 스터디룸 예약 조회. 예약 취소.

[제재 내역] : 도서 연체, 스터디룸 예약 후 노쇼로 인한 제재 내역 조회.

<br>

## 각종 정보 확인(일반 사용자)
![image](https://user-images.githubusercontent.com/65898555/192408979-7a84e594-41b1-4d53-ae50-deee9d7e4d2c.png)
![image](https://user-images.githubusercontent.com/65898555/192409066-d9bb2189-1dd7-4c3b-a6d9-d6ed7e8edc46.png)

공지사항, 도서관 위치, 정책 등을 확인할 수 있습니다.

<br>

## 새로운 책 등록(도서관 직원)
![image](https://user-images.githubusercontent.com/65898555/192408489-2465e3b8-85d8-4c27-9d5c-5f295698084e.png)

새로운 책을 등록하기 위해서는 이미지를 함께 등록해야 합니다. 같은 ISBN을 가진 도서를 등록할 수 있습니다. 하지만 등록번호, 청구기호는 유일해야 합니다.

<br>

## 대출, 반납(도서관 직원)

![image](https://user-images.githubusercontent.com/65898555/192408671-ecb87cb0-2ea1-4620-a068-4aacec04f161.png)

![image](https://user-images.githubusercontent.com/65898555/192408408-03856d4e-0f73-46dd-9e6d-c33ee66e2d48.png)

대출, 반납을 원하는 일반 사용자를 검색해 해당 이메일을 클릭합니다.

대출하고 싶은 도서를 대출대기 버튼을 클릭해 대출 대기 구역으로 이동시키고, 대출하기 버튼을 클릭하면 대출처리 됩니다.

반납 버튼을 클릭하면 반납처리, 분실 버튼을 클릭하면 해당 도서는 분실 처리가 됩니다.

<br>

## 희망도서 신청 관리(도서관 직원)
![image](https://user-images.githubusercontent.com/65898555/192408587-3179c3f2-1849-4f3a-aa50-cee65446abc7.png)
![image](https://user-images.githubusercontent.com/65898555/192408616-bc98bf5d-e28e-4f0e-a208-244cdb9c7d93.png)

일반 사용자가 신청한 희망도서 신청을 처리합니다. 일반 사용자는 마이페이지를 통해 희망도서 신청 진행상황을 확인할 수 있습니다. 

<br>


## 스터디룸 예약 관리(도서관 직원)
![image](https://user-images.githubusercontent.com/65898555/192408710-d11c1443-be73-4940-92b7-bf32f0c943a5.png)
![image](https://user-images.githubusercontent.com/65898555/192408754-4a84d7dd-36af-4492-8e37-00d071eeb654.png)
![image](https://user-images.githubusercontent.com/65898555/192408776-1fded330-2a63-489f-b7b0-cbc0b2ac2624.png)

[스터디룸 관리] : 스터디룸 등록, 수정, 사용 중단, 사용 재개

[승인 대기중 예약] : 일반 유저가 예약에 대해서 승인, 거절 기능

[입실 대기] : 승인 완료된 예약 조회, 입실처리, 예약취소처리, no-show 처리 기능

[사용 중 스터디룸] : 입실처리 된 예약 조회 및 퇴실 처리 기능

[처리완료 예약] 예약거절, no-show, 퇴실 처리된 예약 조회

<br>

## 공지사항 관리(도서관 직원)

![image](https://user-images.githubusercontent.com/65898555/192408884-06b21dfc-6f5f-4477-8dc0-024039c27528.png)

공지사항을 등록, 수정, 삭제할 수 있습니다. 


