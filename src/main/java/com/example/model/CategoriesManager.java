package com.example.model;

import com.example.daoLayer.daos.CategoriesDAO;
import com.example.daoLayer.daos.UsersDAO;
import com.example.daoLayer.entities.Category;
import com.example.daoLayer.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bartek on 2017-03-11.
 */
@Component
public class CategoriesManager {

  private final CategoriesDAO categoriesDAO;
  private final UsersDAO usersDAO;

  @Autowired
  public CategoriesManager(@Nonnull final CategoriesDAO categoriesDAO, @Nonnull final UsersDAO usersDAO) {
    this.categoriesDAO = categoriesDAO;
    this.usersDAO = usersDAO;
  }

  public List<Category> getCategories() {
    return categoriesDAO.getAll();
  }

  public List<Category> getUserCategories(@Nonnull final User user) {
    return categoriesDAO.getAll();
  }

  public ArrayList<User> getTrainersFromCategory(@Nonnull final Category category) {
    return usersDAO.getCustomersWithCategory(category);
  }

  public Category getCategory(long id) {
    return categoriesDAO.getCategoryById(id);
  }

}
