package io.palette.utility.imagePicker

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object RxImageConverters {

    fun uriToFile(context: Context, uri: Uri, file: File): Observable<File> {
        return Observable.create(ObservableOnSubscribe<File> { emitter ->
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                copyInputStreamToFile(inputStream!!, file)
                emitter.onNext(file)
                emitter.onComplete()
            } catch (e: Exception) {
                Log.e(RxImageConverters::class.java.simpleName, "Error converting uri", e)
                emitter.onError(e)
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }

    fun uriToBitmap(context: Context, uri: Uri): Observable<Bitmap> {
        return Observable.create(ObservableOnSubscribe<Bitmap> { emitter ->
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                emitter.onNext(bitmap)
                emitter.onComplete()
            } catch (e: IOException) {
                Log.e(RxImageConverters::class.java.simpleName, "Error converting uri", e)
                emitter.onError(e)
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }

    @Throws(IOException::class)
    private fun copyInputStreamToFile(`in`: InputStream, file: File) {
        val out = FileOutputStream(file)
        val buf = ByteArray(10 * 1024)
        val len: Int = `in`.read(buf)
        while (len > 0) {
            out.write(buf, 0, len)
        }
        out.close()
        `in`.close()
    }

}
