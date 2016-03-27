package com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.renderers;

import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.RenderParams;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items.Cube;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items.Plane;

import java.util.ArrayList;

/**
 * Digital EQ-like bars.
 */
public class TestRenderer extends Renderer {

  public TestRenderer(RenderParams renderParams) {
    super(renderParams);

    for (int i = 0; i < 10; i++) {
      scene.add(new Cube(2, 2, 2, new float[]{0, 2 * i, -10f}, scene.renderParams));
    }
  }

  @Override
  public void updateVisualiserWave(byte[] waveBytes) {
    super.updateVisualiserWave(waveBytes);
  }

  @Override
  public void updateVisualiserFft(byte[] fftBytes) {
    super.updateVisualiserFft(fftBytes);
  }

  @Override
  public void render() {
    super.render(); //Draw all visible objects in the object list.
  }
}
