package services.db.dao;

import com.google.inject.Inject;
import models.PrefixListRequest;
import services.db.entity.PhonePrefix;
import services.db.mapper.PrefixesMapper;

import java.util.List;

public class PrefixesDAO {

    public PrefixesMapper mapper;

    @Inject
    public PrefixesDAO( PrefixesMapper mapper ) {
        this.mapper = mapper;
    }

    public int getPrefixListCount( PrefixListRequest request ) {
        return mapper.getPrefixListCount( request );
    }

    public List< PhonePrefix > getPrefixList( PrefixListRequest request ) {
        return mapper.getPrefixList( request );
    }

    public void insertPrefixes( List< PhonePrefix > phonePrefixes ) {
        mapper.insertPrefixes( phonePrefixes );
    }

    public void deletePrefixById( long id ) {
        mapper.deletePrefixById( id );
    }

    public PhonePrefix findPhonePrefixByPrefix( String prefix ) {
        return mapper.findPhonePrefixByPrefix( prefix );
    }

    public void updatePhonePrefix( PhonePrefix phonePrefix ) {
        mapper.updatePhonePrefix( phonePrefix );
    }

}
