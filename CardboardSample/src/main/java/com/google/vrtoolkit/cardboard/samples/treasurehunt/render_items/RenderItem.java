package com.google.vrtoolkit.cardboard.samples.treasurehunt.render_items;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.google.vrtoolkit.cardboard.samples.treasurehunt.RenderParams;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;


public abstract class RenderItem {
  //Render parameters.
  protected final RenderParams renderParams;

  protected boolean visible = true;

  //Position, normals, colour, index buffers.
  protected FloatBuffer vertexBuf, colourBuf, normalBuf;
  protected ByteBuffer indexBuf;

  //
  protected float[] modelLocal, modelView, modelViewProjection;

  public RenderItem(float[] localPosition, RenderParams renderParams) {

    //Set parameters to local vars.
    this.renderParams = renderParams;

    //Initialise
    modelLocal = new float[16];
    modelView = new float[16];
    modelViewProjection = new float[16];

    //Set object to inital position in local space.
    updateModelPosition(localPosition);

  }

  //Updates the model position in local space.
  public void updateModelPosition(float modelPosition[]) {
    Matrix.setIdentityM(modelLocal, 0);
    Matrix.translateM(modelLocal, 0, modelPosition[0], modelPosition[1], modelPosition[2]);
  }

  //Render object with standard settings. Returns true if successfully rendered.
  public boolean redraw(float[] lightPosInEyeSpace, float[] view, float[] perspective) {
    if (!visible) { return true; } //Do not render if invisible.

    Matrix.multiplyMM(modelView, 0, view, 0, modelLocal, 0);
    Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);

    // Set ModelView, MVP, //position, normals, and color.
    GLES20.glUniform3fv(renderParams.lightPosParam, 1, lightPosInEyeSpace, 0);

    //Converts Model Space (I.E (0, 0, 0) == object center) to world Space (Everything relative to some arbitrary 0, 0, 0)
    GLES20.glUniformMatrix4fv(renderParams.modelLocalParam, 1, false, modelLocal, 0);

    //Converts World space to view Space, 'such a way that each coordinate is as seen from the camera or viewer's point of view.'
    GLES20.glUniformMatrix4fv(renderParams.modelViewParam, 1, false, modelView, 0);

    //Converts view space to clip space, i.e adding perspective. No, I have no idea either.
    GLES20.glUniformMatrix4fv(renderParams.modelViewProjectionParam, 1, false, modelViewProjection, 0);

    //Position, normals, and color.
    GLES20.glVertexAttribPointer(renderParams.vertexParam, 3, GLES20.GL_FLOAT, false, 0, vertexBuf);
    //GLES20.glVertexAttribPointer(cubeNormalParam, 3, GLES20.GL_FLOAT, false, 0, cubeNormals);
    GLES20.glVertexAttribPointer(renderParams.colourParam, 4, GLES20.GL_FLOAT, false, 0, colourBuf);

    if (indexBuf != null) {
      GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexBuf.capacity(), GLES20.GL_UNSIGNED_BYTE, indexBuf);
    } else {
      GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexBuf.capacity());
    }

    return true; //report successful render.
  }

  public float[] getModelLocal() {
    return modelLocal;
  }

  public void setModelLocal(float[] modelLocal) {
    this.modelLocal = modelLocal;
  }
}
