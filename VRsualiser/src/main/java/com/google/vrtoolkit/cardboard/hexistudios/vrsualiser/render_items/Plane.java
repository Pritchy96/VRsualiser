package com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items;

import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.RenderParams;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Tom on 25/03/2016.
 */
public class Plane extends RenderItem {

  private float vertices[] = {};

  private float colors[] = {};

  private byte indices[] = {3, 1, 0, 2, 3, 1};

  public Plane(float width, float height, float depth, float[] localPosition, boolean indexed,
               RenderParams renderParams) {
    super(localPosition, renderParams);

    ByteBuffer vertByteBuf, colourByteBuf;

    if (indexed) {
      vertices = new float[]{
          0, 0, 0,
          0 + width, 0, 0,
          0 + width, 0 + height, 0 + depth,
          0, 0 + height, 0+ depth,
      };

      colors = new float[] {
        0.01f, 0.29f, 0.58f, 1.0f,
          0.01f, 0.29f, 0.58f, 1.0f,
          0.13f, 0.75f, 0.77f, 1.0f,
          0.13f, 0.75f, 0.77f, 1.0f,
      };

      indexBuf = ByteBuffer.allocateDirect(indices.length);
      indexBuf.put(indices);
      indexBuf.position(0);
    } else {  //Setup non indexed buffers.
      vertices = new float[]{
          0, 0, 0,
          0 + width, 0, 0,
          0 + width, 0 + height, 0 + depth,
          0, 0, 0,
          0 + width, 0 + height, 0 + depth,
          0, 0 + height, 0+ depth,
      };

      colors = new float[]{
        0.0f, 1.0f, 0.0f, 1.0f,
          0.0f, 1.0f, 0.0f, 1.0f,
          0.0f, 1.0f, 0.0f, 1.0f,
          1.0f, 0.5f, 0.0f, 1.0f,
          1.0f, 0.5f, 0.0f, 1.0f,
          1.0f, 0.5f, 0.0f, 1.0f,
      };

      indexBuf = null;  //Super will render as arrays instead of indices if this is null.
    }

    vertByteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
    vertByteBuf.order(ByteOrder.nativeOrder());
    vertexBuf = vertByteBuf.asFloatBuffer();
    vertexBuf.put(vertices);
    vertexBuf.position(0);

    colourByteBuf = ByteBuffer.allocateDirect(colors.length * 4);
    colourByteBuf.order(ByteOrder.nativeOrder());
    colourBuf = colourByteBuf.asFloatBuffer();
    colourBuf.put(colors);
    colourBuf.position(0);
  }

  @Override
  public boolean redraw(float[] lightPosInEyeSpace, float[] view, float[] perspective) {
    super.redraw(lightPosInEyeSpace, view, perspective);
    return true;
  }


}


