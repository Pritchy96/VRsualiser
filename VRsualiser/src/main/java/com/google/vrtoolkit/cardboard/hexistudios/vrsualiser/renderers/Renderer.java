package com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.renderers;

import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.RenderParams;
import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.Scene;

/**
 * Created by Tom on 27/03/2016.
 */
public abstract class Renderer {

  public Scene scene;

  public Renderer(RenderParams renderParams) {
    scene = new Scene(renderParams);
  }

  public void render() {
    scene.redraw();
  }
}
