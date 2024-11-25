<h2>기술 스택</h2>
<p>
        <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white">
        <img src="https://img.shields.io/badge/Android-34A853?style=for-the-badge&logo=android&logoColor=white">
        <img src="https://img.shields.io/badge/Jetpack Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white">
        <img src="https://img.shields.io/badge/Android Studio-34A853?style=for-the-badge&logo=androidstudio&logoColor=white">
        <img src="https://img.shields.io/badge/adobe after effects-9999FF?style=for-the-badge&logo=adobeaftereffects&logoColor=white">
</p>
<br>
본 프로젝트는 MVVM 패턴 + AAC 라이브러리를 기반으로 제작되었으며, API 통신은 Retrofit2를 통해 이뤄졌습니다.<br>
After Effects를 활용하여 splash screen의 아이콘 애니메이션을 구현하였습니다.

<h2>프로젝트 내용</h2>
<ol>
        <li>Architecture</li>
        <br>
        <li>구현 사항
                <ul>
                        <li>JWT 검증 interceptor</li>
                                <ul>
                                        <li>access Token이 만료되어 API로부터 401 응답을 받을 시, 자동으로 헤더에 refresh Token을 포함시켜 API에 JWT 재발급 요청합니다. JWT 재발급 성공 시, access Token과 refresh Token은 새로 갱신됩니다.</li>
                                </ul>
                        <br>
                        <li>회원가입 / 로그인 / 자동 로그인</li>
                                <ul>
                                        <li>ID와 password를 입력하여 계정 생성 / 로그인을 API에 요청합니다. 성공 시, JWT를 발급 받으며 Local에 저장합니다.</li>
                                        <li>애플리케이션 첫 실행 시, Local에 저장되어 있는 계정 정보를 기반하여 로그인을 API에 자동으로 요청합니다. 성공 시 JWT를 발급받으며, 실패시 Local에 저장된 계정 정보가 삭제되고 로그인 화면으로 이동합니다.</li>
                                </ul>
                        <br>
                        <li>화면 전환</li>
                                <ul>
                                        <li>모든 View 전환은 navigation에 의해 수행됩니다.</li>
                                </ul>
                        <br>
                        <li>게시글 생성 / 조회 / 수정 / 삭제
                                <ul>
                                        <li>게시글 조회</li>
                                        <ul>
                                                <li>게시글마다 부여된 고유의 id가 존재하며, 해당 id로 API 게시글 내용 요청을 할 수 있습니다.</li>
                                                <li>게시글 목록 갱신 방법은 Pagenation을 따르며, 새로 갱신된 게시글 목록의 절반 이상을 읽을 시 API에 추가적인 게시글 목록을 자동으로 요청합니다.</li>
                                                <li>삭제된 게시글을 조회 시 요청 거부되며, 게시글 목록에서 해당 게시글을 제거합니다.</li>
                                        </ul>
                                </ul>
                                <ul>
                                        <li>게시글 생성</li>
                                        <ul>
                                                <li>이미지 업로드 시 API로부터 presigned url를 발급받으며, 해당 url로 PUT 요청을 수행함으로써 대용량 이미지(10MB 이상)도 트래픽 문제 없이 업로드 할 수 있습니다.</li>
                                                <li>클라이언트에서 S3으로 이미지 업로드 요청이 성공적으로 수행된 후, 게시글 제목, 내용 등의 text 데이터를 서버에 저장 요청할 수 있도록 하였으므로, 업로드 시간차로 인한 무결성 문제가 존재하지 않습니다.</li>
                                                <li>게시글에 이미지를 첨부한 순서가 저장되며, 게시글 조회 시 해당 순서대로 이미지를 볼 수 있습니다.</li>
                                        </ul>
                                </ul>
                                <ul>
                                        <li>게시글 수정</li>
                                        <ul>
                                                <li>API에 특정 id에 대한 게시글 내용을 응답받은 후, 게시글 생성 View에서 수정할 수 있습니다.</li>
                                                <li>이미지 첨부 / 삭제로 이미지 순서를 수정할 수 있으며, 게시글 조회 시 수정된 순서가 반영됩니다.</li>
                                                <li>게시글 수정 단계에서 변경된 이미지, 텍스트들은 수정 완료가 되기 전까지 최종 게시글 내용에 반영되지 않습니다.</li>
                                                <li>다른 유저의 게시글은 수정할 수 없습니다.</li>
                                        </ul>
                                </ul>
                                <ul>
                                        <li>게시글 삭제</li>
                                        <ul>
                                                <li>다른 유저의 게시글은 삭제할 수 없습니다.</li>
                                        </ul>
                                </ul>
                        </li>
                        <br>
                        <li>게시글 찜하기
                                <ul>
                                        <li>다른 사람의 게시글을 찜하거나 취소할 수 있으며, 나의 정보에서 찜한 게시글들을 확인할 수 있습니다.</li>
                                </ul>
                        </li>
                        <br>
                        <li>스플래시 화면
                                <ul>
                                        <li>애플리케이션 첫 시작 시, 애니메이션이 적용된 로고를 보여줍니다.</li>
                                </ul>
                        </li>
                        <br>
                        <li>사용자 테마별 색상 변화</li>
                        <br>
                        <li>기존 애플리케이션 대비 UI/UX 개선</li>
                </ul>
        </li>
        <br>
        <li>Trouble Shootings</li>
