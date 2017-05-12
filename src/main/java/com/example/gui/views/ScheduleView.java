package com.example.gui.views;

import com.example.Model.*;
import com.example.entities.Category;
import com.example.entities.Customer;
import com.example.entities.Training;
import com.example.gui.ui.DashboardUI;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.*;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.themes.Reindeer;
import javafx.scene.control.DatePicker;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Bartek on 2017-03-25.
 */
@PreserveOnRefresh
@org.springframework.stereotype.Component
@SpringView(name = "scheduleView")
@Theme("mytheme")
public class ScheduleView extends SecuredView implements View {

    private String gap="            ";
    public final URL DEFAULT_IMAGE_PATH=new URL("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQCeShBiPnm0h2AP4S5X2lISkauLSNNSqN61tndLC0KJYwt3EY1E04WPi0");
    private TrainingManager trainingManager= new TrainingManager();
    private DateField datePicker=new DateField();
    private CategoriesManager catManager=new CategoriesManager();
    private HorizontalLayout dateLayout=new HorizontalLayout();
    private AbsoluteLayout[] daysLayouts=new AbsoluteLayout[8];
    private Window addTrainingWindow=new Window();
    private Window confirmDelete=new Window();
    private Window confirmReservation=new Window();
    private AbsoluteLayout addTrainingLayout=new AbsoluteLayout();
    private AbsoluteLayout confirmDeleteLayout=new AbsoluteLayout();
    private AbsoluteLayout confirmReservationLayout=new AbsoluteLayout();
    private Customer loggedInCustomer=(Customer) VaadinSession.getCurrent().getAttribute("loggedInCustomer");
    private Label dateLabels[]=new Label[7];
    private CheckBox weekDays[]=new CheckBox[7];
    private DateField trainingDate=new DateField("Pick date");
    private NativeSelect weeks =new NativeSelect("Pick number of weeks");
    private NativeSelect hours =new NativeSelect("Pick hour");
    private NativeSelect minutes =new NativeSelect("Pick minute");
    private NativeSelect hoursLength =new NativeSelect("Pick hour length");
    private NativeSelect minutesLength =new NativeSelect("Pick minutes length");
    private NativeSelect category =new NativeSelect("Pick category");
    private TextField city= new TextField("Place of training");
    private TextField description= new TextField("Description");
    private Customer customer=(Customer) VaadinSession.getCurrent().getAttribute("customerToSee");
    private Training training=(Training) VaadinSession.getCurrent().getAttribute("training");
    private Date[] weekDates=new Date[7];
    private DashboardUI currentUI=(DashboardUI)UI.getCurrent();
    private Panel loginPanel=currentUI.getLoginPanel();
    private JsonReader reader=new JsonReader();
    private Image logo=new Image("", new ThemeResource("icons/logo2.png"));
    private VerticalLayout userLayout=new VerticalLayout();

    public ScheduleView() throws MalformedURLException {
    }


