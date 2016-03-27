package com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.renderers;

import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.RenderParams;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items.Cube;

/**
 * Digital EQ-like bars.
 */
public class SimpleBars extends Renderer {

  Cube[] barCubes;
  private int maxBarNum, numOfBars, maxBarHeight, divisions = 4;


  public SimpleBars(RenderParams renderParams) {
    super(renderParams);

  }

  //Params[0] = max number of columns, params[1] = max number of rows
  @Override
  public void setup(Object[] params) {
    //Setup number of columns
    maxBarNum = (Integer)params[0];
    maxBarHeight = (Integer)params[1];

    numOfBars = maxBarNum/divisions;
    barCubes = new Cube[numOfBars];  //Define number of bars.
    float scale = 0.8f;

    for (int x = 0; x < numOfBars; x++) {
      Cube tempRef = new Cube(scale, scale, scale, new float[]{(x - (numOfBars/2))*scale, 0, -20f}, scene.renderParams);
      barCubes[x] = tempRef;
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

    for (int i = 0; i < fftBytes.length/divisions; i++) {
      byte rfk = fftBytes[divisions * i];
      byte ifk = fftBytes[divisions * i + 1];
      float magnitude = (rfk * rfk + ifk * ifk);
      int dbValue = (int) (10 * Math.log10(magnitude));
      if (dbValue < 0) {dbValue = 0;}
      barCubes[i].setHeight(dbValue);
    }


  }

  @Override
  public void render() {
    super.render(); //Draw all visible objects in the object list.
  }
}
