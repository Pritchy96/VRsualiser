package com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items;

import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.RenderParams;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Tom on 25/03/2016.
 */
public class Triangle extends RenderItem {

  private float vertices[] = {};

  public float[] getVertices() {
    return vertices;
  }

  public void setVertices(float[] vertices) {
    this.vertices = vertices;
    ByteBuffer vertByteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
    vertByteBuf.order(ByteOrder.nativeOrder());
    vertexBuf.clear();
    vertexBuf = vertByteBuf.asFloatBuffer();
    vertexBuf.put(vertices);
    vertexBuf.position(0);
  }

  public float[] getColors() {
    return colors;
  }

  public void setColors(float[] colors) {
    this.colors = colors;
    ByteBuffer colourByteBuf = ByteBuffer.allocateDirect(colors.length * 4);
    colourByteBuf.order(ByteOrder.nativeOrder());
    colourBuf.clear();
    colourBuf = colourByteBuf.asFloatBuffer();
    colourBuf.put(colors);
    colourBuf.position(0);
  }

  private float colors[] = {};

  private byte indices[] = {0, 1, 2};

  @Override
  public boolean isVisible() {
    return super.isVisible();
  }

  public Triangle(float[] v1, float[] v2, float[] v3,
                  RenderParams renderParams) {
    super(new float[]{0, 0, 0}, renderParams);

    ByteBuffer vertByteBuf, colourByteBuf;
    vertices = new float[]{v1[0], v1[1], v1[2],
                           v2[0], v2[1], v2[2],
                           v3[0], v3[1], v3[2]};

      colors = new float[] {
          0.01f, 0.29f, 0.58f, 1.0f,
          0.01f, 0.29f, 0.58f, 1.0f,
          0.13f, 0.75f, 0.77f, 1.0f,
          0.01f, 0.29f, 0.58f, 1.0f
      };

      indexBuf = ByteBuffer.allocateDirect(indices.length);
      indexBuf.put(indices);
      indexBuf.position(0);

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


