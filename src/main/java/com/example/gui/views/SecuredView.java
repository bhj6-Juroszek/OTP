package com.example.gui.views;

import com.example.daoLayer.entities.User;
import com.example.gui.ui.DashboardUI;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;

public abstract class SecuredView extends AbsoluteLayout {

    private User user =(User)VaadinSession.getCurrent().getAttribute("loggedInCustomer");
    abstract protected void prepareView();

    protected void authorize(DashboardUI ui)
    {

        if(user !=null) {
            prepareView();
        }
        else{
            ui.getNavigator().navigateTo("mainView");
        }
    }
    protected void authorizeAdmin(DashboardUI ui)
    {
        if(user !=null && user.getRole() ) {
            prepareView();
        }
        else{
            ui.getNavigator().navigateTo("mainView");

        }
    }
}
