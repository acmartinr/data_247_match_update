package services.db.dao;

import com.google.inject.Inject;
import services.db.entity.SMS;
import services.db.mapper.SMSMapper;

import java.util.List;

public class SMSDAO {

    private SMSMapper mapper;

    @Inject SMSDAO( SMSMapper mapper ) {
        this.mapper = mapper;
    }

    public List< SMS > getAll() {
        return mapper.getAll();
    }

    public void removeMessageById( int id ) {
        mapper.removeMessageById( id );
    }

    public void insertSMS( SMS sms ) {
        mapper.insert( sms );
    }

    public void removeMessagesByUserId( int userId ) {
        mapper.removeMessagesByUserId( userId );
    }
}
