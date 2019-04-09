package com.example.imagesgame

import android.os.AsyncTask
import android.util.Log
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream


class DownloadTask : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg urls: String): String {

        var result = ""
        val url: URL

        // Agregar permiso en AndroidManifest.xml
        var urlConnection: HttpURLConnection? = null

        try {
            url = URL(urls[0])


            urlConnection = url.openConnection() as HttpURLConnection
            val inputStream = urlConnection.inputStream
            val inputStreamReader = InputStreamReader(inputStream)

            // Esto es muy estilo C
            // Se lee un caracter a la vez (como cuando se hace gets() en C o C++)

            var data = inputStreamReader.read()
            while (data != -1) {
                val character = data.toChar()
                result += character
                data = inputStreamReader.read()
            }
            //result = convertStreamToString(inputStream)


            return result
        } catch (e: Exception) {
            e.printStackTrace()
            return "Error"
        }

    }

    private fun convertStreamToString(`is`: InputStream): String {
        /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()

        var line: String? = null
        try {
            line = reader.readLine()
            while (line != null) {
                sb.append(line!! + "\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return sb.toString()
    }
}