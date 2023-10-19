package com.alexlyxy.alexretrofitlessonfivesearch

import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexlyxy.alexretrofitlessonfivesearch.adapters.ProductAdapter
import com.alexlyxy.alexretrofitlessonfivesearch.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ProductAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ProductAdapter()
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val mainApi = retrofit.create(MainApi::class.java)

        binding.sv.setOnQueryTextListener(object : OnQueryTextListener {

            // CoroutineScope в этом месте для onQueryTextSubmit - запрос
            // идет после нажатия на кнопку поиск. Its name : "submit" - подтвердить
            override fun onQueryTextSubmit(query: String?): Boolean {
                CoroutineScope(Dispatchers.IO).launch {
                    //val list = mainApi.getProductsByName(newText)
                    // Чтобы разобраться с newText делаем Wrap c newText.let
                    val list = query?.let { mainApi.getProductsByName(it) }
                    runOnUiThread {
                        binding.apply {
                            //adapter.submitList(list.products)
                            adapter.submitList(list?.products)
                        }
                    }
                }
                return true
            }

            // CoroutineScope в этом месте для onQueryTextChange - запрос
            // идет после ввода кажной буквы,  то есть изменения в тексте

            override fun onQueryTextChange(newText: String?): Boolean {
               /* CoroutineScope(Dispatchers.IO).launch {
                    //val list = mainApi.getProductsByName(newText)
                    // Чтобы разобраться с newText делаем Wrap c newText.let
                    val list = newText?.let { mainApi.getProductsByName(it) }
                    runOnUiThread {
                        binding.apply {
                            //adapter.submitList(list.products)
                            adapter.submitList(list?.products)
                        }
                    }
                }  */
                return true
            }
        })
    }
}
