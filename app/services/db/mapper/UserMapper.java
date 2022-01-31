package services.db.mapper;

import models.*;
import org.apache.ibatis.annotations.*;
import services.db.entity.Comment;
import services.db.entity.Payment;
import services.db.entity.PaymentRequest;
import services.db.entity.RegistrationRequest;
import services.db.entity.User;

import java.util.List;

public interface UserMapper {

    @Select( "SELECT * FROM users WHERE ( lower(username) = lower(#{name}) OR lower(email) = lower(#{name}) ) AND password = #{password}" )
    User findUserByNameOrEmailAndPassword(@Param("name") String name,
                                          @Param("password") String password);

    @Select("SELECT * FROM users WHERE lower(username)=lower(#{username})")
    User findUserByName(@Param("username") String username);

    @Select("SELECT * FROM users WHERE lower(email)=lower(#{email})")
    User findUserByEmail(@Param("email") String email);

    void insertRegistrationRequest(RegistrationRequest request);

    @Select("SELECT * FROM registrationRequests WHERE uuid=#{uuid}")
    RegistrationRequest findRegistrationRequestByUUID(@Param("uuid") String token);

    @Delete("DELETE FROM registrationRequests WHERE email=#{email}")
    void removeRegistrationRequest(RegistrationRequest registrationRequest);

    void insertUser(User user);

    @Update( "UPDATE users SET address=#{address}, companyName=#{companyName} WHERE id=#{id}" )
    void updateUserDetailsById(User user);

    @Select( "SELECT * FROM users WHERE id=#{id}" )
    User findUserById(@Param( "id" ) int id);

    @Update( "UPDATE users SET password=#{password} WHERE id=#{id}" )
    void updatePasswordById( @Param( "password" ) String newPassword,
                             @Param( "id" ) int id );

    @Update( "UPDATE users SET verificationCode=#{code} WHERE id=#{id}" )
    void updateVerificationCodeById( @Param( "id" ) int userId,
                                     @Param( "code" ) String code );

    @Select( "SELECT verificationCode FROM users WHERE id = #{id}" )
    String findUserVerificationCodeById( @Param( "id" ) int id );

    @Update( "UPDATE users SET verified = #{value} WHERE id = #{id}" )
    void updateVerifiedById( @Param( "id" ) int id,
                             @Param( "value" ) int value );

    @Select( "SELECT * FROM users WHERE phone = #{phone} limit 1")
    User findUserByPhone( @Param( "phone" ) String phone );

    @Select( "SELECT * FROM users WHERE ip = #{ip} limit 1" )
    User findUserByIp( @Param( "ip" ) String ip );

    @Update( "UPDATE users SET balance = #{balance} WHERE id = #{userId}" )
    void updateBalanceByUserId( @Param( "userId" ) int userId,
                                @Param( "balance" ) float balance );

    @Update( "UPDATE users SET cardHolderDetails = #{cardHolderDetails} WHERE id = #{userId} " )
    void updateCardHolderDetailsById( @Param( "userId" ) int userId,
                                      @Param( "cardHolderDetails" ) String cardHolderDetails );

    int getUserListCount( UserListRequest request );

    List< User > getUserList( UserListRequest request );

    @Update( "UPDATE users SET status = #{status} WHERE id = #{userId}" )
    void updateStatusById( @Param( "userId" ) int id,
                           @Param( "status" ) User.Status status );

    @Select( "SELECT * FROM users WHERE status IS NULL" )
    List< User > findUsersWithEmptyStatus();

    void updateUser( User user );

    @Delete( "DELETE FROM users WHERE id = #{userId}" )
    void deleteUserById( @Param( "userId" ) Integer id );

    int getPaymentListCount( PaymentListRequest request );

    List< Payment > getPaymentList( PaymentListRequest request );

    void insertPayment( Payment payment );

    @Select( "SELECT username, email FROM users ORDER BY username" )
    List<UserAutoComplete> getUserAutocomplete();

    @Select( "SELECT username, email FROM users WHERE resellerId=#{resellerId} OR id=#{resellerId} ORDER BY username " )
    List<UserAutoComplete> getUserAutocompleteForReseller(@Param("resellerId") int resellerId);

    @Select( "SELECT username, email FROM users WHERE resellerId=#{resellerId} ORDER BY username " )
    List<UserAutoComplete> getUserPaymentAutocompleteForReseller(@Param("resellerId") int resellerId);

    @Update( "UPDATE users SET phone = #{phone}, verified = 0, verificationCode = null WHERE id = #{userId}" )
    void updateUserPhoneById( @Param( "phone" ) String phone,
                              @Param( "userId" ) int id );

    @Update( "UPDATE users SET token = #{token} WHERE lower(email) = lower(#{email})" )
    void updateTokenByEmail( @Param( "email" ) String email,
                             @Param( "token" ) String token );

    @Select( "SELECT * FROM users WHERE token = #{token}" )
    User findUserByToken( @Param( "token" ) String token );

