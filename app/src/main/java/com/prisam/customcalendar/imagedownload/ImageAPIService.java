package com.prisam.customcalendar.imagedownload;

import android.util.Log;

import com.prisam.customcalendar.common.RetrofitNetworkManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by vamsi on 10/12/2017.
 */

class ImageAPIService {

    private static final String TAG = "ImageAPIService";

    public interface ImagesAPICallback {
        void onSuccess(ArrayList<ImageResponseModel> imageResponseModel);

        void onFailure(ImageResponseModel imageResponseModel);
    }

    public interface ImagesAPI {
        @GET("glide.json")
        Call<ArrayList<ImageResponseModel>> getImages();
    }

    /*Service implementation*/
    static void getImages(final WeakReference<ImagesAPICallback> imagesAPICallbackReference) {

        String BASE_URL = "http://api.androidhive.info/json/";
        ImagesAPI imageAPIService = RetrofitNetworkManager.getInstance().createService(ImagesAPI.class, BASE_URL);
        Call responseMethodCall = imageAPIService.getImages();

        Callback<ArrayList<ImageResponseModel>> callback = new Callback<ArrayList<ImageResponseModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ImageResponseModel>> call, Response<ArrayList<ImageResponseModel>> response) {
                Log.d(TAG, "Success : " + response.body());
                if (response.body() == null) {
                    ImageResponseModel model = new ImageResponseModel(true, "Something went wrong.");
                    imagesAPICallbackReference.get().onFailure(model);
                } else {
                    imagesAPICallbackReference.get().onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ImageResponseModel>> call, Throwable t) {
                Log.d(TAG, "Error : " + t.getMessage());
                ImageResponseModel model = new ImageResponseModel(true, t.getMessage());
                imagesAPICallbackReference.get().onFailure(model);
            }
        };

        responseMethodCall.enqueue(callback);
    }
}
