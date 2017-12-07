package com.example.model;

import com.example.daoLayer.DAOHandler;
import com.example.daoLayer.daos.CategoriesDAO;
import com.example.daoLayer.daos.UsersDAO;
import com.example.daoLayer.entities.Category;
import com.example.daoLayer.entities.User;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bartek on 2017-03-11.
 */
@Component
public class CategoriesManager {

    private CategoriesDAO categoriesDAO = DAOHandler.categoriesDAO;
    private UsersDAO usersDAO = DAOHandler.usersDAO;


    public List<Category> getCategories()
    {
        return categoriesDAO.getAll();
    }

    public List<Category> getUserCategories(@Nonnull final User user)
    {
        return categoriesDAO.getAll();
    }


    public ArrayList<User> getTrainersFromCategory(@Nonnull final Category category)
    {
        return usersDAO.getCustomersWithCategory(category);
    }

    public Category getCategory(long id)
    {
        return categoriesDAO.getCategoryById(id);
    }

}
