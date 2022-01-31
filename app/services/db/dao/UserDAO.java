package services.db.dao;

import com.google.inject.Inject;
import models.*;
import services.db.entity.Comment;
import services.db.entity.Payment;
import services.db.entity.PaymentRequest;
import services.db.entity.RegistrationRequest;
import services.db.entity.User;
import services.db.mapper.UserMapper;

import java.util.List;

public class UserDAO {

    public UserMapper mapper;

    @Inject
    UserDAO( UserMapper mapper ) {
        this.mapper = mapper;
    }

    public User findUserByNameOrEmailAndPassword(String name, String password) {
        return mapper.findUserByNameOrEmailAndPassword(name, password);
    }

    public User findUserByName(String username) {
        return mapper.findUserByName(username);
    }

    public User findUserByEmail(String email) {
        return mapper.findUserByEmail(email);
    }

    public void saveRegistrationRequest(RegistrationRequest request) {
        mapper.insertRegistrationRequest(request);
    }

    public RegistrationRequest findRegistrationRequestByUUID(String token) {
        return mapper.findRegistrationRequestByUUID(token);
    }

    public void removeRegistrationRequest(RegistrationRequest registrationRequest) {
        mapper.removeRegistrationRequest(registrationRequest);
    }

    public void saveUser(User user) {
        mapper.insertUser(user);
    }

    public void updateUserDetailsById( User user ) {
        User dbUser = findUserById(user.getId());
        if ( dbUser.getPhone() == null || !dbUser.getPhone().equals( user.getPhone() ) ) {
            user.setVerified( 0 );
            mapper.updateUserPhoneById( user.getPhone(), user.getId() );
        }

        mapper.updateUserDetailsById( user );
    }

    public User findUserById( int id ) {
        return mapper.findUserById( id );
    }

    public void updatePasswordById( String newPassword, int id ) {
        mapper.updatePasswordById( newPassword, id );
    }

    public void updateVerificationCodeById( int userId, String code ) {
        mapper.updateVerificationCodeById( userId, code );
    }

    public String findUserVerificationCodeById( int id ) {
        return mapper.findUserVerificationCodeById( id );
    }

    public void updateVerifiedById( int id, int value ) {
        mapper.updateVerifiedById( id, value );

        User user = mapper.findUserById(id);
        if ( user != null && !User.Status.BLOCKED.equals( user.getStatus() ) ) {
            if ( value == 1 ) {
                mapper.updateStatusById( id, User.Status.ACTIVE );
            } else {
                mapper.updateStatusById( id, User.Status.NOT_VERIFIED );
            }
        }
    }

    public User findUserByPhone( String phone ) {
        return mapper.findUserByPhone( phone );
    }

    public User findUserByIp( String ip ) {
        return mapper.findUserByIp( ip );
    }

    public void updateBalanceByUserId( int userId, float balance ) {
        mapper.updateBalanceByUserId( userId, balance );
    }

    public void updateCardHolderDetailsById( int userId, String cardHolderDetails ) {
        mapper.updateCardHolderDetailsById( userId, cardHolderDetails );
    }

    public int getUserListCount( UserListRequest request ) {
        return mapper.getUserListCount( request );
    }

    public List< User > getUserList( UserListRequest request ) {
        return mapper.getUserList( request );
    }

    public void updateStatusById( int id, User.Status status ) {
        mapper.updateStatusById( id, status );
    }

    public List< User > findUsersWithEmptyStatus() {
        return mapper.findUsersWithEmptyStatus();
    }

    public void updateUser( User user ) {
        mapper.updateUser( user );
    }

    public void deleteUserById( Integer id ) {
        mapper.deleteUserById( id );
    }

    public int getPaymentListCount( PaymentListRequest request ) {
        return mapper.getPaymentListCount( request );
    }

    public List< Payment > getPaymentList( PaymentListRequest request ) {
        return mapper.getPaymentList( request );
    }

    public void insertPayment( Payment payment ) {
        mapper.insertPayment( payment );
    }

    public List<UserAutoComplete> getUserAutocomplete() {
        return mapper.getUserAutocomplete();
    }

    public List<UserAutoComplete> getUserAutocompleteForReseller(int resellerId) {
        return mapper.getUserAutocompleteForReseller(resellerId);
    }

    public List<UserAutoComplete> getUserPaymentAutocompleteForReseller(int resellerId) {
        return mapper.getUserPaymentAutocompleteForReseller(resellerId);
    }

    public void updateTokenByEmail( String email, String token ) {
        mapper.updateTokenByEmail( email, token );
    }

    public User findUserByToken( String token ) {
        return mapper.findUserByToken( token );
    }

