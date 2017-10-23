package com.example.model;

import com.example.daos.CategoriesDAO;
import com.example.daos.CustomersDAO;
import com.example.entities.Category;
import com.example.entities.Customer;

import java.util.ArrayList;

/**
 * Created by Bartek on 2017-03-11.
 */
public class CategoriesManager {

    private CategoriesDAO categoriesRep=CategoriesDAO.getInstance();
    private CustomersDAO customersRep=CustomersDAO.getInstance();


    public ArrayList<Category> getCategories()
    {
        return categoriesRep.getAll();
    }

    public ArrayList<Category> getUserCategories(Customer customer)
    {
        ArrayList<Category> result=new ArrayList<Category>();
        String categories[]=customer.getCategories().split(",");
        if(customer.getCategories()!="") {
            for (int i = 0; i < categories.length; i++) {
                if(!categories[i].equals("")) {
                    result.add(categoriesRep.getCategoryById(Long.valueOf(categories[i])));
                }
            }
        }

        return result;
    }


    public ArrayList<Customer> getTrainersFromCategory(Category category)
    {
        return customersRep.getCustomersWithCategory(category);
    }

    public Category getCategory(long id)
    {
        return categoriesRep.getCategoryById(id);
    }

}
