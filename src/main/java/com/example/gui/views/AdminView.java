package com.example.gui.views;


import com.example.daoLayer.daos.UsersDAO;
import com.example.daoLayer.entities.*;
import com.example.gui.ui.DashboardUI;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Theme("reindeer")
@org.springframework.stereotype.Component
@SpringView(name = "adminView")
public class AdminView extends SecuredView implements View {

    @Autowired
    UsersDAO customersRep;

    private DashboardUI currentUI=(DashboardUI) UI.getCurrent();
    private Boolean clicked=false;
    private Boolean confirmation=false;
    private Window window=new Window();
    private Window customerWindow=new Window();
    private Window deliveryWindow=new Window();
    private Window itemWindow=new Window();
    private Window configWindow=new Window();
    private Window categoryWindow= new Window();
    private Window confirmationWindow=new Window();
    private TextField windowTextField=new TextField("Value");
    private List<User> customersList=new ArrayList<User>();
    private List<Window> windows=new ArrayList<Window>();
    private Button configUsersButton=null;
    private Button logoutButton=null;
    private Button backButton=null;
    private Panel adminPanel=null;
    private Panel configPanel=null;
    private AbsoluteLayout windowLayout=new AbsoluteLayout();
    private AbsoluteLayout windowItemLayout=new AbsoluteLayout();
    private AbsoluteLayout windowCustomerLayout=new AbsoluteLayout();
    private AbsoluteLayout windowCategoryLayout=new AbsoluteLayout();
    private AbsoluteLayout confirmationWindowLayout=new AbsoluteLayout();
    private AbsoluteLayout defaultLayout=null;
    private AbsoluteLayout adminLayout=null;
    private AbsoluteLayout configUsersLayout=null;
    private TextField newCustomerName=new TextField("Name:");
    private TextField newCustomerSurname=new TextField("Surname:");
    private TextField newCustomerAdress=new TextField("Adress:");
    private TextField newCustomerMail=new TextField("Mail:");
    private TextField newCustomerLogin=new TextField("Login:");
    private TextField newCustomerPassword=new TextField("Password:");
    private CheckBox newCustomerRole=new CheckBox("Role");
    private TextField newCustomerCategories=new TextField("Categories");

