package services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import models.EmailConfig;
import models.FeedbackRequest;
import models.SendEmailRequest;
import services.db.entity.ListEntity;
import services.db.entity.PaymentRequest;
import services.db.entity.RegistrationRequest;
import play.Logger;
import services.db.entity.User;

import views.html.email.registration;
import views.html.email.recovery;
import views.html.email.payment;
import views.html.email.feedback;
import views.html.email.purchased;
import views.html.email.download;
import views.html.email.campaign;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EmailService {

    private DateFormat dueDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public void sendRegistrationEmail(RegistrationRequest request, User reseller) {
        try {
            final Config config = ConfigFactory.load();
            String webHost = config.getString( "app.web.host" );

            String domain = "MakeMyData";
            String adminEmail = "support@makemydata.com";
            EmailConfig emailConfig = new EmailConfig(config);

            if (reseller != null && reseller.getDomains() != null) {
                switch (reseller.getDomains()) {
                    case "makedatalist.com":
                        webHost = "https://www.makedatalist.com";
                        domain = "MakeDataList";
                        adminEmail = "admin@makedatalist.com";
                        emailConfig = EmailConfig.forMakeDataList(config);
                        break;
                    case "makethedata.com":
                        webHost = "https://www.makethedata.com";
                        domain = "MakeTheData";
                        adminEmail = "admin@makethedata.com";
                        emailConfig = EmailConfig.forMakeTheData(config);
                        break;
                    case "reidatalist.com":
                        webHost = "https://www.reidatalist.com";
                        domain = "ReiDataList";
                        adminEmail = "admin@reidatalist.com";
                        emailConfig = EmailConfig.forMakeTheData(config);
                        break;
                    case "axiomleads.com":
                        webHost = "http://axiomleads.com";
                        domain = "AxiomLeads";
                        adminEmail = "admin@axiomleads.com";
                        emailConfig = EmailConfig.forMakeTheData(config);
                        break;
                    case "multimedialists.com":
                    case "multimedialists.net":
                        webHost = "https://multimedialists.com";
                        domain = "MultimediaLists";
                        adminEmail = "admin@multimedialists.com";
                        emailConfig = EmailConfig.forMakeTheData(config);
                        break;
                    case "mytargetdata.com":
                        webHost = "https://www.mytargetdata.com";
                        domain = "MyTargetData";
                        adminEmail = "admin@mytargetdata.com";
                        emailConfig = EmailConfig.forMakeTheData(config);
                        break;
                    case "sales-list.com":
                        webHost = "https://www.sales-list.com";
                        domain = "SalesList";
                        adminEmail = "admin@sales-list.com";
                        emailConfig = EmailConfig.forMakeTheData(config);
                        break;
                }
            }

            StringBuilder linkBuilder = new StringBuilder();
            if (reseller != null &&
                    ("axiomleads.com".equalsIgnoreCase(reseller.getDomains()) ||
                     "multimedialists.com".equalsIgnoreCase(reseller.getDomains()) ||
                     "multimedialists.net".equalsIgnoreCase(reseller.getDomains()))) {
                linkBuilder.append(webHost).append("/?token=").append(request.getUuid());
            } else {
                linkBuilder.append(webHost).append("/#/login?token=").append(request.getUuid());
            }

            Logger.info( "Registration link: " + linkBuilder.toString() );

            String emailBody = registration.render(linkBuilder.toString(), domain, adminEmail ).toString();

            EmailConfig finalEmailConfig = emailConfig;
            String finalDomain = domain;
            Thread mailThread = new Thread(() -> {
                try {
                    sendEmail(
                            request.getEmail(),
                            String.format("Email Validation: %s", finalDomain),
                            emailBody,
                            finalEmailConfig);
                } catch ( Exception e ) {
                    Logger.error( "Error on sending registration email: ", e );
                }
            });
            mailThread.start();
        } catch ( Exception e ) {
            Logger.error( "Error on sending registration email: ", e );
        }
    }

    private void sendEmail(String receiver, String subject, String body, EmailConfig config) throws Exception {
        sendEmail(receiver, subject, null, null, body, config);
    }

    private void sendEmail(String receiver, String subject, String cc, String bcc, String body, EmailConfig config) throws Exception {
        Email from = new Email(config.getFrom());
        if (config.getFromName() != null) {
            from.setName(config.getFromName());
        }

        Email to = new Email(receiver);
        Content content = new Content(config.getContentType(), body);

        Mail mail = new Mail(from, subject, to, content);
        if (cc != null || bcc != null) {
            if (mail.personalization.size() > 0) {
                if (cc != null) {
                    Email ccEmail = new Email(cc);
                    mail.personalization.get(0).addCc(ccEmail);
                }

                if (bcc != null) {
                    Email bccEmail = new Email(bcc);
                    mail.personalization.get(0).addBcc(bccEmail);
                }
            }
        }

        if (config.getReply() != null) {
            mail.setReplyTo(new Email(config.getReply()));
        }

        SendGrid sg = new SendGrid(config.getAPI_KEY());

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);
        Logger.info("Status: {}, body: {}, headers: {}", response.getStatusCode(), response.getBody(), response.getHeaders());
    }

    public void sendRecoveryPasswordEmail(String email, String token, User reseller) {
        try {
            Config config = ConfigFactory.load();
            String webHost = config.getString( "app.web.host" );

            String domain = "MakeMyData.com";
            String adminEmail = "admin@makemydata.com";
            EmailConfig emailConfig = new EmailConfig(config);

            if (reseller != null && reseller.getDomains() != null) {
                switch (reseller.getDomains()) {
                    case "makedatalist.com":
                        webHost = "https://www.makedatalist.com";
                        domain = "MakeDataList.com";
                        adminEmail = "admin@makedatalist.com";
                        emailConfig = EmailConfig.forMakeDataList(config);
                        break;
                    case "makethedata.com":
                        webHost = "https://www.makethedata.com";
                        domain = "MakeTheData.com";
                        adminEmail = "admin@makethedata.com";
                        emailConfig = EmailConfig.forMakeTheData(config);
                        break;
                    case "reidatalist.com":
                        webHost = "https://www.reidatalist.com";
                        domain = "ReiDataList";
                        adminEmail = "admin@reidatalist.com";
                        emailConfig = EmailConfig.forMakeTheData(config);
                        break;
                    case "mytargetdata.com":
                        webHost = "https://www.mytargetdata.com";
                        domain = "MyTargetData";
                        adminEmail = "admin@mytargetdata.com";
                        emailConfig = EmailConfig.forMakeTheData(config);
                        break;
                    case "sales-list.com":
                        webHost = "https://www.sales-list.com";
                        domain = "SalesList";
                        adminEmail = "admin@sales-list.com";
                        emailConfig = EmailConfig.forMakeTheData(config);
                        break;
                    case "multimedialists.com":
                        webHost = "https://multimedialists.com";
                        domain = "MultimediaLists";
                        adminEmail = "admin@multimedialists.com";
                        emailConfig = EmailConfig.forMakeTheData(config);
                        break;
                    case "multimedialists.net":
                        webHost = "https://multimedialists.net";
                        domain = "MultimediaLists";
                        adminEmail = "admin@multimedialists.net";
                        emailConfig = EmailConfig.forMakeTheData(config);
                        break;

                }
            }

            StringBuilder linkBuilder = new StringBuilder();
            linkBuilder.append( webHost );

            if (reseller != null &&
                    ("multimedialists.com".equalsIgnoreCase(reseller.getDomains()) ||
                     "multimedialists.net".equalsIgnoreCase(reseller.getDomains()))) {
                linkBuilder.append("/?pas_token=").append(token);
            } else {
                linkBuilder.append( "/#/login?pas_token=" ).append(token);
            }


            Logger.info( "Recovery link: " + linkBuilder.toString() );

            String emailBody = recovery.render(linkBuilder.toString(), domain, adminEmail).toString();

            EmailConfig finalEmailConfig = emailConfig;
            Thread mailThread = new Thread(() -> {
                try {
                    sendEmail(email, "Password recovery", emailBody, finalEmailConfig);
                } catch ( Exception e ) {
                    Logger.error( "Error on sending registration email: ", e );
                }
            });
            mailThread.start();
        } catch ( Exception e ) {
            Logger.error( "Error on sending registration email: ", e );
        }
    }

    public void sendDownloadListEmail(ListEntity list, String email, User reseller) {
        try {
            Config config = ConfigFactory.load();

            String adminEmail = "support@makemydata.com";
            if (reseller != null && reseller.getDomains() != null) {
                switch (reseller.getDomains()) {
                    case "makedatalist.com":
                        adminEmail = "support@makedatalist.com";
                        break;
                    case "makethedata.com":
                        adminEmail = "support@makethedata.com";
                        break;
                    case "reidatalist.com":
                        adminEmail = "support@reidatalist.com";
                        break;
                    case "axiomleads.com":
                        adminEmail = "support@axiomleads.com";
                        break;
                    case "multimedialists.com":
                    case "multimedialists.net":
                        adminEmail = "support@multimedialists.com";
                        break;
                    case "mytargetdata.com":
                        adminEmail = "support@mytargetdata.com";
                        break;
                    case "sales-list.com":
                        adminEmail = "admin@sales-list.com";
                        break;
                }
            }

            String title = "Your download link for list '" + list.getName() + "' is ready";
            EmailConfig emailConfig = new EmailConfig(config);

            StringBuilder linkBuilder = new StringBuilder();
            linkBuilder.append("https://makemydata.com/rest/public/lists/download/").
                        append(list.getId()).
                        append('/').
                        append(URLEncoder.encode(list.getName(), "UTF-8"));

            String emailBody = download.render(linkBuilder.toString(), title, adminEmail).toString();

            EmailConfig finalEmailConfig = emailConfig;
            Thread mailThread = new Thread(() -> {
                try {
                    sendEmail(email, "Your list download link is ready", emailBody, finalEmailConfig);
                } catch ( Exception e ) {
                    Logger.error( "Error on sending registration email: ", e );
                }
            });
            mailThread.start();
        } catch ( Exception e ) {
            Logger.error( "Error on sending registration email: ", e );
        }

    }

    public void sendNewPaymentEmail(User user, float amount, User reseller) {
        try {
            Config config = ConfigFactory.load();

            String subject = String.format( "User (%s %s) paid $%.2f", user.getUsername(), user.getEmail(), amount );

            String userValue = String.format( "%s (%s)", user.getUsername(), user.getEmail() );
            String amountValue = String.format( "%.2f", amount );

            String emailBody = payment.render( userValue, amountValue ).toString();

            Thread mailThread = new Thread(() -> {
                try {
                    String emailTo = "phil@everydata.com";
                    if (reseller != null && reseller.getNotificationEmail() != null && reseller.getNotificationEmail().length() > 0) {
                        emailTo = reseller.getNotificationEmail();
                    }

                    sendEmail(emailTo, subject, null, null, emailBody, new EmailConfig(config) );
                } catch ( Exception e ) {
                    Logger.error( "Error on sending registration email: ", e );
                }
            });
            mailThread.start();
        } catch ( Exception e ) {
            Logger.error( "Error on sending registration email: ", e );
        }
    }

    public void sendFeedbackEmail(FeedbackRequest feedbackRequest, User reseller) {
        try {
            Config config = ConfigFactory.load();
            String name = (feedbackRequest.getName() != null && feedbackRequest.getName().length() > 0) ?
                    feedbackRequest.getName() : "unknown user";

            String subject = String.format("%s's feedback", name);
            String[] linesArray = feedbackRequest.getMessage().split("\n");
            List<String> lines = new LinkedList();
            for (String line: linesArray) {
                lines.add(line);
            }

            String emailBody = feedback.render(
                    name,
                    feedbackRequest.getEmail(),
                    null,
                    lines).toString();

            Thread mailThread = new Thread(() -> {
                try {
                    String email = "phil@everydata.com";
                    if (reseller != null) {
                        if (reseller.getNotificationEmail() != null && reseller.getNotificationEmail().length() > 0) {
                            email = reseller.getNotificationEmail();
                        } else {
                            email = reseller.getEmail();
                        }
                    }

                    sendEmail(
                            email,
                            subject,
                            emailBody,
                            new EmailConfig(config) );
                } catch ( Exception e ) {
                    Logger.error( "Error on sending feedback email: ", e );
                }
            });
            mailThread.start();
        } catch ( Exception e ) {
            Logger.error( "Error on sending feedback email: ", e );
        }
    }

    public void sendListPurchasedEmail(User reseller, User user, ListEntity list, long count, float amount) {
        try {
            Config config = ConfigFactory.load();
            String subject = "MakeMyData: a list has been purchased";

            String emailBody = purchased.render(
                    count,
                    list.getTableName(),
                    amount,
                    user.getUsername(),
                    list.getName()).toString();

            final String finalSubject = subject;
            Thread mailThread = new Thread(() -> {
                try {
                    String email = reseller.getNotificationEmail();
                    sendEmail( email, finalSubject, emailBody, new EmailConfig(config) );
                } catch ( Exception e ) {
                    Logger.error( "Error on sending list purchased email: ", e );
                }
            });
            mailThread.start();
        } catch ( Exception e ) {
            Logger.error( "Error on sending list purchased email: ", e );
        }
    }

    public void sendBulkEmail(String email, SendEmailRequest request) {
        Config config = ConfigFactory.load();

        try {
            sendEmail(email, request.getSubject(), request.getBody(), new EmailConfig(
                    config, request.getFrom(), request.getFromName(), request.getReply()));
        } catch ( Exception e ) {
            Logger.error( "Error on sending bulk email: ", e );
        }
    }

    public void sendRegistrationCompletedEmail(User user, User reseller) {
        try {
            Config config = ConfigFactory.load();
            final String subject = "A user registration is completed";

            String emailBody = "The new user has been registered.<br/>" +
                    "Username: " + user.getUsername() + "<br/>" +
                    "Email: " + user.getEmail() + "<br/>";

            if (reseller != null &&
                    (reseller.getNotificationEmail() == null || reseller.getNotificationEmail().length() == 0)) {
                emailBody =  emailBody + "Reseller: " + reseller.getUsername() + "<br/>";
            }

            String content = emailBody;
            Thread mailThread = new Thread(() -> {
                try {
                    String emailTo = "phil@everydata.com";
                    if (reseller != null && reseller.getNotificationEmail() != null && reseller.getNotificationEmail().length() > 0) {
                        emailTo = reseller.getNotificationEmail();
                    }

                    sendEmail(emailTo, subject, null, null, content, new EmailConfig(config) );
                } catch ( Exception e ) {
                    Logger.error( "Error on sending user has been registered email: ", e );
                }
            });
            mailThread.start();
        } catch ( Exception e ) {
            Logger.error( "Error on sending user has been registered email: ", e );
        }
    }

    public void sendStartCampaignEmail(User user, ListEntity list, String message) {
        try {
            Config config = ConfigFactory.load();
            final EmailConfig emailConfig = new EmailConfig(config);

            StringBuilder linkBuilder = new StringBuilder();
            linkBuilder.append("https://makemydata.com/rest/public/lists/download/").
                    append(list.getId()).
                    append('/').
                    append(URLEncoder.encode(list.getName(), "UTF-8"));

            String emailBody = campaign.render(user.getUsername(), message, linkBuilder.toString()).toString();

            Thread mailThread = new Thread(() -> {
                try {
                    sendEmail("contact@textalldata.com", "User has ordered an SMS campaign", emailBody, emailConfig);
                } catch (Exception e) {
                    Logger.error("Error on sending registration email: ", e);
                }
            });
            mailThread.start();
        } catch (Exception e) {
            Logger.error("Error on sending registration email: ", e);
        }
    }

    public void sendPaymentRequestedPaidEmail(PaymentRequest paymentRequest) {
        try {
            Config config = ConfigFactory.load();

            String subject = String.format("Your payment request status is changed!");
            String emailBody = "Invoice #" +  paymentRequest.getId() + " is paid!<br/>" +
                    "Amount: $" + (int)paymentRequest.getAmount() + "<br/>" +
                    "Due Date: " + dueDateFormat.format(new Date(paymentRequest.getDueDate())) + "<br/>" +
                    "Note: " + paymentRequest.getNote() + "<br/>";

                    Thread mailThread = new Thread(() -> {
                try {
                    String email = "phil@everydata.com";

                    sendEmail(email, subject, emailBody, new EmailConfig(config) );
                } catch ( Exception e ) {
                    Logger.error( "Error on sending feedback email: ", e );
                }
            });
            mailThread.start();
        } catch ( Exception e ) {
            Logger.error( "Error on sending feedback email: ", e );
        }
    }
}
