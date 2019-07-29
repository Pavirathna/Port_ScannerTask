package com.auxolabs.scanner;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class SiteCheckerApp extends JFrame implements ActionListener {


        private static final long serialVersionUID = 2884600754343147821L;
        private static final int WIDTH = 250;
        private static final int HEIGHT = 375;


        private boolean displayAll = false;


        private JTextField site, time, mailId,password;

        private JTextArea output;
        private JCheckBox toggleDisplayAll;
        private JButton check;
        private JPanel settingsPanel;



    public SiteCheckerApp() {
            super(" Site Checker and Time Scheduling");

            initComponents();

            super.setLayout(new FlowLayout ());
            super.setSize(300, 500);
            super.setLocationRelativeTo(null);
            super.setResizable(false);
            super.setVisible(true);
            super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        }



        private final void initComponents() {


            this.site = new JTextField(20);
            this.time = new JTextField(3);

            this.mailId = new JTextField(20);
            this.password=new JTextField ( 10 );

            this.output = new JTextArea(10, 20);
            this.output.setEditable(false);
            this.output.setLineWrap(true);



            this.toggleDisplayAll = new JCheckBox("Check Site With Time Schedule");
          //  this.toggleDisplayAll.addChangeListener(this);


            this.check = new JButton("SUBMIT");
            this.check.addActionListener(this);


            this.settingsPanel = new JPanel(new FlowLayout());
            this.settingsPanel.setBorder(BorderFactory.createTitledBorder("Site Checker"));

            this.settingsPanel.setPreferredSize(new Dimension(230, 230));
            this.settingsPanel.add(new JLabel("Enter site "));
            this.settingsPanel.add(this.site);

            this.settingsPanel.add(new JLabel("Enter time in minute: "));
            this.settingsPanel.add(this.time);

            this.settingsPanel.add(new JLabel(" Enter Mail Id"));
            this.settingsPanel.add(this.mailId);

            this.settingsPanel.add(new JLabel(" Enter Password"));
            this.settingsPanel.add(this.password);

            this.settingsPanel.add(this.toggleDisplayAll);
            this.settingsPanel.add(this.check);



            super.add(this.settingsPanel);

        }

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == this.check) {

               // this.output.setText("Starting scan..." + System.lineSeparator());
                System.out.println ("Enter");
                try {
                    siteContents (this.site.getText(), this.time.getText());
                } catch (Exception e) {
                    e.printStackTrace ();
                }

            }
        }

    private  void siteContents(String site, String time) throws Exception {
        int minutes;
        minutes = Integer.parseInt(time);
        timeSchedule(minutes,site);
    }

    public  void timeSchedule(int time,String site) throws InterruptedException, IOException, MessagingException {

        for (int i = 0; i < 3; i++) {
            TimeUnit.MINUTES.sleep(time);
            DateFormat df = new SimpleDateFormat ("dd/MM/yy HH:mm:ss");
            Date dateob = new Date();
            System.out.println(df.format(dateob));
            siteCheck(site);

        }
    }
    public  void siteCheck(String site) throws MessagingException, IOException,NullPointerException {


            URL obj = new URL(site);
            URLConnection conn = obj.openConnection ();
        String server = conn.getHeaderField ( "Server" );
          if (server != null) {
            System.out.println("Server - " + server);
            } else {
                          System.out.println ( "Key 'Server' is not found!" );
                       sendMail (mailId,password);
            }

        }



    private void sendMail(JTextField mailId, JTextField password)throws MessagingException {

        String recepient = "auxodev1234@gmail.com";
        Properties properties = new Properties ();
        properties.put ( "mail..smtp.auth", "true" );
        properties.put ( "mail.smtp.starttls.enable", "true" );
        properties.put ( "mail.smtp.host", "smtp.gmail.com" );
        properties.put ( "mail.smtp.port", "587" );
        final String myAccountEmail = String.valueOf(mailId);
        final String passWord = String.valueOf(password);
        properties.setProperty ( "mail.smtp.user", String.valueOf(mailId));
        properties.setProperty ( "mail.smtp.password", String.valueOf(password));
        properties.setProperty ( "mail.smtp.auth", "true" );
        Session session = Session.getInstance ( properties, new Authenticator () {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println ( "Email PassWord Authenticate Successfully" );
                return new PasswordAuthentication ( myAccountEmail, passWord );
            }
        } );
        Message message = prepareMessage ( session, myAccountEmail, recepient );
        Transport.send ( message );
        System.out.println ( "Message sent successfully!!" );

    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recepient) {

        try {
            Message message = new MimeMessage ( session );
            message.setFrom ( new InternetAddress ( myAccountEmail ) );
            message.setRecipient ( Message.RecipientType.TO, new InternetAddress ( recepient ) );
            message.setSubject ( "Site Checker" );
            message.setText ( "That Site was down" );
            return message;
        } catch (MessagingException e) {
            e.printStackTrace ();
        }
        return null;
    }
    public  boolean check(JTextField site){
        Pattern p = Pattern.compile("(http://|https://)(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?");
        Matcher m;
        m=p.matcher((CharSequence) site);
        boolean matches = m.matches();
        return matches;
    }





    public static void main(String[] args) {
        @SuppressWarnings("unused")
        SiteCheckerApp psg = new SiteCheckerApp ();
    }


}


