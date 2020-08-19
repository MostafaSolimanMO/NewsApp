package com.example.daii.udacitynewsapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.daii.udacitynewsapp.Model.Article;
import com.example.daii.udacitynewsapp.Model.Repository.NewsRepository;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {

    Application application;
    private NewsRepository newsRepository;
    private MutableLiveData<List<Article>> article;

    public NewsViewModel(Application application) {
        super(application);
        this.application = application;
        newsRepository = new NewsRepository(application);
        article = newsRepository.getMutableLiveData();
    }

    public MutableLiveData<List<Article>> getAllArticle() {
        return article;
    }

    public void Refresh() {
        newsRepository = new NewsRepository(application);
        article = newsRepository.getMutableLiveData();
    }
}
