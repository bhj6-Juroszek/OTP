package com.example.gui.views;

import com.example.daoLayer.DAOHandler;
import com.example.daoLayer.daos.CitiesDAO;
import com.example.daoLayer.daos.UsersDAO;
import com.example.backend.utils.MailManager;
import com.example.daoLayer.entities.User;
import com.example.gui.ui.DashboardUI;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Theme("reindeer")
@PreserveOnRefresh
@org.springframework.stereotype.Component
@SpringView(name = "registerView")
public class RegisterView extends VerticalLayout implements View {

    @Autowired
    private UsersDAO customerRep;
    private Label info=new Label("Finish your registration");
    private TextField name=new TextField("First Name");
    private TextField surname=new TextField("Second Name");
    final CitiesDAO citiesDAO = DAOHandler.citiesDAO;
    TextField city = new TextField("City");
    private TextField postCode=new TextField("Post code");
    private TextField apartamentNumber=new TextField("Apartment/House number");
    private Button backButton=null;
    private DashboardUI currentUI=(DashboardUI)UI.getCurrent();
    private Button registerButton=null;
    private FormLayout registerContent=null;
    private TextField registerLogin=null;
    private TextField registerMail=null;
    private PasswordField registerPassword=null;
    private PasswordField registerPassword2=null;
    private Panel registerPanel=null;

    @PostConstruct
    private void init()
    {
        setStyleName("backColor");
        setHeight("2500px");
        setWidth("2500px");
        /*****************************************************************************************************************/
        //                                   REGISTER PANEL
        /*****************************************************************************************************************/
        registerPanel = new Panel("REGISTER");
        registerPanel.setWidth("100%");
        registerPanel.setHeight("100%");
        HorizontalLayout registerLayout1 = new HorizontalLayout();
        registerLayout1.setSpacing(true);
        HorizontalLayout registerLayout2 = new HorizontalLayout();
        registerLayout2.setSpacing(true);
        registerLogin = new TextField("Write Login");
        registerMail = new TextField("Write Mail");
        registerPassword = new PasswordField("Write Password");
        registerPassword2 = new PasswordField("Write Password again");
        registerButton = new Button("Finish registration", (Button.ClickListener) event -> {
            if(registerPassword.getValue().equals(registerPassword2.getValue())) {
                if (!customerRep.existsAnother(registerMail.getValue(), "mail",(long)-1)) {
                    if (!customerRep.existsAnother(registerLogin.getValue(), "login",(long)-1)) {
                        if(!registerMail.getValue().equals("")&&!registerLogin.getValue().equals("")&&!registerPassword.getValue().equals("")) {
                            if(name.getValue().equals("")||surname.getValue().equals("")||city.getValue().equals("")||postCode.getValue().equals("")||apartamentNumber.getValue().equals(""))
                            {
                                Notification.show("Fill all fields!",
                                        Notification.Type.WARNING_MESSAGE);
                            }
                            else {
                                String token = UUID.randomUUID().toString();
                                MailManager sender=new MailManager();
                                if(sender.sendMail("Juroszek",
                                        registerMail.getValue(),
                                        "Automatically generated Message",
                                        "Your account has been created. Click https://software-architecture.herokuapp.com/confirm.html?id="+token+" to finish registration;"))
                                {
                                    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                                    String hashedPassword = passwordEncoder.encode(registerPassword.getValue());

                                    customerRep.saveToDB(new User(name.getValue(),
                                            city.getValue() + " " + postCode.getValue() + "nr: " + apartamentNumber.getValue(),
                                            registerMail.getValue(),
                                            registerLogin.getValue(),
                                            hashedPassword,
                                            false,
                                            "",
                                            token));
                                    Notification.show("Your account has been created!",
                                            Notification.Type.HUMANIZED_MESSAGE);
                                }
                                else {
                                    Notification.show("Your account has not been created! Wrong mail adress!",
                                            Notification.Type.HUMANIZED_MESSAGE);
                                }
                                name.setValue("");
                                surname.setValue("");
                                registerMail.setValue("");
                                registerLogin.setValue("");
                                city.setValue("");
                                postCode.setValue("");
                                apartamentNumber.setValue("");
                                currentUI.getNavigator().navigateTo("mainView");

                            }

                        }
                        else{
                            Notification.show("Fill all fields",
                                    Notification.Type.WARNING_MESSAGE);
                            registerMail.setValue("");
                        }
                    } else {
                        Notification.show("Login already taken",
                                Notification.Type.WARNING_MESSAGE);
                        registerLogin.setValue("");
                    }
                } else {
                    Notification.show("Mail already taken",
                            Notification.Type.WARNING_MESSAGE);
                    registerMail.setValue("");
                }
            }
            else{
                Notification.show("Both passwords must be the same!",
                        Notification.Type.WARNING_MESSAGE);
                registerPassword.setValue("");
                registerPassword2.setValue("");

            }
        });
        registerContent = new FormLayout();
        registerLayout1.addComponents(registerLogin, registerPassword);
        registerLayout2.addComponents(registerMail, registerPassword2);
        registerContent.addComponents(registerLayout1, registerLayout2);
        registerContent.addStyleName("backColor");
        backButton = new Button("Back to mainView", (Button.ClickListener) event -> currentUI.getNavigator().navigateTo("mainView"));
        addStyleName("backColor");

        registerContent.addComponents(info,
                name,
                surname,
                city,
                postCode,
                apartamentNumber,
                registerButton,
                backButton);
        registerPanel.setContent(registerContent);
        addComponent(registerPanel);
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}