    public void updatePasswordByToken( String token, String password ) {
        mapper.updatePasswordByToken( token, password );
    }

    public int findMaxResellerNumber() {
        Integer result = mapper.findMaxResellerNumber();
        if (result == null) {
            return 0;
        } else {
            return result;
        }
    }

    public int findResellerIdByNumber(int number) {
        User user = mapper.findResellerByNumber(number);
        if (user == null) {
            return 0;
        } else {
            return user.getId();
        }
    }

    public User findResellerByNumber(int number) {
        return mapper.findResellerByNumber(number);
    }

    public int getRegistrationRequestListCount(UserListRequest request) {
        return mapper.getRegistrationRequestListCount(request);
    }

    public List<RegistrationRequest> getRegistrationRequestList(UserListRequest request) {
        return mapper.getRegistrationRequestList(request);
    }

    public RegistrationRequest findRegistrationRequestById(int id) {
        return mapper.findRegistrationRequestById(id);
    }

    public void removeRegistrationRequestById(int id) {
        mapper.removeRegistrationRequestById(id);
    }

    public List<User> findResellersByHost(String requestHost) {
        return mapper.findResellersByHost("%" + requestHost.toLowerCase() + "%");
    }

    public List<User> getAllUsers() {
        return mapper.getAllUsers();
    }

    public List<User> getResellerUsers(int resellerId) {
        return mapper.getResellerUsers(resellerId);
    }

    public String getSupportPhoneNumber(int userId) {
        return mapper.getSupportPhoneNumber(userId);
    }

    public void updateUserNoteAndStatus(UserNoteRequest request) {
        mapper.updateUserNoteAndStatus(request);
    }

    public String getStripePublicKeyByResellerIdOnly(int resellerId) {
        return mapper.getStripePublicKeyByResellerId(resellerId);
    }

    public String getStripePublicKeyByResellerId(int resellerId) {
        String publicKey = mapper.getStripePublicKeyByResellerId(resellerId);

        if (publicKey == null) {
            publicKey = mapper.getStripePublicKeyByResellerId(0);
        }

        return publicKey;
    }

    public String getStripePrivateKeyByResellerId(int resellerId) {
        String privateKey = mapper.getStripePrivateKeyByResellerId(resellerId);

        if (privateKey == null) {
            privateKey = mapper.getStripePrivateKeyByResellerId(0);
        }

        return privateKey;
    }

    public List<User> findUsersWithRole(int role) {
        return mapper.findUsersWithRole(role);
    }

    public void updateUsersForRemovedReseller(int id) {
        mapper.updateUsersForRemovedReseller(id);
    }

    public boolean checkIpBlocked(String remoteAddress) {
        return mapper.checkIpBlocked(remoteAddress) != null;
    }

    public void updateUserPassword(int id, String password) {
        mapper.updateUserPassword(id, password);
    }

    public void saveComment(Comment comment) {
        mapper.saveComment(comment);
    }

    public void updateComment(Comment comment) {
        mapper.updateComment(comment);
    }

    public int getCommentListCount(CommentListRequest request) {
        return mapper.getCommentListCount(request);
    }

    public List<Comment> getCommentList(CommentListRequest request) {
        return mapper.getCommentList(request);
    }

    public void deleteCommentById(long commentId) {
        mapper.deleteCommentById(commentId);
    }

    public List<Comment> getApprovedComments() {
        return mapper.getApprovedComments();
    }

    public void updateLastActivityDateByUserId(int userId, long currentTimeMillis) {
        mapper.updateLastActivityDateByUserId(userId, currentTimeMillis);
    }

    public void removePaymentRequestById(int id) {
        mapper.removePaymentRequestById(id);
    }

    public void savePaymentRequest(PaymentRequest paymentRequest) {
        mapper.savePaymentRequest(paymentRequest);
    }

    public int getPaymentRequestListCount(StringListRequest request) {
        Integer count = mapper.getPaymentRequestListCount(request);
        return count == null ? 0 : count;
    }

    public List<PaymentRequest> getPaymentRequestList(StringListRequest request) {
        return mapper.getPaymentRequestList(request);
    }

    public void updatePaymentRequest(PaymentRequest paymentRequest) {
        mapper.updatePaymentRequest(paymentRequest);
    }

    public PaymentRequest getPaymentRequestById(Integer id) {
        return mapper.getPaymentRequestById(id);
    }

    public void updatePaymentRequestPaidStatusById(int id, boolean status) {
        mapper.updatePaymentRequestPaidStatusById(id, status);
    }

    public void insertRegistrationDetails(String email, String details) {
        mapper.insertRegistrationDetails(email, details, System.currentTimeMillis());
    }
}
