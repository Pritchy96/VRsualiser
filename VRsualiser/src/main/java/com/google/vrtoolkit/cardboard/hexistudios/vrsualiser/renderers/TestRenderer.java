package com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.renderers;

import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.RenderParams;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items.Triangle;

import java.util.ArrayList;

/**
 * Digital EQ-like bars.
 */
public class TestRenderer extends Renderer {
  ArrayList<Triangle> faces = new ArrayList<Triangle>();

  public TestRenderer(RenderParams renderParams) {
    super(renderParams);

    glLineWidth(2.5);
    glColor3f(1.0, 0.0, 0.0);
    glBegin(GL_LINES);
    glVertex3f(0.0, 0.0, 0.0);
    glVertex3f(15, 0, 0);
    glEnd();


  }


  @Override
  public void updateVisualiserWave(byte[] waveBytes) {
    super.updateVisualiserWave(waveBytes);
  }

  @Override
  public void updateVisualiserFft(byte[] fftBytes) {
    super.updateVisualiserFft(fftBytes);

    int divisions = faces.size();
    for (int i = 0; i < divisions; i++) {

      Triangle face = faces.get(i);

      int index = (int)Math.floor(fftBytes.length/divisions);

      byte rfk = fftBytes[index * i];
      byte ifk = fftBytes[index * i + 1];
      float magnitude = (rfk * rfk + ifk * ifk);
      float dbValue = (float) (Math.log10(magnitude)/2);
      System.out.println(dbValue);
      if (dbValue < 0) {dbValue = 0.5f;} else if (dbValue > 1) { dbValue = 1;}

      float colours[] = new float[] {
          (float)0f, (float)dbValue, (float)dbValue, 1.0f,
          (float)0f, (float)dbValue, (float)dbValue, 1.0f,
          (float)0f, (float)dbValue, (float)dbValue, 1.0f,
          (float)0f, (float)dbValue, (float)dbValue, 1.0f
      };

      face.setColors(colours);
    }
  }






  @Override
  public void render() {
    super.render(); //Draw all visible objects in the object list.
  }
}

/*
  public void spherify(Triangle t) {
    float[] newVerts = new float[t.getVertices().length];
    float[] oldVerts = t.getVertices();

    //Loop through each vert (divide by three because float[] = x, y, z of each vert in series.
    for (int i = 0; i < newVerts.length / 3; i++) {
      newVerts[i * 3] = (float) (oldVerts[i * 3] * Math.sqrt(1.0 - (oldVerts[i * 3 + 1] * oldVerts[i * 3 + 1] / 2.0) - (oldVerts[i * 3 + 2] * oldVerts[i * 3 + 2] / 2.0)
          + (oldVerts[i * 3 + 1] * oldVerts[i * 3 + 1] * oldVerts[i * 3 + 2] * oldVerts[i * 3 + 2] / 3.0)));

      newVerts[i * 3 + 1] = (float) (oldVerts[i * 3 + 1] * Math.sqrt(1.0 - (oldVerts[i * 3 + 2] * oldVerts[i * 3 + 2] / 2.0) - (oldVerts[i * 3] * oldVerts[i * 3] / 2.0)
          + (oldVerts[i * 3 + 2] * oldVerts[i * 3 + 2] * oldVerts[i * 3] * oldVerts[i * 3] / 3.0)));

      newVerts[i * 3 + 2] = (float) (oldVerts[i * 3 + 2] * Math.sqrt(1.0 - (oldVerts[i * 3] * oldVerts[i * 3] / 2.0) - (oldVerts[i * 3 + 1] * oldVerts[i * 3 + 1] / 2.0)
          + (oldVerts[i * 3] * oldVerts[i * 3] * oldVerts[i * 3 + 1] * oldVerts[i * 3 + 1] / 3.0)));
    }

    t.setVertices(newVerts);
  }

 */