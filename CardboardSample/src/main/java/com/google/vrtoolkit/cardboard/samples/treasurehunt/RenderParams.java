package com.google.vrtoolkit.cardboard.samples.treasurehunt;

/**
 * Just holds each of the renderParam constants to clear up constructors.
 */
public class RenderParams {
  public final int lightPosParam, modelLocalParam, modelViewParam,
      modelViewProjectionParam, normalParam, vertexParam, colourParam;

  public RenderParams( int lightPosParam, int modelLocalParam, int modelViewParam,
                       int modelViewProjectionParam, int vertexParam, int normalParam,
                       int colourParam) {
    //Set parameters to local vars.
    this.lightPosParam = lightPosParam;
    this.modelLocalParam = modelLocalParam;
    this.modelViewParam = modelViewParam;
    this.modelViewProjectionParam = modelViewProjectionParam;
    this.vertexParam = vertexParam;
    this.normalParam = normalParam;
    this.colourParam = colourParam;
  }
}
