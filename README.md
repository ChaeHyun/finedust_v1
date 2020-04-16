# 개인프로젝트 - 들숨날숨
들숨날숨은 한국환경공단 OPEN API로부터 미세먼지 등 대기환경 정보를 받아서 표현해주는 애플리케이션입니다.

[플레이스토어 링크](https://play.google.com/store/apps/details?id=ch.breatheinandout)

[개인 블로그](https://saucecode.tistory.com/)

## 개발목적

+ REST API로부터 데이터를 받아서 사용하는 방법을 배웁니다.
+ Presenter(MVP패턴)를 사용하여 코드를 분리하기위해 노력합니다.
+ 실제로 동작하는 하나의 앱을 스스로 완성시켜봅니다.



## 아키텍쳐

![아키텍쳐](https://k.kakaocdn.net/dn/uMXcd/btqDmnjfv8J/4pezv9JlJYW8km91BKxAn1/img.png)



### Model

한국환경공단에서 제공하는 JSON파일에 맞는 POJO(Plain Old Java Object) 클래스를 구성한다.

+ 미세먼지 값을 저장하는 AirCondition
+ 미세먼지 관측소를 저장하는 Station
+ 주소 검색을 위해 사용하는 Addresses
+ 좌표값 저장 및 변환을 위해 사용하는 GpsData
+ 예보정보를 저장하는 Forecast

등으로 이루어져 있습니다.



### View

View는 사용자에게 보여지는 화면에 관한 클래스들을 담고 있습니다.

MainActivity, AirConditionFragment, ForecastFragment, SearchAddressFragment 등이 해당 패키지에 속합니다.

사용자에게 안드로이드 디바이스의 권한을 얻는 작업 외의 <u>통신관련 로직은 모두 Presenter를 통해서 수행합니다.</u>



### Presenter

View 이외의 로직을 전담합니다. **각 View마다 대응되는 Presenter가 존재**하고, View가 요청하는 작업을 수행해서 결과값을 View에게 넘겨주는 역할을 합니다.

Fragment으로부터 호출을 받으면 로직을 수행한 후 View의 interface를 호출하여 결과값을 View로 전달합니다.


![Logic](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2F4T6DJ%2FbtqDlOORKK3%2FECgRYpcPepO2UQJQKMTaN0%2Fimg.png)



## 실행화면

![전체 실행화면](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2FbI6zLY%2FbtqDmmLky7s%2FXyHb5Htp2GT8GpcEit63aK%2Fimg.png)

![위치저장 및 선택의 접근방법 다양화](https://k.kakaocdn.net/dn/cu9WAT/btqDk5pZC7N/RVJn5x8i0CyyFgDa02Swhk/img.png)

![사용자 피드백을 통해 업데이트한 기능](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2FA5ndy%2FbtqDlOO8TOA%2FElco0dMIQ4RScljj68AEI1%2Fimg.png)


## 배운 점

자체 서버없이 환경공단의 서버에서 일방적으로 데이터를 가지고 오는 환경이라서 통신하는데 어려움을 겪었습니다.

예를들어 현재위치에 대한 미세먼지 정보를 가져오기 위해서는 

+ 디바이스에서 좌표정보를 얻음
+ 좌표변환을 위한 통신 1회
+ 현재좌표를 통해 근접 측정소 정보를 가져오는 통신 1회
+ 해당 측정소명을 파라미터로 미세먼지 정보를 얻어오는 통신 1회

총 4단계를 거쳐야 현재 위치에 맞는 측정소의 미세먼지 값을 읽어올 수 있었습니다.

만약 서버개발자와 함께 협업할 수 있는 상황이었다면, 위 4단계의 파라미터를 복합적으로 전송하여 단 1회만에 원하는 정보를 얻을 수 있도록 개선할 수 있었을 것이라 생각합니다.


그리고 사용자들의 피드백을 받아보면 다양한 요구사항을 충족시키기가 쉽지 않았습니다. 여러 사람들이 동시에 편하게 여기는 UI/UX를 지원하는것이 굉장히 어려운 일이라는 것을 배울수 있었습니다.


그럼에도 서버와 통신해서 JSON값을 변환하고 사용하는 과정을 배울 수 있어서 너무 유익했고,

또 평소에 사용하던 View 컴포넌트들을 벗어나 편리한 UI를 제공하기 위해 ViewPager, Dialog, Font 등 여러가지 컴포넌트를 사용할 수 있어서 배움에 큰 도움이 된 프로젝트였습니다.


## 아쉬운 점

여러가지 기능이 추가될 수록 하나의 파일에 코드량이 너무 많아져서 유지보수하는데 어려움을 겪었습니다.

그래서 **디자인패턴의 필요성**을 크게 체감했고, Dagger와 같은 **DI 시스템**을 사용하는 필요성에 대해 이해할 수 있었습니다.


## GitHub

[Github link : 프로젝트 : 미세먼지정보 앱 들숨날숨](https://github.com/ChaeHyun/finedust_v1)



