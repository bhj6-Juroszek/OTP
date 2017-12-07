package com.example.gui.views;

import com.example.AccountsApplication;
import com.example.backend.controllersEntities.responses.LoginResponse;
import com.example.daoLayer.DAOHandler;
import com.example.daoLayer.daos.CitiesDAO;
import com.example.daoLayer.daos.UsersDAO;
import com.example.daoLayer.entities.Category;
import com.example.daoLayer.entities.Training;
import com.example.daoLayer.entities.User;
import com.example.gui.ui.DashboardUI;
import com.example.model.CategoriesManager;
import com.example.model.PasswordReminder;
import com.example.model.TrainingManager;
import com.example.model.UserManager;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.log4j.Level.INFO;

@SpringView(name = "mainView")
@PreserveOnRefresh
@Theme("mytheme")
public class MainView extends AbsoluteLayout implements View {

  private final static Logger LOGGER = Logger.getLogger(AccountsApplication.class);
  private UsersDAO customerRep = DAOHandler.usersDAO;

  private User loggedInUser = (User) VaadinSession.getCurrent().getAttribute("loggedInCustomer");
  private ComboBox sortBy = null;
  private TextField area = null;
  private TextField maxPrice = null;
  private DateField initialDate = null;
  private DateField lastDate = null;
  private ComboBox citySelect = null;
  private UserManager userManager = new UserManager();
  private CategoriesManager catManager = new CategoriesManager();
  public final URL DEFAULT_IMAGE_PATH = new URL(
      "https://encrypted-tbn2.gstatic" +
          ".com/images?q=tbn:ANd9GcQCeShBiPnm0h2AP4S5X2lISkauLSNNSqN61tndLC0KJYwt3EY1E04WPi0");
  private DashboardUI currentUI = (DashboardUI) UI.getCurrent();
  private Label loginInfo = null;
  private Panel loginPanel;
  private CheckBox showOnline = new CheckBox("Show online trainings");

  public MainView() throws MalformedURLException {
  }

  private Image img = new Image();
  private PasswordReminder reminder;
  private VerticalLayout listLayout = new VerticalLayout();
  private TextField login = null;
  private PasswordField password = null;
  private Button loginButton = null;
  private VerticalLayout loginContent = null;
  private VerticalLayout loginContent2 = null;
  private HorizontalLayout panels = new HorizontalLayout();
  private Window window = new Window();

