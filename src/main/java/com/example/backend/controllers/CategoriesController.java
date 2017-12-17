package com.example.backend.controllers;

import com.example.daoLayer.entities.Category;
import com.example.backend.model.CategoriesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Nonnull;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@Controller
@RequestMapping("/")
public class CategoriesController {

  private CategoriesManager categoriesManager;

  @RequestMapping(value = "/categoryList", method = GET)
  public @ResponseBody
  List<Category> getCategories() {
    return categoriesManager.getCategories();
  }

  @Autowired
  public void setCategoriesManager(@Nonnull final CategoriesManager categoriesManager) {
    this.categoriesManager = categoriesManager;
  }
}
