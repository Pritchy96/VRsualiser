package com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items;

import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.RenderParams;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Tom on 25/03/2016.
 */
public class Cube extends RenderItem {

  private float vertices[] = {};

  private float colors[] = {
      0.0f, 1.0f, 0.0f, 1.0f,
      0.0f, 1.0f, 0.0f, 1.0f,
      1.0f, 0.5f, 0.0f, 1.0f,
      1.0f, 0.5f, 0.0f, 1.0f,
      1.0f, 0.0f, 0.0f, 1.0f,
      1.0f, 0.0f, 0.0f, 1.0f,
      0.0f, 0.0f, 1.0f, 1.0f,
      1.0f, 0.0f, 1.0f, 1.0f
  };

  private byte indices[] = {
      0, 4, 5, 0, 5, 1,
      1, 5, 6, 1, 6, 2,
      2, 6, 7, 2, 7, 3,
      3, 7, 4, 3, 4, 0,
      4, 7, 6, 4, 6, 5,
      3, 0, 1, 3, 1, 2
  };

  public Cube(float width, float height, float depth, float[] localPosition,
              RenderParams renderParams) {
    super(localPosition, renderParams);

    vertices = new float[]{
        0, 0, 0,
        0 + width, 0, 0,
        0 + width, 0 + height, 0,
        0, 0 + height, 0,
        0, 0, 0 + depth,
        0 + width, 0, 0 + depth,
        0 + width, 0 + height, 0 + depth,
        0, 0 + height, 0 + depth
    };

    ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
    byteBuf.order(ByteOrder.nativeOrder());
    vertexBuf = byteBuf.asFloatBuffer();
    vertexBuf.put(vertices);
    vertexBuf.position(0);

    byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
    byteBuf.order(ByteOrder.nativeOrder());
    colourBuf = byteBuf.asFloatBuffer();
    colourBuf.put(colors);
    colourBuf.position(0);

    indexBuf = ByteBuffer.allocateDirect(indices.length);
    indexBuf.put(indices);
    indexBuf.position(0);
  }

  public void setHeight(float newHeight) {
    vertices[7] = newHeight;
    vertices[10] = newHeight;
    vertices[19] = newHeight;
    vertices[22] = newHeight;

    ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
    byteBuf.order(ByteOrder.nativeOrder());
    vertexBuf = byteBuf.asFloatBuffer();
    vertexBuf.put(vertices);
    vertexBuf.position(0);
  }

  @Override
  public boolean redraw(float[] lightPosInEyeSpace, float[] view, float[] perspective) {
    super.redraw(lightPosInEyeSpace, view, perspective);
    return true;
  }


}


