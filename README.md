구현 사항
<ul>
        <li>회원가입 / 로그인 / 자동 로그인</li>
        <li>게시글 생성 / 조회 / 수정 / 삭제</li>
        <li>게시글 찜하기</li>
        <li>스플래시 화면</li>
        <li>사용자 테마별 색상 변화</li>
        <li><a href="https://github.com/jhw010406/carrot-market-clone-frontend?tab=readme-ov-file#%EA%B8%B0%EC%A1%B4-%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98-%EB%8C%80%EB%B9%84-ux-%EA%B0%9C%EC%84%A0-%EC%82%AC%ED%95%AD">기존 애플리케이션 대비 UX 개선</a></li>
</ul>

### 스플래시 화면
애플리케이션 첫 실행 시 등장하는 화면에 애니메이션을 만들어 넣었습니다.
<table align=center>
        <tr>
                <td width=240 align=center>다크모드 활성화 시</td>
                <td width=240 align=center>다크모드 비활성화 시</td>
        <tr>
                <td align=center><img src="https://github.com/user-attachments/assets/aafc4f4a-63f8-4f85-82b2-bac3330696e2" width=100%></td>
                <td align=center><img src="https://github.com/user-attachments/assets/a8fc12e7-250c-4cd8-bc74-2e8b17ce4566" width=100%></td>
        </tr>
</table>
<br>


### 사용자 테마별 색상 변화
<table align=center>
        <tr>
                <td width=240 align=center>다크모드 활성화 시</td>
                <td width=240 align=center>다크모드 비활성화 시</td>
        <tr>
                <td align=center><img src="https://github.com/user-attachments/assets/ce7957dc-d827-4b38-a18d-7c07ed0600aa" width=100%></td>
                <td align=center><img src="https://github.com/user-attachments/assets/0be60050-c109-4eae-b2be-14ddebdb9a26" width=100%></td>
        </tr>
</table>
<br>

## Trouble Shootings
<ul>
        <li>UI 성능 최적화
                <ul>
                        <li>모든 composable의 애니메이션을 GraphicsLayer 내부에서만 일어나도록 수정하였습니다.</li>
                        <li></li>
                        <li>위 수정사항들로 특정 composable에서 애니메이션 발생 시, 상위 composable의 Recomposition count를 1회로 현저히 줄어들도록 개선하였습니다.</li>
                </ul>
        </li>
        <li>test2</li>
</ul>
<br>

## 기존 애플리케이션 대비 UX 개선 사항

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
