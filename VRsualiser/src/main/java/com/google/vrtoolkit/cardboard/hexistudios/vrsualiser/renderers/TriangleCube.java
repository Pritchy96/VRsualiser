package com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.renderers;

import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.RenderParams;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items.Triangle;

import java.util.ArrayList;

/**
 * A cube of flashing triangles.
 */
public class TriangleCube extends Renderer {
  ArrayList<Triangle> faces = new ArrayList<Triangle>();

  public TriangleCube(RenderParams renderParams, int size) {
    super(renderParams);

    //Define triangles. Bit of a mess to read, but it works well.
    //Easy to understand if drawn out.
    for (int x = -size; x < size; x++) {
      for (int y = -size; y < size; y++) {
        faces.add(new Triangle(   //Top1
            new float[]{x, size, y},
            new float[]{x + 1, size, y},
            new float[]{x + 1, size, y + 1},
            scene.renderParams));

        faces.add(new Triangle( //Top2
            new float[]{x, size, y},
            new float[]{x, size, y + 1},
            new float[]{x + 1, size, y + 1},
            scene.renderParams));

        faces.add(new Triangle( //Bottom1
            new float[]{x + 1, -size, y + 1},
            new float[]{x + 1, -size, y},
            new float[]{x, -size, y},
            scene.renderParams));

        faces.add(new Triangle( //Bottom2
            new float[]{x + 1, -size, y + 1},
            new float[]{x, -size, y + 1},
            new float[]{x, -size, y},
            scene.renderParams));

        faces.add(new Triangle(
            new float[]{-size, x, y},
            new float[]{-size, x, y + 1},
            new float[]{-size, x + 1, y},
            scene.renderParams));

        faces.add(new Triangle(
            new float[]{-size, x + 1, y},
            new float[]{-size, x, y + 1},
            new float[]{-size, x + 1, y + 1},
            scene.renderParams));

        faces.add(new Triangle(
            new float[]{size, x, y},
            new float[]{size, x, y + 1},
            new float[]{size, x + 1, y},
            scene.renderParams));

        faces.add(new Triangle(
            new float[]{size, x + 1, y + 1},
            new float[]{size, x, y + 1},
            new float[]{size, x + 1, y},
            scene.renderParams));

        faces.add(new Triangle(
            new float[]{x, y, -size},
            new float[]{x + 1, y, -size},
            new float[]{x, y + 1, -size},
            scene.renderParams));

        faces.add(new Triangle(
            new float[]{x + 1, y + 1, -size},
            new float[]{x + 1, y, -size},
            new float[]{x, y + 1, -size},
            scene.renderParams));

        faces.add(new Triangle(
            new float[]{x, y, size},
            new float[]{x + 1, y, size},
            new float[]{x, y + 1, size},
            scene.renderParams));

        faces.add(new Triangle(
            new float[]{x + 1, y + 1, size},
            new float[]{x + 1, y, size},
            new float[]{x, y + 1, size},
            scene.renderParams));
      }
    }

    //add triangles to scene.
    for (Triangle t : faces) {
      scene.add(t); //Add for rendering.
    }
  }

  //Convert each vert on a cube to a sphere - google 'quadsphere'
  public float[] spherify(float[] oldVerts) {
    float[] newVerts = new float[oldVerts.length];

    newVerts[0] = (float) (oldVerts[0] * Math.sqrt(1.0 - (oldVerts[1] * oldVerts[1] / 2.0) - (oldVerts[2] * oldVerts[2] / 2.0)
        + (oldVerts[1] * oldVerts[1] * oldVerts[2] * oldVerts[2] / 3.0)));

    newVerts[1] = (float) (oldVerts[1] * Math.sqrt(1.0 - (oldVerts[2] * oldVerts[2] / 2.0) - (oldVerts[0] * oldVerts[0] / 2.0)
        + (oldVerts[2] * oldVerts[2] * oldVerts[0] * oldVerts[0] / 3.0)));

    newVerts[2] = (float) (oldVerts[2] * Math.sqrt(1.0 - (oldVerts[0] * oldVerts[0] / 2.0) - (oldVerts[1] * oldVerts[1] / 2.0)
        + (oldVerts[0] * oldVerts[0] * oldVerts[1] * oldVerts[1] / 3.0)));

    return newVerts;
  }

  @Override
  public void updateVisualiserWave(byte[] waveBytes) {
    super.updateVisualiserWave(waveBytes);
  }

  @Override
  public void updateVisualiserFft(byte[] fftBytes) {
    super.updateVisualiserFft(fftBytes);
    int numOfFaces = faces.size();
    int index = (int)Math.floor(fftBytes.length/numOfFaces);

    for (int i = 0; i < numOfFaces; i++) {
      Triangle face = faces.get(i); //Get current face

      byte rfk = fftBytes[index * i];
      byte ifk = fftBytes[index * i + 1];
      float magnitude = (rfk * rfk + ifk * ifk);
      float dbValue = (float)(Math.log10(magnitude)+1/3);  //Temp number massaging to get roughly between 1 and  0.
      //Hard capping as temp number massaging is far from perfect.
      if (dbValue < 0) {dbValue = 0.0f;} else if (dbValue > 1) { dbValue = 1;}

      float colours[] = new float[] { //Construct new colour array.
          0f, dbValue, dbValue, 1.0f,
          0f, dbValue, dbValue, 1.0f,
          0f, dbValue, dbValue, 1.0f,
          0f, dbValue, dbValue, 1.0f
      };

      face.setColors(colours);  //Send new colour arya to triangle for processing to colBuffer.
    }
  }

  @Override
  public void render() {
    super.render(); //Draw all visible objects in the object list.
  }
}

/* OLD spherify method.
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