package com.example.tradingapp.model.data.post

sealed class PostSubject(
    val subjects : List<String>
){
    object townInformation : PostSubject(
        subjects = listOf(
            "맛집",
            "생활/편의",
            "병원/약국",
            "이사/시공",
            "주거/부동산",
            "교육",
            "미용"
        )
    )
    object withNeighbor : PostSubject(
        subjects = listOf(
            "반려동물",
            "운동",
            "고민/사연",
            "동네친구",
            "취미",
            "동네풍경",
            "임신/육아"
        )
    )
    object news : PostSubject(
        subjects = listOf(
            "분실/실종",
            "동네사건사고",
            "공공소식"
        )
    )
    object others : PostSubject(
        subjects = listOf(
            "일반"
        )
    )
}
