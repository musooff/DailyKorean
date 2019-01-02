package com.dailykorean.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import java.io.ByteArrayOutputStream

/**
 * Created by musooff on 01/01/2019.
 */

class ShareUtils(val context: Context) {

    companion object {
        private const val FACEBOOK_APPLICATION_ID = "com.facebook.katana"
    }

    fun shareExpression(activity: Activity, view: View){
        PermissionUtils.requestStorage(activity, object : PermissionUtils.Companion.OnPermissionResult {
            override fun onResult(requestCode: Int, granted: Boolean, permissions: Array<String>) {
                if (granted){
                    val bitmap = screenShot(view)
                    val uri = getImageUri(bitmap)
                    shareScreen(uri, FACEBOOK_APPLICATION_ID)
                }
            }
        })

    }

    private fun shareScreen(pngUri: Uri, sharingApp: String) {

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/png"
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "your sharing text")
        shareIntent.putExtra(android.content.Intent.EXTRA_STREAM, pngUri)
        if (isFacebookInstalled()) {
            shareIntent.setPackage(sharingApp)
        }
        context.startActivity(shareIntent)

    }

    private fun getImageUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver,
                bitmap, "", "")
        return Uri.parse(path)
    }

    private fun screenShot(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun isFacebookInstalled(): Boolean {
        return try {
            val info = context.packageManager.getApplicationInfo("com.facebook.katana", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    }

}