    protected void prepareView() {

        datePicker.setValue(new Date());
        if(customer !=null && customer.getId()==loggedInCustomer.getId())
        {
            prepareAddTrainingWindow();
            prepareConfirmRemoveWindow();
        }
        else{
            prepareConfirmReservationWindow();
        }
        logo.setHeight("200px");
        logo.setWidth("350px");
        logo.addClickListener(event ->currentUI.getNavigator().navigateTo("mainView"));
        setHeight("3500px");
        setWidth("2500px");
        setStyleName("backColor");
        userLayout.setWidth("400px");
        userLayout.setHeightUndefined();
        userLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Image img=new Image();
        if(UserManager.testImage(training.getOwner().getImageUrl()))
        {
            img.setSource(new ExternalResource(training.getOwner().getImageUrl()));
        }
        else{
            img.setSource(new ExternalResource(DEFAULT_IMAGE_PATH));
        }
        img.setHeight("100px");
        img.setWidth("100px");
        userLayout.addComponent(img);
        Label nameInfoLabel=new Label(String.format("%s %s",customer.getName(),customer.getSurname()));
        nameInfoLabel.setStyleName("whiteBigText");
        nameInfoLabel.setHeight("75px");
        nameInfoLabel.setWidthUndefined();
        userLayout.addComponent(nameInfoLabel);
        Label mailLabel=new Label(String.format("Mail: %s",customer.getMail()));
        mailLabel.setStyleName("whiteBigText");
        mailLabel.setHeight("75px");
        mailLabel.setWidthUndefined();
        userLayout.addComponent(mailLabel);
        NativeButton visitProfileButton = new NativeButton(String.format(String.format("Visit %s's profile",customer.getName())), (Button.ClickListener) event -> {
            currentUI.getNavigator().navigateTo("profileSeeView");
        });
        visitProfileButton.setVisible(true);
        visitProfileButton.addStyleName("panelButton");
        visitProfileButton.setWidth("450px");
        visitProfileButton.setHeight("75px");
        userLayout.addComponent(visitProfileButton);
        if(customer !=null && customer.getId()==loggedInCustomer.getId())
        {
            NativeButton addTraining = new NativeButton(String.format("Add training"), (Button.ClickListener) event -> {
                addTrainingWindow.setVisible(true);
                this.setEnabled(false);
            });

            addTraining.setVisible(true);
            addTraining.addStyleName("panelButton");
            addTraining.setWidth("450px");
            addTraining.setHeight("75px");
            userLayout.addComponent(addTraining);
        }
        NativeButton seeSchedule=new NativeButton(String.format("Go to %s's schedule",customer.getName()), (Button.ClickListener) event -> {
            prepareSchedule();
        });
        seeSchedule.setStyleName("panelButton");
        seeSchedule.setWidth("400px");
        seeSchedule.setHeight("75px");
        userLayout.addComponent(seeSchedule);
        userLayout.setStyleName("filters");


        if(training!=null) {
            prepareTrainingView();
        }
        else{
            prepareSchedule();
        }




    }
    @PostConstruct
    private void init()
    {

        currentUI=(DashboardUI) UI.getCurrent();
        authorize(currentUI);
    }

    private void addPanelAndLogo()
    {

        addComponent(logo, "top:0%;left:65%");
        addComponent(loginPanel);
        addComponent(userLayout,"left:1%;top:20%;");

    }

    private void prepareTrainingView()
    {
        removeAllComponents();
        VaadinSession.getCurrent().setAttribute("training",null);

            Place placeToSee=null;
            if(!training.getCity().isEmpty())
            {
                placeToSee = reader.getCity(training.getCity());
            }
            if (placeToSee != null) {
                VerticalLayout mapLayout = new VerticalLayout();
                mapLayout.setHeight("40%");
                mapLayout.setWidth("40%");
                mapLayout.addComponent(currentUI.getGoogleMap(placeToSee.getLat(), placeToSee.getLng(),"Place of training: "+training.getCity(),"wiadomość"));
                addComponent(mapLayout, "top:10%;left:65%;");

            }

        addPanelAndLogo();
        HorizontalLayout trainingLayout=new HorizontalLayout();
        trainingLayout.setStyleName("filters");
        trainingLayout.setHeight("200px");
        trainingLayout.setWidth("1500px");
        Label price=new Label("Total cost: "+gap+training.getPrice());
        price.setWidth("300px");
        price.setHeightUndefined();
        price.setStyleName("whiteBigText");
        double minutes=(training.getHour())%1*60;
        double hour=training.getHour()-(minutes/60);
        double toMinutes=(training.getHour()+training.getLength())%1*60;
        double toHour=training.getHour()+training.getLength()-(toMinutes/60);
        Label length=new Label(String.format("Training from: %.0f:%.0f to %.0f:%.0f",hour,minutes,toHour,toMinutes));
        Label description=new Label("Description:"+gap+training.getDescription());
        description.setWidth("300px");
        description.setHeightUndefined();
        description.setStyleName("whiteBigText");
        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MMM-yy");
        Label dateLabel=new Label(formatDate.format(training.getDate()));
        dateLabel.setStyleName("whiteBigText");
        dateLabel.setWidth("300px");
        dateLabel.setHeightUndefined();
        length.setStyleName("whiteBigText");
        length.setWidth("300px");
        length.setHeightUndefined();
        NativeButton confirmButton=new NativeButton("Confirm training reservation", (Button.ClickListener) event -> {
            if(customer.getId()==loggedInCustomer.getId())
            {
                this.setEnabled(false);
                confirmDelete.setVisible(true);
            }
            else{
                confirmReservation.setVisible(true);
                this.setEnabled(false);
            }
        });
        confirmButton.setImmediate(true);
        if(customer.getId()==loggedInCustomer.getId())
        {confirmButton.setCaption("Delete training");}
        confirmButton.setHeight("200px");
        confirmButton.setWidth("250px");
        confirmButton.setStyleName("panelButton");
        trainingLayout.addComponents(description,dateLabel,length,price);
        trainingLayout.addComponent(confirmButton);
        trainingLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponent(trainingLayout,"left:0%;top:12%;");
    }

