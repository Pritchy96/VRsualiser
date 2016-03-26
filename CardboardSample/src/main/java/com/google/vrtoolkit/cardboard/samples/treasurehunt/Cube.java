package com.google.vrtoolkit.cardboard.samples.treasurehunt;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Tom on 25/03/2016.
 */
public class Cube {
  private FloatBuffer mVertexBuffer;
  private FloatBuffer mColorBuffer;
  private ByteBuffer mIndexBuffer;

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

  public Cube(float x, float y, float z, float width,
              float height, float depth) {

    vertices = new float[]{
        x,         y,          z,
        x + width, y,          z,
        x + width, y + height, z,
        x,         y + height, z,
        x,         y,           z + depth,
        x + width, y,           z + depth,
        x + width, y + height,  z + depth,
        x,         y + height,  z + depth
    };


    ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
    byteBuf.order(ByteOrder.nativeOrder());
    mVertexBuffer = byteBuf.asFloatBuffer();
    mVertexBuffer.put(vertices);
    mVertexBuffer.position(0);

    byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
    byteBuf.order(ByteOrder.nativeOrder());
    mColorBuffer = byteBuf.asFloatBuffer();
    mColorBuffer.put(colors);
    mColorBuffer.position(0);

    mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
    mIndexBuffer.put(indices);
    mIndexBuffer.position(0);
  }

  public void draw(int cubePositionParam, int cubeColorParam, int cubeModelViewProjectionParam,
                   float[] modelViewProjection) {

    //GLES20.glVertexAttribPointer(floorNormalParam, 3, GLES20.GL_FLOAT, false, 0, floorNormals);
    //GLES20.glVertexAttribPointer(floorColorParam, 4, GLES20.GL_FLOAT, false, 0, floorColors);


    GLES20.glUniformMatrix4fv(cubeModelViewProjectionParam, 1, false, modelViewProjection, 0);

    GLES20.glFrontFace(GLES20.GL_CCW);
    //mVertexBuffer.position(0);

    GLES20.glVertexAttribPointer(cubePositionParam, 3, GLES20.GL_FLOAT, false,
        0, mVertexBuffer);

    // Pass in the color information
   // mColorBuffer.position(0);
    GLES20.glVertexAttribPointer(cubeColorParam, 4, GLES20.GL_FLOAT, false,
        0, mColorBuffer);


    GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_BYTE,
        mIndexBuffer);
  }
}
