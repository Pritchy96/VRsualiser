package com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.renderers;

import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.RenderParams;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items.Cube;

/**
 * Digital EQ-like bars, rendered in a cricle around the user.
 */
public class CircleBars extends Renderer {

  Cube[] barCubes;
  private final float radius = 15;
  private float maxBarNum, numOfBars, maxBarHeight, barDivisions = 4,
      rotDivisions, circumference;

  public CircleBars(RenderParams renderParams) {
    super(renderParams);
  }

  //Params[0] = max number of columns, params[1] = max number of rows
  @Override
  public void setup(Object[] params) {
    //Setup number of columns
    maxBarNum = (Integer)params[0];
    maxBarHeight = (Integer)params[1];

    numOfBars = maxBarNum/barDivisions;
    barCubes = new Cube[(int)numOfBars];  //Define number of bars.
    float scale = 0.6f;
    //float scale = radius * (float)Math.sin(rotDivisions);
    circumference =  2 * (float)Math.PI * radius;
    rotDivisions = ((float)Math.PI*2)/numOfBars; //rotational divisions: needed distance between each bar when spaced out in a circle.

    for (int i = 0; i < numOfBars; i++) {
      float theta = rotDivisions * i; //The position this bar is in the circle in radians.
      float x = radius * (float)Math.cos(theta);
      float y = radius * (float)Math.sin(theta);

      Cube tempRef = new Cube(scale, scale, scale, new float[]{x, -20f, y}, scene.renderParams);
      barCubes[i] = tempRef;
      scene.add(tempRef); //Add cube to renderList.
    }
  }

  @Override
  public void updateVisualiserWave(byte[] waveBytes) {
    super.updateVisualiserWave(waveBytes);
  }

  @Override
  public void updateVisualiserFft(byte[] fftBytes) {
    super.updateVisualiserFft(fftBytes);

    for (int i = 0; i < numOfBars; i++) {
      byte rfk = fftBytes[(int)(barDivisions * i)];
      byte ifk = fftBytes[(int)(barDivisions * i + 1)];
      float magnitude = (rfk * rfk + ifk * ifk);
      float dbValue = (float) ((10 * Math.log10(magnitude)));

      barCubes[i].setHeight(dbValue);
    }
  }

  @Override
  public void render() {
    super.render(); //Draw all visible objects in the object list.
  }
}
