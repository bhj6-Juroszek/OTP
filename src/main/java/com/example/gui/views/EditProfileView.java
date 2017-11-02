package com.example.gui.views;

import com.example.daoLayer.daos.UsersDAO;
import com.example.model.ProfilesManager;
import com.example.daoLayer.entities.User;
import com.example.gui.ui.DashboardUI;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
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
    private UsersDAO userRepo;

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
        User user =(User) VaadinSession.getCurrent().getAttribute("loggedInCustomer");
        if(profiles.getUserProfile(user)==null)
        {
            profiles.createProfile(user);
        }
        setHeight("1500px");
        textArea=new RichTextArea();
        textArea.setValue(profiles.getUserProfile(user).getText());
        textArea.setWidth("1500px");
        textArea.setHeight("1500px");
        addComponent(textArea,"top:15%; bottom:35%;"+"left:25%; right:25%;");
        textArea.addValueChangeListener(event -> System.out.println(textArea.getValue()));

        NativeButton saveChangesButton=new NativeButton("Save changes",(ClickListener) -> {
            profiles.changeProfileText(user, textArea.getValue());
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
