package com.example.gui.views;

import com.example.daoLayer.daos.UsersDAO;
import com.example.model.ProfilesManager;
import com.example.model.RatesManager;
import com.example.model.UserManager;
import com.example.daoLayer.entities.User;
import com.example.daoLayer.entities.Profile;
import com.example.daoLayer.entities.Rate;
import com.example.gui.ui.DashboardUI;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Bartek on 2017-03-08.
 */

@Theme("mytheme")
@org.springframework.stereotype.Component
@SpringView(name = "profileSeeView")
public class SeeProfileView extends AbsoluteLayout implements View {

    @Autowired
    UsersDAO userRepo;
    public final URL DEFAULT_IMAGE_PATH=new URL("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQCeShBiPnm0h2AP4S5X2lISkauLSNNSqN61tndLC0KJYwt3EY1E04WPi0");
    private DashboardUI currentUI=(DashboardUI) UI.getCurrent();
    private ProfilesManager profiles=new ProfilesManager();
    private RatesManager manager=new RatesManager();
    private User user =(User) VaadinSession.getCurrent().getAttribute("customerToSee");
    private User loggedInUser =(User) VaadinSession.getCurrent().getAttribute("loggedInCustomer");
    private Profile profile=profiles.getUserProfile(user);
    private TextField comment = new TextField("Write your comment here");
    private Panel loginPanel=currentUI.getLoginPanel();
    private Image logo=new Image("", new ThemeResource("icons/logo2.png"));
    private VerticalLayout userLayout=new VerticalLayout();

    public SeeProfileView() throws MalformedURLException {
    }


    protected void prepareView() {

        logo.setHeight("200px");
        logo.setWidth("350px");
        logo.addClickListener(event ->currentUI.getNavigator().navigateTo("mainView"));
        addComponent(logo, "top:0%;left:65%");
        addComponent(loginPanel);
        addStyleName("backColor");
        if(profile==null)
        {
            profiles.createProfile(user);
            profile=profiles.getUserProfile(user);
        }
        setHeight("2000px");
        Label profileLabel=new Label("",ContentMode.HTML);
        profileLabel.setValue(
                profile.getText()
        );
        profileLabel.setWidth("1500px");
        profileLabel.setHeight("1500px");
        addComponent(profileLabel,"top:15%;left:30%;");
        double rate=manager.getProfileAverageRate(profile);
        int numberOfRates=manager.getProfileRates(
                profiles.getUserProfile(user)
        ).size();
        String textFormatted=String.format("Average rate for this user: %.2g (from:%d rates;)"
        ,rate,numberOfRates);
        addComponent(new Label(textFormatted
        ),"top:50%; left:45%;");

        createRateButtons();
        prepareUserDetails();
    }


    @PostConstruct
    private void init()
    {
        prepareView();
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {

    }

    private void createRateButtons()
    {
        Rate rate;
        if(loggedInUser !=null && loggedInUser.getId()!=profile.getUserId()) {
            if ((rate = manager.ifUserRated(loggedInUser, profile)) != null) {
                addComponent(new Label("You rated: "
                        + rate.getValue()
                ), "top:55%; left:45%;");
            } else {
                comment.setHeight("25px");
                comment.setWidth("200px");
                comment.setValue("");
                int cssShift = 40;
                for (int i = 1; i < 11; i++) {
                    final int finalI = i;
                    Button rateButton = new Button(String.format("[%d]", i), (Button.ClickListener) event -> {manager.rateProfile(
                        loggedInUser
                            , profile
                            , finalI
                            , comment.getValue());
                        Notification.show("Your rate has been saved",
                                Notification.Type.HUMANIZED_MESSAGE);
                    });
                    rateButton.addStyleName(Reindeer.BUTTON_LINK);
                    rateButton.addStyleName("panelButton");
                    addComponent(rateButton, String.format("top:55%%; left:%d%%;", cssShift));
                    cssShift += 2;
                }

                addComponent(comment,"top:57%;left:35%;right:35%;");
            }
        }

    }


    private void prepareUserDetails()
    {

        userLayout.setWidth("400px");
        userLayout.setHeightUndefined();
        userLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Image img=new Image();
        if(UserManager.testImage(user.getImageUrl()))
        {
            img.setSource(new ExternalResource(user.getImageUrl()));
        }
        else{
            img.setSource(new ExternalResource(DEFAULT_IMAGE_PATH));
        }
        img.setHeight("100px");
        img.setWidth("100px");
        userLayout.addComponent(img);
        Label nameInfoLabel=new Label(user.getName());
        nameInfoLabel.setStyleName("whiteBigText");
        nameInfoLabel.setHeight("75px");
        nameInfoLabel.setWidthUndefined();
        userLayout.addComponent(nameInfoLabel);
        Label mailLabel=new Label(String.format("Mail: %s", user.getMail()));
        mailLabel.setStyleName("whiteBigText");
        mailLabel.setHeight("75px");
        mailLabel.setWidthUndefined();
        userLayout.addComponent(mailLabel);
        NativeButton seeSchedule=new NativeButton(String.format("Go to %s's schedule", user.getName()), (Button.ClickListener) event -> {
            VaadinSession.getCurrent().setAttribute("customerToSee", user);
            currentUI.getNavigator().navigateTo("scheduleView");
        });
        seeSchedule.setStyleName("panelButton");
        seeSchedule.setWidth("400px");
        seeSchedule.setHeight("75px");
        userLayout.addComponent(seeSchedule);
        userLayout.setStyleName("filters");
        addComponent(userLayout,"top:25%;left:1%;");
    }
}