  @PostConstruct
  private void init() throws MySQLIntegrityConstraintViolationException {

    Image logo = new Image("", new ThemeResource("icons/logo2.png"));
    logo.setHeight("200px");
    logo.setWidth("350px");
    addComponent(logo, "top:0%;left:65%");
    loginPanel = currentUI.getLoginPanel();
    setWidth("2500px");
    setHeight("2500px");
    addStyleName("backColor");

    /*****************************************************************************************************************/
    //                                   LOGIN PANEL
    /*****************************************************************************************************************/
    loginPanel.setHeight("400px");
    loginPanel.setWidth("400px");
    login = new TextField("Login");
    password = new PasswordField("Password");
    loginInfo = new Label();
    loginInfo.addStyleName("whiteBigText");
    loginInfo.setWidth("100px");
    loginButton = new Button("Login", (Button.ClickListener) event -> {
      final LoginResponse response = userManager.login(login.getValue(), password.getValue());
      LOGGER.log(INFO, "Login request for: user="+login.getValue()+", pass="+password.getValue()+" return code="+response.getResponseCode());
      if (response.getResponseCode() != 1) {
        Notification.show("Wrong user or Password, try again!",
            Notification.Type.WARNING_MESSAGE);
      } else {
        loggedInUser = response.getUserContext().getUser();
        if (!loggedInUser.getConfirmation().equals("")) {
          Notification.show("Account not active. Check your mail adress",
              Notification.Type.WARNING_MESSAGE);
        } else {
          VaadinSession.getCurrent().setAttribute("loggedInCustomer", loggedInUser);
          if (loggedInUser != null && UserManager.testImage(loggedInUser.getImageUrl())) {
            img.setSource(new ExternalResource(loggedInUser.getImageUrl()));
          } else {
            img.setSource(new ExternalResource(DEFAULT_IMAGE_PATH));
          }
          refresh();

        }
      }
    }
    );
    loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    loginButton.setStyleName(Reindeer.LAYOUT_BLUE);
    Button registrationButton = new Button("Dont have account? click here!",
        (Button.ClickListener) event -> currentUI.getNavigator().navigateTo("registerView"));
    registrationButton.setStyleName(Reindeer.BUTTON_LINK);
    Button remindPasswordButton = new Button("You forgot you password ? Click here!",
        (Button.ClickListener) event -> window.setVisible(true));
    remindPasswordButton.setStyleName(Reindeer.BUTTON_LINK);
    loginContent = new VerticalLayout();
    loginContent.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
    loginContent2 = new VerticalLayout();
    loginContent2.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
    loginContent2.addComponent(loginInfo);
    loginContent2.setComponentAlignment(loginInfo, Alignment.MIDDLE_CENTER);
    loginContent.addComponent(login);
    loginContent.addComponent(password);
    loginContent.addComponent(loginButton);
    loginContent.addComponent(registrationButton);
    loginContent.addComponent(remindPasswordButton);
    loginPanel.setContent(loginContent);
    loginPanel.addStyleName(Runo.PANEL_LIGHT);
    loginPanel.setStyleName("loginPanel");
    loginContent.setStyleName("loginPanel");
    loginContent2.setStyleName("loginPanel");
    loginContent.setHeight("100%");
    loginContent.setWidth("100%");
    loginContent2.setWidth("100%");
    loginContent2.setHeight("100%");
    MenuBar.Command menuCommand = selectedItem -> navigate(selectedItem.getText());
    MenuBar mainMenu = new MenuBar();
    mainMenu.setWidth("100px");
    mainMenu.setHeight("50px");
    MenuBar.MenuItem main;
    main = mainMenu.addItem("", new ThemeResource("icons/main.png"), null);
    main.setStyleName("mainMenu");
    main.addItem("Main", new ThemeResource("icons/home.png"), menuCommand).setStyleName("mainMenu");
    main.addItem("Admin", new ThemeResource("icons/crown.png"), menuCommand).setStyleName("mainMenu");
    main.addItem("See Profile", new ThemeResource("icons/dice.png"), menuCommand).setStyleName("mainMenu");
    main.addItem("Edit Profile", new ThemeResource("icons/settings.png"), menuCommand).setStyleName("mainMenu");
    main.addItem("Settings", new ThemeResource("icons/settings.png"), menuCommand).setStyleName("mainMenu");
    main.addItem("Logout", new ThemeResource("icons/logout.png"), menuCommand).setStyleName("mainMenu");
    mainMenu.setWidth("60px");
    mainMenu.setStyleName("mainMenu");
    if (loggedInUser != null && UserManager.testImage(loggedInUser.getImageUrl())) {
      img.setSource(new ExternalResource(loggedInUser.getImageUrl()));
    } else {
      img.setSource(new ExternalResource(DEFAULT_IMAGE_PATH));
    }
    img.setHeight("120px");
    img.setWidth("120px");
    img.setStyleName("roundImage");
    loginContent2.addComponent(img);
    loginContent2.addComponent(mainMenu);
    panels.addComponent(loginPanel);

    /*****************************************************************************************************************/

    /*****************************************************************************************************************/
    //                                   Password remind Window
    /*****************************************************************************************************************/

    currentUI.addWindow(window);
    window.removeAllCloseShortcuts();
    window.setCaption("Remind password");
    window.setWidth(300.0f, Unit.PIXELS);
    window.setClosable(false);
    window.setHeight(300.0f, Unit.PIXELS);
    window.setPosition(500, 250);
    window.setModal(true);
    window.setDraggable(false);
    window.setResizable(false);
    window.setVisible(false);
    window.setImmediate(true);
    AbsoluteLayout windowLayout = new AbsoluteLayout();
    window.setContent(windowLayout);
    windowLayout.setStyleName(Reindeer.LAYOUT_BLACK);
    TextField mailText = new TextField();
    mailText.setCaption("Write your mail here");
    Button continueButton = new Button("Continue", (Button.ClickListener) event -> {
      reminder = new PasswordReminder();
      if (reminder.changePassword(mailText.getValue())) {
        window.setVisible(false);
        Notification.show("Your password has been changed",
            Notification.Type.HUMANIZED_MESSAGE);
      } else {
        window.setVisible(false);
        Notification.show("Error occured, try again",
            Notification.Type.WARNING_MESSAGE);
      }
    });

    Button cancelButton = new Button("Cancel", (Button.ClickListener) event -> {
      mailText.setValue("");
      window.setVisible(false);
    });

    windowLayout.addComponent(mailText, "top:10%;left:10%;");
    windowLayout.addComponent(continueButton, "top:25%;left:15%;");
    windowLayout.addComponent(cancelButton, "top:25%;left:45%;");
    /*****************************************************************************************************************/

    addComponent(panels);

    /*****************************************************************************************************************/
    refresh();
    prepareCategoriesMenu();
    prepareSearcher();
  }

