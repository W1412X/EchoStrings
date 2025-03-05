package com.android.echostrings;

import androidx.appcompat.app.AppCompatActivity;
import com.android.echostrings.R;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.echostrings.components.ChordButton;
import com.android.echostrings.data.ChordLearnData;
import com.google.mediapipe.components.CameraHelper;
import com.google.mediapipe.components.CameraXPreviewHelper;
import com.google.mediapipe.components.ExternalTextureConverter;
import com.google.mediapipe.components.FrameProcessor;
import com.google.mediapipe.components.PermissionHelper;
import com.google.mediapipe.formats.proto.LandmarkProto;
import com.google.mediapipe.framework.AndroidAssetUtil;
import com.google.mediapipe.framework.AndroidPacketCreator;
import com.google.mediapipe.framework.Packet;
import com.google.mediapipe.framework.PacketGetter;
import com.google.mediapipe.glutil.EglManager;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChordLearnActivity extends AppCompatActivity {
    /**
     * hand skeleton recognition part
     */
    private static final String TAG = "ChordLearnActivity";
    private static final String BINARY_GRAPH_NAME = "hand_tracking_mobile_gpu.binarypb";
    private static final String INPUT_VIDEO_STREAM_NAME = "input_video";
    private static final String OUTPUT_VIDEO_STREAM_NAME = "output_video";
    private static final String OUTPUT_LANDMARKS_STREAM_NAME = "hand_landmarks";
    private static final String INPUT_NUM_HANDS_SIDE_PACKET_NAME = "num_hands";
    private static final int NUM_HANDS = 2;
    /**
     * the array of classes
     * the model classify result as index
     * c_chords[index] is the result
     */
    private ArrayList<String> chord_classes;
    private static final CameraHelper.CameraFacing CAMERA_FACING = CameraHelper.CameraFacing.FRONT;
    // Flips the camera-preview frames vertically before sending them into FrameProcessor to be
    // processed in a MediaPipe graph, and flips the processed frames back when they are displayed.
    // This is needed because OpenGL represents images assuming the image origin is at the bottom-left
    // corner, whereas MediaPipe in general assumes the image origin is at top-left.
    private static final boolean FLIP_FRAMES_VERTICALLY = true;

    static {
        // Load all native libraries needed by the app.
        System.loadLibrary("mediapipe_jni");
        System.loadLibrary("opencv_java3");
    }

    // {@link SurfaceTexture} where the camera-preview frames can be accessed.
    private SurfaceTexture previewFrameTexture;
    // {@link SurfaceView} that displays the camera-preview frames processed by a MediaPipe graph.
    private SurfaceView previewDisplayView;
    // Creates and manages an {@link EGLContext}.
    private EglManager eglManager;
    // Sends camera-preview frames into a MediaPipe graph for processing, and displays the processed
    // frames onto a {@link Surface}.
    private FrameProcessor processor;
    // Converts the GL_TEXTURE_EXTERNAL_OES texture from Android camera into a regular texture to be
    // consumed by {@link FrameProcessor} and the underlying MediaPipe graph.
    private ExternalTextureConverter converter;
    // ApplicationInfo for retrieving metadata defined in the manifest.
    private ApplicationInfo applicationInfo;
    // Handles camera access via the {@link CameraX} Jetpack support library.
    private CameraXPreviewHelper cameraHelper;
    private Module chord_model;
    //to update the UI
    private Handler handler;

    /**
     * audio recognition part
     */
    private MediaRecorder audio_recorder;
    private String audio_path;
    private RelativeLayout overlay;

    /**
     *  views
     */
    private LinearLayout chord_btn_container;
    private ChordButton learning_chord_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord_learn);
        try {
            applicationInfo =
                    getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Cannot find application info: " + e);
        }

        previewDisplayView = new SurfaceView(this);
        setupPreviewDisplayView();
        //setup handler
        handler=new Handler();
        // Initialize asset manager so that MediaPipe native libraries can access the app assets, e.g.,
        // binary graphs.
        AndroidAssetUtil.initializeNativeAssetManager(this);
        eglManager = new EglManager(null);
        processor =
                new FrameProcessor(
                        this,
                        eglManager.getNativeContext(),
                        BINARY_GRAPH_NAME,
                        INPUT_VIDEO_STREAM_NAME,
                        OUTPUT_VIDEO_STREAM_NAME);
        processor
                .getVideoSurfaceOutput()
                .setFlipY(FLIP_FRAMES_VERTICALLY);

        PermissionHelper.checkAndRequestCameraPermissions(this);
        AndroidPacketCreator packetCreator = processor.getPacketCreator();
        Map<String, Packet> inputSidePackets = new HashMap<>();
        inputSidePackets.put(INPUT_NUM_HANDS_SIDE_PACKET_NAME, packetCreator.createInt32(NUM_HANDS));
        processor.setInputSidePackets(inputSidePackets);
        processor.addPacketCallback(
                OUTPUT_LANDMARKS_STREAM_NAME,
                (packet) -> {
                    /**
                     * judge if learning
                     */
                    if(learning_chord_btn!=null){
                        Log.v(TAG, "Received multi-hand landmarks packet.");
                        List<LandmarkProto.NormalizedLandmarkList> multiHandLandmarks =
                                PacketGetter.getProtoVector(packet, LandmarkProto.NormalizedLandmarkList.parser());
                        final String result=getClassifyResult(multiHandLandmarks);
                        /**
                         * if the predict result as same as the learning one
                         * then let user record the audio and upload the server to get the result(or maybe can use the local model)
                         */
                        if(result==learning_chord_btn.getChord()){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    learning_chord_btn.setSkeletonState(true);
                                }
                            });
                        }
                    }
                });
        loadSkeletonClassifyModel(null);

        /**
         * set the audio part
         */
        //PermissionHelper.checkAndRequestAudioPermissions(this);
        audio_path=getExternalFilesDir(Environment.DIRECTORY_MUSIC)+"/chord_audio_now.wav";
        Toast.makeText(ChordLearnActivity.this,audio_path,Toast.LENGTH_SHORT).show();
        chord_btn_container=findViewById(R.id.chord_btn_container);
        initChordBtn();
    }


    @Override
    protected void onResume() {
        super.onResume();
        converter =
                new ExternalTextureConverter(
                        eglManager.getContext(), 2);
        converter.setFlipY(FLIP_FRAMES_VERTICALLY);
        converter.setConsumer(processor);
        if (PermissionHelper.cameraPermissionsGranted(this)) {
            startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        converter.close();

        // Hide preview display until we re-open the camera again.
        previewDisplayView.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void onCameraStarted(SurfaceTexture surfaceTexture) {
        previewFrameTexture = surfaceTexture;
        // Make the display view visible to start showing the preview. This triggers the
        // SurfaceHolder.Callback added to (the holder of) previewDisplayView.
        previewDisplayView.setVisibility(View.VISIBLE);
    }

    protected Size cameraTargetResolution() {
        return null; // No preference and let the camera (helper) decide.
    }

    public void startCamera() {
        cameraHelper = new CameraXPreviewHelper();
        cameraHelper.setOnCameraStartedListener(
                surfaceTexture -> {
                    onCameraStarted(surfaceTexture);
                });
        CameraHelper.CameraFacing cameraFacing = CameraHelper.CameraFacing.FRONT;
        cameraHelper.startCamera(
                this, cameraFacing, /*unusedSurfaceTexture=*/ null, cameraTargetResolution());
    }

    protected Size computeViewSize(int width, int height) {
        return new Size(width, height);
    }

    protected void onPreviewDisplaySurfaceChanged(
            SurfaceHolder holder, int format, int width, int height) {
        // (Re-)Compute the ideal size of the camera-preview display (the area that the
        // camera-preview frames get rendered onto, potentially with scaling and rotation)
        // based on the size of the SurfaceView that contains the display.
        Size viewSize = computeViewSize(width, height);
        Size displaySize = cameraHelper.computeDisplaySizeFromViewSize(viewSize);
        boolean isCameraRotated = cameraHelper.isCameraRotated();

        // Connect the converter to the camera-preview frames as its input (via
        // previewFrameTexture), and configure the output width and height as the computed
        // display size.
        converter.setSurfaceTextureAndAttachToGLContext(
                previewFrameTexture,
                isCameraRotated ? displaySize.getHeight() : displaySize.getWidth(),
                isCameraRotated ? displaySize.getWidth() : displaySize.getHeight());
    }



    private void setupPreviewDisplayView() {
        previewDisplayView.setVisibility(View.GONE);
        ViewGroup viewGroup = findViewById(R.id.preview_display_layout);
        viewGroup.addView(previewDisplayView);

        previewDisplayView
                .getHolder()
                .addCallback(
                        new SurfaceHolder.Callback() {
                            @Override
                            public void surfaceCreated(SurfaceHolder holder) {
                                processor.getVideoSurfaceOutput().setSurface(holder.getSurface());
                            }

                            @Override
                            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                                onPreviewDisplaySurfaceChanged(holder, format, width, height);
                            }

                            @Override
                            public void surfaceDestroyed(SurfaceHolder holder) {
                                processor.getVideoSurfaceOutput().setSurface(null);
                            }
                        });
    }

    /**
     * get the classification result
     * @param multiHandLandmarks
     * @return a string with the class or the tip message
     */
    private String getClassifyResult(List<LandmarkProto.NormalizedLandmarkList>multiHandLandmarks){
        if(multiHandLandmarks.isEmpty()){
            return "未检测到手势";
        }else if(multiHandLandmarks.size()>1){
            return "请仅展示按压和弦手";
        }
        /**
         * we have already ensure there's only on list in the multihandmarks
         */
        LandmarkProto.NormalizedLandmarkList landMarks=multiHandLandmarks.get(0);
        float[] inputData=new float[63];
        int markIndex=0;
        for(LandmarkProto.NormalizedLandmark landmark:landMarks.getLandmarkList()){
            inputData[0+3*markIndex]=landmark.getX();
            inputData[1+3*markIndex]=landmark.getY();
            inputData[2+3*markIndex]=landmark.getZ();
            markIndex++;
        }
        Tensor inputTensor = Tensor.fromBlob(inputData, new long[]{21, 3});
        Tensor outputTensor=chord_model.forward(IValue.from(inputTensor)).toTensor();
        float[] outputData = outputTensor.getDataAsFloatArray();
        int maxIndex=0;
        int indexNow=0;
        float maxVal=outputData[0];
        for(float val:outputData){
            if(val>maxVal){
                maxVal=val;
                maxIndex=indexNow;
            }
            indexNow++;
        }
        return chord_classes.get(maxIndex);
    }

    /**
     * load the skeleton classify model
     * meanwhile get the chord list
     * @param chord a String such as c,g default c
     */
    private void loadSkeletonClassifyModel(String chord){
        //default get c chord
        if(chord==null){
            chord="c";
        }
        // register AssertManager
        AssetManager assetManager = getAssets();
        // try to load the model
        try {
            InputStream inputStream = assetManager.open(chord+"_skeleton_model.pt");
            File modelFile = new File(getCacheDir(), chord+"_skeleton_model.pt");
            FileOutputStream outputStream = new FileOutputStream(modelFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
            chord_model = Module.load(modelFile.getAbsolutePath());
            Toast.makeText(ChordLearnActivity.this,"加载模型成功",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ChordLearnActivity.this,"加载模型失败",Toast.LENGTH_SHORT).show();
        }
        // init the c_chords
        chord_classes=new ArrayList<String>();
        //set the class
        switch (chord){
            case "c":
                chord_classes.add("c");chord_classes.add("dm");chord_classes.add("em");chord_classes.add("f");chord_classes.add("g");chord_classes.add("am");
                break;
            default:
                Toast.makeText(ChordLearnActivity.this,"未添加和弦",Toast.LENGTH_SHORT).show();
        }

        //
    }

    private void initChordBtn(){
        for(int i=0;i<ChordLearnData.getMajors().size();i++){
            String major=ChordLearnData.getMajors().get(i);
            for(int u=0;u<ChordLearnData.getChords(major).size();u++){
                String chord=ChordLearnData.getChords(major).get(u);
                ChordButton btn=new ChordButton(ChordLearnActivity.this,audio_recorder,audio_path,major,chord);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn.externalClick();
                        /**
                         * if clicked and used to be closed
                         * user learn this chord
                         */
                        if(btn.getSelectState()){
                            learning_chord_btn=btn;
                            for(int i=0;i<chord_btn_container.getChildCount();i++){
                                if(!((ChordButton)chord_btn_container.getChildAt(i)).getSelectState()){
                                    /**
                                     *  if unselected
                                     *  then set the skeleton state to false
                                     */
                                    ((ChordButton)chord_btn_container.getChildAt(i)).setSkeletonState(false);
                                }else if(((ChordButton)chord_btn_container.getChildAt(i))!=learning_chord_btn){
                                    /**
                                     * if selected as well as the btn is not the clicked btn
                                     * call click to close it
                                     */
                                    ((ChordButton)chord_btn_container.getChildAt(i)).externalClick();
                                }
                            }
                        }
                        /**
                         * else user just want to close it
                         * set the learning btn to null
                         */
                        else{
                            learning_chord_btn=null;
                        }

                    }
                });
                chord_btn_container.addView(btn);
            }
        }
    }

}