package com.example.backend.controllersEntities.responses;

import java.util.List;

public class MaterialsResponse extends ResponseWithCode{
  private List<String> materialsPaths;

  public List<String> getMaterialsPaths() {
    return materialsPaths;
  }

  public void setMaterialsPaths(final List<String> materialsPaths) {
    this.materialsPaths = materialsPaths;
  }
}
