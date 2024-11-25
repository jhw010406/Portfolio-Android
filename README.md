<h2>프로젝트 내용</h2>
<ol>
        <li>구현 사항
                <ul>
                        <li>회원가입 / 로그인 / 자동 로그인</li>
                        <li>게시글 생성 / 조회 / 수정 / 삭제
                                <ul>
                                        <li>게시글 생성</li>
                                        <ul>
                                                <li></li>
                                        </ul>
                                </ul>
                                <ul>
                                        <li>게시글 수정</li>
                                        <ul>
                                                <li></li>
                                        </ul>
                                </ul>
                        </li>
                        <li>게시글 찜하기
                                <ul>
                                        <li>다른 사람의 게시글을 찜할 수 있으며, 나의 정보에서 찜한 게시글들을 확인할 수 있습니다.</li>
                                </ul>
                        </li>
                        <li>스플래시 화면
                                <ul>
                                        <li>애플리케이션 첫 시작 시, 애니메이션이 적용된 로고를 보여줍니다.</li>
                                </ul>
                        </li>
                        <li>사용자 테마별 색상 변화</li>
                        <li>기존 애플리케이션 대비 UI/UX 개선</li>
                </ul>
        </li>
        <li>Trouble Shootings</li>
</ol>

### 게시글 수정
https://github.com/user-attachments/assets/30f11751-65ea-472c-9234-68a41adba386


### 스플래시 화면
<table align=center>
        <tr>
                <td width=240 align=center>다크모드 활성화 시</td>
                <td width=240 align=center>다크모드 비활성화 시</td>
        <tr>
                <td align=center><img src="https://github.com/user-attachments/assets/ca7e6eca-dffe-4e61-8985-fb4dfb2970e4" width=100%></td>
                <td align=center><img src="https://github.com/user-attachments/assets/2a43dcb6-9f6d-49af-8278-35f6dbb98899" width=100%></td>
        </tr>
</table>
<br>


### 사용자 테마별 색상 변화
<table align=center>
        <tr>
                <td width=240 align=center>다크모드 활성화 시</td>
                <td width=240 align=center>다크모드 비활성화 시</td>
        </tr>
        <tr>
                <td align=center><img src="https://github.com/user-attachments/assets/ce7957dc-d827-4b38-a18d-7c07ed0600aa" width=100%></td>
                <td align=center><img src="https://github.com/user-attachments/assets/0be60050-c109-4eae-b2be-14ddebdb9a26" width=100%></td>
        </tr>
        <tr>
                <td align=center><img src="https://github.com/user-attachments/assets/6537b806-4db9-4fee-a5eb-6903972d1e40" width=100%></td>
                <td align=center><img src="https://github.com/user-attachments/assets/8c9a2fda-830b-4151-a016-cf8db0c23888" width=100%></td>
        </tr>
</table>
<br>


### 기존 애플리케이션 대비 UI/UX 개선 사항

<table>
        <tr>
          <td colspan="2" align=center><h3>게시글 중복 클릭 개선</h3></td>
        </tr>
        <tr>
          <td width="50%" align=center><image src="https://github.com/user-attachments/assets/89aa024b-fada-4355-ad68-4efc7796de3d" width="100%"></image></td>
          <td width="50%" align=center><image src="https://github.com/user-attachments/assets/0f50e995-87ee-417d-9e41-d08755e9b03a" width="100%"></image></td>
        </tr>
        <tr>
          <td align=center>본 프로젝트</td>
          <td align=center>기존 당근마켓 애플리케이션</td>
        </tr>
        <tr>
          <td align=center>별도의 StateFlow를 추가하여,<br>한 게시글만 조회하도록 개선하였습니다.<br>이를 통해 서버에 게시글 조회를 연속으로 요청하는 것을 방지할 수 있습니다.</td>
          <td align=center>게시글 중복 클릭 시,<br>선택한 모든 게시글을 조회합니다.</td>
        </tr>
</table>

<table>
        <tr>
          <td colspan="2", align=center><h3>홈 화면 글쓰기 버튼 및 하단 네비게이션바 중복 클릭 개선</h3></td>
        </tr>
        <tr>
          <td width="50%" align=center><image src="https://github.com/user-attachments/assets/2220a149-b9fb-4095-8d19-ec9499e5f3da" width="100%"></image></td>
          <td width="50%" align=center><image src="https://github.com/user-attachments/assets/7f7482a4-70a9-4973-8ddb-cd6794bac40d" width="100%"></image></td>
        </tr>
        <tr>
          <td align=center>본 프로젝트</td>
          <td align=center>기존 당근마켓 애플리케이션</td>
        </tr>
        <tr>
          <td align=center>별도의 StateFlow를 추가하여,<br>둘 중 한 작업만 수행되도록 개선하였습니다.</td>
          <td align=center>홈 화면의 글쓰기 버튼과<br>하단 네비게이션 아이템을 동시에 클릭할 시,<br>두 가지의 작업이 함께 수행됩니다.</td>
        </tr>
      </table>

<table>
        <tr>
          <td colspan="2", align=center><h3>홈 화면 글쓰기 버튼 애니메이션 개선</h3></td>
        </tr>
        <tr>
          <td width=50% align=center><image src="https://github.com/user-attachments/assets/8f6bc67c-7dd2-4ab0-8bc1-4b92fd1062a9" width="100%"></image></td>
          <td width=50% align=center><image src="https://github.com/user-attachments/assets/b73d146a-1d02-429b-9aff-45c4c4d95fde" width="100%"></image></td>
        </tr>
        <tr>
          <td align=center>본 프로젝트</td>
          <td align=center>기존 당근마켓 애플리케이션</td>
        </tr>
        <tr>
          <td align=center>별도의 StateFlow를 추가하여 활용함으로써 버튼 애니메이션을 위해 별도의 view 생성을 하지 않았으며,<br>버튼 비활성화 시 반투명 검은색 바탕이 페이드 아웃 되도록 개선하였습니다.</td>
          <td align=center>홈 화면의 글쓰기 버튼을 클릭할 시,<br>홈 화면 위에 새로운 view와 버튼을 덧붙여 애니메이션을 구현함으로써<br>서로 다른 두 개의 버튼이 겹쳐보이는 모습을 보입니다.</td>
        </tr>
</table>

## Trouble Shootings
<ul>
        <li>UI 성능 최적화
                <ul>
                        <li>모든 composable의 애니메이션을 Modifier.graphicsLayer 내부에서만 일어나도록 하였으며, 상태에 따라 노출되던 surface composable들은 Modifier.draw로 재구성하였습니다.</li>
                        <li>위 수정사항들로 특정 composable에서 애니메이션 발생 시, 상위 composable의 Recomposition count를 1회로 현저히 줄어들도록 개선하였습니다.</li>
                </ul>
        </li>
        <li>Splash Screen 중단 문제 개선
                <ul>
                        <li>MainActivity의 모든 구성 준비가 끝날 시, Splash Screen의 애니메이션이 전부 끝나지 않았음에도 중단되는 문제가 있었습니다.</li>
                        <li>splashScreen.setOnExitAnimationListener에서 참조할 수 있는 SplashScreenView의 멤버 변수들을 활용하여 splash screen의 애니메이션이 종료되기 전까지 중단되지 않도록 개선하였습니다.</li>
                </ul>
        </li>
</ul>
<br>
