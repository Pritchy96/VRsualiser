package com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.renderers;

import android.widget.Toast;

import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.CardboardOverlayView;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.R;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.RenderParams;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items.Cube;

import java.util.ArrayList;

/**
 * Digital EQ-like bars.
 */
public class SimpleBars extends Renderer {

  ArrayList<ArrayList<Cube>> barCubes;

  public SimpleBars(RenderParams renderParams) {
    super(renderParams);
    barCubes = new ArrayList<ArrayList<Cube>>();
  }

  //Params[0] = numOfRows, params[1] = numOfColumns.
  @Override
  public void setup(Object[] params) {
    //Setup number of columns

    float scale = 0.2f;

    for (int i = 0; i < (Integer)params[0]; i++) {
      barCubes.add(new ArrayList<Cube>());

      for (float j = 0; j < (Integer)params[1]; j++) {
        Cube tempRef = new Cube(scale, scale, scale, new float[]{(j*scale) - (((Integer)params[0])/2), i*scale, -10f}, scene.renderParams);
        barCubes.get(i).add(tempRef);
        scene.add(tempRef); //Add cube to renderList.
      }

      //System.out.println(((params[0]).toString() + ", " + params[1].toString()));
    }
  }

  @Override
  public void updateVisualiserWave(byte[] waveBytes) {
    super.updateVisualiserWave(waveBytes);
  }

  @Override
  public void updateVisualiserFft(byte[] fftBytes) {
    super.updateVisualiserFft(fftBytes);

    int increment = 900/ barCubes.get(0).size();

    for (int i = 0; i < barCubes.size(); i++) {
      for (int j = 0; j < barCubes.get(0).size(); j++) {
        Double fftVal = (double)(fftBytes[j*increment])/6.4;
        barCubes.get(i).get(j).setVisible(fftVal > i);
      }
    }
  }

  @Override
  public void render() {
    super.render(); //Draw all visible objects in the object list.
  }
}