  /*****************************************************************************************************************/

  private void refresh() {
    loggedInUser = (User) VaadinSession.getCurrent().getAttribute("loggedInCustomer");
    loginPanel = currentUI.getLoginPanel();
    if (loggedInUser != null) {
      loginPanel.setContent(loginContent2);
      loginInfo.setValue("\r\n" + loggedInUser.getName());
    } else {
      loginPanel.setContent(loginContent);
    }
    currentUI.setLoginPanel(loginPanel);

  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    refresh();
  }

  private void prepareCategoriesMenu() {
    VerticalLayout buttonsLayout = new VerticalLayout();
    buttonsLayout.setSpacing(false);
    buttonsLayout.setMargin(false);
    buttonsLayout.setHeightUndefined();
    buttonsLayout.setWidth("400px");
    List<Category> allCategories = catManager.getCategories();
    for (Category cat : allCategories) {
      NativeButton button = new NativeButton(cat.getName(), (Button.ClickListener) event -> {
        prepareCustomersList(cat);
      });
      button.setWidth("100%");
      button.setHeight("50px");
      button.setStyleName("panelButton");
      buttonsLayout.addComponent(button);
    }
    addComponent(buttonsLayout, "left:0%;top:30%;");

  }

  private void prepareCustomersList(Category cat) {
    TrainingManager dao = new TrainingManager();
    listLayout.removeAllComponents();
    listLayout.setWidth("1300px");
    listLayout.setHeightUndefined();
    String cityName = null;
    if (citySelect.getValue() != null) {
      cityName = citySelect.getValue().toString();
    }
    double maxPrice = 0;
    double range = 0;
    try {
      if (!area.getValue().equals("")) {
        range = Double.parseDouble(area.getValue());
      }

      if (!this.maxPrice.getValue().equals("")) {
        maxPrice = Double.parseDouble(this.maxPrice.getValue());
      }
    } catch (NumberFormatException exception) {
      Notification.show("Price and distance must be a numeric value!!",
          Notification.Type.WARNING_MESSAGE);
      return;
    }
    String sortBy = null;
    if (this.sortBy.getValue() != null) {
      sortBy = this.sortBy.getValue().toString();
    }
    ArrayList<Training> trainings = dao
        .getTrainingsByFilters(cityName, range, cat, initialDate.getValue(), lastDate.getValue(), maxPrice, sortBy,
            showOnline.getValue());
    HorizontalLayout titlesLayout = new HorizontalLayout();
    if (!trainings.isEmpty()) {
      Label emptyLabel = new Label();
      emptyLabel.setWidth("120px");
      emptyLabel.setHeight("120px");
      Label nameAndSurnameTitle = new Label("Trainer name");
      nameAndSurnameTitle.setWidth("250px");
      nameAndSurnameTitle.setHeightUndefined();
      nameAndSurnameTitle.addStyleName("whiteBigText");
      Label adressLabelTitle = new Label("Place of training");
      adressLabelTitle.setWidth("250px");
      adressLabelTitle.setHeightUndefined();
      adressLabelTitle.addStyleName("whiteBigText");
      Label dateLabelTitle = new Label("Date of training");
      dateLabelTitle.setWidth("120px");
      dateLabelTitle.setHeightUndefined();
      dateLabelTitle.addStyleName("whiteBigText");
      Label priceLabelTitle = new Label(" Price[zł] : ");
      priceLabelTitle.setWidth("120px");
      priceLabelTitle.setHeightUndefined();
      priceLabelTitle.setStyleName("whiteBigText");
      Label emptyLabel2 = new Label();
      emptyLabel2.setWidth("150px");
      emptyLabel2.setHeight("120px");
      titlesLayout.addStyleName("usersView");
      titlesLayout.setHeight("120px");
      titlesLayout.setWidth("1300px");
      titlesLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
      titlesLayout.addComponent(emptyLabel);
      titlesLayout.addComponent(nameAndSurnameTitle);
      titlesLayout.addComponent(adressLabelTitle);
      titlesLayout.addComponent(dateLabelTitle);
      titlesLayout.addComponent(priceLabelTitle);
      titlesLayout.addComponent(emptyLabel2);

    } else {
      Label noFoundLabel = new Label("No results found");
      noFoundLabel.setWidth("100%");
      noFoundLabel.setHeight("100%");
      noFoundLabel.addStyleName("whiteBigText");
      titlesLayout.addComponent(noFoundLabel);
      addComponent(listLayout, "top:35%; left:25%;");
      listLayout.addComponent(titlesLayout);
      return;
    }

    addComponent(listLayout, "top:35%; left:25%;");
    prepareCustomersOnPages(titlesLayout, trainings, 0);
  }