    @Update( "UPDATE users SET password = #{password}, token = NULL WHERE token = #{token}" )
    void updatePasswordByToken( @Param( "token" ) String token,
                                @Param( "password" ) String password );

    @Select("SELECT MAX(resellerNumber) FROM users")
    Integer findMaxResellerNumber();

    @Select("SELECT * FROM users WHERE resellerNumber=#{number}")
    User findResellerByNumber(@Param("number") int number);

    int getRegistrationRequestListCount(UserListRequest request);

    List<RegistrationRequest> getRegistrationRequestList(UserListRequest request);

    @Select("SELECT * FROM registrationRequests WHERE id=#{id}")
    RegistrationRequest findRegistrationRequestById(@Param("id") int id);

    @Delete("DELETE FROM registrationRequests WHERE id=#{id}")
    void removeRegistrationRequestById(@Param("id") int id);

    @Select("SELECT * FROM users WHERE lower(domains) LIKE #{domain}")
    List<User> findResellersByHost(String domain);

    @Select("SELECT * FROM users")
    List<User> getAllUsers();

    @Select("SELECT * FROM users WHERE resellerId=#{resellerId}")
    List<User> getResellerUsers(@Param("resellerId") int resellerId);

    @Select("SELECT u2.supportPhone FROM users u1 JOIN users u2 ON u2.id = u1.resellerId AND u1.id=#{userId}")
    String getSupportPhoneNumber(@Param("userId") int userId);

    @Update("UPDATE users SET note=#{note}, noteStatus=#{noteStatus} WHERE id=#{userId}")
    void updateUserNoteAndStatus(UserNoteRequest request);

    @Select("SELECT publicKey FROM stripeKeys WHERE resellerId = #{resellerId}")
    String getStripePublicKeyByResellerId(@Param("resellerId") int resellerId);

    @Select("SELECT privateKey FROM stripeKeys WHERE resellerId = #{resellerId}")
    String getStripePrivateKeyByResellerId(@Param("resellerId") int resellerId);

    @Select("SELECT id, username FROM users where role = #{role} ORDER BY username")
    List<User> findUsersWithRole(@Param("role") int role);

    @Update("UPDATE users SET resellerId = 0 WHERE resellerId = #{resellerId}")
    void updateUsersForRemovedReseller(@Param("resellerId") int id);

    @Select("SELECT ip FROM ipAddressBlocked WHERE ip = #{address}")
    String checkIpBlocked(@Param("address") String remoteAddress);

    @Update("UPDATE users SET password = #{password} WHERE id = #{id}")
    void updateUserPassword(@Param("id") int id,
                            @Param("password") String password);

    void saveComment(Comment comment);

    void updateComment(Comment comment);

    int getCommentListCount(CommentListRequest request);

    List<Comment> getCommentList(CommentListRequest request);

    @Delete("DELETE FROM comments WHERE id = #{id}")
    void deleteCommentById(@Param("id") long commentId);

    @Select("SELECT name, comment, answer, date FROM comments WHERE approved=TRUE ORDER BY DATE desc")
    List<Comment> getApprovedComments();

    @Update("UPDATE users SET lastActivityDate=#{date} WHERE id=#{userId}")
    void updateLastActivityDateByUserId(@Param("userId") int userId,
                                        @Param("date") long currentTimeMillis);

    @Delete("DELETE FROM paymentRequests WHERE id=#{id}")
    void removePaymentRequestById(@Param("id") int id);

    @Insert("INSERT INTO paymentRequests(note, amount, dueDate, date, paid) " +
            "VALUES (#{note}, #{amount}, #{dueDate}, #{date}, #{paid})")
    void savePaymentRequest(PaymentRequest paymentRequest);

    @Select("SELECT count(id) FROM paymentRequests WHERE note ILIKE #{searchValue}")
    Integer getPaymentRequestListCount(StringListRequest request);

    @Select("SELECT * FROM paymentRequests WHERE note ILIKE #{searchValue} " +
            "ORDER BY date DESC LIMIT #{limit} OFFSET #{offset}")
    List<PaymentRequest> getPaymentRequestList(StringListRequest request);

    @Update("UPDATE paymentRequests SET note=#{note}, amount=#{amount}, dueDate=#{dueDate} WHERE id=#{id}")
    void updatePaymentRequest(PaymentRequest paymentRequest);

    @Select("SELECT * FROM paymentRequests WHERE id=#{id}")
    PaymentRequest getPaymentRequestById(@Param("id") Integer id);

    @Update("UPDATE paymentRequests SET paid=#{paid} WHERE id=#{id}")
    void updatePaymentRequestPaidStatusById(@Param("id") int id, @Param("paid") boolean status);

    @Insert("INSERT INTO registrationInfo(email, message, date) VALUES(#{email}, #{details}, #{date})")
    void insertRegistrationDetails(@Param("email") String email,
                                   @Param("details") String details,
                                   @Param("date") long date);

}