</ol>

<h2>1. Architecture</h2>


<h2>2. 구현 사항</h2>

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
          <td align=center>별도의 StateFlow를 추가함으로써 버튼 애니메이션을 위해 새 composable 생성을 하지 않았으며,<br>버튼 비활성화 시 반투명 검은색 바탕이 페이드 아웃 되도록 개선하였습니다.</td>
          <td align=center>홈 화면의 글쓰기 버튼을 클릭할 시,<br>홈 화면 위에 새로운 composable와 버튼을 덧붙여 애니메이션을 구현함으로써<br>서로 다른 두 개의 버튼이 겹쳐보이는 모습을 보입니다.</td>
        </tr>
</table>

<h2>3. Trouble Shootings</h2>
<ul>
        <li>UI 성능 최적화
                <ul>
                        <li>모든 composable의 애니메이션을 Modifier.graphicsLayer 내부에서만 일어나도록 하였으며, 상태에 따라 노출되던 composable들은 Modifier.draw로 재구성하였습니다.</li>
                        <li>위 수정사항들로 특정 composable에서 애니메이션 발생 시, 상위 composable의 Recomposition count를 1회로 현저히 줄어들도록 개선하였습니다.</li>
                </ul>
        </li>
        <br>
        <li>composable 연속 클릭 방지
                <ul>
                        <li>clickable composable을 빠르게 두 번 이상 클릭 시, 응답이 클릭한 수 만큼 중복으로 발생하는 문제가 있었으며, 다수의 상용 앱에서 해당 문제를 방치 중인 것으로 확인했습니다.</li>
                        <li>본 프로젝트에선 StateFlow를 활용한 별도의 ClickListener를 생성하여, 클릭 요청은 한 번만 받을 수 있도록 개선하였습니다.</li>
                </ul>
        </li>
        <br>
        <li>Splash Screen 중단 문제 개선
                <ul>
                        <li>MainActivity의 모든 구성 준비가 끝날 시, Splash Screen의 애니메이션이 전부 끝나지 않았음에도 중단되는 문제가 있었습니다.</li>
                        <li>splashScreen.setOnExitAnimationListener에서 참조할 수 있는 SplashScreenView의 멤버 변수들을 활용하여 splash screen의 애니메이션이 종료되기 전까지 중단되지 않도록 개선하였습니다.</li>
                </ul>
        </li>
        <br>
        <li>단일 이미지 선택 시, 강제 종료 문제 해결
                <ul>
                        <li>단일 이미지를 선택하기 위해서 ActivityResultContracts.PickMultipleVisualMedia를 호출 할 경우, 강제종료되는 이슈가 존재합니다.</li>
                        <li>이는 ActivityResultContracts.PickMultipleVisualMedia가 최대 이미지 선택 개수에서 1 이하의 자연수를 허용하지 않는 문제에서 강제종료가 발생하는 것이므로, rememberLauncherForActivityResult에서 람다 함수로 별도의 분기를 추가하여 단일 이미지 선택 시 ActivityResultContracts.PickVisualMedia를 호출하도록 수정하였습니다.</li>
                </ul>
        </li>
</ul>
<br>
