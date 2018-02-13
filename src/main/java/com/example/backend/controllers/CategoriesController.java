package com.example.backend.controllers;

import com.example.backend.services.CategoriesService;
import com.example.daoLayer.entities.Category;
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

  private CategoriesService categoriesService;

  @RequestMapping(value = "/categories", method = GET)
  public @ResponseBody
  List<Category> getCategories() {
    return categoriesService.getCategories();
  }

  @Autowired
  public void setCategoriesService(@Nonnull final CategoriesService categoriesService) {
    this.categoriesService = categoriesService;
  }
}
