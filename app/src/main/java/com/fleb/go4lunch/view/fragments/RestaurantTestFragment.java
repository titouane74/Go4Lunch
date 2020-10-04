package com.fleb.go4lunch.view.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fleb.go4lunch.R;
import com.fleb.go4lunch.model.CommentPost;
import com.fleb.go4lunch.model.Post;
import com.fleb.go4lunch.network.ApiClient;
import com.fleb.go4lunch.network.JsonRetrofitApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Florence LE BOURNOT on 07/07/2020
 */


public class RestaurantTestFragment extends BaseFragment {

    private TextView textViewResult;
    private JsonRetrofitApi mJsonRetrofitApi;

    public RestaurantTestFragment() {}

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_restaurant_test; }

    @Override
    protected void configureFragmentOnCreateView(View pView) {


        textViewResult = pView.findViewById(R.id.text_view_result);

        mJsonRetrofitApi = ApiClient.getClient(JsonRetrofitApi.BASE_URL_TEST).create(JsonRetrofitApi.class);

//        getPostsMap();
//        getPostsMultipleFirstParameter();
//        getPosts();
//        getComments();
            getCommentsUrl();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getPosts() {


        Call<List<Post>> lCall = mJsonRetrofitApi.getPosts(1,4,null,null);

        lCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<Post> lPosts = response.body();

                for (Post post : lPosts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n" ;
                    content += "User ID: " + post.getUserId() + "\n" ;
                    content += "Title: " + post.getTitle() + "\n" ;
                    content += "Text: " + post.getText() + "\n\n" ;

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
    private void getPostsMultipleFirstParameter() {
        Call<List<Post>> lCall = mJsonRetrofitApi.getPostsMultipleFirstParameter(new Integer[] {2,3,6},null,null);

        lCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<Post> lPosts = response.body();

                for (Post post : lPosts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n" ;
                    content += "User ID: " + post.getUserId() + "\n" ;
                    content += "Title: " + post.getTitle() + "\n" ;
                    content += "Text: " + post.getText() + "\n\n" ;

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
    private void getPostsMap() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId","1");
        parameters.put("_sort","id");
        parameters.put("_order","desc");

        Call<List<Post>> lCall = mJsonRetrofitApi.getPostsMap(parameters);

        lCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<Post> lPosts = response.body();

                for (Post post : lPosts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n" ;
                    content += "User ID: " + post.getUserId() + "\n" ;
                    content += "Title: " + post.getTitle() + "\n" ;
                    content += "Text: " + post.getText() + "\n\n" ;

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
    private void getComments() {
        Call<List<CommentPost>> lCall = mJsonRetrofitApi.getComments(3);
        lCall.enqueue(new Callback<List<CommentPost>>() {
            @Override
            public void onResponse(Call<List<CommentPost>> call, Response<List<CommentPost>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<CommentPost> lCommentPostList = response.body();

                for (CommentPost lCommentPost : lCommentPostList) {
                    String content = "";
                    content += "ID: " + lCommentPost.getId() + "\n" ;
                    content += "Post ID: " + lCommentPost.getPostId() + "\n" ;
                    content += "Name: " + lCommentPost.getName() + "\n" ;
                    content += "Email: " + lCommentPost.getEmail() + "\n" ;
                    content += "Text: " + lCommentPost.getText() + "\n\n" ;

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<CommentPost>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
    private void getCommentsUrl() {
//        Call<List<CommentPost>> lCall = mJsonPlaceHolderApi.getComments("posts/3/comments");
        Call<List<CommentPost>> lCall = mJsonRetrofitApi.getComments(
                "https://jsonplaceholder.typicode.com/posts/3/comments");
        lCall.enqueue(new Callback<List<CommentPost>>() {
            @Override
            public void onResponse(Call<List<CommentPost>> call, Response<List<CommentPost>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<CommentPost> lCommentPostList = response.body();

                for (CommentPost lCommentPost : lCommentPostList) {
                    String content = "";
                    content += "ID: " + lCommentPost.getId() + "\n" ;
                    content += "Post ID: " + lCommentPost.getPostId() + "\n" ;
                    content += "Name: " + lCommentPost.getName() + "\n" ;
                    content += "Email: " + lCommentPost.getEmail() + "\n" ;
                    content += "Text: " + lCommentPost.getText() + "\n\n" ;

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<CommentPost>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}