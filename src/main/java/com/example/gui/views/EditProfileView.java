package com.example.gui.views;

import com.example.DAOS.CustomersDAO;
import com.example.Model.ProfilesManager;
import com.example.entities.Customer;
import com.example.gui.ui.DashboardUI;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by Bartek on 2017-03-08.
 */

@Theme("reindeer")
@org.springframework.stereotype.Component
@SpringView(name = "profileEditView")
public class EditProfileView extends SecuredView implements View {


    @Autowired
    private CustomersDAO userRepo;

    private RichTextArea textArea=null;
    private DashboardUI currentUI=(DashboardUI) UI.getCurrent();
    private Panel loginPanel=currentUI.getLoginPanel();
    private ProfilesManager profiles=new ProfilesManager();
    private Image logo=new Image("", new ThemeResource("icons/logo2.png"));
    @Override
    protected void prepareView() {
        addComponent(loginPanel);
        addStyleName("backColor");
        logo.setHeight("200px");
        logo.setWidth("350px");
        logo.addClickListener(event ->currentUI.getNavigator().navigateTo("mainView"));
        addComponent(logo, "top:0%;left:65%");
        Customer customer=(Customer) VaadinSession.getCurrent().getAttribute("loggedInCustomer");
        if(profiles.getUserProfile(customer)==null)
        {
            profiles.createProfile(customer);
        }
        setHeight("1500px");
        textArea=new RichTextArea();
        textArea.setValue(profiles.getUserProfile(customer).getText());
        textArea.setWidth("1500px");
        textArea.setHeight("1500px");
        addComponent(textArea,"top:15%; bottom:35%;"+"left:25%; right:25%;");
        textArea.addValueChangeListener(event -> System.out.println(textArea.getValue()));

        NativeButton saveChangesButton=new NativeButton("Save changes",(ClickListener) -> {
            profiles.changeProfileText(customer, textArea.getValue());
            Notification.show("Data saved succesfully",
                    Notification.Type.HUMANIZED_MESSAGE);
        });
        saveChangesButton.setWidth("120px");
        saveChangesButton.setHeight("75px");
        saveChangesButton.setStyleName("panelButtonSmall");


        addComponent(saveChangesButton,"top:35%;"+"left:5%;");


    }

    @PostConstruct
    private void init()
    {
        authorize((DashboardUI) UI.getCurrent());
    }
    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {

    }
}
