/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.vrtoolkit.cardboard.hexistudios.vrsualiser;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items.*;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.renderers.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.khronos.egl.EGLConfig;


public class MainActivity extends CardboardActivity implements CardboardView.StereoRenderer {

  private static final String TAG = "MainActivity";

  private static final float Z_NEAR = 0.1f;
  private static final float Z_FAR = 10000.0f;

  private static final float CAMERA_Z = 0.01f;

  private static final float YAW_LIMIT = 0.12f;
  private static final float PITCH_LIMIT = 0.12f;

  // We keep the light always position just above the user.
  private static final float[] LIGHT_POS_IN_WORLD_SPACE = new float[]{0.0f, 2.0f, 0.0f, 1.0f};


  private float[] camera;
  private float[] headView;
  private float[] headRotation;
  private float floorDepth = 20f;

  private int renderProgram;
  private CardboardOverlayView overlayView;
  private Renderer renderer;
  private Analyser audioAnalyser;


  /**
   * Checks if we've had an error inside of OpenGL ES, and if so what that error is.
   *
   * @param label Label to report in case of error.
   */
  private static void checkGLError(String label) {
    int error;
    while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
      Log.e(TAG, label + ": glError " + error);
      throw new RuntimeException(label + ": glError " + error);
    }
  }

  /**
   * Converts a raw text file, saved as a resource, into an OpenGL ES shader.
   *
   * @param type  The type of shader we will be creating.
   * @param resId The resource ID of the raw text file about to be turned into a shader.
   * @return The shader object handler.
   */
  private int loadGLShader(int type, int resId) {
    String code = readRawTextFile(resId);
    int shader = GLES20.glCreateShader(type);
    GLES20.glShaderSource(shader, code);
    GLES20.glCompileShader(shader);

    // Get the compilation status.
    final int[] compileStatus = new int[1];
    GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

    // If the compilation failed, delete the shader.
    if (compileStatus[0] == 0) {
      Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
      GLES20.glDeleteShader(shader);
      shader = 0;
    }

    if (shader == 0) {
      throw new RuntimeException("Error creating shader.");
    }

    return shader;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater=getMenuInflater();
    inflater.inflate(R.menu.options, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.simplebars:
        changeRenderer(new SimpleBars(renderer.scene.renderParams));
        return true;

      case R.id.circularbars:
        changeRenderer(new CircleBars(renderer.scene.renderParams));
        return true;

      case R.id.test:
        renderer = new TestRenderer(renderer.scene.renderParams);
        return true;

      default:
        renderer = new TestRenderer(renderer.scene.renderParams);
        return super.onOptionsItemSelected(item);
    }
  }

  /**
   * Sets the view to our CardboardView and initializes the transformation matrices we will use
   * to render our scene.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    setContentView(R.layout.common_ui);
    CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
    cardboardView.setRestoreGLStateEnabled(false);
    cardboardView.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
    cardboardView.setRenderer(this);
    setCardboardView(cardboardView);

    //Set audio adjustment to Media instead of phone ringing volume.
    setVolumeControlStream(AudioManager.STREAM_MUSIC);

    //Init vars.
    camera = new float[16];
    headRotation = new float[4];
    headView = new float[16];

    overlayView = (CardboardOverlayView) findViewById(R.id.overlay);
    overlayView.show3DToast("3D Toast example.");
  }

  @Override
  public void onSurfaceChanged(int width, int height) {
    Log.i(TAG, "onSurfaceChanged");
  }

  /**
   * Creates the buffers we use to store information about the 3D world.
   * <p/>
   * <p>OpenGL doesn't use Java arrays, but rather needs data in a format it can understand.
   * Hence we use ByteBuffers.
   *
   * @param config The EGL configuration used when creating the surface.
   */
  @Override
  public void onSurfaceCreated(EGLConfig config) {
    Log.i(TAG, "onSurfaceCreated");

    GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f); // Dark background so text shows up well.

    int vertexShader = loadGLShader(GLES20.GL_VERTEX_SHADER, R.raw.light_vertex);
    int fragShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, R.raw.grid_fragment);
    int passthroughShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, R.raw.passthrough_fragment);

    renderProgram = GLES20.glCreateProgram();
    GLES20.glAttachShader(renderProgram, vertexShader);
    GLES20.glAttachShader(renderProgram, fragShader);
    GLES20.glLinkProgram(renderProgram);
    GLES20.glUseProgram(renderProgram);

    checkGLError("Render program init");

    //Set up object to cut down constructor size.
    RenderParams renderParams = new RenderParams(
        GLES20.glGetUniformLocation(renderProgram, "u_LightPos"),
        GLES20.glGetUniformLocation(renderProgram, "u_Model"),
        GLES20.glGetUniformLocation(renderProgram, "u_MVMatrix"),
        GLES20.glGetUniformLocation(renderProgram, "u_MVP"),
        GLES20.glGetAttribLocation(renderProgram, "a_Position"),
        GLES20.glGetAttribLocation(renderProgram, "a_Normal"),
        GLES20.glGetAttribLocation(renderProgram, "a_Color"));

    renderer = new CircleBars(renderParams);  //Init scene.
    audioAnalyser = new Analyser(renderer); //Init Analyser, must be done after renderer.

    GLES20.glEnableVertexAttribArray(renderer.scene.renderParams.vertexParam);
    GLES20.glEnableVertexAttribArray(renderer.scene.renderParams.normalParam);
    GLES20.glEnableVertexAttribArray(renderer.scene.renderParams.colourParam);

    checkGLError("Render program params");

    //Floor.
    renderer.scene.add(new Plane(200, 0, 200, new float[]{-100, -floorDepth, -100}, renderer.scene.renderParams));
    checkGLError("floor created");

    checkGLError("onSurfaceCreated");
  }

  public void changeRenderer(Renderer renderer) {
    audioAnalyser.onDestroy();
    th


    audioAnalyser = new Analyser(renderer); //Init Analyser, must be done after renderer.

    GLES20.glEnableVertexAttribArray(renderer.scene.renderParams.vertexParam);
    GLES20.glEnableVertexAttribArray(renderer.scene.renderParams.normalParam);
    GLES20.glEnableVertexAttribArray(renderer.scene.renderParams.colourParam);

    checkGLError("Render program params");

    //Floor.
    renderer.scene.add(new Plane(200, 0, 200, new float[]{-100, -floorDepth, -100}, renderer.scene.renderParams));
  }

  @Override
  public void onPause() {
    audioAnalyser.onPause(isFinishing());
    super.onPause();
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onRendererShutdown() {
    Log.i(TAG, "onRendererShutdown");
  }

  @Override
  public void onDestroy() {
    audioAnalyser.onDestroy();
    super.onDestroy();
  }

  /**
   * Converts a raw text file into a string.
   *
   * @param resId The resource ID of the raw text file about to be turned into a shader.
   * @return The context of the text file, or null in case of error.
   */
  private String readRawTextFile(int resId) {
    InputStream inputStream = getResources().openRawResource(resId);
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append("\n");
      }
      reader.close();
      return sb.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Prepares OpenGL ES before we draw a frame.
   *
   * @param headTransform The head transformation in the new frame.
   */
  @Override
  public void onNewFrame(HeadTransform headTransform) {

    // Build the camera matrix and apply it to the ModelView.
    Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

    headTransform.getHeadView(headView, 0);

    // Update the 3d audio engine with the most recent head rotation.
    headTransform.getQuaternion(headRotation, 0);

    checkGLError("onReadyToDraw");
  }

  /**
   * Draws a frame for an eye.
   *
   * @param eye The eye to render. Includes all required transformations.
   */
  @Override
  public void onDrawEye(Eye eye) {
    checkGLError("Old error caught in onDrawEye");
    GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

    // Apply the eye transformation to the camera.
    Matrix.multiplyMM(renderer.scene.view, 0, eye.getEyeView(), 0, camera, 0);

    // Set the position of the light
    Matrix.multiplyMV(renderer.scene.lightPosInEyeSpace, 0, renderer.scene.view, 0, LIGHT_POS_IN_WORLD_SPACE, 0);


    // Build the ModelView and ModelViewProjection matrices
    // for calculating cube position and light.
    renderer.scene.perspective = eye.getPerspective(Z_NEAR, Z_FAR);

    GLES20.glUseProgram(renderProgram);
    renderer.render(); //Render the scene.
    checkGLError("Error rendering, caught at highest level.");
  }

  @Override
  public void onFinishFrame(Viewport viewport) {
  }

  /**
   * Called when the Cardboard trigger is pulled.
   */
  @Override
  public void onCardboardTrigger() {
    Log.i(TAG, "onCardboardTrigger");
  }

  /**
   * Check if user is looking at object by calculating where the object is in eye-space.
   *
   * @return true if the user is looking at the object.
   */
  private boolean isLookingAtObject() {
    float[] initVec = {0, 0, 0, 1.0f};
    float[] objPositionVec = new float[4];

    // Convert object space to camera space. Use the headView from onNewFrame.
    //Matrix.multiplyMV(objPositionVec, 0, modelView, 0, initVec, 0);

    float pitch = (float) Math.atan2(objPositionVec[1], -objPositionVec[2]);
    float yaw = (float) Math.atan2(objPositionVec[0], -objPositionVec[2]);

    return Math.abs(pitch) < PITCH_LIMIT && Math.abs(yaw) < YAW_LIMIT;
  }
}
