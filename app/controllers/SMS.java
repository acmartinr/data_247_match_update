package controllers;

import com.google.inject.Inject;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.db.dao.SMSDAO;
import services.db.dao.UserDAO;
import services.db.entity.Messages;
import services.db.entity.User;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

public class SMS extends Controller {

    private final SMSDAO smsDao;
    private final UserDAO userDAO;

    private final String phoneField = "phone";

    @Inject
    public SMS( SMSDAO smsDao,
                UserDAO userDAO ) {
        this.smsDao = smsDao;
        this.userDAO = userDAO;
    }

    public Result getSMSList() throws Exception {
        Messages result = new Messages();
        List< Messages.Message > messages = new LinkedList();

        List< services.db.entity.SMS > smsMessages = smsDao.getAll();
        smsMessages.forEach( message -> {
            messages.add( new Messages.Message( message.getPhone(), message.getId(), message.getMessage() ) );
            smsDao.removeMessageById( message.getId() );
        } );
        result.setMessages( messages );

        JAXBContext context = JAXBContext.newInstance( Messages.class );
        StringWriter writer = new StringWriter();
        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        marshaller.marshal( result, writer );

        return ok( writer.toString() ).as( "application/xml" );
    }

    public Result handleIncomeSMS() {
        String phone = request().getQueryString( phoneField );
        if ( phone != null ) {
            phone = phone.replace( "-", "" );
        } else {
            Logger.error( "Empty phone param: " + request().uri() );
            return notFound();
        }

        User user = userDAO.findUserByPhone( phone );
        if ( user == null && phone.length() == 11 && phone.startsWith( "1" ) ) {
            user = userDAO.findUserByPhone( phone.substring( 1, phone.length() ) );
        }

        if ( user != null ) {
            userDAO.updateVerifiedById( user.getId(), 1 );
        }

        return ok();
    }
}