  private void prepareCustomersOnPages(HorizontalLayout titlesLayout, ArrayList<Training> trainings, int number) {
    listLayout.removeAllComponents();
    listLayout.addComponent(titlesLayout);
    SimpleDateFormat formatDate = new SimpleDateFormat("dd-MMM-yy");
    for (int i = number * 10; i < number * 10 + 10 && i < trainings.size(); i++) {
      Training training = trainings.get(i);
      HorizontalLayout userLayout = new HorizontalLayout();
      Image img = new Image();
      if (UserManager.testImage(training.getOwner().getImageUrl())) {
        img.setSource(new ExternalResource(training.getOwner().getImageUrl()));
      } else {
        img.setSource(new ExternalResource(DEFAULT_IMAGE_PATH));
      }
      Label nameAndSurname = new Label(training.getOwner().getName());
      Label adressLabel = null;
      if (training.getCity().isEmpty()) {
        adressLabel = new Label("Online");
      } else {
        adressLabel = new Label(training.getCity());
      }
      Label dateLabel = new Label(formatDate.format(training.getDate()));
      Label priceLabel = new Label(String.format("%d", training.getPrice()));
      NativeButton goToProfileButton = new NativeButton("Look up training", (Button.ClickListener) event -> {
        if (loggedInUser != null) {
          VaadinSession.getCurrent().setAttribute("customerToSee", (User) training.getOwner());
          VaadinSession.getCurrent().setAttribute("training", (Training) training);
          currentUI.getNavigator().navigateTo("scheduleView");
        } else {
          Notification.show("To proceed you must login first!",
              Notification.Type.WARNING_MESSAGE);
        }
      });
      img.setWidth("120px");
      img.setHeight("120px");
      priceLabel.setWidth("120px");
      priceLabel.setHeightUndefined();
      priceLabel.addStyleName("whiteBigText");
      dateLabel.setWidth("120px");
      dateLabel.setHeightUndefined();
      dateLabel.addStyleName("whiteBigText");
      nameAndSurname.setWidth("250px");
      nameAndSurname.setHeightUndefined();
      nameAndSurname.addStyleName("whiteBigText");
      adressLabel.setWidth("250px");
      adressLabel.setHeightUndefined();
      adressLabel.addStyleName("whiteBigText");
      goToProfileButton.setWidth("150px");
      goToProfileButton.setHeight("120px");
      goToProfileButton.addStyleName("panelButtonSmall");
      userLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
      userLayout.addComponent(img);
      userLayout.addComponent(nameAndSurname);
      userLayout.addComponent(adressLabel);
      userLayout.addComponent(dateLabel);
      userLayout.addComponent(priceLabel);
      userLayout.addComponent(goToProfileButton);
      userLayout.addStyleName("usersView");
      userLayout.setHeight("120px");
      userLayout.setWidth("1300px");
      listLayout.addComponent(userLayout);
    }
    HorizontalLayout buttonsLayout = new HorizontalLayout();
    buttonsLayout.setWidth("100%");
    buttonsLayout.setHeight("75px");
    buttonsLayout.addStyleName("usersView");
    int border = trainings.size() / 10;
    if (trainings.size() > 50) {
      int shifter = 1;
      if (number == 0) {
        shifter = 0;
      }
      for (int i = 0; i < number + 5 - shifter && i < border + 1; i++) {
        NativeButton pageButton = new NativeButton("" + (i + 1));
        pageButton.addClickListener(event -> {
          prepareCustomersOnPages(titlesLayout, trainings, Integer.parseInt(pageButton.getCaption()) - 1);
        });
        pageButton.setStyleName("panelButtonSmall");
        pageButton.setHeight("100%");
        pageButton.setWidth("75px");
        if (i == number) {
          pageButton.setEnabled(false);
        }
        if (i == 0) {
          i += number - shifter - 1;
        }
        buttonsLayout.addComponent(pageButton);
      }
    } else {
      for (int i = 0; i < 5 && i < border + 1; i++) {
        NativeButton pageButton = new NativeButton("" + (i + 1));
        pageButton.addClickListener(event -> {
          prepareCustomersOnPages(titlesLayout, trainings, Integer.parseInt(pageButton.getCaption()) - 1);
        });
        pageButton.setStyleName("panelButtonSmall");
        pageButton.setHeight("100%");
        pageButton.setWidth("75px");
        if (i == number) {
          pageButton.setEnabled(false);
        }
        buttonsLayout.addComponent(pageButton);
      }
    }
    listLayout.addComponent(buttonsLayout);
  }