    private void prepareSchedule()
    {

        removeAllComponents();
        addPanelAndLogo();
        dateLayout.removeAllComponents();
        dateLayout.setHeight("2800px");
        dateLayout.setWidthUndefined();
        for(int i=0;i<8;i++)
        {
            daysLayouts[i]=new AbsoluteLayout();
            daysLayouts[i].setWidth("200px");
            daysLayouts[i].setHeight("100%");
            dateLayout.addComponent(daysLayouts[i]);

            for(int y=0;y<20;y++)
            {
                if(y==0 && i>0)
                {
                    dateLabels[i-1]=new Label(String.format("<center>"
                            +"<b>%s"+
                            "</p><center>",TrainingManager.getDay(i-1)),ContentMode.HTML);
                    dateLabels[i-1].setWidth("100%");
                    dateLabels[i-1].setHeight("140px");
                    dateLabels[i-1].addStyleName("hourLabel");
                    daysLayouts[i].addComponent(dateLabels[i-1],"top:0%;");
                }
                else if(i==0 && y>0)
                {
                    Label hourLabel=new Label(String.format( "<center>"
                            +"<b>%d:00"+
                            "</p><center>",y+5),ContentMode.HTML);
                    hourLabel.setWidth("100%");
                    hourLabel.setHeight("140px");
                    hourLabel.addStyleName("hourLabel");
                    daysLayouts[i].addComponent(hourLabel,String.format("top:%dpx;",140*y));
                }
                else{
                    Label emptyLabel=new Label("");
                    emptyLabel.setWidth("100%");
                    emptyLabel.setHeight("139px");
                    emptyLabel.addStyleName("hourLabel");
                    daysLayouts[i].addComponent(emptyLabel,String.format("top:%dpx;",140*y));
                }
            }


        }
        addComponent(dateLayout,"top:15%;left:20%;");
        datePicker.addValueChangeListener(event -> {
            if (!datePicker.getValue().before(new Date())) {
                weekDates = TrainingManager.getWeekFromDate(datePicker.getValue());
                setLabels(weekDates);
                prepareSchedule();
            }
            else{
                datePicker.setValue(new Date());
            }
        });
        prepareUserButtons();
        weekDates=TrainingManager.getWeekFromDate(datePicker.getValue());
        setLabels(weekDates);
        prepareTrainingsButtons();
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {

    }

    private void prepareUserButtons()
    {

        addComponent(datePicker,"top:7%; left:55%");


    }

    private void prepareAddTrainingWindow()
    {

        addTrainingWindow.setHeight("700px");
        addTrainingWindow.setWidth("300px");
        addTrainingLayout.setHeight("700px");
        addTrainingLayout.setWidth("300px");
        addTrainingLayout.setStyleName(Reindeer.LAYOUT_BLACK);
        addTrainingWindow.setContent(addTrainingLayout);
        addTrainingWindow.setClosable(false);
        addTrainingWindow.setVisible(false);
        addTrainingWindow.setImmediate(true);
        addTrainingWindow.setPosition(500, 500);
        currentUI.addWindow(addTrainingWindow);
        addTrainingWindow.setResizable(false);

        for(int i=1;i<11;i++)
        {
            weeks.addItem(i);
        }
        for(int i=6;i<24;i++)
        {
            hours.addItem(i);
        }
        hours.setValue(0);
        for(int i=0;i<60;i++)
        {
            minutes.addItem(i);
        }
        minutes.setValue(0);
        for(int i=0;i<24;i++)
        {
            hoursLength.addItem(i);
        }
        hours.setValue(0);
        for(int i=0;i<60;i++)
        {
            minutesLength.addItem(i);
        }
        minutes.setValue(0);
        trainingDate.setValue(new Date());
        Button save = new Button(String.format("Save trainings"), (Button.ClickListener) event -> {
            if(!(weeks.getValue()!=null && (int)weeks.getValue()>0)){
                Notification.show("Wrong data! ",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }
            if(hours.getValue()==null){
                Notification.show("Wrong data! ",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }
            if(minutes==null){
                Notification.show("Wrong data! ",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }
            if(hoursLength.getValue()==null || minutesLength.getValue()==null || (int)hoursLength.getValue()+(int)minutesLength.getValue()==0 ){
                Notification.show("Wrong data! ",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }
            if(trainingDate.getValue()==null){
                Notification.show("Wrong data! ",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }
            if(category.getValue()==null){
                Notification.show("Wrong data! ",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }

                if(city.getValue()!="" && new JsonReader().getCity(city.getValue())==null){
                    Notification.show("Couldn't find this place! Try less detailed adress or leave empty for online training",
                            Notification.Type.WARNING_MESSAGE);
                    return;
                }

            this.setEnabled(false);
            for(String message:saveTrainings()) {
                Notification.show("Training duplication in: "+message,
                        Notification.Type.WARNING_MESSAGE);
            }
            weeks.setValue(0);
            hoursLength.setValue(0);
            minutesLength.setValue(0);
            minutes.setValue(0);
            hours.setValue(0);
            trainingDate.setValue(new Date());
            for (CheckBox c:weekDays)
            {
                c.setValue(false);
            }
            addTrainingWindow.setVisible(false);
            this.setEnabled(true);
        });
        Button cancel = new Button(String.format("Cancel"), (Button.ClickListener) event -> {
            weeks.setValue(0);
            hoursLength.setValue(0);
            minutesLength.setValue(0);
            minutes.setValue(0);
            hours.setValue(6);
            for (CheckBox c:weekDays)
            {
                c.setValue(false);
            }
            addTrainingWindow.setVisible(false);
            this.setEnabled(true);
        });


        for(Category cat:catManager.getUserCategories(loggedInCustomer))
        {
            category.addItem(cat);
        }
        int cssShift=30;
        for(int i=0;i<7;i++)
        {
            weekDays[i]=new CheckBox(TrainingManager.getDay(i));
            weekDays[i].setValue(false);
            addTrainingLayout.addComponent(weekDays[i],String.format("top:%d%%;left:25%%;right:25%%;",cssShift));
            cssShift+=3;
        }
        trainingDate.addValueChangeListener(event ->{if(trainingDate.getValue().before(new Date()))
        {
            trainingDate.setValue(new Date());
        }});

        addTrainingLayout.addComponent(category,"top:5%;left:25%;right:25%;");
        addTrainingLayout.addComponent(hours,"top:15%;left:25%;");
        addTrainingLayout.addComponent(minutes,"top:15%;left:60%;");
        addTrainingLayout.addComponent(hoursLength,"top:25%;left:25%;");
        addTrainingLayout.addComponent(minutesLength,"top:25%;left:60%;");
        addTrainingLayout.addComponent(city,"top:35%;left:60%;");
        addTrainingLayout.addComponent(description,"top:45%;left:60%;");
        addTrainingLayout.addComponent(trainingDate,"top:60%;left:25%;right:25%;");
        addTrainingLayout.addComponent(weeks,"top:70%;left:25%;right:25%;");
        addTrainingLayout.addComponent(cancel,"top:80%;left:25%;right:25%;");
        addTrainingLayout.addComponent(save,"top:85%;left:25%;right:25%;");
    }





    private void prepareConfirmReservationWindow()
    {

        confirmReservation.setHeight("400px");
        confirmReservation.setWidth("400px");
        confirmReservationLayout.setHeight("400px");
        confirmReservationLayout.setWidth("400px");
        confirmReservationLayout.setStyleName(Reindeer.LAYOUT_BLACK);
        confirmReservation.setContent(confirmReservationLayout);
        confirmReservation.setClosable(false);
        confirmReservation.setVisible(false);
        confirmReservation.setImmediate(true);
        confirmReservation.setPosition(500, 500);

        confirmReservation.setCaption("Confirm your choice");
        Label label=new Label("Do you want to reserve this training?");
        Button cancel = new Button(String.format("Cancel"), (Button.ClickListener) event -> {
            confirmReservation.setVisible(false);
            this.setEnabled(true);
        });
        Button ok = new Button(String.format("Yes"), (Button.ClickListener) event -> {
            trainingManager.reserve(training,loggedInCustomer.getId());
            prepareSchedule();
            confirmReservation.setVisible(false);
            this.setEnabled(true);
        });
        confirmReservationLayout.addComponent(label,"top:10%;right:25%;left:25%;");
        confirmReservationLayout.addComponent(ok,"top:35%;left:25%;");
        confirmReservationLayout.addComponent(cancel,"top:35%;left:65%;");
        currentUI.addWindow(confirmReservation);
    }

    private void prepareConfirmRemoveWindow()
    {
        confirmDelete.setClosable(false);
        confirmDelete.setVisible(false);
        confirmDelete.setImmediate(true);
        confirmDelete.setContent(confirmDeleteLayout);
        confirmDeleteLayout.setStyleName(Reindeer.LAYOUT_BLACK);
        confirmDelete.setHeight("400px");
        confirmDelete.setWidth("400px");
        confirmDeleteLayout.setHeight("400px");
        confirmDeleteLayout.setWidth("400px");
        confirmDelete.setPosition(500, 500);
        confirmDelete.setCaption("Confirm your choice");
        Label label=new Label("Do you want to delete this training?");
        Button cancel = new Button(String.format("Cancel"), (Button.ClickListener) event -> {
            confirmDelete.setVisible(false);
            this.setEnabled(true);
        });
        Button ok = new Button(String.format("Yes"), (Button.ClickListener) event -> {
            trainingManager.remove(training);
            prepareTrainingsButtons();
            confirmDelete.setVisible(false);
            this.setEnabled(true);
        });
        confirmDeleteLayout.addComponent(label,"top:10%;right:25%;left:25%;");
        confirmDeleteLayout.addComponent(ok,"top:35%;left:25%;");
        confirmDeleteLayout.addComponent(cancel,"top:35%;left:65%;");
        currentUI.addWindow(confirmDelete);
    }

    private void setLabels(Date[] dates)
    {
        java.util.Calendar c = java.util.Calendar.getInstance();
        for(int i=0;i<7;i++)
        {
            c.setTime(dates[i]);
            int dayOfMonth = c.get(java.util.Calendar.DAY_OF_MONTH);
            int month = c.get(java.util.Calendar.MONTH);
            dateLabels[i].setImmediate(true);
            dateLabels[i].setValue(String.format("<center>"
                    +"<b>%s<br>%d-%d"+
                    "</p><center>",TrainingManager.getDay(i),dayOfMonth,month+1));
        }
    }

    private List<String> saveTrainings()
    {
        List<String> resultSet=new ArrayList<>();
        Date firstMonday=TrainingManager.getWeekFromDate(trainingDate.getValue())[0];
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(firstMonday);
        for(int i=0;i<(int)weeks.getValue();i++)
        {
         for (int y=0; y<7; y++)
         {
             if(weekDays[y].getValue())
             {
                 Training training =new Training();
                 double hour=(int)hours.getValue()+(int)minutes.getValue()/(double)60;
                 training. setHour(hour);
                 double length=(int)hoursLength.getValue()+(int)minutesLength.getValue()/(double)60;
                 training.setLength(length);
                 training.setProfileId(loggedInCustomer.getProfileId());
                 Category cat=(Category)category.getValue();
                 training.setCategory(cat.getId());
                 training.setTakenById(0);
                 training.setCity(city.getValue());
                 training.setDescription(description.getValue());
                 if(!trainingManager.saveTraining(training,c.getTime()))
                 {
                     resultSet.add(c.getTime().toString());
                 }
             }
             c.add(java.util.Calendar.DATE,1);
         }

        }

        this.setEnabled(true);
        return resultSet;
    }
     private void prepareTrainingsButtons()
     {

         for(int i=1;i<weekDates.length+1;i++)
         {
             ArrayList<Training> trainings=trainingManager.getTrainingsFromDate(weekDates[i-1],customer.getProfileId());
             for(Training training:trainings)
             {
                 double hour=training.getLength()%1;
                 double minute=(training.getLength()-hour)*60;
                 NativeButton trainingButton=new NativeButton(String.format("%s :length: %.0f:%.0f",catManager.getCategory(training.getCategory()).getName(),hour,minute), (Button.ClickListener) event -> {

                     this.training=training;
                     prepareTrainingView();

             });
                 trainingButton.setWidth("100%");
                 if(training.getTakenById()==0 && training.getDate().after(new Date()))
                 {
                     trainingButton.setStyleName("freeTrainingButton");
                 }
                 else{
                     trainingButton.setStyleName("takenTrainingButton");
                     trainingButton.setEnabled(false);
                 }
                 double height=140*training.getLength();
                 trainingButton.setHeight(String.format("%.0fpx",height));
                 daysLayouts[i].addComponent(trainingButton,String.format("top:%dpx;",(int)(140*(training.getHour()-6))));
             }
         }


     }


}
