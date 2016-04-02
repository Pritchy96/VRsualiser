package com.google.vrtoolkit.cardboard.hexistudios.vrsualiser;

import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.render_items.RenderItem;

import java.util.ArrayList;

/**
 * Holds an array of RenderItems to be drawn as well as the variables needed to do so.
 */
public class Scene {
  private ArrayList<RenderItem> renderArray = new ArrayList<RenderItem>();
  public float[] lightPosInEyeSpace = new float[4], view, perspective;
  public RenderParams renderParams;

  public Scene(RenderParams renderParams) {
    view = new float[16];
    this.renderParams = renderParams;
  }

  //Add item to array for rendering.
  public void add(RenderItem item) {
    renderArray.add(item);
  }

  public void redraw() {
    for (RenderItem item : renderArray) {
     item.redraw(lightPosInEyeSpace, view, perspective);
    }
  }

  public ArrayList<RenderItem> getRenderArray() {
    return renderArray;
  }
}