    @PostConstruct
    private void init()
    {
        authorizeAdmin((DashboardUI) UI.getCurrent());
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
    protected void prepareView() {
        confirmationWindow.removeAllCloseShortcuts();
        confirmationWindow.setCaption("Confirm changes");
        confirmationWindow.setWidth(250.0f, Unit.PIXELS);
        confirmationWindow.setClosable(false);
        confirmationWindow.setHeight(250.0f, Unit.PIXELS);
        confirmationWindow.setPosition(500, 250);
        confirmationWindow.setModal(true);
        confirmationWindow.setDraggable(false);
        confirmationWindow.setResizable(false);
        confirmationWindow.setVisible(false);
        confirmationWindow.setImmediate(true);
        currentUI.addWindow(confirmationWindow);
        setWidth("100%");
        setHeight("2000px");
        currentUI.addWindow(window);
        addStyleName(Reindeer.LAYOUT_BLUE);
        window.removeAllCloseShortcuts();
        window.setCaption("Changing data");
        window.setWidth(300.0f, Unit.PIXELS);
        window.setClosable(false);
        window.setHeight(300.0f, Unit.PIXELS);
        window.setPosition(500, 250);
        window.setModal(true);
        window.setDraggable(false);
        window.setResizable(false);
        window.setVisible(false);
        window.setImmediate(true);
        windows.add(itemWindow);
        windows.add(configWindow);
        windows.add(customerWindow);
        windows.add(categoryWindow);
        windows.add(deliveryWindow);
        for (Window window : windows) {
            window.setWidth(300.0f, Unit.PIXELS);
            window.setClosable(false);
            window.setHeight(900.0f, Unit.PIXELS);
            window.setPosition(500, 100);
            window.setModal(true);
            window.setDraggable(false);
            window.setResizable(false);
            window.setVisible(false);
            window.setImmediate(true);
        }
        itemWindow.removeAllCloseShortcuts();
        windowLayout.addStyleName(Reindeer.LAYOUT_BLACK);
        windowItemLayout.addStyleName(Reindeer.LAYOUT_BLACK);
        windowCustomerLayout.addStyleName(Reindeer.LAYOUT_BLACK);
        configPanel = new Panel();
        configPanel.setImmediate(true);
        configPanel.setHeight("100%");
        configPanel.setWidth("100%");
        defaultLayout = new AbsoluteLayout();
        defaultLayout.addStyleName(Reindeer.LAYOUT_BLUE);
        configPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        configUsersLayout = new AbsoluteLayout();
        configUsersLayout.setHeight("100%");
        configUsersLayout.setWidth("100%");
        configUsersLayout.addStyleName(Reindeer.LAYOUT_BLUE);
        logoutButton = new Button("Logout", (Button.ClickListener) event -> {
            VaadinSession.getCurrent().setAttribute("loggedInCustomer",null);
            currentUI.getNavigator().navigateTo("mainView");
        });
        backButton = new Button("Back", (Button.ClickListener) event -> currentUI.getNavigator().navigateTo("mainView"));



        /***********************************************************************************************************/
        //                                      ADMIN PANEL
        /***********************************************************************************************************/
        adminPanel = new Panel();
        adminPanel.setWidth("800px");
        adminPanel.setHeight("200px");
        adminPanel.addStyleName(Reindeer.PANEL_LIGHT);
        adminLayout = new AbsoluteLayout();
        adminLayout.setWidth("800px");
        adminLayout.setHeight("200px");
        adminLayout.addStyleName(Reindeer.LAYOUT_BLUE);
        configUsersButton = new Button("Users", (Button.ClickListener) event -> configUsers());
        backButton.addStyleName(Runo.BUTTON_BIG);
        backButton.addStyleName(Runo.BUTTON_DEFAULT);
        logoutButton.addStyleName(Runo.BUTTON_BIG);
        logoutButton.addStyleName(Runo.BUTTON_DEFAULT);
        configUsersButton.addStyleName(Runo.BUTTON_BIG);
        backButton.addStyleName(Runo.BUTTON_DEFAULT);
        logoutButton.addStyleName(Runo.BUTTON_DEFAULT);
        adminLayout.addComponent(backButton, "top:50%;left:0;");
        adminLayout.addComponent(logoutButton, "top:50%;left:100px;");
        adminLayout.addComponent(configUsersButton, "top:50%;left:200px;");
        Label label = new Label("PANEL");
        label.addStyleName(Runo.LABEL_H2);
        adminLayout.addComponent(label, "top:25%;left:50%;");
        adminPanel.setContent(adminLayout);
        addComponent(adminPanel, "top:5%;left:15%;" + "bottom:60%;right:15%");
        configPanel.setContent(defaultLayout);
        addComponent(configPanel, "top:20%;left:10%;" + "bottom:30%;right:10%;");
        /***********************************************************************************************************/


        /***********************************************************************************************************/
        //                                      WINDOW ADD CUSTOMER
        /***********************************************************************************************************/
        currentUI.addWindow(customerWindow);
        customerWindow.setContent(windowCustomerLayout);
        windowCustomerLayout.addComponent(new Label("NEW CUSTOMER"), "top:10px;left:100px");
        windowCustomerLayout.addComponent(newCustomerName, "top:80px;left:100px");
        windowCustomerLayout.addComponent(newCustomerAdress, "top:240px;left:100px");
        windowCustomerLayout.addComponent(newCustomerMail, "top:320px;left:100px");
        windowCustomerLayout.addComponent(newCustomerLogin, "top:400px;left:100px");
        windowCustomerLayout.addComponent(newCustomerPassword, "top:480px;left:100px");
        windowCustomerLayout.addComponent(newCustomerRole, "top:560px;left:100px");
        Button windowAddCustomer = new Button("OK", (ClickListener) -> {
            User custom = new User(newCustomerName.getValue(),
                    newCustomerAdress.getValue(),
                    newCustomerMail.getValue(),
                    newCustomerLogin.getValue(),
                    newCustomerPassword.getValue(),
                    newCustomerRole.getValue(),
                    ""
            );
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(custom.getPassword());
            custom.setPassword(hashedPassword);
            if (customersRep.saveToDB(custom)) {
                customersList.add(customersRep.getCustomerByLogin(custom.getLogin()));
                Notification.show("Changes has been saved successfully!",
                        Notification.Type.HUMANIZED_MESSAGE);
            } else {
                Notification.show("Some error occured, possible duplicate data",
                        Notification.Type.WARNING_MESSAGE);
            }
            newCustomerName.setValue("");
            newCustomerSurname.setValue("");
            newCustomerAdress.setValue("");
            newCustomerMail.setValue("");
            newCustomerLogin.setValue("");
            newCustomerPassword.setValue("");
            newCustomerRole.setValue(false);
            customerWindow.setVisible(false);
            configUsers();
        });
        windowCustomerLayout.addComponent(windowAddCustomer, "top:700px;left:100px");
}       /***********************************************************************************************************/




      
    /***********************************************************************************************************/
    //                                          CONFIG USERS
    /***********************************************************************************************************/
    private void configUsers()
    {
        customersList.clear();
        customerListLoad();
        windowLayout.removeAllComponents();
        windowLayout.addComponent(windowTextField,"bottom:50%;left:80px;");
        configUsersLayout.removeAllComponents();
        Table usersTable=new Table("UsersConfig");
        usersTable.addStyleName(Reindeer.LAYOUT_BLUE);
        usersTable.addContainerProperty("Name", String.class, null);
        usersTable.addContainerProperty("Surname", String.class, null);
        usersTable.addContainerProperty("Mail", String.class, null);
        usersTable.addContainerProperty("Remove", Button.class, null);
        usersTable.addContainerProperty("Change name", Button.class, null);
        usersTable.addContainerProperty("Change surname", Button.class, null);
        usersTable.addContainerProperty("Change login", Button.class, null);
        usersTable.addContainerProperty("Change adress", Button.class, null);
        int i=1;
        for(User user :customersList)
        {
            Button remove = new Button("Remove", (Button.ClickListener) event -> {
                confirm(user);
            });
            Button name = new Button("Change name", (Button.ClickListener) event -> {
                    windowTextField.setValue(user.getName());

            Button windowButtonName=new Button("OK", (ClickListener)-> {
                user.setName(windowTextField.getValue());
                updateCustomers(user);
            });
            windowLayout.addComponent(windowButtonName,"bottom:30px;left:115px;");
            window.setVisible(true);
        });

            Button login = new Button("Change login", (Button.ClickListener) event -> {
                windowTextField.setValue(user.getLogin());
                Button windowButtonLogin=new Button("OK", (ClickListener)-> {
                    user.setLogin(windowTextField.getValue());
                    updateCustomers(user);
                });
                windowLayout.addComponent(windowButtonLogin,"bottom:30px;left:115px;");
                window.setContent(windowLayout);
                window.setVisible(true);
            });
            Button adress = new Button("Change adress", (Button.ClickListener) event -> {
                windowTextField.setValue(user.getAdress());
                Button windowButtonAdress=new Button("OK", (ClickListener)-> {
                    user.setAdress(windowTextField.getValue());
                    updateCustomers(user);
                });
                windowLayout.addComponent(windowButtonAdress,"bottom:30px;left:115px;");
                window.setContent(windowLayout);
                window.setVisible(true);
            });
            usersTable.addItem(new Object[]{user.getName(),
                    user.getMail(),
                    remove,
                    name,
                    login,
                    adress,
            }, i++);
        }
        Button addUserButton=new Button("Add new User", (ClickListener)-> {
            customerWindow.setVisible(true);
        });
        window.setContent(windowLayout);
        configUsersLayout.addComponent(usersTable);
        configUsersLayout.addComponent(addUserButton,"bottom:45%;left:100px;");
        configPanel.setContent(configUsersLayout);
    }

    private void confirm(User user)
    {
            confirmationWindow.setVisible(true);
        confirmationWindow.setContent(confirmationWindowLayout);
        Button  confirm = new Button("YES", (Button.ClickListener) event -> {
            customersRep.delete(user);
            configUsers();
            confirmationWindow.setVisible(false);
        });

        Button decline = new Button("NO", (Button.ClickListener) event -> {
            confirmationWindow.setVisible(false);
        });
        confirmationWindow.setContent(confirmationWindowLayout);
        confirmationWindowLayout.addComponent(new Label("Do you want to confirm operation?"), "top:10px;left:100px");
        confirmationWindowLayout.addComponent(confirm, "top:75px;left:50px");
        confirmationWindowLayout.addComponent(decline, "top:75px;left:100px");
    }

    private void updateCustomers(User user)
    {
        if(customersRep.updateRecord(user)) {
            window.setVisible(false);
            windowTextField.setValue("");
            configUsers();
            Notification.show("Changes has been saved successfully!",
                    Notification.Type.HUMANIZED_MESSAGE);
        }else{
            Notification.show("Some error occured, possible duplicate data",
                    Notification.Type.WARNING_MESSAGE);
        }
    }
    /***********************************************************************************************************/



    /***********************************************************************************************************/
    //                             LOAD DATA FROM DATABASE
    /***********************************************************************************************************/
    private void customerListLoad() {
        long y = 1;
        for (int i = 0; i < customersRep.getSize(); i++) {
            while (customersRep.getCustomerById(y) == null) {
                y++;
            }
                customersList.add(customersRep.getCustomerById(y));
            y++;
        }
    }



    /***********************************************************************************************************/

    AdminView()
    {
        currentUI=(DashboardUI) UI.getCurrent();
    }
}
