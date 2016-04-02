package com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.renderers;

import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.RenderParams;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items.Cube;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items.Plane;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items.Triangle;

import java.util.ArrayList;

/**
 * Digital EQ-like bars.
 */
public class TestRenderer extends Renderer {

  public TestRenderer(RenderParams renderParams) {
    super(renderParams);

    for (int i = 0; i < 10; i++) {
      //scene.add(new Cube(2, 2, 2, new float[]{0, 2 * i, -10f}, scene.renderParams));
    }

    float width = 5, height = 4, depth = 5;

    Triangle top1 = new Triangle(
        new float[]{-width, height, -depth},
        new float[]{width, height, -depth},
        new float[]{width, height, depth},
        scene.renderParams),

        top2 = new Triangle(
            new float[]{-width, height, -depth},
            new float[]{-width, height, depth},
            new float[]{width, height, depth},
            scene.renderParams),

        bottom1 = new Triangle(
            new float[]{width, -height, depth},
            new float[]{width, -height, -depth},
            new float[]{-width, -height, -depth},
            scene.renderParams),

        bottom2 = new Triangle(
            new float[]{width, -height, depth},
            new float[]{-width, -height, depth},
            new float[]{-width, -height, -depth},
            scene.renderParams),

        left1 = new Triangle(
            new float[]{-width, -height, -depth},
            new float[]{-width, -height, depth},
            new float[]{-width, height, -depth},
            scene.renderParams),

        left2 = new Triangle(
            new float[]{-width, height, -depth},
            new float[]{-width, -height, depth},
            new float[]{-width, height, depth},
            scene.renderParams),

        right1 = new Triangle(
            new float[]{width, -height, -depth},
            new float[]{width, -height, depth},
            new float[]{width, height, -depth},
            scene.renderParams),

        right2 = new Triangle(
            new float[]{width, height, depth},
            new float[]{width, -height, depth},
            new float[]{width, height, -depth},
            scene.renderParams),

        front1 = new Triangle(
            new float[]{-width, -height, -depth},
            new float[]{width, -height, -depth},
            new float[]{-width, height, -depth},
            scene.renderParams),

        front2 = new Triangle(
            new float[]{width, height, -depth},
            new float[]{width, -height, -depth},
            new float[]{-width, height, -depth},
            scene.renderParams),

        back1 = new Triangle(
            new float[]{-width, -height, depth},
            new float[]{width, -height, depth},
            new float[]{-width, height, depth},
            scene.renderParams),

        back2 = new Triangle(
            new float[]{width, height, depth},
            new float[]{width, -height, depth},
            new float[]{-width, height, depth},
            scene.renderParams);


    scene.add(top1);
    scene.add(top2);
    scene.add(bottom1);
    scene.add(bottom2);
    scene.add(left1);
    scene.add(left2);
    scene.add(right1);
    scene.add(right2);
    scene.add(front1);
    scene.add(front2);
    scene.add(back1);
    scene.add(back2);
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
