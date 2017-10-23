package com.example.gui.views;

import com.example.daoLayer.entities.Customer;
import com.example.gui.ui.DashboardUI;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;

public abstract class SecuredView extends AbsoluteLayout {

    private Customer customer=(Customer)VaadinSession.getCurrent().getAttribute("loggedInCustomer");
    abstract protected void prepareView();

    protected void authorize(DashboardUI ui)
    {

        if(customer!=null) {
            prepareView();
        }
        else{
            ui.getNavigator().navigateTo("mainView");
        }
    }
    protected void authorizeAdmin(DashboardUI ui)
    {
        if(customer!=null && customer.getRole() ) {
            prepareView();
        }
        else{
            ui.getNavigator().navigateTo("mainView");

        }
    }
}
