package controllers;

import com.google.inject.Inject;
import models.PrefixListRequest;
import models.PrefixListResponse;
import models.Response;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.db.dao.PrefixesDAO;
import services.db.dao.UserDAO;
import services.db.entity.PhonePrefix;
import services.db.entity.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class PhonePrefixes extends Controller {

    private final PrefixesDAO prefixesDAO;
    private final UserDAO userDAO;

    private final String TYPE_LANDLINE = "landline";
    private final String TYPE_CELL_PHONE = "cell phone";

    @Inject
    public PhonePrefixes( PrefixesDAO prefixesDAO,
                          UserDAO userDAO ) {
        this.prefixesDAO = prefixesDAO;
        this.userDAO = userDAO;
    }

    public Result getPrefixesList() {
        if ( !checkAccess() ) {
            return forbidden();
        }

        PrefixListRequest request = Json.fromJson( request().body().asJson(), PrefixListRequest.class );

        int prefixesCount = prefixesDAO.getPrefixListCount( request );
        List< PhonePrefix > prefixesList = prefixesDAO.getPrefixList( request );

        return ok( Json.toJson( new PrefixListResponse( prefixesCount, prefixesList ) ) );
    }

    public Result importPrefixesFromCSV() {
        try {
            Http.MultipartFormData< File > body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart< File > file = body.getFile( "file" );
            if ( file != null ) {
                List< PhonePrefix > phonePrefixes = new LinkedList();

                BufferedReader reader = new BufferedReader( new FileReader( file.getFile() ) );
                String line = reader.readLine();
                while ( line != null && !"".equalsIgnoreCase( line ) ) {
                    String[] parts = line.split(",");
                    phonePrefixes.add( new PhonePrefix(
                            parts[ 1 ],
                            TYPE_LANDLINE.equals( parts[ 0 ] ) ? 0 : 1
                    ) );

                    if ( phonePrefixes.size() > 1000 ) {
                        prefixesDAO.insertPrefixes( phonePrefixes );
                        phonePrefixes.clear();
                    }

                    line = reader.readLine();
                }

                if ( phonePrefixes.size() > 0 ) {
                    prefixesDAO.insertPrefixes( phonePrefixes );
                }

                return ok( Json.toJson( Response.OK() ) );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return ok( Json.toJson( Response.ERROR() ) );
    }

    public Result deletePhonePrefix( long id ) {
        if ( !checkAccess() ) {
            return forbidden();
        }

        prefixesDAO.deletePrefixById( id );
        return ok( Json.toJson( Response.OK() ) );
    }

    public Result updatePhonePrefix() {
        if ( !checkAccess() ) {
            return forbidden();
        }

        PhonePrefix phonePrefix = Json.fromJson( request().body().asJson(), PhonePrefix.class );
        PhonePrefix dbPhonePrefix = prefixesDAO.findPhonePrefixByPrefix( phonePrefix.getPrefix() );
        if ( dbPhonePrefix != null && dbPhonePrefix.getId() != phonePrefix.getId() ) {
            return ok( Json.toJson( Response.ERROR() ) );
        } else {
            phonePrefix.setPrefix( phonePrefix.getPrefix() + "%" );
            prefixesDAO.updatePhonePrefix( phonePrefix );

            return ok( Json.toJson( Response.OK() ) );
        }
    }

    private boolean checkAccess() {
        String authHeader = request().getHeader("auth");
        if ( authHeader != null ) {
            try {
                String[] parts = authHeader.split(":");
                Integer id = Integer.parseInt( parts[ 0 ] );

                User user = userDAO.findUserById(id);
                if ( user != null &&
                        (user.getRole() == User.ROLE_ADMIN || user.getRole() == User.ROLE_MANAGER)) {
                    return true;
                }
            } catch ( Exception e ) { e.printStackTrace(); }
        }

        return false;
    }
}
