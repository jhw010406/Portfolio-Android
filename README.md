### 기존 대비 개선 사항

<table>
        <tr>
          <td colspan="2" align=center><h3>게시글 중복 클릭 개선</h3></td>
        </tr>
        <tr>
          <td width="50%" align=center><image src="https://github.com/user-attachments/assets/a30e4ecd-a881-48bb-b6bd-4dde268105af" width="100%"></image></td>
          <td width="50%" align=center><image src="https://github.com/user-attachments/assets/0f50e995-87ee-417d-9e41-d08755e9b03a" width="100%"></image></td>
        </tr>
        <tr>
          <td width="50%" align=center>본 프로젝트</td>
          <td width="50%" align=center>기존 당근마켓 애플리케이션</td>
        </tr>
        <tr>
          <td width="50%" align=center>별도의 StateFlow를 추가하여,<br>한 게시글만 조회하도록 개선하였습니다.<br>이를 통해 서버에 게시글 조회를 연속으로 요청하는 것을 방지할 수 있습니다.</td>
          <td width="50%" align=center>게시글 중복 클릭 시,<br>선택한 모든 게시글을 조회합니다.</td>
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
          <td width="50%" align=center>본 프로젝트</td>
          <td width="50%" align=center>기존 당근마켓 애플리케이션</td>
        </tr>
        <tr>
          <td width="50%" align=center>별도의 StateFlow를 추가하여,<br>둘 중 하나의 작업만 수행되도록 개선하였습니다.</td>
          <td width="50%" align=center>홈 화면의 글쓰기 버튼과<br>하단 네비게이션 아이템을 동시에 클릭할 시,<br>두 가지의 작업이 함께 수행됩니다.</td>
        </tr>
      </table>

<table>
        <tr>
          <td colspan="2", align=center><h3>홈 화면 글쓰기 버튼 애니메이션 개선</h3></td>
        </tr>
        <tr>
          <td width="50%" align=center><image src="https://github.com/user-attachments/assets/8f6bc67c-7dd2-4ab0-8bc1-4b92fd1062a9" width="100%"></image></td>
          <td width="50%" align=center><image src="https://github.com/user-attachments/assets/b73d146a-1d02-429b-9aff-45c4c4d95fde" width="100%"></image></td>
        </tr>
        <tr>
          <td width="50%" align=center>본 프로젝트</td>
          <td width="50%" align=center>기존 당근마켓 애플리케이션</td>
        </tr>
        <tr>
          <td width="50%" align=center>별도의 StateFlow를 추가하여 활용함으로써 버튼 애니메이션을 위해 별도의 view 생성을 하지 않았으며,<br>버튼 비활성화 시 반투명 검은색 바탕이 페이드 아웃 되도록 개선하였습니다.</td>
          <td width="50%" align=center>홈 화면의 글쓰기 버튼을<br>클릭할 시,<br>홈 화면 위에 새로운 view와 버튼을 덧붙여 애니메이션을 구현함으로써<br>서로 다른 두 개의 버튼이 겹쳐보이는 모습을 보입니다.</td>
        </tr>
</table>
