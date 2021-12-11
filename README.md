# Project: 들숨날숨

"들숨날숨"은 한국환경공단 OPEN API로 부터 미세먼지 등 대기환경 정보를 받아서 표현해주는 안드로이드 애플리케이션입니다.

[플레이스토어](https://play.google.com/store/apps/details?id=ch.breatheinandout)

[Github: 미세먼지정보 앱 - 들숨날숨](https://github.com/ChaeHyun/finedust_v1/tree/kotlin)



# 목표

- **REST API**에서 데이터를 받아서 사용하는 방법을 학습합니다.
- **MVVM 패턴**을 사용해서 코드를 분리하기 위해 노력합니다.
- Dagger를 사용해서 **Dependency Injection**을 적용합니다.
- 프로젝트는 모두 **Kotlin**을 이용해서 작성합니다.
- 실제로 동작하는 하나의 앱을 처음부터 끝까지 완성시켜봅니다.



# 아키텍처

![img](https://blog.kakaocdn.net/dn/bOxutZ/btrnCPn64C8/ZlGuVRSqwVlJbwoHrSFysK/img.png)

- **View Layer** : 사용자에게 보여지는 화면을 구성하는 로직을 처리합니다.
  - ViewModel은 Activity와 Fragment가 뷰를 그리는데 필요한 데이터를 가져오고 보관하는 역할을 합니다.
- **Domain Layer** : 비즈니스 로직을 처리합니다.
  - 필요한 데이터를 데이터 레이어로부터 제공받습니다.
  - 로직 처리 후 결과물을 ViewModel에게 반환합니다.
  - 최대한 Pure-kotlin 코드만을 사용합니다.
- **Data Layer** : 도메인의 요청을 받고, 데이터 소스를 사용하여 요청한 데이터를 입출력합니다.
  - 서로 다른 데이터 소스는 별도의 데이터 모델을 사용하여 처리합니다.



레이어를 통해 역할을 분담시키고, Loosely-coupled 형태로 의사소통하도록 디자인합니다.

약한 결합의 디자인은 팀 단위로 일을 분배할 때도 효과적이며, 유연하게 코드를 수정할 수 있어서 효과적인 방법입니다.



# 실행화면

![img](https://blog.kakaocdn.net/dn/EVVv1/btrnC21Yuqz/aFYKtxDSfhb0bEiHMUbjN1/img.png)

![img](https://blog.kakaocdn.net/dn/br3ML2/btrnBhFSrSy/HIUx9eDbdU6KQtMIFEDIN1/img.png)



# 사용 라이브러리

- Retrofit, OkHttp : REST API와 통신하기 위해 사용합니다.
- Dagger-Hilt : Dependency Injection을 위해 사용합니다.
- Room : 로컬에 데이터를 저장하기 위해서 Room Database를 사용합니다.
- Coroutine : 비동기 동작을 구현하기 위해서 코루틴을 사용합니다.
- Navigation Component : 화면이동을 위해서 Jetpack Navigation을 사용합니다.
- Glide : 이미지 로딩을 위해서 사용합니다.
- Play-services-location : 위치정보 획득을 위해 사용합니다.