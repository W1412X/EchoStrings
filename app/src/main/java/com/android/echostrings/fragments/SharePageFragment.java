package com.android.echostrings.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.os.Handler;
import android.widget.Toast;

import java.lang.Runnable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.echostrings.activities.NewPostActivity;
import com.android.echostrings.adapter.PostAdapter;
import com.android.echostrings.data.PostItem;
import com.android.echostrings.network.ApiService;
import com.android.echostrings.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayoutMediator;

import com.android.echostrings.R;
import com.android.echostrings.activities.DiscussionActivity;
import com.android.echostrings.activities.ChatActivity;
import com.android.echostrings.activities.ActivityListActivity;
import com.android.echostrings.activities.MentorActivity;
import com.android.echostrings.adapter.ActivityBannerAdapter;
import com.android.echostrings.data.ActivityItem;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SharePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SharePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SharePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SharePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SharePageFragment newInstance(String param1, String param2) {
        SharePageFragment fragment = new SharePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private ViewPager2 bannerViewPager;
    private TabLayout tabDots;
    private List<ActivityItem> activityItems = new ArrayList<>();
    private LinearLayout quickAccessLayout;
    private Handler handler = new Handler();
    private RecyclerView postRecyclerView;
    private Runnable runnable;
    private TabLayoutMediator tabLayoutMediator;

    private PostAdapter adapter;
    private List<PostItem> postList = new ArrayList<>();
    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_page, container, false);
        Toolbar toolbar =view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.community_menu);


        RecyclerView recyclerView = view.findViewById(R.id.postRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PostAdapter(requireContext(), postList);

        recyclerView.setAdapter(adapter);
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        loadPosts(); // 加载帖子

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_search) {
//                    Intent intent = new Intent(getActivity(), Page1Activity.class);
//                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.action_new_post) {
                    Intent intent = new Intent(getActivity(), NewPostActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.action_my_posts) {

                    return true;
                }
                return false;
            }
        });

        return view;
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        requireActivity().invalidateOptionsMenu();
        Log.d("SharePageFragment", "setHasOptionsMenu 设置成功");


        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        tabDots = view.findViewById(R.id.tabDots);
        quickAccessLayout = view.findViewById(R.id.quickAccessLayout);
        postRecyclerView = view.findViewById(R.id.postRecyclerView);


        setupBannerSlider();



        initDemoData();


        setupQuickAccessButtons();
    }
    public void showToast(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void initDemoData() {
        activityItems.add(new ActivityItem(
                "7月弹唱大赛：周杰伦专场挑战",
                "2023.07.01 - 2023.07.31",

                "进行中",
                "https://example.com/banner1.jpg"
        ));

        activityItems.add(new ActivityItem(
                "吉他新手训练营",
                "2023.08.01 - 2023.08.31",

                "进行中",
                "https://example.com/banner2.jpg"
        ));
    }
    private void loadPosts() {
        if (adapter == null) {
            Log.e("SharePageFragment", "Adapter 未初始化！");
            return;
        }

        apiService.getPosts().enqueue(new Callback<List<PostItem>>() {
            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postList.clear();
                    postList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d("SharePageFragment", "加载成功: " + response.body().size() + " 条帖子");
                } else {
                    Log.e("SharePageFragment", "API 响应为空");
                }
            }

            @Override
            public void onFailure(Call<List<PostItem>> call, Throwable t) {
                Log.e("SharePageFragment", "加载失败: " + t.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            PostItem newPost = (PostItem) data.getSerializableExtra("newPost");
            if (newPost != null) {
                adapter.addPost(newPost);
            }
        }
    }

    private void setupBannerSlider() {
        if (activityItems.isEmpty()) return;

        ActivityBannerAdapter bannerAdapter = new ActivityBannerAdapter(activityItems, item -> {
            showToast("点击活动：" + item.getTitle());
        });

        bannerViewPager.setAdapter(bannerAdapter);


        int startPosition = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % activityItems.size();
        bannerViewPager.setCurrentItem(startPosition, false);
        if (tabLayoutMediator == null) {  // 防止重复绑定
            tabLayoutMediator = new TabLayoutMediator(tabDots, bannerViewPager, (tab, position) -> {});
            tabLayoutMediator.attach();
        }
        ViewPager2.PageTransformer pageTransformer = (page, position) -> {
            float absPos = Math.abs(position);
            page.setAlpha(1 - absPos/3);
            page.setScaleY(1 - absPos/5);
        };
        bannerViewPager.setPageTransformer(pageTransformer);


        setupAutoScroll();




    }
    private void setupAutoScroll() {

         runnable = new Runnable() {
            @Override
            public void run() {
                int currentPosition = bannerViewPager.getCurrentItem();
                bannerViewPager.setCurrentItem(currentPosition + 1, true);
                handler.postDelayed(this, 3000); // 3秒切换一次
            }
        };
        handler.postDelayed(runnable, 3000);
        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    handler.removeCallbacks(runnable); // 用户手动滑动时暂停自动滚动
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    handler.postDelayed(runnable, 3000); // 停止后重新开始自动滚动
                }
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        
        handler.removeCallbacks(runnable);
        if (tabLayoutMediator != null) {
            tabLayoutMediator.detach(); // 解绑
            tabLayoutMediator = null;   // 避免内存泄漏
        }
        handler.removeCallbacksAndMessages(null);

    }

    private void setupQuickAccessButtons() {



        MaterialButton chatButton = quickAccessLayout.findViewById(R.id.chatButton);
        chatButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        });


        MaterialButton activityButton = quickAccessLayout.findViewById(R.id.activityButton);
        activityButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ActivityListActivity.class);
            startActivity(intent);
        });


        MaterialButton mentorButton = quickAccessLayout.findViewById(R.id.mentorButton);
        mentorButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MentorActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("SharePageFragment", "菜单创建成功");
        inflater.inflate(R.menu.community_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Toast.makeText(getContext(), "点击了搜索", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.action_new_post) {
            Toast.makeText(getContext(), "点击了发新帖", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), NewPostActivity.class);
            Log.d("SharePageFragment", "点击发新帖");

            startActivity(intent);
        } else if (id == R.id.action_my_posts) {
            Toast.makeText(getContext(), "点击了我的帖子", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_favorites) {
            Toast.makeText(getContext(), "点击了我的收藏", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_events) {
            Toast.makeText(getContext(), "点击了活动通知", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_mentorship) {
            Toast.makeText(getContext(), "点击了师徒管理", Toast.LENGTH_SHORT).show();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }



}
