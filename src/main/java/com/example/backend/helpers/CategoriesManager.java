package com.example.backend.helpers;

import com.example.daoLayer.daos.CategoriesDAO;
import com.example.daoLayer.daos.UsersDAO;
import com.example.daoLayer.entities.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Bartek on 2017-03-11.
 */
@Component
public class CategoriesManager {
  private static final Logger LOGGER = LogManager.getLogger(CategoriesManager.class);
  private final CategoriesDAO categoriesDAO;
  private final UsersDAO usersDAO;

  @Autowired
  public CategoriesManager(@Nonnull final CategoriesDAO categoriesDAO, @Nonnull final UsersDAO usersDAO) {
    this.categoriesDAO = categoriesDAO;
    this.usersDAO = usersDAO;
  }

  public List<Category> getCategories() {
    final List<Category> categories = categoriesDAO.getAll();
    LOGGER.info("Loaded list of categories: {}", categories);
    return categories;
  }

  public Category getCategory(@Nonnull final String id) {
    return categoriesDAO.getCategoryById(id);
  }

}
