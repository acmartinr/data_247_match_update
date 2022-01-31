package services.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class User {

    public static int ROLE_ADMIN = 3;
    public static int ROLE_MANAGER = 2;
    public static int ROLE_RESELLER = 1;
    public static int ROLE_USER = 0;

    private int id;

    private String username;
    private String email;
    private String phone;
    private String companyName;
    private String address;
    private String promoCode;
    private String password;
    private int verified;
    private String ip;
    private Status status;
    private int admin;
    private float balance;
    private long date;
    private boolean restriction;
    private int resellerId;
    private int resellerNumber;
    private String reseller;
    private boolean becomeReseller;
    private String domains;
    private int role;
    private String supportPhone;
    private String note;
    private String noteStatus;
    private int originalRole;
    private boolean filterDNC;
    private boolean filterEmptyPhone;
    private boolean filterEmail;
    private boolean allowManageMoney;
    private boolean validateRegistrationRequests;
    private int invitationDomain;
    private boolean multipleGeographicParametersEnabled;
    private String newPassword;
    private boolean allowCustomersKeyword;
    private boolean allowBusinessKeyword;
    private boolean allowCarriersSearch;
    private boolean allowMatchResponder;
    private boolean allowMatchCraigslist;
    private boolean allowMatchConsumers;
    private boolean allowMatchOptIn;
    private boolean allowMatchDirectory;
    private boolean allowMatchBusinessDetailed;
    private boolean allowMatchingLists;
    private boolean allowDetailedBusinessKeywords;
    private boolean allowPayments;
    private boolean allowDataSourceItemsPrices;
    private boolean allowTransferToSuppression;
    private long lastActivityDate;
    private String notificationEmail;
    private boolean allowBusinessEmailFilter;
    private boolean listAdditionalCodeEnabled;

    private String allowedMatchesList;

    @JsonIgnore private String cardHolderDetails;

    public User() {}

    public String getAllowedMatchesList() {
        return allowedMatchesList;
    }

    public void setAllowedMatchesList(String allowedMatchesList) {
        this.allowedMatchesList = allowedMatchesList;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username == null ? "no name" : username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCompanyName() { return companyName == null ? "" : companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getAddress() { return address == null ? "" : address; }
    public void setAddress(String address) { this.address = address; }

    public String getPromoCode() { return promoCode; }
    public void setPromoCode(String promoCode) { this.promoCode = promoCode; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public float getBalance() { return balance; }
    public void setBalance( float balance ) { this.balance = balance; }

    @JsonIgnore public String getCardHolderDetails() { return cardHolderDetails; }
    @JsonIgnore public void setCardHolderDetails(String cardHolderDetails) { this.cardHolderDetails = cardHolderDetails; }

    public int getAdmin() { return admin; }
    public void setAdmin( int admin ) { this.admin = admin; }

    public Status getStatus() { return status; }
    public void setStatus( Status status ) { this.status = status; }

    public static User fromRegistrationRequest(RegistrationRequest registrationRequest) {
        User result = new User();
        result.setUsername( registrationRequest.getUsername() );
        result.setEmail( registrationRequest.getEmail() );
        result.setPhone( registrationRequest.getPhone() );
        result.setCompanyName( registrationRequest.getCompanyName() );
        result.setAddress( registrationRequest.getAddress() );
        result.setPromoCode( registrationRequest.getPromoCode() );
        result.setPassword( registrationRequest.getPassword() );
        result.setVerified( 0 );
        result.setIp( registrationRequest.getIp() );
        //result.setBalance( 10.0f );
        result.setBalance(0.0f);
        result.setDate( System.currentTimeMillis() );
        result.setStatus( Status.ACTIVE );
        result.setRestriction(true);
        result.setResellerId(registrationRequest.getResellerId());
        result.setRole(ROLE_USER);
        result.setAllowPayments(false);

        return result;
    }

    public long getDate() { return date; }
    public void setDate( long date ) { this.date = date; }

    public int getVerified() { return verified; }
    public void setVerified( int verified ) { this.verified = verified; }

    public String getIp() { return ip; }
    public void setIp( String ip ) { this.ip = ip; }

    public boolean isRestriction() { return restriction; }
    public void setRestriction(boolean restriction) { this.restriction = restriction; }

    public int getResellerId() { return resellerId; }
    public void setResellerId(int resellerId) { this.resellerId = resellerId; }

    public int getResellerNumber() { return resellerNumber; }
    public void setResellerNumber(int resellerNumber) { this.resellerNumber = resellerNumber; }

    public String getReseller() { return reseller; }
    public void setReseller(String reseller) { this.reseller = reseller; }

    public boolean isBecomeReseller() { return becomeReseller; }
    public void setBecomeReseller(boolean becomeReseller) { this.becomeReseller = becomeReseller; }

    public String getDomains() { return domains; }
    public void setDomains(String domains) { this.domains = domains; }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }

    public String getSupportPhone() { return supportPhone; }
    public void setSupportPhone(String supportPhone) { this.supportPhone = supportPhone; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getNoteStatus() { return noteStatus; }
    public void setNoteStatus(String noteStatus) { this.noteStatus = noteStatus; }

    public int getOriginalRole() { return originalRole; }
    public void setOriginalRole(int originalRole) { this.originalRole = originalRole; }

    public boolean isFilterDNC() { return filterDNC; }
    public void setFilterDNC(boolean filterDNC) { this.filterDNC = filterDNC; }

    public boolean isFilterEmptyPhone() { return filterEmptyPhone; }
    public void setFilterEmptyPhone(boolean filterEmptyPhone) { this.filterEmptyPhone = filterEmptyPhone; }

    public boolean isAllowManageMoney() { return allowManageMoney; }
    public void setAllowManageMoney(boolean allowManageMoney) { this.allowManageMoney = allowManageMoney; }

    public boolean isFilterEmail() {
        return filterEmail;
    }

    public void setFilterEmail(boolean filterEmail) {
        this.filterEmail = filterEmail;
    }

    public boolean isValidateRegistrationRequests() {
        return validateRegistrationRequests;
    }

    public void setValidateRegistrationRequests(boolean validateRegistrationRequests) {
        this.validateRegistrationRequests = validateRegistrationRequests;
    }

    public int getInvitationDomain() {
        return invitationDomain;
    }

    public void setInvitationDomain(int invitationDomain) {
        this.invitationDomain = invitationDomain;
    }

    public boolean isMultipleGeographicParametersEnabled() {
        return multipleGeographicParametersEnabled;
    }

    public void setMultipleGeographicParametersEnabled(boolean multipleGeographicParametersEnabled) {
        this.multipleGeographicParametersEnabled = multipleGeographicParametersEnabled;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public boolean isAllowCustomersKeyword() {
        return allowCustomersKeyword;
    }

    public void setAllowCustomersKeyword(boolean allowCustomersKeyword) {
        this.allowCustomersKeyword = allowCustomersKeyword;
    }

    public boolean isAllowBusinessKeyword() {
        return allowBusinessKeyword;
    }

    public void setAllowBusinessKeyword(boolean allowBusinessKeyword) {
        this.allowBusinessKeyword = allowBusinessKeyword;
    }

    public boolean isAllowCarriersSearch() {
        return allowCarriersSearch;
    }

    public void setAllowCarriersSearch(boolean allowCarriersSearch) {
        this.allowCarriersSearch = allowCarriersSearch;
    }

    public boolean isAllowMatchResponder() {
        return allowMatchResponder;
    }

    public void setAllowMatchResponder(boolean allowMatchResponder) {
        this.allowMatchResponder = allowMatchResponder;
    }

    public long getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(long lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public boolean isAllowMatchCraigslist() {
        return allowMatchCraigslist;
    }

    public void setAllowMatchCraigslist(boolean allowMatchCraigslist) {
        this.allowMatchCraigslist = allowMatchCraigslist;
    }

    public boolean isAllowBusinessEmailFilter() {
        return allowBusinessEmailFilter;
    }

    public void setAllowBusinessEmailFilter(boolean allowBusinessEmailFilter) {
        this.allowBusinessEmailFilter = allowBusinessEmailFilter;
    }

    public boolean isAllowMatchConsumers() {
        return allowMatchConsumers;
    }

    public void setAllowMatchConsumers(boolean allowMatchConsumers) {
        this.allowMatchConsumers = allowMatchConsumers;
    }

    public boolean isAllowMatchOptIn() {
        return allowMatchOptIn;
    }

    public void setAllowMatchOptIn(boolean allowMatchOptIn) {
        this.allowMatchOptIn = allowMatchOptIn;
    }

    public boolean isAllowMatchDirectory() {
        return allowMatchDirectory;
    }

    public void setAllowMatchDirectory(boolean allowMatchDirectory) {
        this.allowMatchDirectory = allowMatchDirectory;
    }

    public boolean isAllowMatchBusinessDetailed() {
        return allowMatchBusinessDetailed;
    }

    public void setAllowMatchBusinessDetailed(boolean allowMatchBusinessDetailed) {
        this.allowMatchBusinessDetailed = allowMatchBusinessDetailed;
    }

    public boolean isAllowMatchingLists() {
        return allowMatchingLists;
    }

    public void setAllowMatchingLists(boolean allowMatchingLists) {
        this.allowMatchingLists = allowMatchingLists;
    }

    public boolean isAllowDetailedBusinessKeywords() {
        return allowDetailedBusinessKeywords;
    }

    public void setAllowDetailedBusinessKeywords(boolean allowDetailedBusinessKeywords) {
        this.allowDetailedBusinessKeywords = allowDetailedBusinessKeywords;
    }

    public boolean isAllowPayments() {
        return allowPayments;
    }

    public void setAllowPayments(boolean allowPayments) {
        this.allowPayments = allowPayments;
    }

    public boolean isAllowDataSourceItemsPrices() {
        return allowDataSourceItemsPrices;
    }

    public void setAllowDataSourceItemsPrices(boolean allowDataSourceItemsPrices) {
        this.allowDataSourceItemsPrices = allowDataSourceItemsPrices;
    }

    public boolean isAllowTransferToSuppression() {
        return allowTransferToSuppression;
    }

    public void setAllowTransferToSuppression(boolean allowTransferToSuppression) {
        this.allowTransferToSuppression = allowTransferToSuppression;
    }

    public boolean isListAdditionalCodeEnabled() {
        return listAdditionalCodeEnabled;
    }

    public void setListAdditionalCodeEnabled(boolean listAdditionalCodeEnabled) {
        this.listAdditionalCodeEnabled = listAdditionalCodeEnabled;
    }

    public enum Status {
        NOT_VERIFIED,
        ACTIVE,
        BLOCKED
    }
}
