# [ JAVA Network Base Framework ]
## 정의
~~~
Netty 기반 네트워크 프로그램 제작을 위한 프레임워크
~~~
  
## 코드 구조
<img width="1754" alt="image" src="https://user-images.githubusercontent.com/37236920/179636651-6127a220-d381-47c0-ba0b-f6052f283f82.png">
  
## 관계도
<img width="976" alt="image" src="https://user-images.githubusercontent.com/37236920/179636737-b30721b7-94c5-4572-a232-310a21619741.png">
  
## 개념 설명
~~~
1) GroupSocket
  - 멀티캐스팅과 로컬 리슨 소켓을 한번에 편리하게 관리한다.

2) SocketManager
  - GroupSocket 을 관리한다.
  - Network Interface 를 관리한다.

3) Socket
  - 네트워크 통신 식별자인 Session ID 를 가짐으로써 외부에서 어떤 네트워크 자원을 사용할 것인지 알 수 있다.
  - NettyChannel 을 관리한다.

4) NetInterface
  - 특정 네트워크 인터페이스를 설정하여 해당 설정에 의해 모든 네트워크 리소스가 관리되도록 한다.

5) DestinationRecord
  - 메세지를 보낼 타겟 정보를 관리한다.

6) GroupEndpointId
  - 엔드포인트 주소와 이 주소를 가지는 소켓에서 필터링할 주소를 설정한다.

7) NettyChannel
  - L4 프로토콜(TCP, UDP)에 따라 열고 싶은 채널을 설정할 수 있다.
  - 해당 클래스 객체로 인해 네트워크 통신을 할 수 있다.
~~~
  
