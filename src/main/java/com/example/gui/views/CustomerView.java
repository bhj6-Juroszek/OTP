package com.example.gui.views;

import com.example.DAOS.CustomersDAO;
import com.example.Model.UserManager;
import com.example.entities.Customer;
import com.example.gui.ui.DashboardUI;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;

@Theme("Runo")
@org.springframework.stereotype.Component
@SpringView(name = "customerView")
public class CustomerView extends SecuredView implements View {
    private TextField name=new TextField("Name");
    private TextField surname=new TextField("Surname");
    private TextField adress=new TextField("Adress");
    private PasswordField passwordText=new PasswordField("New Password");
    private PasswordField passwordText2=new PasswordField("Password again");
    private NativeButton saveChangesButton=null;
    private String mail=null;
    private DashboardUI currentUI=(DashboardUI)UI.getCurrent();
    private Customer currentCustomer=(Customer)VaadinSession.getCurrent().getAttribute("loggedInCustomer");
    private Panel loginPanel=currentUI.getLoginPanel();
    private TextField imageUrl=new TextField("");
    public final URL DEFAULT_IMAGE_PATH=new URL("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQCeShBiPnm0h2AP4S5X2lISkauLSNNSqN61tndLC0KJYwt3EY1E04WPi0");
    @Autowired
    private CustomersDAO customerRep;

    public CustomerView() throws MalformedURLException {
    }

    @PostConstruct
    private void init()
    {
        authorize((DashboardUI) UI.getCurrent());
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    protected void prepareView()
    {
        Image img=new Image();
        if(UserManager.testImage(currentCustomer.getImageUrl()))
        {
            img.setSource(new ExternalResource(currentCustomer.getImageUrl()));
        }
        else{
            img.setSource(new ExternalResource(DEFAULT_IMAGE_PATH));
        }
        img.setWidth("150px");
        img.setHeight("150px");
        addComponent(img,"top:105px;left:37%");
        addComponent(imageUrl,"top:250px;left:37%");
        NativeButton checkImage=new NativeButton("Check your image url");
        checkImage.addClickListener(event -> {
            if(UserManager.testImage(imageUrl.getValue()))
            {
                img.setSource(new ExternalResource(imageUrl.getValue()));
            }
            else{
                img.setSource(new ExternalResource(DEFAULT_IMAGE_PATH));
            }
        });
        checkImage.setWidth("300px");
        checkImage.setHeight("75px");
        checkImage.setStyleName("panelButton");
        addComponent(checkImage,"top:290px;left:37%");
        addComponent(loginPanel);
        Image logo=new Image("", new ThemeResource("icons/logo2.png"));
        logo.setHeight("200px");
        logo.setWidth("350px");
        addComponent(logo, "top:0%;left:65%");
        currentCustomer=(Customer)VaadinSession.getCurrent().getAttribute("loggedInCustomer");
        addStyleName("backColor");
        setWidth("100%");
        setHeight("1500px");
        mail=currentCustomer.getMail();
        name.setValue(currentCustomer.getName());
        addComponent(name,"top:70px;left:25%");
        surname.setValue(currentCustomer.getSurname());
        addComponent(surname,"top:105px;left:25%");
        adress.setValue(currentCustomer.getAdress());
        addComponent(adress,"top:140px;left:25%");
        addComponent(passwordText,"top:180px;left:25%");
        addComponent(passwordText2,"top:220px;left:25%");
        saveChangesButton=new NativeButton("Save changes", (ClickListener)-> {
           saveChanges();
        });
        saveChangesButton.setHeight("75px");
        saveChangesButton.setWidth("300px");
        saveChangesButton.setStyleName("panelButton");
        addComponent(saveChangesButton,"top:500px;left:5%");
    }
    private void saveChanges()
    {
        if(!passwordText.getValue().equals("")) {
            if (passwordText.getValue().equals(passwordText2.getValue())) {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String hashedPassword = passwordEncoder.encode(passwordText.getValue());
                Customer savedCustomer=new Customer(name.getValue(),
                        surname.getValue(),
                        adress.getValue(),
                        currentCustomer.getMail(),
                        currentCustomer.getLogin(),
                        hashedPassword,
                        currentCustomer.getRole(),
                        currentCustomer.getCategories(),
                        imageUrl.getValue(),
                        currentCustomer.getProfileId(),"");
                savedCustomer.setId(currentCustomer.getId());
                customerRep.updateRecord(
                        savedCustomer
                );

                Notification.show("Data changed",
                        Notification.Type.HUMANIZED_MESSAGE);
                VaadinSession.getCurrent().setAttribute("loggedInCustomer", null);
                currentUI.getNavigator().navigateTo("mainView");
            } else {
                Notification.show("Password can't be empty, and both passwords must be the same!",
                        Notification.Type.HUMANIZED_MESSAGE);
                passwordText.setValue("");
                passwordText2.setValue("");
            }
        }else{
            Customer savedCustomer=new Customer(name.getValue(),
                    surname.getValue(),
                    adress.getValue(),
                    currentCustomer.getMail(),
                    currentCustomer.getLogin(),
                    currentCustomer.getPassword(),
                    currentCustomer.getRole(),
                    currentCustomer.getCategories(),
                    imageUrl.getValue(),
                    currentCustomer.getProfileId(),
                    ""
            );
            savedCustomer.setId(currentCustomer.getId());
            customerRep.updateRecord(savedCustomer
                    );
            Notification.show("Data changed",
                    Notification.Type.HUMANIZED_MESSAGE);
            VaadinSession.getCurrent().setAttribute("loggedInCustomer",null);
            currentUI.getNavigator().navigateTo("mainView");

        }
    }

}