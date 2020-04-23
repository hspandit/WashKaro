package inspire2connect.inspire2connect.news;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.about.aboutActivity;
import inspire2connect.inspire2connect.survey.maleFemaleActivity;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;

public class onAIrActivity extends BaseActivity {
    private static String LOG_TAG = "onAIrActivity";
    final private int STORAGE_PERMISSION = 1;
    final private int MIC_PERMISSION = 2;
    boolean flag = false;
    ImageView lab_logo, app_logo;
    Double threshold;
    DatabaseReference mDatabaseReference, ref, ref_for_thr, ref_for_ver;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private MediaPlayer mMediaPlayer;
    private long version_from_database = -1, version_from_app;
    private RecyclerView.LayoutManager mLayoutManager;

    private void requestPermission() {
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(CardViewActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},MIC_PERMISSION);
        }*/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(onAIrActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(onAIrActivity.this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(onAIrActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case MIC_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(onAIrActivity.this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(onAIrActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        /*if(mMediaPlayer!=null && mMediaPlayer.isPlaying())
        {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer=null;
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        /*if(mMediaPlayer!=null && mMediaPlayer.isPlaying())
        {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer=null;
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about_us) {
            Intent i = new Intent(onAIrActivity.this, aboutActivity.class);
            startActivity(i);
        } else if (id == R.id.privacy_policy) {
            openPrivacyPolicy(this);
        } else if (id == R.id.lang_togg_butt) {
            toggleLang(this);
        } else if (id == R.id.Survey) {
            Intent i = new Intent(onAIrActivity.this, maleFemaleActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_air);
        // Making notification bar transparent
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.onair_tile);

        requestPermission();


        lab_logo = findViewById(R.id.progress_bar);
        //app_logo=(ImageView)(findViewById(R.id.app_logo));
        //getSupportActionBar().setBackgroundDrawable(app_logo);
        final ArrayList results = new ArrayList<newsObject>();
        //final CircularProgressDrawable cpd=new CircularProgressDrawable(this);

        //cpd=(CircularProgressDrawable)findViewById(R.id.circularprogressbar);
        // cpd.setVisible(true,false);
        //cpd.setStyle(CircularProgressDrawable.LARGE);
        //cpd.setBackgroundColor(1);
        //cpd.setCenterRadius(2.0f);
        //ArrayList results = new ArrayList<DataObject>();
        final ProgressBar cpd = findViewById(R.id.circularprogressbar);
        for (int index = 0; index < 1; index++) {
            newsObject obj = new newsObject("Some Primary Text " + index,
                    "Secondary " + index);
            results.add(index, obj);
        }
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(results);
        mRecyclerView.setAdapter(mAdapter);

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);
        ref_for_thr = FirebaseDatabase.getInstance().getReference();
//        ref_for_thr.child("information").child("d1").child("Gender").setValue("Male");
        Query thr_query = ref_for_thr.child("threshold");

        thr_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //threshold=(Double) dataSnapshot.child("threshold").getValue();
                //Log.d("threshold",Double.toString(threshold));
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //threshold_class obj_th=snapshot.getValue(threshold_class.class);
                    threshold = (Double) snapshot.getValue();
                    Logd("threshold", Double.toString(threshold));
                }
                ref = FirebaseDatabase.getInstance().getReference();
                Query lastQuery = ref.child("hindi").orderByKey();
                lastQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!flag) {
                            //cpd.start();
                            int count = 0;
                            ArrayList results2 = new ArrayList<newsObject>();
                            HashMap<String, Story> hn = new HashMap<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Story user = snapshot.getValue(Story.class);
                                hn.put(snapshot.getKey(), user);
                            }
//                            ArrayList<Story_Details> temp=new ArrayList<Story_Details>();
//                            for (Map.Entry<String, Story_Details> it : hn.entrySet())
//                            {
//                                temp.add(it.getValue());
//                            }
//                            Log.d("Before_Sorting",Double.toString(temp.get(0).getSimilarity())+" "+Double.toString(temp.get(1).getSimilarity()));
//                            Collections.sort(temp);
//                            Log.d("Before_Sorting",Double.toString(temp.get(0).getSimilarity())+" "+Double.toString(temp.get(1).getSimilarity()));

                            for (Map.Entry<String, Story> it : hn.entrySet()) {
                                Logd("Database", it.getValue().getTitle());
                                newsObject obj = new newsObject((count + 1) + ".  " + it.getValue().getTitle(), it.getValue().getStory());
                                //Log.d("Databse",Integer.toString(it.getValue().getNumber_of_relevant_votes())+" "+Double.toString(threshold));
                                try {
                                    if (it.getValue().getSimilarity() >= threshold)
                                        results2.add(obj);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                count++;
                            }
                            if (count == 0) {
                                mAdapter = new MyRecyclerViewAdapter(results);
                            } else {

                                mAdapter = new MyRecyclerViewAdapter(results2);
                            }
                            mRecyclerView.setAdapter(mAdapter);
                            cpd.setVisibility(View.INVISIBLE);
                            //cpd.setVisible(false,false);
                            lab_logo.setVisibility(View.INVISIBLE);
                            // app_logo.setVisibility(View.INVISIBLE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            ConstraintLayout currentLayout =
                                    findViewById(R.id.constraint_layout);

                            //currentLayout.setBackgroundColor(getResources().getColor(R.color.backColor));
                /*try {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.introduction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mMediaPlayer.start();*/
                            flag = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Logd("threshold", "The read failed: " + databaseError.getCode());
            }
        });
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            version_from_app = info.versionCode;
            Logd("threshod", "Version from app" + version_from_app + (version_from_app == version_from_database));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ref_for_ver = FirebaseDatabase.getInstance().getReference();
        Query ver_query = ref_for_thr.child("version");
        ver_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //threshold_class obj_th=snapshot.getValue(threshold_class.class);
                    version_from_database = (Long) snapshot.getValue();

                    Logd("threshold", Long.toString(version_from_database));
//                    if (version_from_app != version_from_database) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(CardViewActivity.this);
//                        builder.setTitle(R.string.app_name);
//                        builder.setIcon(R.drawable.image);
//                        builder.setMessage("Please update your app.")
//                                .setCancelable(false);
//                        AlertDialog alert = builder.create();
//                        alert.show();
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
//        ref=FirebaseDatabase.getInstance().getReference();
//        Query lastQuery = ref.child("hindi").orderByKey();
//        lastQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                if (! flag)
//                {
//                    cpd.start();
//                int count = 0;
//                ArrayList results2 = new ArrayList<DataObject>();
//                HashMap<String, Story_Details> hn = new HashMap<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren())
//                {
//                    Story_Details user = snapshot.getValue(Story_Details.class);
//                    hn.put(snapshot.getKey(), user);
//                }
//                for (Map.Entry<String, Story_Details> it : hn.entrySet())
//                {
//                    Log.d("Database", it.getValue().getTitle());
//                    DataObject obj = new DataObject(Integer.toString(count + 1) + ".  " + it.getValue().getTitle(), it.getValue().getStory());
//                    //Log.d("Databse",Integer.toString(it.getValue().getNumber_of_relevant_votes())+" "+Double.toString(threshold));
//                    try {
//                        if (it.getValue().getSimilarity() >= threshold)
//                            results2.add(obj);
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                        count++;
//                }
//                if (count == 0) {
//                    mAdapter = new MyRecyclerViewAdapter(results);
//                } else {
//                    mAdapter = new MyRecyclerViewAdapter(results2);
//                }
//                mRecyclerView.setAdapter(mAdapter);
//                cpd.stop();
//                lab_logo.setVisibility(View.INVISIBLE);
//               // app_logo.setVisibility(View.INVISIBLE);
//                mRecyclerView.setVisibility(View.VISIBLE);
//                    ConstraintLayout currentLayout =
//                            (ConstraintLayout) findViewById(R.id.constraint_layout);
//
//                    //currentLayout.setBackgroundColor(getResources().getColor(R.color.backColor));
//                /*try {
//                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.introduction);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                mMediaPlayer.start();*/
//                flag=true;
//            }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError)
//            {
//
//            }
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        //return super.onSupportNavigateUp();
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent i = new Intent(onAIrActivity.this, text2speech2_2Activity.class);
                i.putExtra("position", Integer.toString(position));
                /*if(mMediaPlayer!=null)
                {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer=null;
                }*/
                startActivity(i);
            }
        });
    }
}
