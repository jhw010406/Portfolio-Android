package com.example.tradingapp.model.data

data class PreviewItemData(
    // 게시글 아이디
    val id : Int,
    // 물건 사진
    val productImage : String,
    // 물건 종류
    val type : String,
    // 게시글 제목
    val title : String,
    // 중고차 연식
    val modelYear : String?,
    // 중고차 주행 거리
    val mileage : String?,
    // 게시 시간
    val postingTime : String,
    // 가격
    val price : String,
    // 문의 중인 수
    val chattingCount : String,
    // 찜 횟수
    val favoritesCount : String,
    // 주소
    val address : Address
)
