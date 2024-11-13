package com.example.tradingapp.model.viewmodel.post

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.tradingapp.BuildConfig
import com.example.tradingapp.model.data.post.Image
import com.example.tradingapp.model.data.request.RequestBodyFromContentURI
import com.example.tradingapp.model.data.post.PostCategories
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.data.post.PostDetailsTrading
import com.example.tradingapp.model.repository.ImageDataRepository
import com.example.tradingapp.model.repository.PostDataRepository

class WritePostForTradingViewModel(
    val title: MutableState<String> = mutableStateOf(""),
    val content: MutableState<String> = mutableStateOf(""),
    val productPrice: MutableState<String> = mutableStateOf(""),
    val location: MutableState<String> = mutableStateOf(""),
    val forSelling: MutableState<Boolean> = mutableStateOf(true),
    val acceptNegotiation: MutableState<Boolean> = mutableStateOf(false),
    val acceptFreebieRequest: MutableState<Boolean> = mutableStateOf(false),
    val selectedImages: SnapshotStateList<Image> = mutableStateListOf()
) : ViewModel(){
    var tag : String = ""
    var productPriceInt : Int = 0

    fun uploadPostForTrading(
        posterUID : Int,
        callback : (Boolean) -> Unit
    ) {
        // List< pair<filename, image_index> >
        val imageList = mutableListOf< Pair<String, Int> >()

        selectedImages.forEachIndexed() { index, image ->
            imageList.add(Pair(image.name, index))
            Log.d(tag, imageList[index].toString())
        }

        PostDataRepository.uploadPost(
            inputPostDetails = PostDetails(
                postID = 0,
                posterUID = posterUID,
                posterID = null,
                category = PostCategories.TRADING.value,
                thumbnailImageUrl = null,
                title = title.value,
                content = content.value,
                viewCount = 0,
                uploadDate = null,
                modifyDate = null,
                location = null,
                postDetailsTrading = PostDetailsTrading(
                    productImagesForUpload = imageList,
                    productImagesForGet = null,
                    productType = "물건 종류",
                    productPrice = productPriceInt,
                    modelYear = null,
                    mileage = null,
                    chatCount = 0,
                    favoriteCount = 0
                ),
                postDetailsCommunity = null
            )
        ){ responseCode ->
            when (responseCode / 100) {
                2 -> {
                    callback(true)
                }
                else -> {
                    callback(false)
                }
            }
        }
    }

    fun getValidPrice(
        input : String
    ) : String {
        var resultInt = 0
        val inputLength = input.length

        if (inputLength == 0){
            productPriceInt = 0
            return input
        }

        for (idx : Int in 0..inputLength - 1){
            resultInt = resultInt * 10 + (input[idx] - '0')
        }
        productPriceInt = resultInt

        return productPriceInt.toString()
    }

    fun isValidPostDetails() : Boolean {
        return (
                        title.value.isNotBlank() &&
                        selectedImages.isNotEmpty() &&
                        content.value.isNotBlank() &&
                        productPrice.value.isNotBlank()
                )
    }

    fun addImageToList(
        inputUri : Uri,
        context : Context,
        currentImagesCount : Int,
        callback : (Int) -> Unit
    ) {
        val requestBody = RequestBodyFromContentURI(context, inputUri)
        var isSuccessful = true
        var errorMessageNumber = 0
        val errorMessages : List<String> = listOf(
            "put image failed",
            "get pre-signed url failed"
        )
        val image = Image(
            0,
            requestBody.filename,
            requestBody.contentType,
            "",
            BuildConfig.STORAGE_ADDRESS,
            requestBody,
            true,
            0
        )

        Log.d(tag, "input URI : ${inputUri}")
        selectedImages.add(image)
        try {
            if (requestBody.contentType == null) throw Exception("file's content type is null")

            ImageDataRepository.getImagePreSignedURL(
                tag,
                image.name,
                image.contentType!!
            ) { getImage, getURLSucceed ->
                Log.d(tag, "get pre signed url succeed")

                if (getURLSucceed){
                    image.name = getImage!!.name
                    image.preSignedUrl = getImage.preSignedUrl
                    image.url += getImage.name
                    ImageDataRepository.putImage(tag, image) { putImageSucceed ->

                        if (putImageSucceed) {
                            image.loading = false
                            Log.d(tag, "upload image succeed")
                        }
                        else{
                            isSuccessful = false
                            errorMessageNumber = 0
                        }
                    }
                }
                else{
                    isSuccessful = false
                    errorMessageNumber = 1
                }
            }

            if (!isSuccessful){
                throw Exception(errorMessages[errorMessageNumber])
            }

        } catch (e : Exception) {
            Log.e(tag, e.message, e)
            callback(currentImagesCount - 1)
            return
        }
    }

    fun deleteImageFromList(
        inputImage : Image,
        callback: (Boolean) -> Unit
    ) {
        ImageDataRepository.deletePostImage(tag, inputImage.name){ isSuccessful ->
            if (isSuccessful){
                selectedImages.remove(inputImage)
                callback(true)
            }
            else{
                callback(false)
            }
        }
    }
}