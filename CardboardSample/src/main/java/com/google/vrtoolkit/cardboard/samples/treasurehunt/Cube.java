package com.google.vrtoolkit.cardboard.samples.treasurehunt;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Tom on 25/03/2016.
 */
public class Cube {

  private FloatBuffer verticesBuf, colourBuf;
  private ByteBuffer indexBuf;
  private float[] modelLocal, modelView, modelViewProjection;

  private float vertices[] = {};

  private float colors[] = {
      0.0f,  1.0f,  0.0f,  1.0f,
      0.0f,  1.0f,  0.0f,  1.0f,
      1.0f,  0.5f,  0.0f,  1.0f,
      1.0f,  0.5f,  0.0f,  1.0f,
      1.0f,  0.0f,  0.0f,  1.0f,
      1.0f,  0.0f,  0.0f,  1.0f,
      0.0f,  0.0f,  1.0f,  1.0f,
      1.0f,  0.0f,  1.0f,  1.0f
  };

  private byte indices[] = {
      0, 4, 5, 0, 5, 1,
      1, 5, 6, 1, 6, 2,
      2, 6, 7, 2, 7, 3,
      3, 7, 4, 3, 4, 0,
      4, 7, 6, 4, 6, 5,
      3, 0, 1, 3, 1, 2
  };

  public Cube(float width,float height, float depth, float[] localPosition) {

    vertices = new float[]{
        0,         0,          0,
        0 + width, 0,          0,
        0 + width, 0 + height, 0,
        0,         0 + height, 0,
        0,         0,           0 + depth,
        0 + width, 0,           0 + depth,
        0 + width, 0 + height,  0 + depth,
        0,         0 + height,  0 + depth
    };


    ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
    byteBuf.order(ByteOrder.nativeOrder());
    verticesBuf = byteBuf.asFloatBuffer();
    verticesBuf.put(vertices);
    verticesBuf.position(0);

    byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
    byteBuf.order(ByteOrder.nativeOrder());
    colourBuf = byteBuf.asFloatBuffer();
    colourBuf.put(colors);
    colourBuf.position(0);

    indexBuf = ByteBuffer.allocateDirect(indices.length);
    indexBuf.put(indices);
    indexBuf.position(0);

    modelLocal = new float[16];
    modelView = new float[16];
    modelViewProjection = new float[16];

    updateModelPosition(localPosition);
  }

  /**
   * Updates the cube model position.
   */
  private void updateModelPosition(float modelPosition[]) {
    Matrix.setIdentityM(modelLocal, 0);
    Matrix.translateM(modelLocal, 0, modelPosition[0], modelPosition[1], modelPosition[2]);
  }

  public void draw(int lightPosParam, int modelLocalParam, int modelViewParam,
                   int modelViewProjectionParam, int vertexParam, int colorParam,
                   float[] lightPosInEyeSpace, float[] view, float[] perspective) {

    Matrix.multiplyMM(modelView, 0, view, 0, modelLocal, 0);
    Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);

    // Set ModelView, MVP, //position, normals, and color.
    GLES20.glUniform3fv(lightPosParam, 1, lightPosInEyeSpace, 0);

    //Converts Model Space (I.E (0, 0, 0) == object center) to world Space (Everything relative to some arbitrary 0, 0, 0)
    GLES20.glUniformMatrix4fv(modelLocalParam, 1, false, modelLocal, 0);

    //Converts World space to view Space, 'such a way that each coordinate is as seen from the camera or viewer's point of view.'
    GLES20.glUniformMatrix4fv(modelViewParam, 1, false, modelView, 0);

    //Converts view space to clip space, i.e adding perspective. No, I have no idea either.
    GLES20.glUniformMatrix4fv(modelViewProjectionParam, 1, false, modelViewProjection, 0);

    //Position, normals, and color.
    GLES20.glVertexAttribPointer(vertexParam, 3, GLES20.GL_FLOAT, false, 0, verticesBuf);
    //GLES20.glVertexAttribPointer(cubeNormalParam, 3, GLES20.GL_FLOAT, false, 0, cubeNormals);
    GLES20.glVertexAttribPointer(colorParam, 4, GLES20.GL_FLOAT, false, 0, colourBuf);

    GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_BYTE, indexBuf);
  }

  public float[] getModelLocal() {
    return modelLocal;
  }

  public void setModelLocal(float[] modelLocal) {
    this.modelLocal = modelLocal;
  }
}


