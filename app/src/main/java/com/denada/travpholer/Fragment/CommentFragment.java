package com.denada.travpholer.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.denada.travpholer.Doc.CGlobal;
import com.denada.travpholer.Doc.Constants;
import com.denada.travpholer.R;
import com.denada.travpholer.adapter.AdapterComment;
import com.denada.travpholer.model.BaseModel;
import com.denada.travpholer.model.Response.ActionResponse;
import com.denada.travpholer.model.Response.CommentResponse;
import com.denada.travpholer.model.SearchTerm;
import com.denada.travpholer.model.TblComment;
import com.denada.travpholer.model.TblPhotos;
import com.denada.travpholer.util.http.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends BaseFragment implements View.OnClickListener {

    ProgressBar progressBar;
    AdapterComment adapterComment;
    EditText editText;
    View btnPost;
    List<TblComment> listData = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 200: {


                        progressBar.setVisibility(View.GONE);
                        break;
                    }
                    case 400: {
                        //network fail
                        RetrofitError error = (RetrofitError) msg.obj;
                        if (error.getMessage() != null) {
                            Log.e("HomeActivity", error.getMessage());
                        }
                        Toast.makeText(getActivity(), "Network Erro", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        break;
                    }

                }
            } catch (Exception ex) {

            }

        }
    };
    private View mRootView;
    private ListView listView;
    private TblPhotos mTblPhotos;
    private String mCountryId = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.activity_comment, container, false);
        progressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        listView = (ListView) mRootView.findViewById(R.id.listView);


        adapterComment = new AdapterComment(getActivity(), listData);
        listView.setAdapter(adapterComment);

        btnPost = mRootView.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(this);

        editText = (EditText) mRootView.findViewById(R.id.editText);


        return mRootView;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        try {
            mTblPhotos = (TblPhotos) bundle.getSerializable("data");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadComment();

    }

    private void loadComment() {
        progressBar.setVisibility(View.VISIBLE);
        SearchTerm term = new SearchTerm();
        term.tu_id = CGlobal.curUser.tu_id;
        term.tp_id_viewtop = mTblPhotos.tp_id;
        Call<CommentResponse> call = ApiClient.getApiClient().onTemplateRequestForCommentResponse(CGlobal.getLastFileName(Constants.ACTION_LOADCOMMENT), BaseModel.getQueryMap(term));
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                CommentResponse commentResponse = response.body();
                if (commentResponse.response == 200) {
                    listData = commentResponse.rows;
                    adapterComment.update(listData);
                } else {

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnPost: {
                String text = editText.getText().toString();
                if (!text.isEmpty()) {
                    TblComment comment = new TblComment();
                    comment.tc_content = text;
                    comment.tc_tpid = mTblPhotos.tp_id;
                    comment.tc_tuid = CGlobal.curUser.tu_id;
                    comment.action = "insert";
                    comment.tc_name = CGlobal.curUser.getUsername();

                    progressBar.setVisibility(View.VISIBLE);
                    Call<ActionResponse> call = ApiClient.getApiClient().onTemplateRequestForActionResponse(CGlobal.getLastFileName(Constants.ACTION_COMMENT), BaseModel.getQueryMap(comment));
                    call.enqueue(new Callback<ActionResponse>() {
                        @Override
                        public void onResponse(Call<ActionResponse> call, Response<ActionResponse> response) {
                            ActionResponse actionResponse = response.body();
                            if (actionResponse.response == 200) {
                                if (actionResponse.tblComment != null) {
                                    listData.add(actionResponse.tblComment);

                                    Intent intent = new Intent();
                                    intent.putExtra("data", actionResponse.tblComment);
                                    intent.putExtra("actiontype", 4);
                                    intent.putExtra("controller", "comment");
                                    intent.setAction(Constants.BROADCAST_PHOTOCHANGED);
                                    intent.putExtra("comment_count", String.valueOf(listData.size()));

                                    adapterComment.update(listData);

                                    // broadcast
                                    getActivity().sendBroadcast(intent);
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error While commenting", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<ActionResponse> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                    editText.setText("");
                }

                break;
            }
        }

    }


}
