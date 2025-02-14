package com.lion.FinalProject_CarryOn_Anywhere.data.server.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class Tools {
    companion object{

        // 이미지를 회전시키는 메서드
        fun rotateBitmap(bitmap: Bitmap, degree:Int): Bitmap {
            // 회전 이미지를 구하기 위한 변환 행렬
            val matrix = Matrix()
            matrix.postRotate(degree.toFloat())
            // 회전 행렬을 적용하여 회전된 이미지를 생성한다.
            val resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
            return resultBitmap
        }

        // 회전 각도값을 구하는 메서드
        fun getDegree(context: Context, uri: Uri):Int{

            // 이미지의 태그 정보에 접근할 수 있는 객체를 생성한다.
            // andorid 10 버전 이상이라면
            val exifInterface = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                // 이미지 데이터를 가져올 수 있는 Content Provider의 Uri를 추출한다.
                val photoUri = MediaStore.setRequireOriginal(uri)
                // 컨텐츠 프로바이더를 통해 파일에 접근할 수 있는 스트림을 추출한다.
                val inputStream = context.contentResolver.openInputStream(photoUri)
                // ExifInterface 객체를 생성한다.
                ExifInterface(inputStream!!)
            } else {
                ExifInterface(uri.path!!)
            }

            // ExifInterface 정보 중 회전 각도 값을 가져온다
            val ori = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)

            // 회전 각도값을 담는다.
            val degree = when(ori){
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }

            return degree
        }


        // 이미지의 사이즈를 줄이는 메서드
        fun resizeBitmap(targetWidth:Int, bitmap: Bitmap): Bitmap {
            // 이미지의 축소/확대 비율을 구한다.
            val ratio = targetWidth.toDouble() / bitmap.width.toDouble()
            // 세로 길이를 구한다.
            val targetHeight = (bitmap.height.toDouble() * ratio).toInt()
            // 크기를 조절한 Bitmap 객체를 생성한다.
            val result = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
            return result
        }

        // 촬영된 사진 데이터를 읽어와 가공하고 상태관리 변수에 담아준다.
        fun takePictureData(context: Context, contentUri: Uri, previewBitmap: MutableState<Bitmap?>){
            // uri를 통해 저장된 사진 데이터를 가져온다.
            val bitmap = BitmapFactory.decodeFile(contentUri.path)
            // 회전 각도값을 가져온다.
            val degree = getDegree(context, contentUri)
            // 회전 시킨 이미지를 가져온다.
            val rotateBitmap = rotateBitmap(bitmap, degree)
            // 사이즈를 줄여준다.
            val resizingBitmap = resizeBitmap(1024, rotateBitmap)

            previewBitmap.value = resizingBitmap

            // 사진 파일은 삭제한다.
            val file = File(contentUri.path!!)
            file.delete()
        }

        // 촬영된 사진 데이터를 읽어와 가공하고 상태관리 변수에 담아준다.
        fun takePicturesData(
            context: Context, contentUri: Uri, previewBitmap: SnapshotStateList<Bitmap>, imageUriList: SnapshotStateList<String>
        ){
            // uri를 통해 저장된 사진 데이터를 가져온다.
            val bitmap = BitmapFactory.decodeFile(contentUri.path)
            // 회전 각도값을 가져온다.
            val degree = getDegree(context, contentUri)
            // 회전 시킨 이미지를 가져온다.
            val rotateBitmap = rotateBitmap(bitmap, degree)
            // 사이즈를 줄여준다.
            val resizingBitmap = resizeBitmap(1024, rotateBitmap)

            previewBitmap.add(resizingBitmap)
            imageUriList.add(contentUri.toString())

            // 사진 파일은 삭제한다.
            val file = File(contentUri.path!!)
            file.delete()
        }

        // 촬영된 사진이 저장될 경로를 반환하는 메서드
        fun gettingPictureUri(context: Context) : Uri {
            // 외부 저장소의 경로를 가져온다.
            val filePath = context.getExternalFilesDir(null).toString()
            // 카메라 런처를 실행한다.
            // 촬영한 사진이 저장될 파일 이름
            val fileName = "/temp_${System.currentTimeMillis()}.jpg"
            // 경로 + 파일이름
            val picPath = "${filePath}${fileName}"
            val file = File(picPath)

            // 사진이 저장될 위치를 관리하는 Uri 객체를 생성ㅎ
            val contentUri = FileProvider.getUriForFile(context, "com.lion.finalprojectshoppingmallservice3team.camera", file)
            return contentUri
        }

        fun takeAlbumData(context: Context, previewUri: Uri?, previewBitmap: MutableState<Bitmap?>) {
            if (previewUri != null) {
                try {
                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val source = ImageDecoder.createSource(context.contentResolver, previewUri)
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        context.contentResolver.openInputStream(previewUri)?.use { inputStream ->
                            BitmapFactory.decodeStream(inputStream) // ✅ 안전한 방식으로 Bitmap 변환
                        }
                    }

                    bitmap?.let {
                        val resizeBitmap = resizeBitmap(1024, it) // ✅ 리사이징 적용
                        previewBitmap.value = resizeBitmap // ✅ 상태 업데이트
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun takeAlbumsData(context: Context, previewUri: Uri?, previewBitmap: SnapshotStateList<Bitmap>, imageUriList: SnapshotStateList<String>) {
            if (previewUri == null) {
                Log.e("takeAlbumsData", "previewUri가 null입니다.")
                return
            }

            try {
                val inputStream = context.contentResolver.openInputStream(previewUri)
                if (inputStream != null) {
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream.close()

                    if (bitmap != null) {
                        val resizedBitmap = resizeBitmap(1024, bitmap)
                        previewBitmap.add(resizedBitmap) // ✅ 선택한 이미지 리스트에 추가
                        imageUriList.add(previewUri.toString())
                    } else {
                        Log.e("takeAlbumsData", "Bitmap 변환 실패: null 반환됨")
                    }
                } else {
                    Log.e("takeAlbumsData", "InputStream이 null입니다.")
                }
            } catch (e: Exception) {
                Log.e("takeAlbumsData", "이미지 가져오기 실패: ${e.message}")
            }
        }

        // 이미지 뷰에 있는 이미지를 파일로 저장한다.
        fun saveBitmap(context: Context, bitmap: Bitmap){
            // 외부 저장소의 경로를 가져온다.
            val filePath = context.getExternalFilesDir(null).toString()
            // 저장할 파일의 경로
            val file = File("${filePath}/uploadTemp.jpg")
            // 파일로 저장한다.
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        }
    }
}