  private void prepareSearcher() {
    sortBy = new ComboBox("Sort by");
    sortBy.addItem("");
    sortBy.addItem("date");
    sortBy.addItem("length");
    sortBy.addItem("price");
    maxPrice = new TextField("Maximum price");
    maxPrice.setStyleName("myTextField");
    area = new TextField("Max distance from city in [km]");
    area.setStyleName("myTextField");
    initialDate = new DateField("Pick from date");
    lastDate = new DateField("Pick to date");
    initialDate.setValue(new Date());
    lastDate.setValue(new Date());
    initialDate.addValueChangeListener(event -> checkDate());
    lastDate.addValueChangeListener(event -> checkDate());
    Button applyFilter = new Button();
    applyFilter.setStyleName(Reindeer.BUTTON_LINK);
    applyFilter.setIcon(new ThemeResource("icons/filter.png"));
    HorizontalLayout searcherLayout = new HorizontalLayout();
    searcherLayout.setWidth("100%");
    searcherLayout.setHeight("100px");
    VerticalLayout[] columns = new VerticalLayout[10];
    for (int i = 0; i < columns.length; i++) {
      columns[i] = new VerticalLayout();
      searcherLayout.addComponent(columns[i]);
      columns[i].setWidth("10%");
      columns[i].setMargin(true);
      columns[i].setHeight("100%");
    }

    CitiesDAO dao = DAOHandler.citiesDAO;
    citySelect = new ComboBox("Select city", dao.getAll());
    columns[0].addComponent(citySelect);
    columns[1].addComponent(sortBy);
    columns[2].addComponent(maxPrice);
    columns[3].addComponent(area);
    columns[7].addComponent(showOnline);
    showOnline.setValue(true);
    columns[5].addComponent(initialDate);
    columns[6].addComponent(lastDate);
    searcherLayout.addStyleName("filters");
    addComponent(searcherLayout, "top:21%;left:21%;");
  }

  private void navigate(String name) {
    if (name.equals("Edit Profile")) {
      currentUI.getNavigator().navigateTo("profileEditView");
    }
    if (name.equals("Admin")) {
      currentUI.getNavigator().navigateTo("adminView");
    }
    if (name.equals("Settings")) {
      currentUI.getNavigator().navigateTo("customerView");
    }
    if (name.equals("See Profile")) {
      VaadinSession.getCurrent()
          .setAttribute("customerToSee", VaadinSession.getCurrent().getAttribute("loggedInCustomer"));
      currentUI.getNavigator().navigateTo("profileSeeView");
    }
    if (name.equals("Logout")) {
      loggedInUser = null;
      password.setValue("");
      VaadinSession.getCurrent().setAttribute("loggedInCustomer", null);
      refresh();
      currentUI.getNavigator().navigateTo("mainView");
    }
    if (name.equals("Main")) {
      currentUI.getNavigator().navigateTo("mainView");
    }
  }

  private void checkDate() {

    if (initialDate.getValue().before(new Date())) {
      initialDate.setValue(new Date());
    }

    if (lastDate.getValue().before(initialDate.getValue())) {
      lastDate.setValue(initialDate.getValue());
    }

  }

}







