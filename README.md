# Sherpa 프로젝트  
  
## 1. 프로젝트의 목적 및 용도  
Naver_Navi_TeamProject는 한이음ICT에서 진행하는 프로보노 멘토링 프로젝트이다.    
해당 프로젝트는 프로젝트 1, 프로젝트 2, 프로젝트 3으로 진행된다.    
  
  
### 프로젝트 1
프로젝트 1은 한성대학교 진로탐색학점제 이수 및 프로보노 프로젝트를 위한 기반을 다지는 프로젝트이다.    
해당 프로그램은 기본적인 보행자 네비게이션을 구성하며, 길안내를 해주는 애플리케이션이다.    

#### 제작된 기능은 다음과 같다.
1. 메인 화면 인터페이스  
2. 목적지 탐샥 진행 시 경로 그리기  
3. 경로 이동 시 이탈 알림  
4. 경로 이동 시 경로 재탐색  

### 프로젝트 1.5
프로젝트 1.5는 진로탐색학점제 마감 이후 난잡해진 코드를 수정하는 기간이다.  
모드 모듈화, 프로젝트 작업관리 등 프로젝트 1에서 발생한 코드들 부족한 팀원 간의 규칙 등을 개선하는 시간이다.  

#### 개선된 사안은 다음과 같다.
1. 코드 모듈화 -> 각 기능들은 클래스의 `process()`를 통해서만 통신한다.  
2. 네트워크 지연 문제 해결 -> 코루틴 `runBlocking` 기능 이용  
3. 개발 단위는 스토리 단위로 진행 -> 선후 관계를 가지는 기능은 지라 이슈 설명란에 입출력 값을 명시하여 병렬 제작  
4. 넓은 스프린트 -> 시험기간에도 스프린트 기한을 넓게 구성하여, 각자의 일정 속에서 유동적으로 프로젝트를 제작하게 함.  
5. 팀원 간의 역할 분담 -> 모든 팀원이 풀스택을 유지하되, 코드 주석 관리 선정, `git branch&commit` 관리, 스프린트 에픽&스토리 선정으로 역할을 나눠 진행  
6. 프로젝트 진행 과정 수정 -> 방학기한(2024.06.22) 이후에는 서버 관련 개발을 진행하고, 서버 없이 제작가능 한 기능들을 프로젝트 2로 할당하기로 결정.  

### 프로젝트 2  
프로젝트 2는 한성대학교 공학경진대회 참여를 위한 프로젝트이다.  
전체 프로젝트의 서버 외적인 부분을 제작하기로 진행하였다.  
프로젝트 2는 앱의 기능을 본인 스스로 이용할 수 있는 사용자(이후 '본인이용'으로 축약)들을 대상으로 제작되었다.  
  - 이후 보호자와 피보호자 앱은 본인이용 앱에서 일부 기능을 제한(제거)하는 방식으로 제작할 예정이다.  

    
## 2. 프로젝트를 시작하는 방법
## Setting
#### project/local.properties
다음과 같은 내용을 프로젝트 `local.properties`에 추가하세요.  
  \* 각 키 값은 개별적으로 승인 받아야합니다.  
```html
CLIENT_ID="v4625bqfbq"
TMAP_APP_KEY="pYbNXZAC0e2pATcZJ5OFe1n2jyC1wDPwcwUUtIs7"
```

## 3. 저작권 라이선스 정보  
  
  
## 4. 외부 리소스 정보
프로젝트 1은 다음과 같은 외부 리소스를 이용합니다.  
  [Naver Map](https://navermaps.github.io/android-map-sdk/guide-ko/): 네이버 지도를 기반으로 제작  
  [Tmap Open API](https://skopenapi.readme.io/reference/%EC%86%8C%EA%B0%9C): 대중교통 api를 활용  

<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
# PR 양식  
__Pull Request는 다음과 같은 구성으로 작성한다.__    
__제목__    
Add: <구현사항 요약> // Add, Update, Bug ect...

__내용__  
\# :: 작업 주제  
\**SRPA-??**  : <진행 사항> // 추가, 완료, 버그, 수정 중 ect...   


\# :: 구현사항 설명      
<구현사항 설명1> ...  

\# :: 보완할점     
<보완사항, 주의사항>      
  \\* <추가로 알아야할 사항>    

# KDoc 양식
1. 긴 설명이 필요한 경우
```
/**
* 함수 설명
*
* @param 변수타입 설명
* @return 리턴타입 설명
* @example 함수이름
*/
```
2. 약식으로 코드를 설명하는 경우
```
/**
* 함수 설명 [변수]
*/
```
# Convention  [![Static Badge](https://img.shields.io/badge/code_convention-kotlin_docs-8A2BE2)](https://kotlinlang.org/docs/coding-conventions.html#source-file-names)

1. 기본은 Camel Case, 상수는 Scream Snake Case, 백킹 프로퍼티는 _이름 / 클래스는 대문자, 패키지는 소문자 시작
2. 파일 이름은 해당 내용을 설명하는 것으로 * Utiil, Data와 같은 단어 지양!
3. 클래스는 명사/명사구, 메서드는 동사/동사구 * 모호한 단어는 지양!
4. 클래스 내용은 속성/초기화 -> Sub Constructor -> Method -> CO 순
5. 긴 Args List / Chained Call / 쿼리의 경우 들여쓰기
6. 한 줄 코드의 경우 중괄호 생략
7. 조건이 3개 이상이면, when / 단순 반복문보다 고차 함수 권장

