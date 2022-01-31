package services.db.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import services.db.entity.SMS;

import java.util.List;

public interface SMSMapper {

    @Select( "SELECT * FROM sms" )
    List< SMS > getAll();

    @Delete( "DELETE FROM sms WHERE id = #{id}" )
    void removeMessageById( @Param( "id" ) int id );

    @Insert( "INSERT INTO sms(userId, phone, message) VALUES (#{userId}, #{phone}, #{message})" )
    void insert( SMS sms );

    @Delete( "DELETE FROM sms WHERE userId = #{userId}" )
    void removeMessagesByUserId( @Param( "userId" ) int userId );
}
