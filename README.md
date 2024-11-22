<table>
  <tr>
    <td colspan="2">게시글 중복 클릭 개선</td>
  </tr>
  <tr>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td colspan="1">본 프로젝트</td>
    <td colspan="1">기존 당근마켓 애플리케이션</td>
  </tr>
  <tr>
    <td>본 프로젝트는 별도의 StateFlow를 추가하여,한 게시글만 조회하도록 개선하였습니다. (viewmodel.clicklistener.MainNavGraph.kt)</td>
    <td>기존 애플리케이션에선 게시글 중복 클릭 시, 선택한 모든 게시글을 조회합니다.</td>
  </tr>
</table>
기존 대비 개선 사항

![게시글 중복 클릭](https://github.com/user-attachments/assets/11f30405-1f57-4c6b-9b79-a08bdbf4e86f)
기존 애플리케이션에선 게시글 중복 클릭 시, 선택한 모든 게시글을 조회합니다.
본 프로젝트는 별도의 StateFlow를 추가하여,한 게시글만 조회하도록 개선하였습니다. (viewmodel.clicklistener.MainNavGraph.kt)
(좌) 본 프로젝트 / (우) 기존 당근마켓 애플리케이션


기존 애플리케이션에선 홈 화면에서 글쓰기 버튼과 하단 네비게이션 아이템 동시 클릭 시, 선택한 모든 게시글을 조회합니다.
본 프로젝트는 별도의 StateFlow를 추가하여, 둘 중 하나만 동작하도록 개선하였습니다. (line 71 in HomeGraph.kt)
![글쓰기 버튼과 네비게이션 동시 클릭](https://github.com/user-attachments/assets/75fdd030-f18d-4bfc-b1b9-91372be25829)
(좌) 본 프로젝트 / (우) 기존 당근마켓 애플리케이션

기존 애플리케이션에선 홈 화면의 글쓰기 버튼을 클릭 시,
홈 화면 위에 새로운 view와 버튼을 덧붙여 애니메이션을 구현함으로써 서로 다른 두 개의 버튼이 겹쳐보이는 모습을 보입니다.
본 프로젝트는 별도의 StateFlow를 추가하여, 버튼을 새로 추가하지 않고 애니메이션이 진행될 수 있도록 개선하였습니다. (line 71 in HomeGraph.kt)
![글쓰기 버튼 애니메이션](https://github.com/user-attachments/assets/b0062e5c-1194-416e-ad17-a0298007144c)
(좌) 본 프로젝트 / (우) 기존 당근마켓 애플리케이션
