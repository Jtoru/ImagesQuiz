package com.example.imagesgame

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import org.jsoup.Jsoup
import java.util.*
import java.util.concurrent.ExecutionException

class MainActivity : AppCompatActivity() {

    lateinit var names : MutableList<String>
    lateinit var imgs : MutableList<String>
    lateinit var imageView : ImageView
    lateinit var button : Button
    lateinit var button2 : Button
    lateinit var button3 : Button
    lateinit var button4 : Button
    private var actualPosition : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)

        names = mutableListOf()
        imgs = mutableListOf()

        var html : String = ""
        val downloadTask = DownloadTask()
        try {
            //html = downloadTask.execute("https://www.thetoptens.com/best-game-thrones-series-characters/").get()
            html = downloadTask.execute("https://www.musicradar.com/news/the-top-10-highest-earning-djs-in-the-world-today").get()
            val doc = Jsoup.parse(html)
            val characters = doc.select("div #article-body h2")
            val images = doc.select("div #article-body figure p img")
            //Log.i("n",characters.toString())
            val n = characters.iterator()
            while(n.hasNext()){
                val oldValue = n.next()
                names.add(oldValue.text())
                Log.e("Character", oldValue.text())
            }

            val imgIterator = images.iterator()
            while(imgIterator.hasNext()){
                val oldValue = imgIterator.next()
                imgs.add(oldValue.attr("data-src"))
                Log.e("ImgUrl", oldValue.attr("data-src"))

            }
            newGame()
        }  catch ( e : InterruptedException) {
            e.printStackTrace()
        } catch (e : ExecutionException) {
            e.printStackTrace()
        }
        //Log.i("HTML", html)

        button.setOnClickListener(clicker)
        button2.setOnClickListener(clicker)
        button3.setOnClickListener(clicker)
        button4.setOnClickListener(clicker)



    }

    fun newGame(){
        val random = Random()
        var excludeList = mutableListOf<Int>()
        //button.text = names[actualPosition]
        //button2.text = names[actualPosition+1]
        //button3.text = names[actualPosition+2]
        //button4.text = names[actualPosition+3]
        actualPosition = random.nextInt(names.size)
        setImage(imgs[actualPosition])
        val buttonNumber = random.nextInt(3)
        val n1 = randomMod(actualPosition)
        excludeList.add(actualPosition)
        excludeList.add(n1)
        if (buttonNumber == 0){
            button.text = names[actualPosition]
            button2.text = names[n1]
            val n2 = getRandomWithExclusion(random,0, names.size, excludeList)
            excludeList.add(n2)
            button3.text = names[n2]
            button4.text = names[getRandomWithExclusion(random, 0, names.size, excludeList)]
        }
        if(buttonNumber == 1){
            button2.text = names[actualPosition]
            button.text = names[n1]
            val n2 = getRandomWithExclusion(random,0, names.size, excludeList)
            excludeList.add(n2)
            button3.text = names[n2]
            button4.text = names[getRandomWithExclusion(random, 0, names.size, excludeList)]
        }
        if(buttonNumber == 2){
            button3.text = names[actualPosition]
            button.text = names[n1]
            val n2 = getRandomWithExclusion(random,0, names.size, excludeList)
            excludeList.add(n2)
            button2.text = names[n2]
            button4.text = names[getRandomWithExclusion(random, 0, names.size, excludeList)]
        }
        if(buttonNumber == 3){
            button4.text = names[actualPosition]
            button.text = names[n1]
            val n2 = getRandomWithExclusion(random,0, names.size, excludeList)
            excludeList.add(n2)
            button3.text = names[n2]
            button2.text = names[getRandomWithExclusion(random, 0, names.size, excludeList)]
        }
    }

    fun setImage(url : String){
        val imgDownload = ImageDownloader()
        var bitmap : Bitmap? = null
        try {
            bitmap = imgDownload.execute(url).get()
            imageView.setImageBitmap(bitmap)
        }catch ( e : InterruptedException) {
            e.printStackTrace()
        } catch (e : ExecutionException) {
            e.printStackTrace()
        }
    }

    fun getRandomWithExclusion(rnd: Random, start: Int, end: Int, exclude: List<Int>): Int {
        var random = start + rnd.nextInt(end - start  - exclude.size)
        for (ex in exclude) {
            if (random < ex) {
                break
            }
            random++
        }
        return random
    }


    fun randomMod(exclude : Int) : Int {
        val random = Random()
        var result = random.nextInt(names.size)
        while (result == exclude){
            result = random.nextInt(names.size)
        }
        return result
    }

    val clicker = object : View.OnClickListener{
        override fun onClick(v: View?) {
            val btn = v as Button
            if(btn.text == names[actualPosition]){
                Toast.makeText(this@MainActivity, "Correcto", Toast.LENGTH_SHORT).show()
                newGame()
            }else{
                Toast.makeText(this@MainActivity, "Error, intenta de nuevo", Toast.LENGTH_SHORT).show()
            }
        }

    }

}
