기존 대비 개선 사항

<div>
  <table>
  <tr>
    <td colspan="2", align=center>게시글 중복 클릭 개선</td>
  </tr>
  <tr>
    <td width=50%, align=center><image src="https://github.com/user-attachments/assets/24cb458b-ca2b-43bf-965d-8df234e9bcae"></image></td>
    <td width=50%, align=center><image src="https://github.com/user-attachments/assets/c73cffdf-c1da-4dfa-af2e-a7c8268f65dd"></image></td>
  </tr>
  <tr>
    <td align=center>본 프로젝트</td>
    <td align=center>기존 당근마켓 애플리케이션</td>
  </tr>
  <tr>
    <td align=center>본 프로젝트는 별도의 StateFlow를 추가하여,<br>한 게시글만 조회하도록 개선하였습니다.<br><a href="https://github.com/jhw010406/carrot-market-clone-frontend/blob/master/app/src/main/java/com/example/tradingapp/viewmodel/clicklistener/MainNavGraph.kt">viewmodel/clicklistener/MainNavGraph.kt</a></td>
    <td align=center>기존 애플리케이션에선 게시글 중복 클릭 시,<br>선택한 모든 게시글을 조회합니다.</td>
  </tr>
</table>

<table>
  <tr>
    <td colspan="2", align=center>홈 화면 글쓰기 버튼 및 하단 네비게이션 아이템 중복 클릭 개선</td>
  </tr>
  <tr>
    <td width=50%, align=center><image src="https://github.com/user-attachments/assets/24cb458b-ca2b-43bf-965d-8df234e9bcae"></image></td>
    <td width=50%, align=center><image src="https://github.com/user-attachments/assets/c73cffdf-c1da-4dfa-af2e-a7c8268f65dd"></image></td>
  </tr>
  <tr>
    <td align=center>본 프로젝트</td>
    <td align=center>기존 당근마켓 애플리케이션</td>
  </tr>
  <tr>
    <td align=center>별도의 StateFlow를 추가하여,<br>둘 중 하나만 동작하도록 개선하였습니다.<br>(line 71 in HomeGraph.kt)<br><a href="https://github.com/jhw010406/carrot-market-clone-frontend/blob/master/app/src/main/java/com/example/tradingapp/viewmodel/clicklistener/MainNavGraph.kt">viewmodel/clicklistener/MainNavGraph.kt</a></td>
    <td align=center>홈 화면에서 글쓰기 버튼과 하단 네비게이션 아이템 동시 클릭 시,<br>두가지의 작업이 동시에 수행됩니다.</td>
  </tr>
</table>

<table>
  <tr>
    <td colspan="2", align=center>홈 화면 글쓰기 버튼 애니메이션 개선</td>
  </tr>
  <tr>
    <td width=50%, align=center><image src="https://github.com/user-attachments/assets/24cb458b-ca2b-43bf-965d-8df234e9bcae"></image></td>
    <td width=50%, align=center><image src="https://github.com/user-attachments/assets/c73cffdf-c1da-4dfa-af2e-a7c8268f65dd"></image></td>
  </tr>
  <tr>
    <td align=center>본 프로젝트</td>
    <td align=center>기존 당근마켓 애플리케이션</td>
  </tr>
  <tr>
    <td align=center>별도의 StateFlow를 추가하여,<br>버튼을 새로 추가하지 않고 애니메이션이 진행될 수 있도록 개선하였습니다.<br>(line 71 in HomeGraph.kt)<br><a href="https://github.com/jhw010406/carrot-market-clone-frontend/blob/master/app/src/main/java/com/example/tradingapp/viewmodel/clicklistener/MainNavGraph.kt">viewmodel/clicklistener/MainNavGraph.kt</a></td>
    <td align=center>홈 화면의 글쓰기 버튼을 클릭 시,<br>홈 화면 위에 새로운 view와 버튼을 덧붙여 애니메이션을 구현함으로써 서로 다른 두 개의 버튼이 겹쳐보이는 모습을 보입니다.</td>
  </tr>
</table>
</div>
