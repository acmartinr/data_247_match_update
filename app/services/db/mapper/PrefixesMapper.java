package services.db.mapper;

import models.PrefixListRequest;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import services.db.entity.PhonePrefix;

import java.util.List;

public interface PrefixesMapper {

    int getPrefixListCount( PrefixListRequest request);

    List< PhonePrefix > getPrefixList( PrefixListRequest request );

    void insertPrefixes( @Param( "prefixes" ) List< PhonePrefix > phonePrefixes );

    @Delete( "DELETE FROM phonePrefixes WHERE id = #{id}" )
    void deletePrefixById( @Param( "id" ) long id );

    @Select( "SELECT * FROM phonePrefixes WHERE prefix = #{prefix} LIMIT 1" )
    PhonePrefix findPhonePrefixByPrefix( @Param( "prefix" ) String prefix );

    @Update( "UPDATE phonePrefixes SET prefix=#{prefix}, type=#{type} WHERE id = #{id}" )
    void updatePhonePrefix( PhonePrefix phonePrefix );

}
